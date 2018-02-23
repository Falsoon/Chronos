import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Room {
	public GeneralPath path;
	public ArrayList<Point> list = new ArrayList<Point>();
	public String desc, title;
	public final int ROOMID;
	public static int idCount = 1;

	public Room(GeneralPath p) {
		path = p;
		ROOMID = idCount;
		idCount++;
	}
	
	public Room(Point p) {
		list.add(p);
		ROOMID = idCount;
		idCount++;
	}

	public Room split(ArrayList<Point> split) {
		for(int i=0;i<list.size();i++) {
			//find the point of intersection
			//add points of the path
			//set points in between 
		}
		return null;
		// get nearest point on path to first point
		// path = new path where new path is orig path
		// prevpoint
		/*Point prevPoint = new Point();
		GeneralPath newP = new GeneralPath();
		GeneralPath newP2 = new GeneralPath();
		double[] coords = new double[6];
		for (PathIterator iterator = path.getPathIterator(null); !iterator.isDone(); iterator.next()) {
			int type = iterator.currentSegment(coords);// only gives one point
			// must find way to get more points
			switch (type) {
			case PathIterator.SEG_MOVETO:
				// set prevpoint = point
				break;
			case PathIterator.SEG_LINETO:
				// compare splitpoint to line between curr and prevpoint
				if (Point.distance(coords[0], coords[1], split.get(0).getX(), split.get(0).getY())
						+ Point.distance(coords[0], coords[1], split.get(0).getX(), split.get(0).getY()) == Point
								.distance(coords[0], coords[1], coords[2], coords[3])) {
					newP.moveTo(coords[0], coords[1]);
					for (int i = 0; i < split.size(); i++) {
						newP.lineTo(split.get(i).getX(), split.get(i).getY());
					}
				}
				break;
			default: System.err.println("Error: Complex lines not implemented in Room");
			}
		}
		path = newP;
		return new Room(newP2);*/
	}
}
