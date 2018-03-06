package civ;

import pdc.*;

/**
 * This class is used to be the civ component/presenter
 * class for the author description form window
 */
public class FormCiv {
	
	private Room room;
	
	public FormCiv() {
		
	}
	
	public void setRoomReference(String str) {
		Room r = RoomList.getRoomByStr(str);
		if(r==null) {
			room = new Room();
		}else {
			room = r;
		}
	}
	
	public String getRoomTitle(){
		if(room ==null) {
			room = new Room();
		}
		return room.title;
	}
	
	public int getRoomID() {
		if(room ==null) {
			room = new Room();
		}
		return room.ROOMID;
	}
	
	public String getRoomDesc() {
		if(room ==null) {
			room = new Room();
		}
		return room.desc;
	}
	
	public void adjustRoomTitleAndDesc(String title, String desc) {
		if(room ==null) {
			room = new Room();
		}
		room.title = title;
		room.desc = desc;
	}
	
	public void addRoomToRoomList() {
		if(room ==null) {
			room = new Room();
		}
		RoomList.add(room);
	}

	public boolean getRoomDrawn(String str) {
		Room r = RoomList.getRoomByStr(str);
		if(r ==null) {return false;}
		return r.isDrawn();
	}
}
