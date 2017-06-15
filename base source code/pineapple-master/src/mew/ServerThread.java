package mew;

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

	private DataOutputStream thisUser;

	ServerThread(JTextArea jta, User person, ArrayList<User> userArray,
			ArrayList<Room> roomArray) {
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
					break;
				}
			}
		}
	}

	// 데이터를 구분
	public synchronized void dataParsing(String data) {
		StringTokenizer token = new StringTokenizer(data, "/"); // 토큰 생성
		String protocol = token.nextToken(); // 토큰으로 분리된 스트링을 숫자로
		String id, pw, rNum, nick, rName, msg;
		System.out.println("서버가 받은 데이터 : " + data);

		switch (protocol) {
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
			member(id, pw);
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
			changeNick(nick);
			break;
		case User.CREATE_ROOM: // 방만들기
			rNum = token.nextToken();
			rName = token.nextToken();
			createRoom(rNum, rName);
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
					if (user.getId().equals(
							roomArray.get(i).getUserArray().get(j).getId())) {
						roomArray.get(i).getUserArray().remove(j);
					}
				}

				// 사용자의 방리스트에서 방을 제거
				for (int j = 0; j < user.getRoomArray().size(); j++) {
					if (Integer.parseInt(rNum) == user.getRoomArray().get(j)
							.getRoomNum()) {
						user.getRoomArray().remove(j);
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
				roomArray.get(i).getUserArray().add(user);
				// 사용자 객체에 방 추가
				user.getRoomArray().add(roomArray.get(i));
				echoMsg(roomArray.get(i), user.toString() + "님이 입장하셨습니다.");
				userList(rNum);
			}
		}
	}

	private void createRoom(String rNum, String rName) {
		Room rm = new Room(rName); // 지정한 제목으로 채팅방 생성
		rm.setMaker(user); // 방장 설정
		rm.setRoomNum(Integer.parseInt(rNum)); // 방번호 설정

		rm.getUserArray().add(user); // 채팅방에 유저(본인) 추가
		roomArray.add(rm); // 룸리스트에 현재 채팅방 추가
		user.getRoomArray().add(rm); // 사용자 객체에 접속한 채팅방을 저장

		echoMsg(User.ECHO01 + "/" + user.toString() + "님이 " + rm.getRoomNum()
				+ "번 채팅방을 개설하셨습니다.");
		echoMsg(rm, user.toString() + "님이 입장하셨습니다.");
		roomList();
		userList(rNum, thisUser);
		jta.append("성공 : " + userArray.toString() + "가 채팅방생성\n");
	}

	private void whisper(String id, String msg) {
		for (int i = 0; i < userArray.size(); i++) {
			if (id.equals(userArray.get(i).getId())) {
				// 귓속말 상대를 찾으면
				try {
					userArray
							.get(i)
							.getDos()
							.writeUTF(
									User.WHISPER + "/" + user.toProtocol()
											+ "/" + msg);
					jta.append("성공 : 귓속말보냄 : " + user.toString() + "가 "
							+ userArray.get(i).toString() + "에게" + "\n");
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
				room.getUserArray()
						.get(i)
						.getDos()
						.writeUTF(
								User.ECHO02 + "/" + room.getRoomNum() + "/"
										+ msg);
				jta.append("성공 : 메시지전송 : " + msg + "\n");
			} catch (IOException e) {
				e.printStackTrace();
				jta.append("에러 : 에코 실패\n");
			}
		}
	}

	// 대기실닉네임 변경
	private void changeNick(String nick) {
		File file = new File("D:\\" + user.getId() + ".txt");
		FileWriter f;
		try {
			f = new FileWriter(file);
			// 파일에 회원정보쓰기 (아이디+패스워드+닉네임)
			f.write(user.getId() + "/" + user.getPw() + "/" + nick);
			f.close();
			thisUser.writeUTF(User.MEMBERSHIP + "/OK");
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// 닉네임은 중복허용함
		jta.append("성공 : 닉네임변경 : " + user.getId() + "님의 닉네임 +"
				+ user.getNickName() + "을 " + nick + "로 변경");
		user.setNickName(nick);

		try {
			user.getDos().writeUTF(User.CHANGE_NICK + "/" + nick);
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
									User.INVITE + "/" + user.getId() + "/"
											+ rNum);
				} catch (IOException e) {
					e.printStackTrace();
					jta.append("에러 : 초대실패-" + userArray.toString() + "\n");
				}
			}
		}
	}

	private void member(String id, String pw) {
		User newUser = new User();
		newUser.setId(id);
		newUser.setPw(pw);

		try {
			File file = new File("D:\\" + id + ".txt");
			if (!file.isFile()) {
				FileWriter f = new FileWriter(file);

				// 파일에 회원정보쓰기 (아이디+패스워드+닉네임)
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
		int inputValue = 0;
		StringBuffer str = new StringBuffer();
		try {
			// 파일 열기
			reader = new FileReader("D:\\" + id + ".txt");
			while ((inputValue = reader.read()) != -1) {
				// 파일 읽음
				str.append((char) inputValue);
			}
			jta.append("성공 : 파일 읽기 : D:\\" + id + ".txt\n");
			reader.close();
			StringTokenizer token = new StringTokenizer(str.toString(), "/"); // 토큰
			// 생성

			try {
				if (id.equals(token.nextToken())) {
					if (pw.equals(token.nextToken())) {
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
						user.setNickName(token.nextToken());
						thisUser.writeUTF(User.LOGIN + "/OK/"
								+ user.getNickName());
						this.user.setOnline(true);

						// 대기실에 에코
						echoMsg(User.ECHO01 + "/" + user.toString()
								+ "님이 입장하셨습니다.");
						jta.append(id + " : 님이 입장하셨습니다.\n");

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
				System.out.println(userArray.get(i).getId() + "지웠다.");
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
					ul += "/"
							+ roomArray.get(i).getUserArray().get(j)
									.toProtocol();
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
					ul += "/"
							+ roomArray.get(i).getUserArray().get(j)
									.toProtocol();
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
