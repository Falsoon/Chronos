package pdc;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Encapsulates all the rooms into a list
 */
public class RoomList {
	public static ArrayList<Room> list = new ArrayList<Room>();

	public static Room getRoom(Point p) {
		Room room = null;
		Iterator<Room> itr = list.iterator();
		boolean found = false;
		while (!found && itr.hasNext()) {
			room = itr.next();
			found = room.contains(p);
		}
		if (!found) {
			room = null;
		}
		return room;
	}

	public static void add(Room r) {
		list.add(r);
	}

	public static void reset() {
		list.clear();
		Room.idCount =1;
	}

	public static Room getRoomById(int id) {
		Room room = null;
		for(int i=0; i< list.size();i++) {
			if(list.get(i).ROOMID==id) {
				room = list.get(i);
			}
		}
		return room;
	}

	public static Room getRoomByStr(String str) {
		boolean found = false;
		Room r = null;
		for(int i=0;i<list.size()&&!found;i++) {
			if(list.get(i).toString().equals(str)) {
				r = list.get(i);
				found = true;
			}
		}
		return r;
	}

	public static void undo() {
		// TODO Auto-generated method stub
		
	}
}
