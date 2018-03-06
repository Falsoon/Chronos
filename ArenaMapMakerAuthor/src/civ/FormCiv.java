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
		room = RoomList.getRoomByStr(str);
	}
	
	public String getRoomTitle(){
		return room.title;
	}
	
	public int getRoomID() {
		return room.ROOMID;
	}
	
	public String getRoomDesc() {
		return room.desc;
	}
	
	public void adjustRoomTitleAndDesc(String title, String desc) {
		room.title = title;
		room.desc = desc;
	}
	
	public void addRoomToRoomList() {
		RoomList.add(room);
	}

	public boolean getRoomDrawn(String str) {
		Room r = RoomList.getRoomByStr(str);
		if(r ==null) {return false;}
		return r.isDrawn();
	}
}
