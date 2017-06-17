package mew;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.*;

public class User {
	private String IP;
	private String nickName; // ����� �г���
	private String id; // ����� ���̵� - IP �ּ�
	private String pw; // password
	private String name; // +����� �̸�
	private boolean online;
	private ArrayList<Room> user_rooms; // ����ڰ� ������ ���� ���

	private DataInputStream dis; // �Է½�Ʈ��
	private DataOutputStream dos; // ��½�Ʈ��

	// PROTOCOLs
	public static final String LOGIN = "EI"; // �α���
	public static final String LOGOUT = "EO"; // �α׾ƿ�
	public static final String MEMBERSHIP = "EM"; // ȸ������

	public static final String INVITE = "EV"; // �ʴ�
	public static final String UPDATE_SELECTEDROOM_USERLIST = "ED"; // ���ǿ��� ������ ä�ù��� ��������Ʈ ������Ʈ
	public static final String UPDATE_ROOM_USERLIST = "ES"; // ä�ù��� ��������Ʈ ������Ʈ
	public static final String UPDATE_USERLIST = "EU"; // ��������Ʈ ������Ʈ
	public static final String UPDATE_ROOMLIST = "ER"; // ä�ù渮��Ʈ ������Ʈ
	public static final String CHANGE_NICK = "EN"; // �г��Ӻ���

	public static final String CREATE_ROOM = "RC"; // ä�ù� ����
	public static final String GETIN_ROOM = "RI"; // ä�ù� ����
	public static final String GETOUT_ROOM = "RO"; // ä�ù� ����
	public static final String ECHO01 = "MM"; // ���� ä��
	public static final String ECHO02 = "ME"; // ä�ù� ä��
	public static final String WHISPER = "MW"; // �ӼӸ�

	public static final String OMOK_INVITE = "OI";

	User() {

	}

	User(String id, String nick, String name) {
		this.id = id;
		this.nickName = nick;
		this.name = name;
	}

	User(DataInputStream dis, DataOutputStream dos) {
		this.dis = dis;
		this.dos = dos;
		setNickName(nickName);
		setName(name);
		user_rooms = new ArrayList<Room>();
	}

	public String toStringforLogin() {
		return id + "/" + pw + "/" + nickName + "/" + name;
	}

	public String toProtocol() {
		return id + "/" + nickName + "/" + name;
	}

	public String toString() {
		return "[" + name + "(" + id + ")" + "]";
	}
	public String toNameString()	// +ä�ù� �� �̸� ǥ�� �κ�
	{
		return "[" + name + "]";
	}
	
	public String toNickNameString()	// +ä�ù� �� �г��� ǥ�� �κ�
	{
		return "[" + nickName + "]";
	}
	
	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	String getName(){	// +�����̸� get
		return name;
	}
	
	void setName(String name){	// +���� �̸� set
		this.name = name;
	}
	
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
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

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public ArrayList<Room> getRoomArray() {
		return user_rooms;
	}

	public void setRooms(ArrayList<Room> rooms) {
		this.user_rooms = rooms;
	}
	
	public void sendObject(int lineNum, int portNum)
	{
		// ObjectOutputStream �� �̿��� ��ü ���� ����
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		
		try{
			
			// object.dat ������ ��ü �ƿ�ǲ��Ʈ���� ����
			fos = new FileOutputStream("C:\\Users\\Park\\Desktop\\Chatting\\base source code\\pineapple-master\\object.dat");
			oos = new ObjectOutputStream(fos);
			
			// �ش� ���Ͽ� 3���� ��ü�� ���������� ����
			
			oos.writeObject(lineNum);
			oos.writeObject(portNum);
			
			// object.dat ���Ͽ� 2���� ��ü ���� �Ϸ�
			System.out.println("��ü�� �����߽��ϴ�.");
		
		}catch(Exception e){
			
			e.printStackTrace();
		
		}finally{
			
			// ��Ʈ���� �ݾ��ش�.
			if(fos != null) try{fos.close();}catch(IOException e){}
			if(oos != null) try{oos.close();}catch(IOException e){}	
		}
	}
	
	public void receiveObject()
	{
		String[] playMode = {"Y", "N"};
		String input = (String) JOptionPane.showInputDialog(null, "Y", "N", JOptionPane.QUESTION_MESSAGE, null, playMode, playMode[0]);
		if(input.equals("N"))
		{
			System.out.println("No �� ����");
			return;
		}

		// ���Ϸ� ���� ��ü ������ �о�´�.
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		
		try{
			
			// object.dat ���Ϸ� ���� ��ü�� �о���� ��Ʈ���� �����Ѵ�.
			fis = new FileInputStream("C:\\Users\\Park\\Desktop\\Chatting\\base source code\\pineapple-master\\object.dat");
			ois = new ObjectInputStream(fis);
			
			// ObjectInputStream���� ���� ��ü �ϳ��� �о�´�.
			int lineNum = (int)(ois.readObject());
			int portNum = (int)(ois.readObject());

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			// ��Ʈ���� �ݾ��ش�.
			if(fis != null) try{fis.close();}catch(IOException e){}
			if(ois != null) try{ois.close();}catch(IOException e){}
		}
	}
}
