import java.awt.geom.GeneralPath;

/*
 * encapsulates room data
 */
public class Room {
	public GeneralPath path;
	public String desc, title;
	public final int ROOMID;
	public static int idCount = 1;
	
	public Room(GeneralPath p) {
		path = p;
		ROOMID = idCount;
		idCount++;
	}
}
