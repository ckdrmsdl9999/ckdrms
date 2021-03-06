package mew;

import java.io.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
public class SixClient implements Runnable {

	private static int PORT = 5555; // 서버포트번호
	private static String IP = ""; // 서버아이피주소
	private Socket socket; // 소켓
	private User user; // 사용자
	private ArrayList<User> userArray;	// + 온라인 유저 목록

	public LoginUI login;
	public RestRoomUI restRoom;
	private DataInputStream dis = null;
	private DataOutputStream dos = null;
	private textAndBackground tab=new textAndBackground();
	public boolean ready = false;

	SixClient() {
		login = new LoginUI(this);
		this.userArray = new ArrayList<User>();
		// 스레드 시작
		Thread thread = new Thread(this);
		thread.start();
	}

	public static void main(String[] args) {
		JDialog.setDefaultLookAndFeelDecorated(true);
		JFrame.setDefaultLookAndFeelDecorated(true);	// 창 UI 변경

		System.out.println("Client start...");
		new SixClient();
	}

	@Override
	public void run() {
		// 소켓 통신 시작
		while (!ready) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// 사용자가 객체 생성 및 아이피설정
		user = new User(dis, dos);
		user.setIP(socket.getInetAddress().getHostAddress());

		// 메시지 리딩
		while (true) {
			try {
				String receivedMsg = dis.readUTF(); // 메시지 받기(대기)
				dataParsing(receivedMsg); // 메시지 해석
			} catch (IOException e) {
				e.printStackTrace();
				try {
					user.getDis().close();
					user.getDos().close();
					socket.close();
					break;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}

		errorMsg("서버프로그램이 먼저 종료되었습니다.");
		// 채팅프로그램 종료
		restRoom.dispose();
	}

	public boolean serverAccess() {
		if (!ready) {
			// 소켓이 연결이 이루어지지 않은 경우에만 실행
			// 즉, 처음 연결시에만 실행
			socket = null;
			IP = login.ipBtn.getText();	// +로그인 시 ip주소를 입력하지 않도록 해당 통신 서버의 ip를 받아옴
			try {
				// 서버접속
				InetSocketAddress inetSockAddr = new InetSocketAddress(
						InetAddress.getByName(IP), PORT);
				socket = new Socket();

				// 지정된 주소로 접속 시도 (3초동안)
				socket.connect(inetSockAddr, 3000);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// 접속이 되면 실행
			if (socket.isBound()) {
				// 입력, 출력 스트림 생성
				try {
					dis = new DataInputStream(socket.getInputStream());
					dos = new DataOutputStream(socket.getOutputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}
				ready = true;
			}
		}
		return ready;
	}

	// 데이터를 구분
	public synchronized void dataParsing(String data) {
		StringTokenizer token = new StringTokenizer(data, "/"); // 토큰 생성
		String protocol = token.nextToken(); // 토큰으로 분리된 스트링
		String id, pw, name, rNum, rType, nick, rName, msg, result;
		System.out.println("받은 데이터 : " + data);
		
		switch (protocol) {
		case User.FRIEND:
			String friendAddArray="";//친구추가할 friend 임시저장할 스트링
			friendAddArray=token.nextToken();
			friendList(friendAddArray);
			break;
		
		case User.LOGIN: // 로그인
			// 사용자가 입력한(전송한) 아이디와 패스워드
			result = token.nextToken();
			if (result.equals("OK")) {
				nick = token.nextToken();
				name = token.nextToken();
				login(nick, name);
			} else {
				msg = token.nextToken();
				errorMsg(msg);
			}
			while(token.hasMoreTokens()){//ServerThread에서 읽어들인 친구목록 friendarray에삽입
				user.getfriendArray().add(token.nextToken());
			}
			break;
			
		case User.LOGOUT:
			logout();
			break;
		case User.MEMBERSHIP: // 회원가입 승인
			result = token.nextToken();
			System.out.println(result);
			if (result.equals("OK")) {
				errorMsg("성공!");
			} else {
				errorMsg("이미 가입되어 있는 아이디입니다.");
			}
			break;
		case User.INVITE: // 초대받기
			id = null;
			// 한명씩 초대
			while (token.hasMoreTokens()) {
				// 초대할 사람의 아이디와 방번호
				id = token.nextToken();
				rNum = token.nextToken();
				rName=token.nextToken();
				rType=token.nextToken();
				invite(id, rNum,rName,rType);
			}
			break;
		case User.UPDATE_USERLIST: // 대기실 사용자 목록
			userList(token);
			break;
		case User.UPDATE_ROOM_USERLIST: // 채팅방 사용자 목록
			// 방번호읽기
			rNum = token.nextToken();
			if(Integer.parseInt(rNum)!=0)	//문용 수정 나와의 채팅방일 경우 userList실행x
				userList(rNum, token);
			break;
		case User.UPDATE_SELECTEDROOM_USERLIST: // 대기실에서 선택한 채팅방의 사용자 목록
			selectedRoomUserList(token);
			break;

		case User.UPDATE_ROOMLIST: // 방 목록
			roomList(token);
			break;
		case User.CHANGE_NICK: // 닉네임 변경(대기실)
			nick = token.nextToken();
			name = token.nextToken();
			changeNick(nick, name);
			break;
		case User.ECHO01: // 대기실 에코
			msg = token.nextToken();
			echoMsg(msg);
			break;
		case User.ECHO02: // 채팅방 에코
			rNum = token.nextToken();
			msg = token.nextToken();
			echoMsgToRoom(rNum, msg);
			break;
	////////(문용, 추가) 
			case User.ECHO03: // 채팅방 에코
				rNum = token.nextToken();
				msg = token.nextToken();
				echoMsgToMyRoom(rNum, msg);
				break;
			case User.ECHOBOT:
				rNum = token.nextToken();
				msg = token.nextToken();
				echoMsgToRoomWithBot(rNum, msg);				//echoMsg(방 넘버, 메시지)실행
				break;
		/////////	
		case User.WHISPER: // 귓속말
			id = token.nextToken();
			nick = token.nextToken();
			name = token.nextToken();
			msg = token.nextToken();
			whisper(id, nick, name, msg);
			break;

		case User.OMOK_INVITE:
			id = token.nextToken();
			String lineNum = token.nextToken();
			String portNum = token.nextToken();
			omokStart(id, lineNum, portNum);
			break;
		}	
	}

	public void omokStart(String id, String lineNum, String portNum)
	{
		String[] select = {"수락", "거절"};
	
		String input = (String) JOptionPane.showInputDialog(null, id + "님이 오목을 신청하셨습니다.", "오목 신청", JOptionPane.QUESTION_MESSAGE, null, select, select[0]);
		if(input.equals("거절"))
		{
			System.out.println("오목 신청 거절");
			return;
		}
		//OmokGame omok = new OmokGame(Integer.parseInt(lineNum), true, portNum, false);
	}
	
	private void logout() {
		try {
			restRoom.dispose();
			user.getDis().close();
			user.getDos().close();
			socket.close();
			restRoom = null;
			user = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 채팅방 내부 사용자 리스트
	private void userList(String rNum, StringTokenizer token) {
		for (int i = 0; i < user.getRoomArray().size(); i++) {
			User tempUser = null;
			if (Integer.parseInt(rNum) == user.getRoomArray().get(i).getRoomNum())
			{
				// 기존에 리스트가 있을 경우 지워줌
				if (user.getRoomArray().get(i).getrUI().model != null)
					user.getRoomArray().get(i).getrUI().model.removeAllElements();
				while (token.hasMoreTokens()) {
					// 아이디와 닉네임을 읽어서 유저 객체 하나를 생성
					String id = token.nextToken();
					String nick = token.nextToken();
					String name = token.nextToken();
					tempUser = new User(id, nick, name);

					String rType = user.getRoomArray().get(i).getRoomType();	// 방 타입을 받아옴
					if(rType.equals("일반"))	// +일반 채팅방일 경우	목록에 이름과 아이디가 뜨게하고
						user.getRoomArray().get(i).getrUI().model.addElement(tempUser.toString());
					else	// +익명 채팅방일 경우 목록에 닉네임만 뜨게 설정
						user.getRoomArray().get(i).getrUI().model.addElement(tempUser.toNickNameString());
				}
			}
			try
			{
				for(int j=0; j<userArray.size(); j++)	// 온라인 유저 목록에 없는 사용자 정보라면 추가
				{
					if(userArray.get(j).getId().equals(tempUser.getId()))
						return;
				}
				userArray.add(tempUser);
			}
			catch(Exception e){	e.getMessage();}
		}
	}

	// 선택한 채팅방의 사용자 리스트
	private void selectedRoomUserList(StringTokenizer token) {
		User tempUser = null;
		// 서버로부터 유저리스트(채팅방)를 업데이트하라는 명령을 받음
		if (!restRoom.level_2_1.isLeaf()) {
			// 리프노드가 아니고, 차일드가 있다면 모두 지움
			restRoom.level_2_1.removeAllChildren();
		}

		String rNum = token.nextToken();	// +방 번호도 넘겨줌
		int roomNum = Integer.parseInt(rNum);
		String roomType = "일반";
		for(int i=0; i<getUser().getRoomArray().size(); i++)
		{
			if(getUser().getRoomArray().get(i).getRoomNum() == roomNum)
			{
				if(getUser().getRoomArray().get(i).getRoomType().equals("익명"))
				{
					roomType = "익명";
				}
			}
		}
		
		while (token.hasMoreTokens()) {
			// 아이디와 닉네임을 읽어서 유저 객체 하나를 생성
			String id = token.nextToken();
			String nick = token.nextToken();
			String name = token.nextToken();
			tempUser = new User(id, nick, name);
			// 채팅방 사용자노드에 추가
			if(roomType.equals("일반"))
				restRoom.level_2_1.add(new DefaultMutableTreeNode(tempUser.toString()));	// 트리의 모델에 추가
			else
				restRoom.level_2_1.add(new DefaultMutableTreeNode(tempUser.toNickNameString()));	// +방 타입 별로 나눠줌
		}
		restRoom.userTree.updateUI();
		try
		{
			for(int i=0; i<userArray.size(); i++)	// 온라인 유저 목록에 없는 사용자 정보라면 추가
			{
				if(userArray.get(i).getId().equals(tempUser.getId()))
					return;
			}
			userArray.add(tempUser);
		}
		catch(Exception e){	e.getMessage();}
	}

	// 대기실 사용자 리스트
	private void userList(StringTokenizer token) {
		User tempUser = null;
		// 서버로부터 유저리스트(대기실)를 업데이트하라는 명령을 받음
		if (restRoom == null) {
			return;
		}

		if (!restRoom.level_2_2.isLeaf()) {
			// 리프노드가 아니고, 차일드가 있다면 모두 지움
			restRoom.level_2_2.removeAllChildren();
		}
		while (token.hasMoreTokens()) {
			// 아이디와 닉네임을 읽어서 유저 객체 하나를 생성
			String id = token.nextToken();
			String nick = token.nextToken();
			String name = token.nextToken();
			tempUser = new User(id, nick, name);

			for (int i = 0; i < restRoom.userArray.size(); i++) {
				if (tempUser.getId().equals(restRoom.userArray.get(i))) {
				}
				if (i == restRoom.userArray.size()) {
					// 배열에 유저가 없으면 추가해줌
					restRoom.userArray.add(tempUser);
				}
			}
			// 대기실 사용자노드에 추가
			restRoom.level_2_2.add(new DefaultMutableTreeNode(tempUser.toString()));
		}
		restRoom.userTree.updateUI();
		try
		{
			for(int i=0; i<userArray.size(); i++)	// 온라인 유저 목록에 없는 사용자 정보라면 추가
			{
				if(!userArray.get(i).getId().equals(tempUser.getId()))
					return;
			}
			userArray.add(tempUser);
		}
		catch(Exception e){	e.getMessage();}
		
		if (restRoom == null) {//////////처음 로그인시친구목록트리 출력
			return;
		}
	
			// 대기실 사용자노드에 추가
		if (!restRoom.level1.isLeaf()) {
			// 리프노드가 아니고, 차일드가 있다면 모두 지움
			restRoom.level1.removeAllChildren();
		}
		for(int ko=0;ko<user.getfriendArray().size();ko++)
		restRoom.level1.add(new DefaultMutableTreeNode(user.getfriendArray().get(ko)));//로그인시userlist와 친구목록함께출력
		restRoom.friendTree.updateUI();
		
		
		
	}

	// 서버로부터 방리스트를 업데이트하라는 명령을 받음
	private void roomList(StringTokenizer token) {
		String rNum, rName, rType;	// +rType 추가
		Room room = new Room();

		// 기존에 리스트가 있을 경우 지워줌
		if (restRoom.model != null) {
			restRoom.model.removeAllElements();
		}

		while (token.hasMoreTokens()) {
			rNum = token.nextToken();
			rName = token.nextToken();
			rType = token.nextToken();	// +rType 추가
			int num = Integer.parseInt(rNum);

	////////(문용, 수정) 나와의 채팅방
				if(num == 0)
				{continue;}
	//////////
			
			// 라스트룸넘버를 업데이트 (최대값+1)
			if (num >= restRoom.lastRoomNum) {
				restRoom.lastRoomNum = num + 1;
			}
			room.setRoomNum(num);
			room.setRoomName(rName);
			room.setRoomType(rType);	// +rType set

			restRoom.model.addElement(room.toProtocol());
		}
	}

	private void errorMsg(String string) {
		int i = JOptionPane.showConfirmDialog(null, string, "이벤트 발생",
				JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
		// 확인 누르면 종료
		if (i == 0) {

		}
	}

	private void login(String nick, String name) {
		// 로그인정보 가져옴
		String id = login.idText.getText();	// +로그인한 아이디
		user.setId(id);	// +아이디 업데이트
		user.setNickName(nick);
		user.setName(name);
		userArray.add(user);

		// 로그인창 닫고 대기실창 열기
		restRoom = new RestRoomUI(SixClient.this);
		restRoom.setLocationRelativeTo(login.loginBtn);	// +상대 위치 지정
		login.dispose();
		restRoom.lb_id.setText(user.getId());
		restRoom.lb_ip.setText(user.getIP());
		restRoom.lb_nick.setText(user.getNickName());
		restRoom.lb_name.setText(user.getName());
	}

	private void whisper(String id, String nickName, String name, String msg) {
		restRoom.restRoomArea.append("("+id+")님으로 부터 : "+msg+"\n");	// 귓속말은 id로만 주고받게 수정
	}

	private void invite(String id, String rNum,String rName, String rType)	//친구초대 받은건 초대받은사람의아이디
	{	
		Room theRoom = new Room(rName); // 방 객체 생성
		theRoom.setRoomNum(Integer.parseInt(rNum)); // 방번호 설정
		theRoom.setRoomType(rType);	// +방 타입 설정
		theRoom.setrUI(new RoomUI(SixClient.this, theRoom, tab)); // UI
		user.getRoomArray().add(theRoom); // 채팅창을 띄움
		theRoom.getUserArray().add(user);	// 해당 유저를 추가
	}
	
	private void changeNick(String nick, String name) {
		user.setNickName(nick);
		user.setName(name);
		restRoom.lb_nick.setText(nick);
		restRoom.lb_name.setText(name);
	}

	private void echoMsg(String msg) {
		// 커서 위치 조정
		if (restRoom != null) {
			restRoom.restRoomArea.setCaretPosition(restRoom.restRoomArea.getText().length());
			restRoom.restRoomArea.append(msg + "\n");
		}
	}

	private void echoMsgToRoom(String rNum, String msg) {
		for (int i = 0; i < user.getRoomArray().size(); i++) {
			if (Integer.parseInt(rNum) == user.getRoomArray().get(i)
					.getRoomNum()) {

				// 사용자 -> 방배열 -> 유아이 -> 텍스트에어리어
				// 커서 위치 조정
				user.getRoomArray().get(i).getrUI().chatArea
						.setCaretPosition(user.getRoomArray().get(i).getrUI().chatArea
								.getText().length());
				// 에코
				user.getRoomArray().get(i).getrUI().chatArea.append(msg + "\n");
			}
		}
	}
	//////////문용, 추가
	private void echoMsgToMyRoom(String rNum, String msg) {	
		for (int i = 0; i < user.getRoomArray().size(); i++) {
			if (Integer.parseInt(rNum) == user.getRoomArray().get(i).getRoomNum()) {
				// 사용자 -> 방배열 -> 유아이 -> 텍스트에어리어
				// 커서 위치 조정
				user.getRoomArray().get(i).getmrUI().chatArea
						.setCaretPosition(user.getRoomArray().get(i).getmrUI().chatArea
								.getText().length());
				// 에코<실제 방에 메시지 전달하는 역할>
				user.getRoomArray().get(i).getmrUI().chatArea.append(msg + "\n");
			}
		}
	}
	///////////
	
	
	private void friendList(String friendaddarray) {
		user.getfriendArray().add(friendaddarray);
		if (restRoom == null) {
			return;
		}
		if (!restRoom.level1.isLeaf()) {
			// 리프노드가 아니고, 차일드가 있다면 모두 지움
			restRoom.level1.removeAllChildren();
		}
		
		for(int ko=0;ko<user.getfriendArray().size();ko++)
			restRoom.level1.add(new DefaultMutableTreeNode(user.getfriendArray().get(ko)));
		
		int ko=user.getfriendArray().size()-1;
		restRoom.friendTree.updateUI();
	}
	
//////////(문용, 추가) 	chatBot이 대답하는  코드
private void echoMsgToRoomWithBot(String rNum, String msg) {			//방번호와, 입력받은 메시지
	for (int i = 0; i < user.getRoomArray().size(); i++) {
		if (Integer.parseInt(rNum) == user.getRoomArray().get(i)
				.getRoomNum()) {

			// 사용자 -> 방배열 -> 유아이 -> 텍스트에어리어
			// 커서 위치 조정
			user.getRoomArray().get(i).getmrUI().chatArea
					.setCaretPosition(user.getRoomArray().get(i).getmrUI().chatArea
							.getText().length());
			// 에코<실제 방에 메시지 전달하는 역할>
			user.getRoomArray().get(i).getmrUI().chatArea.append(msg + "\n");
		}
	}
}
///////

	
	// getter, setter
	public static int getPORT() {
		return PORT;
	}

	public static void setPORT(int pORT) {
		PORT = pORT;
	}

	public static String getIP() {
		return IP;
	}

	public static void setIP(String iP) {
		IP = iP;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LoginUI getLogin() {
		return login;
	}

	public void setLogin(LoginUI login) {
		this.login = login;
	}

	public RestRoomUI getRestRoom() {
		return restRoom;
	}

	public void setRestRoom(RestRoomUI restRoom) {
		this.restRoom = restRoom;
	}

	public DataInputStream getDis() {
		return dis;
	}

	public void setDis(DataInputStream dis) {
		this.dis = dis;
	}

	public DataOutputStream getDos() {
		return dos;
	}

	public void setDos(DataOutputStream dos) {
		this.dos = dos;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	/**
	 * @return the userArray
	 */
	public ArrayList<User> getUserArray() {
		return userArray;
	}

	/**
	 * @param userArray the userArray to set
	 */
	public void setUserArray(ArrayList<User> userArray) {
		this.userArray = userArray;
	}
}
