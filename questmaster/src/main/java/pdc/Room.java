package pdc;

import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.util.ArrayList;

import javafx.geometry.Point2D;

/*
 * encapsulates room data
 */
public class Room {
	public GeneralPath path;
	public ArrayList<Point> list = new ArrayList<Point>();
	public String desc = "", title = "";
	public int ROOMID;
	public static int idCount = 1;
	private ArrayList<Door> doors;

	public Room(GeneralPath p) {
		this.path = p;
		this.ROOMID = idCount;
		idCount++;
		this.makeList(p);
		doors = new ArrayList<Door>();
	}

	private void makeList(GeneralPath p) {
		double[] coords = new double[6];
		Point point;
		for (PathIterator pi = p.getPathIterator(null); !pi.isDone(); pi.next()) {
			pi.currentSegment(coords);
			point = new Point();
			point.setLocation(coords[0], coords[1]);
			this.list.add(point);
		}
	}

	public Room(Point p) {
		this.list.add(p);
		this.ROOMID = idCount;
		idCount++;
		this.makePath();
	}

	public void addDoor(Door d) {
		this.doors.add(d);
	}
	
	public ArrayList<Door> getDoors() {
		return this.doors;
	}

	public int doorCount() {
		return this.doors.size();
	}

	private void makePath() {
		if (this.path == null) {
			this.path = new GeneralPath();
		}
		if (this.path.getCurrentPoint() == null) {
			this.path.moveTo(this.list.get(0).getX(), this.list.get(0).getY());
			for (int i = 1; i < this.list.size(); i++) {
				this.path.lineTo(this.list.get(i).getX(), this.list.get(i).getY());
				;
			}
		} else {
			this.path.reset();
			this.path.moveTo(this.list.get(0).getX(), this.list.get(0).getY());
			for (int i = 1; i < this.list.size(); i++) {
				this.path.lineTo(this.list.get(i).getX(), this.list.get(i).getY());
			}
		}
	}

	public Room(ArrayList<Point> l) {
		this.list = l;
		this.ROOMID = idCount;
		idCount++;
		this.makePath();
	}

	public Room() {
		this.ROOMID = idCount;
		idCount++;
	}

	public Room split(ArrayList<Point> split) {
		if (split.size() < 2) {
			return null;
		}
		if (split.get(0).equals(split.get(split.size() - 1))) {
			return null;
		}
		ArrayList<Point> newL2 = new ArrayList<Point>();
		boolean forder = false, first = false, last = false;
		int findex = -1, lindex = -1, index = 0; // for over list
		for (int i = 0; i < this.list.size() - 1; i++) {
			// check for intersect at first or last
			first = this.pointBetween(split.get(0), this.list.get(i), this.list.get(i + 1));
			last = this.pointBetween(split.get(split.size() - 1), this.list.get(i), this.list.get(i + 1));
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
			start = findex + 1;
			finish = lindex + 1;
			index = split.size() - 1;
			for (int k = 0; k < split.size(); k++) {
				newL2.add(split.get(split.size() - 1 - k));
			}
		} else {
			start = lindex + 1;
			finish = findex + 1;
			newL2.addAll(split);
		}
		for (int i = start; i < finish; i++) {
			newL2.add(this.list.remove(start));

			if (index > 0) {
				index--;
			}
		}
		newL2.add(split.get(index));
		if (forder) {
			this.list.addAll(start, split);
		} else {
			this.list.addAll(start, this.reverse(split));
		}
		this.makePath();
		return new Room(newL2);
	}

	private ArrayList<Point> reverse(ArrayList<Point> split) {
		ArrayList<Point> ret = new ArrayList<Point>();
		for (int i = 0; i < split.size(); i++) {
			ret.add(split.get(split.size() - 1 - i));
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
		if (this.path == null) {
			return false;
		}
		if (this.path.contains(p)) {
			return true;
		}
		for (int i = 0; !found && i < this.list.size() - 1; i++) {
			found = this.pointBetween(p, this.list.get(i), this.list.get(i + 1));
		}
		return found;
	}

	@Override
	public String toString() {
		if (this.path == null) {
			return "*Room#" + this.ROOMID + "-" + this.title;
		} else {
			return "Room#" + this.ROOMID + "-" + this.title;
		}
	}

	public void setPath(GeneralPath clone) {
		this.path = clone;
		this.makeList(this.path);
	}

	public boolean onBoundary(Point p) {
		boolean found = false;
		for (int i = 0; !found && i < this.list.size() - 1; i++) {
			found = this.pointBetween(p, this.list.get(i), this.list.get(i + 1));
		}
		return found;
	}

	public boolean isDrawn() {
		return this.path != null;
	}

	public String getAdjacents() {
		String adjacentRooms = "";
		boolean first = true;
		for (int i = 0; i < RoomList.list.size(); i++) {
			Room r = RoomList.list.get(i);
			if (r.isAdjacent(this)) {
				if (!first) {
					adjacentRooms = adjacentRooms.concat(" and ");
				} else {
					adjacentRooms = adjacentRooms.concat(" is ");
					first = false;
				}
				// is [dir] of [other room]
				adjacentRooms = adjacentRooms.concat(getDirection(r) + " of " + r.title);
			}
		}
		return adjacentRooms;
	}

	private String getDirection(Room r) {
		Point2D roomCenter, otherCenter;
		roomCenter = new Point2D(this.path.getBounds2D().getCenterX(), this.path.getBounds2D().getCenterY());
		otherCenter = new Point2D(r.path.getBounds2D().getCenterX(), r.path.getBounds2D().getCenterY());
		double angle = Math.toDegrees(
				Math.atan((otherCenter.getY() - roomCenter.getY()) / (otherCenter.getX() - roomCenter.getX())));
		if (otherCenter.getX() - roomCenter.getX() >= 0) {
			if (angle >= 67.5) {
				return "north";
			}
			if (angle >= 22.5) {
				return "northwest";
			}
			if (angle >= -22.5) {
				return "west";
			}
			if (angle >= -67.5) {
				return "southwest";
			} else {
				return "south";
			}
		} else {
			if (angle >= 67.5) {
				return "south";
			}
			if (angle >= 22.5) {
				return "southeast";
			}
			if (angle >= -22.5) {
				return "east";
			}
			if (angle >= -67.5) {
				return "northeast";
			} else {
				return "north";
			}
		}
	}

	private boolean isAdjacent(Room room) {
		if (this.equals(room)) {
			return false;
		} else {
			int pOnBoundary =0;
			for(int i=0;i<room.list.size()-1;i++) {
				Point p = room.list.get(i);
				if(onBoundary(p)) {
					pOnBoundary++;
				}
			}
			if(pOnBoundary>=2) {
				return true;
			}else {
				return false;
			}
		}
	}
}
