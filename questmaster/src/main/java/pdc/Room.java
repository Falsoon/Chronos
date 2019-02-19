package pdc;

import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javafx.geometry.Point2D;

/*
 * encapsulates room data
 */
public class Room {
	public ArrayList<Line2D> walls;
	public ArrayList<Point> pointList;
	public String desc = "", title = "";
	public int ROOMID;
	public static int idCount = 1;
	private ArrayList<Door> doors;
	public GeneralPath path;

	public Room(ArrayList<Line2D> walls) {
		this.walls = walls;
		ROOMID = idCount;
		idCount++;
      pointList = new ArrayList<>();
		makeList(walls);
		doors = new ArrayList<>();
		makePath();
	}

	private void makeList(ArrayList<Line2D> walls) {
		walls.forEach(wall->{
         pointList.add(new Point((int)wall.getX1(),(int)wall.getY1()));
         pointList.add(new Point((int)wall.getX2(),(int)wall.getY2()));
      });
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
			this.path.moveTo(this.pointList.get(0).getX(), this.pointList.get(0).getY());
			for (int i = 1; i < this.pointList.size(); i++) {
				this.path.lineTo(this.pointList.get(i).getX(), this.pointList.get(i).getY());
			}
		} else {
			this.path.reset();
			this.path.moveTo(this.pointList.get(0).getX(), this.pointList.get(0).getY());
			for (int i = 1; i < this.pointList.size(); i++) {
				this.path.lineTo(this.pointList.get(i).getX(), this.pointList.get(i).getY());
			}
		}
	}

	/*
	public Room(ArrayList<Point> l) {
		this.pointList = l;
		this.ROOMID = idCount;
		idCount++;
		this.makePath();
	}
	*/

	public Room() {
		this.ROOMID = idCount;
		idCount++;
	}

	/*
	public Room split(ArrayList<Point> split) {
		if (split.size() < 2) {
			return null;
		}
		if (split.get(0).equals(split.get(split.size() - 1))) {
			return null;
		}
		ArrayList<Point> newL2 = new ArrayList<>();
		boolean forder, first, last;
		int findex = -1, lindex = -1, index = 0; // for over pointList
		for (int i = 0; i < this.pointList.size() - 1; i++) {
			// check for intersect at first or last
			first = this.pointBetween(split.get(0), this.pointList.get(i), this.pointList.get(i + 1));
			last = this.pointBetween(split.get(split.size() - 1), this.pointList.get(i), this.pointList.get(i + 1));
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
			newL2.add(this.pointList.remove(start));

			if (index > 0) {
				index--;
			}
		}
		newL2.add(split.get(index));
		if (forder) {
			this.pointList.addAll(start, split);
		} else {
			this.pointList.addAll(start, this.reverse(split));
		}
		this.makePath();
		return new Room(newL2);
	}
	*/

	private ArrayList<Point> reverse(ArrayList<Point> split) {
		ArrayList<Point> ret = new ArrayList<>();
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
		if (this.path == null) {
			return false;
		}
		if (this.path.contains(p)) {
			return true;
		}
		return(onBoundary(p));
	}

   /**
    * Checks if this room contains the specified room
    * @param r the room to check is contained by this room
    * @return true if r is contained by this room
    */
	public boolean contains (Room r){
      return r.pointList.stream().allMatch(this::contains);
   }

	@Override
	public String toString() {
		if (this.path == null) {
			return "*Room#" + this.ROOMID + "-" + this.title;
		} else {
			return "Room#" + this.ROOMID + "-" + this.title;
		}
	}

	/*
	public void setPath(GeneralPath clone) {
		this.path = clone;
		this.makeList(this.path);
	}
	*/

	public boolean onBoundary(Point p) {
		boolean found = false;
		for (int i = 0; !found && i < this.pointList.size() - 1; i++) {
			found = this.pointBetween(p, this.pointList.get(i), this.pointList.get(i + 1));
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
			for(int i = 0; i<room.pointList.size()-1; i++) {
				Point p = room.pointList.get(i);
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
