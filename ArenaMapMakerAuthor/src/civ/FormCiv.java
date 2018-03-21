package civ;

import java.util.ArrayList;

import pdc.*;

/**
 * This class is used to be the civ component/presenter
 * class for the author description form window
 */
public class FormCiv {
	
	private Room room;
	private Door door;
	
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
	
	public ArrayList<Door> getRoomDoors(){
		if(room ==null) {
			room = new Room();
		}
		return room.getDoors();
	}
	
	public int getRoomID() {
		if(room == null) {
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

	public String getDoorTitle() {
		return door.title;
	}

	public int getDoorID() {
		return door.DOORID;
	}

	public String getDoorDesc() {
		return room.desc;
	}

	public void adjustDoorTitleAndDesc(String title, String desc) {
		door.title = title;
		door.desc = desc;
	}

	public void setDoorReference(String str) {
		Door d = DoorList.getDoorByStr(str);
		door = d;
	}

	public void setDoorOpen(boolean selected) {
		if(selected) {
			door.open();
		} else {
			door.close();
		}
		
	}
}
