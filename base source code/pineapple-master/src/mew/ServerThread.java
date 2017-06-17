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

	private ArrayList<User> userArray; // ������ ������ ����ڵ�
	private ArrayList<Room> roomArray; // ������ ������� ä�ù��
	private User user; // ���� ������� �����(������ ������) �����
	private JTextArea jta;
	private boolean onLine = true;
	//------------------------------------------------------------------//
	// id�� �������� ����
	private String id;
	//------------------------------------------------------------------//
	private DataOutputStream thisUser;
	
	ServerThread(JTextArea jta, User person, ArrayList<User> userArray, ArrayList<Room> roomArray) {
		this.roomArray = roomArray;
		this.userArray = userArray;
		this.userArray.add(person); // �迭�� ����� �߰�
		this.user = person;
		this.jta = jta;
		this.thisUser = person.getDos();
	}

	@Override
	public void run() {
		DataInputStream dis = user.getDis(); // �Է� ��Ʈ�� ���

		while (onLine) {
			try {
				String receivedMsg = dis.readUTF(); // �޽��� �ޱ�(���)
				dataParsing(receivedMsg); // �޽��� �ؼ�
				jta.append("���� : �޽��� ���� -" + receivedMsg + "\n");
				jta.setCaretPosition(jta.getText().length());
			} catch (IOException e) {
				try {
					user.getDis().close();
				} catch (IOException e1) {
					e1.printStackTrace();
				} finally {
					jta.append("���� : ����������-�б� ����\n");
					userArrayDelete();	// �������� ���� Ŭ���̾�Ʈ�� ���ܽ�Ű�� �޼ҵ�
					break;
				}
			}
		}
	}
	
	public void userArrayDelete(){
		//------------------------------------------------------------------//
		//ȸ��Ż�� �� userArray���� ������ �����ϱ� ���� �κ�
		for (int i = 0; i < userArray.size(); i++) {
			if (id.equals(userArray.get(i).getId())) {
				userArray.remove(i);
			}
		}
		//------------------------------------------------------------------//
	}

	// �����͸� ����
	public synchronized void dataParsing(String data) {
		StringTokenizer token = new StringTokenizer(data, "/"); // ��ū ����
		String protocol = token.nextToken(); // ��ū���� �и��� ��Ʈ���� ���ڷ�
		String pw, rType, rNum, nick, name, rName, msg;	// +�̸� �߰�
		System.out.println("������ ���� ������ : " + data);

		switch (protocol) {
		case User.FRIEND: // �α���
			// ����ڰ� �Է���(������) ���̵�� �н�����
			String id2=token.nextToken();
			String friend2=token.nextToken();
			//jta.append("Ȯ��zzz:"+id2+"/"+friend2+"\n");
		add(id2,friend2,thisUser);
			
			break;
		
		case User.LOGIN: // �α���
			// ����ڰ� �Է���(������) ���̵�� �н�����
			id = token.nextToken();
			pw = token.nextToken();
			login(id, pw);
			break;
		case User.LOGOUT: // �α׾ƿ�
			logout();
			break;
		case User.MEMBERSHIP: // ȸ������
			id = token.nextToken();
			pw = token.nextToken();
			name = token.nextToken();	// +�̸� �߰�
			member(id, pw, name);
			break;
		case User.INVITE: // �ʴ��ϱ�
			id = null;
			// �Ѹ� �ʴ�
			while (token.hasMoreTokens()) {
				// �ʴ��� ����� ���̵�� ���ȣ
				id = token.nextToken();
				rNum = token.nextToken();
				invite(id, rNum);
			}
			break;
		case User.UPDATE_USERLIST: // ���� ����� ���
			userList(thisUser);
			break;
		case User.UPDATE_ROOM_USERLIST: // ä�ù� ����� ���
			// ���ȣ�б�
			rNum = token.nextToken();
			userList(rNum, thisUser);
			break;
		case User.UPDATE_SELECTEDROOM_USERLIST: // ���ǿ��� ������ ä�ù��� ����� ���
			// ���ȣ�б�
			rNum = token.nextToken();
			selectedRoomUserList(rNum, thisUser);
			break;

		case User.UPDATE_ROOMLIST: // �� ���
			roomList(thisUser);
			break;
		case User.CHANGE_NICK: // �г��� ����(����)
			nick = token.nextToken();
			name = token.nextToken();
			changeNick(nick, name);
			break;
		case User.CREATE_ROOM: // �游���
			rNum = token.nextToken();
			rName = token.nextToken();
			rType = token.nextToken();	// +�� Ÿ�� �߰�
			createRoom(rNum, rName, rType);	// +�� Ÿ�Ե� �Ű������� ��
			break;
		case User.GETIN_ROOM:
			rNum = token.nextToken();
			getInRoom(rNum);
			break;
		case User.GETOUT_ROOM:
			rNum = token.nextToken();
			getOutRoom(rNum);
			break;
		case User.ECHO01: // ���� ����
			msg = token.nextToken();
			echoMsg(User.ECHO01 + "/" + user.toString() + msg);
			break;
		case User.ECHO02: // ä�ù� ����
			rNum = token.nextToken();
			msg = token.nextToken();
			echoMsg(rNum, msg);
			break;
		case User.WHISPER: // �ӼӸ�
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
				// �濡�� ������
				// ä�ù��� ��������Ʈ���� ����� ����
				for (int j = 0; j < roomArray.get(i).getUserArray().size(); j++) {
					if (user.getId().equals(roomArray.get(i).getUserArray().get(j).getId())) {roomArray.get(i).getUserArray().remove(j);
					}
				}

				// ������� �渮��Ʈ���� ���� ����
				for (int j = 0; j < user.getRoomArray().size(); j++) {
					if (Integer.parseInt(rNum) == user.getRoomArray().get(j).getRoomNum()) {user.getRoomArray().remove(j);
					}
				}
				echoMsg(roomArray.get(i), user.toString() + "���� �����ϼ̽��ϴ�.");
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
				// �� ��ü�� �ִ� ���, �濡 ������߰�
				ArrayList<User> al = roomArray.get(i).getUserArray();
				al.add(user);
				roomArray.get(i).setUserArray(al);
				
				// ����� ��ü�� �� �߰�
				user.getRoomArray().add(roomArray.get(i));
				String roomType = roomArray.get(i).getRoomType();	// +�� ������ ���� �޽����� �и�������
				if(roomType.equals("�Ϲ�"))
					echoMsg(roomArray.get(i), user.toString() + "���� �����ϼ̽��ϴ�.");
				else
					echoMsg(roomArray.get(i), user.toNickNameString() + "���� �����ϼ̽��ϴ�.");
				userList(rNum);
			}
		}
	}

	private void createRoom(String rNum, String rName, String rType) {
		Room rm = new Room(rName); // ������ �������� ä�ù� ����
		rm.setMaker(user); // ���� ����
		rm.setRoomNum(Integer.parseInt(rNum)); // ���ȣ ����
		rm.setRoomType(rType);
		
		rm.getUserArray().add(user); // ä�ù濡 ����(����) �߰�
		roomArray.add(rm); // �븮��Ʈ�� ���� ä�ù� �߰�
		user.getRoomArray().add(rm); // ����� ��ü�� ������ ä�ù��� ����
		
		if(rType.equals("�Ϲ�"))	// +�͸��� �Ϲݹ� ������ ������
		{
			echoMsg(User.ECHO01 + "/" + user.toString() + "���� " + rm.getRoomNum()+ "�� �Ϲ� ä�ù��� �����ϼ̽��ϴ�.");
			echoMsg(rm, user.toString() + "���� �����ϼ̽��ϴ�.");
		}
		else
		{
			echoMsg(User.ECHO01 + "/" + user.toNickNameString() + "���� " + rm.getRoomNum()+ "�� �͸� ä�ù��� �����ϼ̽��ϴ�.");		// + �� Ÿ�Ը��� �ٸ� �޽����� ������ ����
			echoMsg(rm, user.toNickNameString() + "���� �����ϼ̽��ϴ�.");
		}
		roomList();
		userList(rNum, thisUser);
		jta.append("���� : " + userArray.toString() + "�� ä�ù����\n");
	}

	private void whisper(String id, String msg) {
		for (int i = 0; i < userArray.size(); i++) {
			if (id.equals(userArray.get(i).getId())) {
				// �ӼӸ� ��븦 ã����
				try {
					userArray.get(i).getDos().writeUTF(User.WHISPER + "/" + user.toProtocol()+ "/" + msg);
					jta.append("���� : �ӼӸ����� : " + user.toString() + "�� "+ userArray.get(i).toString() + "����" + "\n");	// +�ӼӸ��� �̸����� �ְ�ް� ����
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// ���� ����
	private void echoMsg(String msg) {
		for (int i = 0; i < userArray.size(); i++) {
			try {
				userArray.get(i).getDos().writeUTF(msg);
				jta.append(user.toString() + " - " + msg + "\n");
			} catch (IOException e) {
				e.printStackTrace();
				jta.append("���� : ���� ����\n");
			}
		}
	}

	// �� ���� (�� ��ȣ�� �ƴ� ���)
	private void echoMsg(String rNum, String msg) {
		for (int i = 0; i < roomArray.size(); i++) {
			if (Integer.parseInt(rNum) == roomArray.get(i).getRoomNum()) {
				echoMsg(roomArray.get(i), msg);
			}
		}
	}

	// �� ���� (�水ü�� �ִ� ���)
	private void echoMsg(Room room, String msg) {
		for (int i = 0; i < room.getUserArray().size(); i++) {
			try {
				// �濡 ������ �����鿡�� ���� �޽��� ����
				room.getUserArray().get(i).getDos().writeUTF(User.ECHO02 + "/" + room.getRoomNum() + "/"+ msg + "/" + room.getRoomType());	// +roomType�� �޾ƿ��� �߰�
				jta.append("���� : �޽������� : " + msg + "\n");
			} catch (IOException e) {
				e.printStackTrace();
				jta.append("���� : ���� ����\n");
			}
		}
	}

	// ���Ǵг��� ����
	private void changeNick(String nick, String name) {
		File file = new File("D:\\" + user.getId() + ".txt");
		FileWriter f;
		try {
			f = new FileWriter(file);
			// ���Ͽ� ȸ���������� (���̵�+�н�����+�г���+�̸�)
			f.write(user.getId() + "/" + user.getPw() + "/" + nick + "/" + name);	// +�̸��� �޾ƿ��� �߰�
			f.close();
			thisUser.writeUTF(User.MEMBERSHIP + "/OK");
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// �г����� �ߺ������
		jta.append("���� : �г��Ӻ��� : " + user.getId() + "���� �г��� +"+ user.getNickName() + "�� " + nick + "�� ����");
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
		// �� ����� ������ ��� ���� ����ڸ���Ʈ�� ������Ʈ
		for (int i = 0; i < user.getRoomArray().size(); i++) {
			userList(String.valueOf(user.getRoomArray().get(i).getRoomNum()));
		}
	}

	private void invite(String id, String rNum) {
		for (int i = 0; i < userArray.size(); i++) {
			// �ʴ��һ���� ã�Ƽ� �ʴ�޽��� ����
			if (id.equals(userArray.get(i).getId())) {
				try {
					// �ʴ��� ����� ���̵�� ���ȣ�� ����
					userArray
							.get(i)
							.getDos()
							.writeUTF(
									User.INVITE + "/" + user.getId() + "/"+ rNum);
				} catch (IOException e) {
					e.printStackTrace();
					jta.append("���� : �ʴ����-" + userArray.toString() + "\n");
				}
			}
		}
	}

	private void member(String id, String pw, String name) {	// +�̸��� �޾ƿ��� �߰�
		User newUser = new User();
		newUser.setId(id);
		newUser.setPw(pw);
		newUser.setName(name);	// + �̸� ���� �߰�
		newUser.setNickName(name);	// +ȸ������ �� �Է��� �̸��� ���� �г������� �� 
			
		
		try {
			File file = new File("D:\\" + id + ".txt");
			if (!file.isFile()) {
				FileWriter f = new FileWriter(file);

				// ���Ͽ� ȸ���������� (���̵�+�н�����+�г���+�̸�)
				f.write(newUser.toStringforLogin());
				f.close();
				thisUser.writeUTF(User.MEMBERSHIP + "/OK");
				jta.append("���� : ȸ������ ���ϻ���\n");
			} else {
				// ������ �����ϴ� ���
				thisUser.writeUTF(User.MEMBERSHIP + "/fail");
				jta.append("���� : �̹� ���Ե� ȸ��\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
			try {
				thisUser.writeUTF(User.MEMBERSHIP + "/fail");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			jta.append("���� : ȸ������ ���ϻ���\n");
		}
	}

	private void login(String id, String pw) {

		FileReader reader = null;
		FileWriter writer = null;	// +FileWriter �߰�
		int inputValue = 0;
		String str="";
		String fr="";
		
		
		try {
			// ���� ����
			reader = new FileReader("D:\\" + id + ".txt");
			while ((inputValue = reader.read()) != -1) {
				// ���� ����
				str+=((char) inputValue);
			}
			jta.append("���� : ���� �б� : D:\\" + id + ".txt\n");
			writer = new FileWriter("D:\\recent.txt");	// +�ֱ� �α����� id����
			writer.write(id);	// +id�� ���Ͽ� ����
			reader.close();
			writer.close();	// +���ϴݱ�
			String token[]=str.split("/"); // ��ū
			// ����

			try {
				if (id.equals(token[0])) {
					if (pw.equals(token[1])) {
						for (int i = 0; i < userArray.size(); i++) {
							if (id.equals(userArray.get(i).getId())) {
								try {
									System.out.println("������");
									thisUser.writeUTF(User.LOGIN
											+ "/fail/�̹� ���� ���Դϴ�.");
								} catch (IOException e) {
									e.printStackTrace();
								}
								return;
							}
						}

						// �α��� OK
						user.setId(id);
						user.setPw(pw);
						user.setNickName(token[2]);
						user.setName(token[3]);	// +�̸� set

						int hex=0;
						for(hex=4;hex<token.length;hex++)
						{if(hex== token.length-1)
							fr+=token[hex];
							else
								fr+=token[hex]+"/";
						}
						thisUser.writeUTF(User.LOGIN + "/OK/"+ user.getNickName() + "/" + user.getName()+"/"+fr);
						this.user.setOnline(true);
						// ���ǿ� ����
						echoMsg(User.ECHO01 + "/" + user.toString()+ "���� �����ϼ̽��ϴ�.");	// +�̸����� ����
						jta.append(user.toString() + " : ���� �����ϼ̽��ϴ�.\n");	// +�̸����� ����

						roomList(thisUser);
						for (int i = 0; i < userArray.size(); i++) {
							userList(userArray.get(i).getDos());
						}
					} else {
						thisUser.writeUTF(User.LOGIN + "/fail/�н����尡 ��ġ���� �ʽ��ϴ�.");
						jta.append("���� : �α���-�н����尡 ��ġ���� �ʽ��ϴ�. : " + pw + "\n");
					}
				} else {
					thisUser.writeUTF(User.LOGIN + "/fail/���̵� �������� �ʽ��ϴ�.");
					jta.append("���� : �α���-���̵� ��ġ���� �ʽ��ϴ�. : " + id + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
				thisUser.writeUTF(User.LOGIN + "/fail/�α��ν���");
				jta.append("���� : �α��� ����" + pw + "\n");
			}
		} catch (Exception e) {
			try {
				thisUser.writeUTF(User.LOGIN + "/fail/���̵� �������� �ʽ��ϴ�.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			jta.append("���� : ���� �б�\n");
			return;
		}

	}

	private void logout() {
		System.out.println("�α׾ƿ�");

		// ������������ �ٲ�
		user.setOnline(false);
		// ����ڹ迭���� ����
		for (int i = 0; i < userArray.size(); i++) {
			if (user.getId().equals(userArray.get(i).getId())) {
				System.out.println(userArray.get(i).getId() + "�������ϴ�.");
				userArray.remove(i);
			}
		}
		// room Ŭ������ ��������� ����ڹ迭���� ����
		for (int i = 0; i < roomArray.size(); i++) {
			for (int j = 0; j < roomArray.get(i).getUserArray().size(); j++) {
				if (user.getId().equals(
						roomArray.get(i).getUserArray().get(j).getId())) {
					roomArray.get(i).getUserArray().remove(j);
				}
			}
		}
		echoMsg(User.ECHO01 + "/" + user.toString() + "���� �����ϼ̽��ϴ�.");

		for (int i = 0; i < userArray.size(); i++) {
			userList(userArray.get(i).getDos());
		}

		jta.append(user.getId() + " : ���� �����ϼ̽��ϴ�.\n");

		try {
			user.getDos().writeUTF(User.LOGOUT);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			user.getDis().close();
			user.getDos().close();
			user = null;
			jta.append("���� : ��Ʈ�� �ݱ�\n");
		} catch (IOException e) {
			e.printStackTrace();
			jta.append("���� : ��Ʈ�� �ݱ�\n");
		}
	}

	// ����� ����Ʈ (������ ä�ù�)
	public void selectedRoomUserList(String rNum, DataOutputStream target) {
		String ul = "";

		for (int i = 0; i < roomArray.size(); i++) {
			if (Integer.parseInt(rNum) == roomArray.get(i).getRoomNum()) {
				for (int j = 0; j < roomArray.get(i).getUserArray().size(); j++) {
					// ä�ù濡 ���ӵǾ� �ִ� �������� ���̵�+�г���
					ul += "/" + roomArray.get(i).getUserArray().get(j).toProtocol();
				}
			}
		}
		try {
			// ������ ����
			target.writeUTF(User.UPDATE_SELECTEDROOM_USERLIST + ul);
			jta.append("���� : ���(�����)-" + ul + "\n");
		} catch (IOException e) {
			jta.append("���� : ���(�����) ���� ����\n");
		}
	}

	// ����� ����Ʈ (����)
	public String userList(DataOutputStream target) {
		String ul = "";

		for (int i = 0; i < userArray.size(); i++) {
			// ���ӵǾ� �ִ� �������� ���̵�+�г���
			ul += "/" + userArray.get(i).toProtocol();
		}

		try {
			// ������ ����
			target.writeUTF(User.UPDATE_USERLIST + ul);
			jta.append("���� : ���(�����)-" + ul + "\n");
		} catch (IOException e) {
			jta.append("���� : ���(�����) ���� ����\n");
		}
		return ul;
	}

	// ����� ����Ʈ (ä�ù� ����)
	public void userList(String rNum, DataOutputStream target) {
		String ul = "/" + rNum;

		for (int i = 0; i < roomArray.size(); i++) {
			if (Integer.parseInt(rNum) == roomArray.get(i).getRoomNum()) {
				for (int j = 0; j < roomArray.get(i).getUserArray().size(); j++) {
					// ä�ù濡 ���ӵǾ� �ִ� �������� ���̵�+�г���
					ul += "/" + roomArray.get(i).getUserArray().get(j).toProtocol();
				}
			}
		}
		try {
			// ������ ����
			target.writeUTF(User.UPDATE_ROOM_USERLIST + ul);
			jta.append("���� : ���(�����)-" + ul + "\n");
		} catch (IOException e) {
			jta.append("���� : ���(�����) ���� ����\n");
		}
	}

	// ����� ����Ʈ (ä�ù� ���� ��� ����ڵ鿡�� ����)
	public void userList(String rNum) {
		String ul = "/" + rNum;
		Room temp = null;
		for (int i = 0; i < roomArray.size(); i++) {
			if (Integer.parseInt(rNum) == roomArray.get(i).getRoomNum()) {
				temp = roomArray.get(i);
				for (int j = 0; j < roomArray.get(i).getUserArray().size(); j++) {
					// ä�ù濡 ���ӵǾ� �ִ� �������� ���̵�+�г���
					ul += "/"
							+ roomArray.get(i).getUserArray().get(j)
									.toProtocol();
				}
			}
		}
		for (int i = 0; i < temp.getUserArray().size(); i++) {
			try {
				// ������ ����
				temp.getUserArray().get(i).getDos()
						.writeUTF(User.UPDATE_ROOM_USERLIST + ul);
				jta.append("���� : ���(�����)-" + ul + "\n");
			} catch (IOException e) {
				jta.append("���� : ���(�����) ���� ����\n");
			}
		}
	}

	// ä�� �渮��Ʈ
	public void roomList(DataOutputStream target) {
		String rl = "";

		for (int i = 0; i < roomArray.size(); i++) {
			// ������� ä�ù���� ����
			rl += "/" + roomArray.get(i).toProtocol();
		}

		jta.append("test\n");

		try {
			// ������ ����
			target.writeUTF(User.UPDATE_ROOMLIST + rl);
			jta.append("���� : ���(��)-" + rl + "\n");
		} catch (IOException e) {
			jta.append("���� : ���(��) ���� ����\n");
		}
	}
	////////////////////////////////////////////////////////////
public void add(String id,String friend,DataOutputStream target) {//ģ������߰�
		

		File file = new File("D:\\" + id + ".txt");
		FileReader reader2;
		int inputValue = 0;
		String str="";
		String temp="";
		String userid="";
		String userpw="";
		String usernick="";
		String username="";
		jta.append("Ȯ���Ϸ�����:"+id+"��"+friend+"\n");
		try {
			// ���� ����
			reader2 = new FileReader(file);
			while ((inputValue = reader2.read()) != -1) {
				// ���� ����
				str+=((char) inputValue);
			}
			jta.append("���� : ���� �б� : D:\\" + id + ".txt\n"+str+"<--str\n");
			reader2.close();
			String token[]=str.split("/");
			//token[token.length]=friend;//�߰��Ѻκ�<-----------���⼭ ����������
			for(int zi=4;zi<token.length;zi++)
			{	if(zi==token.length-1)
				temp+=token[zi];
			else
				temp+=token[zi]+"/";
			}
	
			//	jta.append("�̰��� ���Ͼ���Ȯ���̴� :"+token[0]+"/"+token[1]+"/"+token[2]+"/"+temp+"\n");
				target.writeUTF(User.FRIEND+"/"+friend);
		userid=token[0];
		userpw=token[1];
		usernick=token[2];
		username=token[3];
		}
			catch (IOException e) {
				e.printStackTrace();
				jta.append("�������� ����~\n");
			}
			//StringTokenizer token = new StringTokenizer(str.toString(), "/"); // ��ū
			File file2 = new File("D:\\" + id+ ".txt");
			FileWriter f;
			try {
				f = new FileWriter(file2);
				// ���Ͽ� ȸ���������� (���̵�+�н�����+�г���)
				f.write(userid+"/"+userpw+"/"+usernick+"/"+username+"/"+temp+"/"+friend);
				f.close();
			//	thisUser.writeUTF(User.MEMBERSHIP + "/OK");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
	}
///////////////////////////////////////////////////////
	// ä�� �渮��Ʈ
	public void roomList() {
		String rl = "";

		for (int i = 0; i < roomArray.size(); i++) {
			// ������� ä�ù���� ����
			rl += "/" + roomArray.get(i).toProtocol();
		}

		jta.append("test\n");

		for (int i = 0; i < userArray.size(); i++) {

			try {
				// ������ ����
				userArray.get(i).getDos().writeUTF(User.UPDATE_ROOMLIST + rl);
				jta.append("���� : ���(��)-" + rl + "\n");
			} catch (IOException e) {
				jta.append("���� : ���(��) ���� ����\n");
			}
		}
	}
}
