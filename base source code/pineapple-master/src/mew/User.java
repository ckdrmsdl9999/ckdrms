package mew;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

public class User {
	private String IP;
	private String nickName; // 사용자 닉네임
	private String id; // 사용자 아이디 - IP 주소
	private String pw; // password
	private String name; // +사용자 이름
	private boolean online;
	private ArrayList<Room> user_rooms; // 사용자가 입장한 방의 목록
	private ArrayList<String> friend=new ArrayList(10);//개개인의 친구목록 저장
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
	
////문용 추가
	public static final String ECHO03 = "MR";		//My room
	public static final String ECHOBOT = "CB";		//챗봇용 코드
////	

	public static final String OMOK_INVITE = "OI";
	public static final String FRIEND = "FR"; // 친구추가
	public static final String CHANGEPW="CP";	// 비밀번호 변경
	
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
	
	public ArrayList<String> getfriendArray() {
		return friend;
	}

	public void setfriend(ArrayList<String> friends) {
		this.friend = friends;
	}
}
