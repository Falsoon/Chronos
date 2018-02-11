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

	public void removeLast() {
		if (!points.isEmpty()) {
			points.remove(points.size() - 1);
		}
	}
}
