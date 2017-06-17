package Chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JTextArea;

public class ServerThread implements Runnable {

	private ArrayList<User> userArray; // 서버에 접속한 사용자들
	private ArrayList<Room> roomArray; // 서버가 열어놓은 채팅방들
	private User user; // 현재 스레드와 연결된(소켓이 생성된) 사용자
	private JTextArea jta;
	private boolean onLine = true;
	//------------------------------------------------------------------//
	// id을 전역변수 선언
	private String id;
	//------------------------------------------------------------------//
	private DataOutputStream thisUser;
	
	ServerThread(JTextArea jta, User person, ArrayList<User> userArray, ArrayList<Room> roomArray) {
		this.roomArray = roomArray;
		this.userArray = userArray;
		this.userArray.add(person); // 배열에 사용자 추가
		this.user = person;
		this.jta = jta;
		this.thisUser = person.getDos();
	}

	@Override
	public void run() {
		DataInputStream dis = user.getDis(); // 입력 스트림 얻기

		while (onLine) {
			try {
				String receivedMsg = dis.readUTF(); // 메시지 받기(대기)
				dataParsing(receivedMsg); // 메시지 해석
				jta.append("성공 : 메시지 읽음 -" + receivedMsg + "\n");
				jta.setCaretPosition(jta.getText().length());
			} catch (IOException e) {
				try {
					user.getDis().close();
				} catch (IOException e1) {
					e1.printStackTrace();
				} finally {
					jta.append("에러 : 서버스레드-읽기 실패\n");
					userArrayDelete();	// 서버에서 떠난 클라이언트를 제외시키는 메소드
					break;
				}
			}
		}
	}
	
	public void userArrayDelete(){
		//------------------------------------------------------------------//
		//회원탈퇴 후 userArray에서 정보를 삭제하기 위한 부분
		for (int i = 0; i < userArray.size(); i++) {
			if (id.equals(userArray.get(i).getId())) {
				userArray.remove(i);
			}
		}
		//------------------------------------------------------------------//
	}

	// 데이터를 구분
	public synchronized void dataParsing(String data) {
		StringTokenizer token = new StringTokenizer(data, "/"); // 토큰 생성
		String protocol = token.nextToken(); // 토큰으로 분리된 스트링을 숫자로
		String pw, rType, rNum, nick, name, rName, msg;	// +이름 추가
		System.out.println("서버가 받은 데이터 : " + data);

		switch (protocol) {
		case User.FRIEND: // 로그인
			// 사용자가 입력한(전송한) 아이디와 패스워드
			String id2=token.nextToken();
			String friend2=token.nextToken();
			//jta.append("확인zzz:"+id2+"/"+friend2+"\n");
		add(id2,friend2,thisUser);
			
			break;
		
		case User.LOGIN: // 로그인
			// 사용자가 입력한(전송한) 아이디와 패스워드
			id = token.nextToken();
			pw = token.nextToken();
			login(id, pw);
			break;
		case User.LOGOUT: // 로그아웃
			logout();
			break;
		case User.MEMBERSHIP: // 회원가입
			id = token.nextToken();
			pw = token.nextToken();
			name = token.nextToken();	// +이름 추가
			member(id, pw, name);
			break;
		case User.INVITE: // 초대하기
			id = null;
			// 한명씩 초대
			while (token.hasMoreTokens()) {
				// 초대할 사람의 아이디와 방번호
				id = token.nextToken();
				rNum = token.nextToken();
				invite(id, rNum);
			}
			break;
		case User.UPDATE_USERLIST: // 대기실 사용자 목록
			userList(thisUser);
			break;
		case User.UPDATE_ROOM_USERLIST: // 채팅방 사용자 목록
			// 방번호읽기
			rNum = token.nextToken();
			userList(rNum, thisUser);
			break;
		case User.UPDATE_SELECTEDROOM_USERLIST: // 대기실에서 선택한 채팅방의 사용자 목록
			// 방번호읽기
			rNum = token.nextToken();
			selectedRoomUserList(rNum, thisUser);
			break;

		case User.UPDATE_ROOMLIST: // 방 목록
			roomList(thisUser);
			break;
		case User.CHANGE_NICK: // 닉네임 변경(대기실)
			nick = token.nextToken();
			name = token.nextToken();
			changeNick(nick, name);
			break;
		case User.CREATE_ROOM: // 방만들기
			rNum = token.nextToken();
			rName = token.nextToken();
			rType = token.nextToken();	// +방 타입 추가
			createRoom(rNum, rName, rType);	// +방 타입도 매개변수로 줌
			break;
		case User.GETIN_ROOM:
			rNum = token.nextToken();
			getInRoom(rNum);
			break;
		case User.GETOUT_ROOM:
			rNum = token.nextToken();
			getOutRoom(rNum);
			break;
		case User.ECHO01: // 대기실 에코
			msg = token.nextToken();
			echoMsg(User.ECHO01 + "/" + user.toString() + msg);
			break;
		case User.ECHO02: // 채팅방 에코
			rNum = token.nextToken();
			msg = token.nextToken();
			echoMsg(rNum, msg);
			break;
		case User.WHISPER: // 귓속말
			id = token.nextToken();
			msg = token.nextToken();
			whisper(id, msg);
			break;
		}
	}

	public void alarm() {

	}

	private void getOutRoom(String rNum) {
		for (int i = 0; i < roomArray.size(); i++) {
			if (Integer.parseInt(rNum) == roomArray.get(i).getRoomNum()) {
				// 방에서 나가기
				// 채팅방의 유저리스트에서 사용자 삭제
				for (int j = 0; j < roomArray.get(i).getUserArray().size(); j++) {
					if (user.getId().equals(roomArray.get(i).getUserArray().get(j).getId())) {roomArray.get(i).getUserArray().remove(j);
					}
				}

				// 사용자의 방리스트에서 방을 제거
				for (int j = 0; j < user.getRoomArray().size(); j++) {
					if (Integer.parseInt(rNum) == user.getRoomArray().get(j).getRoomNum()) {user.getRoomArray().remove(j);
					}
				}
				echoMsg(roomArray.get(i), user.toString() + "님이 퇴장하셨습니다.");
				userList(rNum);

				if (roomArray.get(i).getUserArray().size() <= 0) {
					roomArray.remove(i);
					roomList();
				}
			}
		}
	}

	private void getInRoom(String rNum) {
		for (int i = 0; i < roomArray.size(); i++) {
			if (Integer.parseInt(rNum) == roomArray.get(i).getRoomNum()) {
				// 방 객체가 있는 경우, 방에 사용자추가
				ArrayList<User> al = roomArray.get(i).getUserArray();
				al.add(user);
				roomArray.get(i).setUserArray(al);
				
				// 사용자 객체에 방 추가
				user.getRoomArray().add(roomArray.get(i));
				String roomType = roomArray.get(i).getRoomType();	// +방 종류에 따라 메시지를 분리시켜줌
				if(roomType.equals("일반"))
					echoMsg(roomArray.get(i), user.toString() + "님이 입장하셨습니다.");
				else
					echoMsg(roomArray.get(i), user.toNickNameString() + "님이 입장하셨습니다.");
				userList(rNum);
			}
		}
	}

	private void createRoom(String rNum, String rName, String rType) {
		Room rm = new Room(rName); // 지정한 제목으로 채팅방 생성
		rm.setMaker(user); // 방장 설정
		rm.setRoomNum(Integer.parseInt(rNum)); // 방번호 설정
		rm.setRoomType(rType);
		
		rm.getUserArray().add(user); // 채팅방에 유저(본인) 추가
		roomArray.add(rm); // 룸리스트에 현재 채팅방 추가
		user.getRoomArray().add(rm); // 사용자 객체에 접속한 채팅방을 저장
		
		if(rType.equals("일반"))	// +익명방과 일반방 개설을 나눠줌
		{
			echoMsg(User.ECHO01 + "/" + user.toString() + "님이 " + rm.getRoomNum()+ "번 일반 채팅방을 개설하셨습니다.");
			echoMsg(rm, user.toString() + "님이 입장하셨습니다.");
		}
		else
		{
			echoMsg(User.ECHO01 + "/" + user.toNickNameString() + "님이 " + rm.getRoomNum()+ "번 익명 채팅방을 개설하셨습니다.");		// + 방 타입마다 다른 메시지가 나오게 수정
			echoMsg(rm, user.toNickNameString() + "님이 입장하셨습니다.");
		}
		roomList();
		userList(rNum, thisUser);
		jta.append("성공 : " + userArray.toString() + "가 채팅방생성\n");
	}

	private void whisper(String id, String msg) {
		for (int i = 0; i < userArray.size(); i++) {
			if (id.equals(userArray.get(i).getId())) {
				// 귓속말 상대를 찾으면
				try {
					userArray.get(i).getDos().writeUTF(User.WHISPER + "/" + user.toProtocol()+ "/" + msg);
					jta.append("성공 : 귓속말보냄 : " + user.toString() + "가 "+ userArray.get(i).toString() + "에게" + "\n");	// +귓속말을 이름으로 주고받게 수정
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 대기실 에코
	private void echoMsg(String msg) {
		for (int i = 0; i < userArray.size(); i++) {
			try {
				userArray.get(i).getDos().writeUTF(msg);
				jta.append(user.toString() + " - " + msg + "\n");
			} catch (IOException e) {
				e.printStackTrace();
				jta.append("에러 : 에코 실패\n");
			}
		}
	}

	// 방 에코 (방 번호만 아는 경우)
	private void echoMsg(String rNum, String msg) {
		for (int i = 0; i < roomArray.size(); i++) {
			if (Integer.parseInt(rNum) == roomArray.get(i).getRoomNum()) {
				echoMsg(roomArray.get(i), msg);
			}
		}
	}

	// 방 에코 (방객체가 있는 경우)
	private void echoMsg(Room room, String msg) {
		for (int i = 0; i < room.getUserArray().size(); i++) {
			try {
				// 방에 참가한 유저들에게 에코 메시지 전송
				room.getUserArray().get(i).getDos().writeUTF(User.ECHO02 + "/" + room.getRoomNum() + "/"+ msg + "/" + room.getRoomType());	// +roomType도 받아오게 추가
				jta.append("성공 : 메시지전송 : " + msg + "\n");
			} catch (IOException e) {
				e.printStackTrace();
				jta.append("에러 : 에코 실패\n");
			}
		}
	}

	// 대기실닉네임 변경
	private void changeNick(String nick, String name) {
		File file = new File("D:\\" + user.getId() + ".txt");
		FileWriter f;
		try {
			f = new FileWriter(file);
			// 파일에 회원정보쓰기 (아이디+패스워드+닉네임+이름)
			f.write(user.getId() + "/" + user.getPw() + "/" + nick + "/" + name);	// +이름도 받아오게 추가
			f.close();
			thisUser.writeUTF(User.MEMBERSHIP + "/OK");
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// 닉네임은 중복허용함
		jta.append("성공 : 닉네임변경 : " + user.getId() + "님의 닉네임 +"+ user.getNickName() + "을 " + nick + "로 변경");
		user.setNickName(nick);
		user.setName(name);

		try {
			user.getDos().writeUTF(User.CHANGE_NICK + "/" + nick + "/" + name);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < userArray.size(); i++) {
			userList(userArray.get(i).getDos());
		}
		// 이 사람이 접속한 모든 방의 사용자리스트를 업데이트
		for (int i = 0; i < user.getRoomArray().size(); i++) {
			userList(String.valueOf(user.getRoomArray().get(i).getRoomNum()));
		}
	}

	private void invite(String id, String rNum) {
		for (int i = 0; i < userArray.size(); i++) {
			// 초대할사람을 찾아서 초대메시지 보냄
			if (id.equals(userArray.get(i).getId())) {
				try {
					// 초대한 사람의 아이디와 방번호를 전송
					userArray
							.get(i)
							.getDos()
							.writeUTF(
									User.INVITE + "/" + user.getId() + "/"+ rNum);
				} catch (IOException e) {
					e.printStackTrace();
					jta.append("에러 : 초대실패-" + userArray.toString() + "\n");
				}
			}
		}
	}

	private void member(String id, String pw, String name) {	// +이름도 받아오게 추가
		User newUser = new User();
		newUser.setId(id);
		newUser.setPw(pw);
		newUser.setName(name);	// + 이름 설정 추가
		newUser.setNickName(name);	// +회원가입 시 입력한 이름을 최초 닉네임으로 줌 
			
		
		try {
			File file = new File("D:\\" + id + ".txt");
			if (!file.isFile()) {
				FileWriter f = new FileWriter(file);

				// 파일에 회원정보쓰기 (아이디+패스워드+닉네임+이름)
				f.write(newUser.toStringforLogin());
				f.close();
				thisUser.writeUTF(User.MEMBERSHIP + "/OK");
				jta.append("성공 : 회원가입 파일생성\n");
			} else {
				// 파일이 존재하는 경우
				thisUser.writeUTF(User.MEMBERSHIP + "/fail");
				jta.append("에러 : 이미 가입된 회원\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
			try {
				thisUser.writeUTF(User.MEMBERSHIP + "/fail");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			jta.append("에러 : 회원가입 파일생성\n");
		}
	}

	private void login(String id, String pw) {

		FileReader reader = null;
		FileWriter writer = null;	// +FileWriter 추가
		int inputValue = 0;
		String str="";
		String fr="";
		
		
		try {
			// 파일 열기
			reader = new FileReader("D:\\" + id + ".txt");
			while ((inputValue = reader.read()) != -1) {
				// 파일 읽음
				str+=((char) inputValue);
			}
			jta.append("성공 : 파일 읽기 : D:\\" + id + ".txt\n");
			writer = new FileWriter("D:\\recent.txt");	// +최근 로그인한 id파일
			writer.write(id);	// +id를 파일에 쓰기
			reader.close();
			writer.close();	// +파일닫기
			String token[]=str.split("/"); // 토큰
			// 생성

			try {
				if (id.equals(token[0])) {
					if (pw.equals(token[1])) {
						for (int i = 0; i < userArray.size(); i++) {
							if (id.equals(userArray.get(i).getId())) {
								try {
									System.out.println("접속중");
									thisUser.writeUTF(User.LOGIN
											+ "/fail/이미 접속 중입니다.");
								} catch (IOException e) {
									e.printStackTrace();
								}
								return;
							}
						}

						// 로그인 OK
						user.setId(id);
						user.setPw(pw);
						user.setNickName(token[2]);
						user.setName(token[3]);	// +이름 set

						int hex=0;
						for(hex=4;hex<token.length;hex++)
						{if(hex== token.length-1)
							fr+=token[hex];
							else
								fr+=token[hex]+"/";
						}
						thisUser.writeUTF(User.LOGIN + "/OK/"+ user.getNickName() + "/" + user.getName()+"/"+fr);
						this.user.setOnline(true);
						// 대기실에 에코
						echoMsg(User.ECHO01 + "/" + user.toString()+ "님이 입장하셨습니다.");	// +이름으로 수정
						jta.append(user.toString() + " : 님이 입장하셨습니다.\n");	// +이름으로 수정

						roomList(thisUser);
						for (int i = 0; i < userArray.size(); i++) {
							userList(userArray.get(i).getDos());
						}
					} else {
						thisUser.writeUTF(User.LOGIN + "/fail/패스워드가 일치하지 않습니다.");
						jta.append("에러 : 로그인-패스워드가 일치하지 않습니다. : " + pw + "\n");
					}
				} else {
					thisUser.writeUTF(User.LOGIN + "/fail/아이디가 존재하지 않습니다.");
					jta.append("에러 : 로그인-아이디가 일치하지 않습니다. : " + id + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
				thisUser.writeUTF(User.LOGIN + "/fail/로그인실패");
				jta.append("에러 : 로그인 실패" + pw + "\n");
			}
		} catch (Exception e) {
			try {
				thisUser.writeUTF(User.LOGIN + "/fail/아이디가 존재하지 않습니다.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			jta.append("실패 : 파일 읽기\n");
			return;
		}

	}

	private void logout() {
		System.out.println("로그아웃");

		// 오프라인으로 바꿈
		user.setOnline(false);
		// 사용자배열에서 삭제
		for (int i = 0; i < userArray.size(); i++) {
			if (user.getId().equals(userArray.get(i).getId())) {
				System.out.println(userArray.get(i).getId() + "지웠습니다.");
				userArray.remove(i);
			}
		}
		// room 클래스의 멤버변수인 사용자배열에서 삭제
		for (int i = 0; i < roomArray.size(); i++) {
			for (int j = 0; j < roomArray.get(i).getUserArray().size(); j++) {
				if (user.getId().equals(
						roomArray.get(i).getUserArray().get(j).getId())) {
					roomArray.get(i).getUserArray().remove(j);
				}
			}
		}
		echoMsg(User.ECHO01 + "/" + user.toString() + "님이 퇴장하셨습니다.");

		for (int i = 0; i < userArray.size(); i++) {
			userList(userArray.get(i).getDos());
		}

		jta.append(user.getId() + " : 님이 퇴장하셨습니다.\n");

		try {
			user.getDos().writeUTF(User.LOGOUT);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			user.getDis().close();
			user.getDos().close();
			user = null;
			jta.append("성공 : 스트림 닫기\n");
		} catch (IOException e) {
			e.printStackTrace();
			jta.append("실패 : 스트림 닫기\n");
		}
	}

	// 사용자 리스트 (선택한 채팅방)
	public void selectedRoomUserList(String rNum, DataOutputStream target) {
		String ul = "";

		for (int i = 0; i < roomArray.size(); i++) {
			if (Integer.parseInt(rNum) == roomArray.get(i).getRoomNum()) {
				for (int j = 0; j < roomArray.get(i).getUserArray().size(); j++) {
					// 채팅방에 접속되어 있는 유저들의 아이디+닉네임
					ul += "/" + roomArray.get(i).getUserArray().get(j).toProtocol();
				}
			}
		}
		try {
			// 데이터 전송
			target.writeUTF(User.UPDATE_SELECTEDROOM_USERLIST + ul);
			jta.append("성공 : 목록(사용자)-" + ul + "\n");
		} catch (IOException e) {
			jta.append("에러 : 목록(사용자) 전송 실패\n");
		}
	}

	// 사용자 리스트 (대기실)
	public String userList(DataOutputStream target) {
		String ul = "";

		for (int i = 0; i < userArray.size(); i++) {
			// 접속되어 있는 유저들의 아이디+닉네임
			ul += "/" + userArray.get(i).toProtocol();
		}

		try {
			// 데이터 전송
			target.writeUTF(User.UPDATE_USERLIST + ul);
			jta.append("성공 : 목록(사용자)-" + ul + "\n");
		} catch (IOException e) {
			jta.append("에러 : 목록(사용자) 전송 실패\n");
		}
		return ul;
	}

	// 사용자 리스트 (채팅방 내부)
	public void userList(String rNum, DataOutputStream target) {
		String ul = "/" + rNum;

		for (int i = 0; i < roomArray.size(); i++) {
			if (Integer.parseInt(rNum) == roomArray.get(i).getRoomNum()) {
				for (int j = 0; j < roomArray.get(i).getUserArray().size(); j++) {
					// 채팅방에 접속되어 있는 유저들의 아이디+닉네임
					ul += "/" + roomArray.get(i).getUserArray().get(j).toProtocol();
				}
			}
		}
		try {
			// 데이터 전송
			target.writeUTF(User.UPDATE_ROOM_USERLIST + ul);
			jta.append("성공 : 목록(사용자)-" + ul + "\n");
		} catch (IOException e) {
			jta.append("에러 : 목록(사용자) 전송 실패\n");
		}
	}

	// 사용자 리스트 (채팅방 내부 모든 사용자들에게 전달)
	public void userList(String rNum) {
		String ul = "/" + rNum;
		Room temp = null;
		for (int i = 0; i < roomArray.size(); i++) {
			if (Integer.parseInt(rNum) == roomArray.get(i).getRoomNum()) {
				temp = roomArray.get(i);
				for (int j = 0; j < roomArray.get(i).getUserArray().size(); j++) {
					// 채팅방에 접속되어 있는 유저들의 아이디+닉네임
					ul += "/"
							+ roomArray.get(i).getUserArray().get(j)
									.toProtocol();
				}
			}
		}
		for (int i = 0; i < temp.getUserArray().size(); i++) {
			try {
				// 데이터 전송
				temp.getUserArray().get(i).getDos()
						.writeUTF(User.UPDATE_ROOM_USERLIST + ul);
				jta.append("성공 : 목록(사용자)-" + ul + "\n");
			} catch (IOException e) {
				jta.append("에러 : 목록(사용자) 전송 실패\n");
			}
		}
	}

	// 채팅 방리스트
	public void roomList(DataOutputStream target) {
		String rl = "";

		for (int i = 0; i < roomArray.size(); i++) {
			// 만들어진 채팅방들의 제목
			rl += "/" + roomArray.get(i).toProtocol();
		}

		jta.append("test\n");

		try {
			// 데이터 전송
			target.writeUTF(User.UPDATE_ROOMLIST + rl);
			jta.append("성공 : 목록(방)-" + rl + "\n");
		} catch (IOException e) {
			jta.append("에러 : 목록(방) 전송 실패\n");
		}
	}
	////////////////////////////////////////////////////////////
public void add(String id,String friend,DataOutputStream target) {//친구목록추가
		

		File file = new File("D:\\" + id + ".txt");
		FileReader reader2;
		int inputValue = 0;
		String str="";
		String temp="";
		String userid="";
		String userpw="";
		String usernick="";
		String username="";
		jta.append("확인하려고함:"+id+"과"+friend+"\n");
		try {
			// 파일 열기
			reader2 = new FileReader(file);
			while ((inputValue = reader2.read()) != -1) {
				// 파일 읽음
				str+=((char) inputValue);
			}
			jta.append("성공 : 파일 읽기 : D:\\" + id + ".txt\n"+str+"<--str\n");
			reader2.close();
			String token[]=str.split("/");
			//token[token.length]=friend;//추가한부분<-----------여기서 오류났었음
			for(int zi=4;zi<token.length;zi++)
			{	if(zi==token.length-1)
				temp+=token[zi];
			else
				temp+=token[zi]+"/";
			}
	
			//	jta.append("이것은 파일쓰기확인이다 :"+token[0]+"/"+token[1]+"/"+token[2]+"/"+temp+"\n");
				target.writeUTF(User.FRIEND+"/"+friend);
		userid=token[0];
		userpw=token[1];
		usernick=token[2];
		username=token[3];
		}
			catch (IOException e) {
				e.printStackTrace();
				jta.append("오류나서 실패~\n");
			}
			//StringTokenizer token = new StringTokenizer(str.toString(), "/"); // 토큰
			File file2 = new File("D:\\" + id+ ".txt");
			FileWriter f;
			try {
				f = new FileWriter(file2);
				// 파일에 회원정보쓰기 (아이디+패스워드+닉네임)
				f.write(userid+"/"+userpw+"/"+usernick+"/"+username+"/"+temp+"/"+friend);
				f.close();
			//	thisUser.writeUTF(User.MEMBERSHIP + "/OK");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
	}
///////////////////////////////////////////////////////
	// 채팅 방리스트
	public void roomList() {
		String rl = "";

		for (int i = 0; i < roomArray.size(); i++) {
			// 만들어진 채팅방들의 제목
			rl += "/" + roomArray.get(i).toProtocol();
		}

		jta.append("test\n");

		for (int i = 0; i < userArray.size(); i++) {

			try {
				// 데이터 전송
				userArray.get(i).getDos().writeUTF(User.UPDATE_ROOMLIST + rl);
				jta.append("성공 : 목록(방)-" + rl + "\n");
			} catch (IOException e) {
				jta.append("에러 : 목록(방) 전송 실패\n");
			}
		}
	}
}
