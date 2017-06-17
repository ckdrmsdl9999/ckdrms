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
	private String nickName; // 사용자 닉네임
	private String id; // 사용자 아이디 - IP 주소
	private String pw; // password
	private String name; // +사용자 이름
	private boolean online;
	private ArrayList<Room> user_rooms; // 사용자가 입장한 방의 목록

	private DataInputStream dis; // 입력스트림
	private DataOutputStream dos; // 출력스트림

	// PROTOCOLs
	public static final String LOGIN = "EI"; // 로그인
	public static final String LOGOUT = "EO"; // 로그아웃
	public static final String MEMBERSHIP = "EM"; // 회원가입

	public static final String INVITE = "EV"; // 초대
	public static final String UPDATE_SELECTEDROOM_USERLIST = "ED"; // 대기실에서 선택한 채팅방의 유저리스트 업데이트
	public static final String UPDATE_ROOM_USERLIST = "ES"; // 채팅방의 유저리스트 업데이트
	public static final String UPDATE_USERLIST = "EU"; // 유저리스트 업데이트
	public static final String UPDATE_ROOMLIST = "ER"; // 채팅방리스트 업데이트
	public static final String CHANGE_NICK = "EN"; // 닉네임변경

	public static final String CREATE_ROOM = "RC"; // 채팅방 생성
	public static final String GETIN_ROOM = "RI"; // 채팅방 들어옴
	public static final String GETOUT_ROOM = "RO"; // 채팅방 나감
	public static final String ECHO01 = "MM"; // 대기실 채팅
	public static final String ECHO02 = "ME"; // 채팅방 채팅
	public static final String WHISPER = "MW"; // 귓속말

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
	public String toNameString()	// +채팅방 내 이름 표시 부분
	{
		return "[" + name + "]";
	}
	
	public String toNickNameString()	// +채팅방 내 닉네임 표시 부분
	{
		return "[" + nickName + "]";
	}
	
	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	String getName(){	// +유저이름 get
		return name;
	}
	
	void setName(String name){	// +유저 이름 set
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
		// ObjectOutputStream 을 이용한 객체 파일 저장
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		
		try{
			
			// object.dat 파일의 객체 아웃풋스트림을 생성
			fos = new FileOutputStream("C:\\Users\\Park\\Desktop\\Chatting\\base source code\\pineapple-master\\object.dat");
			oos = new ObjectOutputStream(fos);
			
			// 해당 파일에 3개의 객체를 순차적으로 쓴다
			
			oos.writeObject(lineNum);
			oos.writeObject(portNum);
			
			// object.dat 파일에 2개의 객체 쓰기 완료
			System.out.println("객체를 저장했습니다.");
		
		}catch(Exception e){
			
			e.printStackTrace();
		
		}finally{
			
			// 스트림을 닫아준다.
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
			System.out.println("No 를 선택");
			return;
		}

		// 파일로 부터 객체 데이터 읽어온다.
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		
		try{
			
			// object.dat 파일로 부터 객체를 읽어오는 스트림을 생성한다.
			fis = new FileInputStream("C:\\Users\\Park\\Desktop\\Chatting\\base source code\\pineapple-master\\object.dat");
			ois = new ObjectInputStream(fis);
			
			// ObjectInputStream으로 부터 객체 하나씩 읽어온다.
			int lineNum = (int)(ois.readObject());
			int portNum = (int)(ois.readObject());

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			// 스트림을 닫아준다.
			if(fis != null) try{fis.close();}catch(IOException e){}
			if(ois != null) try{ois.close();}catch(IOException e){}
		}
	}
}
