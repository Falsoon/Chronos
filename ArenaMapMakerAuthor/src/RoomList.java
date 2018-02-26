import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Iterator;


/*
 * Encapsulates all the rooms into a list
 */
public class RoomList {
	private static ArrayList<Room> list = new ArrayList<Room>();

	public static Room getRoom(GeneralPath curr) {
		Room room = null;
		Iterator<Room> itr = list.iterator();
		boolean found = false;
		while (!found &&itr.hasNext()) {
			room = itr.next();
			found = room.path.equals(curr);
		}
		if(found) {
			return room;
		}else {
			room = new Room(curr);
			list.add(room);
			return room;
			
		}
	}
}
