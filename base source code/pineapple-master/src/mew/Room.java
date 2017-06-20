package mew;

import java.util.ArrayList;

public class Room {

	private int roomNum;
	private String roomType;	// +방의 종류(일반/익명)
	private String roomName;
	private ArrayList<User> roomUserArray; // 채팅방에 접속한 사람들
	private User maker; // 방장, 방만든사람
	private RoomUI rUI; // 방 UI
	
	public Room() {
		roomUserArray = new ArrayList<User>();
	}

	public Room(String roomName) {
		roomUserArray = new ArrayList<User>();
		this.roomName = roomName;
	}

	public String toProtocol() {
		return roomNum + "/" + roomName + "/" + roomType;	//+roomType까지로 수정
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

	public String getRoomType() {	// +방 종류 get(일반/익명)
		return roomType;
	}
	public void setRoomType(String roomType){	// +방 종류  set

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
