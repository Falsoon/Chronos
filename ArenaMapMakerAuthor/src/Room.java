import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.util.ArrayList;

public class Room {
	public GeneralPath path;
	public ArrayList<Point> list = new ArrayList<Point>();
	public String desc="", title="";
	public int ROOMID;
	public static int idCount = 1;

	public Room(GeneralPath p) {
		path = p;
		ROOMID = idCount;
		idCount++;
		makeList(p);
	}

	private void makeList(GeneralPath p) {
		double[] coords = new double[6];
		Point point;
		for (PathIterator pi = p.getPathIterator(null); !pi.isDone(); pi.next()) {
			pi.currentSegment(coords);
			point = new Point();
			point.setLocation(coords[0], coords[1]);
			list.add(point);
		}
	}

	public Room(Point p) {
		list.add(p);
		ROOMID = idCount;
		idCount++;
		makePath();
	}

	private void makePath() {
		if (path == null) {
			path = new GeneralPath();
		}
		if (path.getCurrentPoint() == null) {
			path.moveTo(list.get(0).getX(), list.get(0).getY());
			for (int i = 1; i < list.size(); i++) {
				path.lineTo(list.get(i).getX(), list.get(i).getY());
				;
			}
		} else {
			path.reset();
			path.moveTo(list.get(0).getX(), list.get(0).getY());
			for (int i = 1; i < list.size(); i++) {
				path.lineTo(list.get(i).getX(), list.get(i).getY());
			}
		}
	}

	public Room(ArrayList<Point> l) {
		list = l;
		ROOMID = idCount;
		idCount++;
		makePath();
	}


	public Room() {
		ROOMID = idCount;
		idCount++;
	}

	public Room split(ArrayList<Point> split) {
		if (split.size() < 2) {
			return null;
		}
		ArrayList<Point> newL2 = new ArrayList<Point>();
		boolean forder = false, first = false, last = false;
		int findex = -1, lindex = -1, index = 0; // for over list
		for (int i = 0; i < list.size() - 1; i++) {
			// check for intersect at first or last
			first = pointBetween(split.get(0), list.get(i), list.get(i + 1));
			last = pointBetween(split.get(split.size() - 1), list.get(i), list.get(i + 1));
			if (first) {
				findex = i;
			}
			if (last) {
				lindex = i;
			}
		}
		forder = lindex > findex;
		int start, finish;
		if (forder) {
			start = findex+1;
			finish = lindex+1;
			index = split.size() - 1;
			for (int k = 0; k < split.size(); k++) {
				newL2.add(split.get(split.size() - 1 - k));
			}
		} else {
			start = lindex+1;
			finish = findex+1;
			newL2.addAll(split);
		}
		for (int i = start; i < finish; i++) {
			newL2.add(list.remove(start));
			
			if (index > 0) {
				index--;
			}
		}
		newL2.add(split.get(index));
		if(forder) {
			list.addAll(start, split);
		}else {
			list.addAll(start, reverse(split));
		}
		makePath();
		return new Room(newL2);
	}

	private  ArrayList<Point> reverse(ArrayList<Point> split) {
		ArrayList<Point> ret = new ArrayList<Point>();
		for(int i=0;i<split.size();i++) {
			ret.add(split.get(split.size()-1-i));
		}
		return ret;
	}

	private boolean pointBetween(Point p1, Point p2, Point p3) {
		return Point.distance(p2.getX(), p2.getY(), p1.getX(), p1.getY())
				+ Point.distance(p1.getX(), p1.getY(), p3.getX(), p3.getY()) == Point.distance(p2.getX(), p2.getY(),
						p3.getX(), p3.getY());
	}

	public boolean contains(Point p) {
		boolean found = false;
		if(path==null) {
			return false;
		}
		if(path.contains(p)) {
			return true;
		}
		for(int i =0; !found &&i< list.size()-1;i++) {
			found = pointBetween(p, list.get(i), list.get(i + 1));
		}
		return found;
	}
	
	@Override
	public String toString() {
		return "Room#"+ROOMID+" :"+title;
	}

	public void setPath(GeneralPath clone) {
		path = clone;
		makeList(path);
	}
}
