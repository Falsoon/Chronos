package pdc;

import java.util.ArrayList;


/**
 * Encapsulates all the doors into a pointList
 */
public class DoorList {
	public static ArrayList<Door> list = new ArrayList<Door>();
	protected static Door mostRecent;

	public static void add(Door d) {
		list.add(d);
		mostRecent = d;
	}

	public static void reset() {
		list.clear();
		Room.idCount =1;
	}

	public static Door getDoorById(int id) {
		Door door = null;
		for(int i=0; i< list.size();i++) {
			if(list.get(i).DOORID==id) {
				door = list.get(i);
			}
		}
		return door;
	}

	public static Door getDoorByStr(String str) {
		boolean found = false;
		Door d = null;
		for(int i=0;i<list.size()&&!found;i++) {
			if(list.get(i).toString().equals(str)) {
				d = list.get(i);
				found = true;
			}
		}
		return d;
	}

	public static void undo() {
		list.remove(mostRecent);
		
	}
}
