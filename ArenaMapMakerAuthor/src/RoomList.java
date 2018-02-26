import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Iterator;

public class RoomList {
	private static ArrayList<Room> list = new ArrayList<Room>();

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
	}
}
