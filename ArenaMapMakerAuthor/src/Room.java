import java.awt.geom.GeneralPath;

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
