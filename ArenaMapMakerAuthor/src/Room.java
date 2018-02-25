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

	public Room(ArrayList<Point> newL2) {
		list = newL2;
		ROOMID = idCount;
		idCount++;
	}

	public Room split(ArrayList<Point> split) {
		ArrayList<Point> newL = new ArrayList<Point>();
		ArrayList<Point> newL2 = new ArrayList<Point>();
		boolean found = false,first = false, last = false;
		int index=0;
		// for over list
		for (int i = 0; i < list.size()-1; i++) {
			// check for intersect at first or last
			first = pointBetween(split.get(0), list.get(i), list.get(i + 1))||first;
			last = pointBetween(split.get(split.size() - 1), list.get(i), list.get(i + 1))||last;
			// if both false
			if (!first && !last) {
				if (!found) {
					newL.add(list.get(i));
				}else {
					newL2.add(list.get(i));
				}
			}
			// if first add split points until last intersects
			if (first && !last) {
				found = true;
				index = split.size()-1;
				newL.add(list.get(i));
				for (int k = 0; k < split.size(); k++) {
					newL.add(split.get(k));
					newL2.add(split.get(split.size()-1-k));
				}
			}
			// if last add split points until first intersects
			if (last && !first) {
				found = true;
				newL.add(list.get(i));
				for (int k = split.size() - 1; k >= 0; k--) {
					newL.add(split.get(k));
					newL2.add(split.get(split.size()-1-k));
				}
			}
			// then just add rest of list
			if (first && last) {
				newL2.add(list.get(i));
			}
		}
		newL2.add(split.get(index));
		newL.add(list.get(0));
		list = newL;
		return new Room(newL2);

		/*
		 * for (int i = 0; i < list.size(); i++) {
		 * 
		 * // find the point of intersection if (Point.distance(list.get(i).getX(),
		 * list.get(i).getY(), split.get(0).getX(), split.get(0).getY()) +
		 * Point.distance(split.get(0).getX(), split.get(0).getY(), list.get(i +
		 * 1).getX(), list.get(i + 1).getY()) == Point.distance(list.get(i).getX(),
		 * list.get(i).getY(), list.get(i + 1).getX(), list.get(i + 1).getY())) { // add
		 * points of the path list.set(i, split.get(0)); for (int k = i; k <
		 * split.size(); k++) { list.set(k, split.get(k)); } // find some way to finish
		 * the shape. } if (Point.distance(list.get(i).getX(), list.get(i).getY(),
		 * split.get(split.size() - 1).getX(), split.get(split.size() - 1).getY()) +
		 * Point.distance(split.get(split.size() - 1).getX(), split.get(split.size() -
		 * 1).getY(), list.get(i + 1).getX(), list.get(i + 1).getY()) ==
		 * Point.distance(list.get(i).getX(), list.get(i).getY(), list.get(i +
		 * 1).getX(), list.get(i + 1).getY())) { // when split leaves room
		 * 
		 * for (int k = i; k < split.size(); k++) { list.set(k, split.get(k)); } // find
		 * some way to finish the shape. }
		 * 
		 * // set points in between }
		 * 
		 * return null;
		 */
		// get nearest point on path to first point
		// path = new path where new path is orig path
		// prevpoint
		/*
		 * Point prevPoint = new Point(); GeneralPath newP = new GeneralPath();
		 * GeneralPath newP2 = new GeneralPath(); double[] coords = new double[6]; for
		 * (PathIterator iterator = path.getPathIterator(null); !iterator.isDone();
		 * iterator.next()) { int type = iterator.currentSegment(coords);// only gives
		 * one point // must find way to get more points switch (type) { case
		 * PathIterator.SEG_MOVETO: // set prevpoint = point break; case
		 * PathIterator.SEG_LINETO: // compare splitpoint to line between curr and
		 * prevpoint if (Point.distance(coords[0], coords[1], split.get(0).getX(),
		 * split.get(0).getY()) + Point.distance(coords[0], coords[1],
		 * split.get(0).getX(), split.get(0).getY()) == Point .distance(coords[0],
		 * coords[1], coords[2], coords[3])) { newP.moveTo(coords[0], coords[1]); for
		 * (int i = 0; i < split.size(); i++) { newP.lineTo(split.get(i).getX(),
		 * split.get(i).getY()); } } break; default:
		 * System.err.println("Error: Complex lines not implemented in Room"); } } path
		 * = newP; return new Room(newP2);
		 */
	}

	private boolean pointBetween(Point p1, Point p2, Point p3) {
		return Point.distance(p2.getX(), p2.getY(), p1.getX(), p1.getY())
				+ Point.distance(p1.getX(), p1.getY(), p3.getX(), p3.getY()) == Point.distance(p2.getX(), p2.getY(),
						p3.getX(), p3.getY());
	}
}
