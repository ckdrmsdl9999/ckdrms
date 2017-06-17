package mew;

import java.util.ArrayList;

public class Room {

	private int roomNum;
	private String roomType;	// +���� ����(�Ϲ�/�͸�)
	private String roomName;
	private ArrayList<User> roomUserArray; // ä�ù濡 ������ �����
	private User maker; // ����, �游����
	private RoomUI rUI; // �� UI

	public Room() {
		roomUserArray = new ArrayList<User>();
	}

	public Room(String roomName) {
		roomUserArray = new ArrayList<User>();
		this.roomName = roomName;
	}

	public String toProtocol() {
		return roomNum + "/" + roomName + "/" + roomType;	//+roomType������ ����
	}

	// getter/setter
	public int getRoomNum() {
		return roomNum;
	}
	
	public void setRoomNum(int roomNum) {
		this.roomNum = roomNum;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getRoomType() {	// +�� ���� get(�Ϲ�/�͸�)
		return roomType;
	}
	public void setRoomType(String roomType){	// +�� ����  set

		this.roomType = roomType;
	}
	
	public ArrayList<User> getUserArray() {
		return roomUserArray;
	}
	
	public void setUserArray(ArrayList<User> userArray) {
		this.roomUserArray = userArray;
	}

	public User getMaker() {
		return maker;
	}

	public void setMaker(User user) {
		this.maker = user;
	}

	public RoomUI getrUI() {
		return rUI;
	}

	public void setrUI(RoomUI rUI) {
		this.rUI = rUI;
	}

}
