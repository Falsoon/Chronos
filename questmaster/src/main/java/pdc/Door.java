package pdc;
import java.awt.geom.GeneralPath;

/**
 * This class encapsulates all the logic for door objects
 */
public class Door {
	public GeneralPath path;
	public Boolean open;
	public Room room;
	public String desc = "", title = "";
	public int DOORID;
	public static int idCount = 1;


	public Door(GeneralPath p) {
		path = p;
		open = false;
		this.DOORID = idCount;
		idCount++;
	}

	public void open() {
		open = true;
	}

	public void close() {
		open = false;
	}

	@Override
	public String toString() {
		if (this.path == null) {
			return "*Door#" + this.DOORID + "-" + this.title;
		} else {
			return "Door#" + this.DOORID + "-" + this.title;
		}
	}
}
