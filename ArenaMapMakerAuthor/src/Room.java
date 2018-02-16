import java.awt.Point;
import java.util.ArrayList;

public class Room {
	public ArrayList<Point> points;

	public Room(Point p) {
		points = new ArrayList<Point>();
		points.add(p);
	}

	public void add(Point p) {
		points.add(p);
	}

	public Point removeLast() {
		Point p = null;
		if (!points.isEmpty()) {
			p = points.remove(points.size() - 1);
		}
		return p;
	}
	
	public boolean isEmpty() {
		return points.isEmpty();
	}
	
}
