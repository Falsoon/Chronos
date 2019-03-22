package pdc;

import javafx.geometry.Point2D;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

/**
 * Represents a room on the map
 */
public class Room {
	public ArrayList<Wall> walls;
	public ArrayList<Point> pointList;
	public String desc = "", title = "";
	public int ROOMID;
	public static int idCount = 1;
	private ArrayList<Door> doors;
	public GeneralPath path;

   /**
    * Create a room from a list of walls, typical case
    * @param walls the walls of the room
    */
	public Room(ArrayList<Wall> walls) {
		this.walls = walls;
		ROOMID = idCount;
		idCount++;
      pointList = new ArrayList<>();
		makeList(walls);
		doors = new ArrayList<>();
		makePath();
	}

   /**
    * No-argument constructor
    */
   public Room() {
      this.ROOMID = idCount;
      idCount++;
   }

   /**
    * Can assign id, title, description.  Used when splitting a room
    * @param id the ID of the room
    * @param title the title of the room
    * @param desc the description of the room
    * @param walls the walls of the room
    */
   public Room(int id,String title, String desc, ArrayList<Wall> walls){
      this.walls = walls;
      ROOMID = id;
      this.title = title;
      this.desc = desc;
      pointList = new ArrayList<>();
      makeList(walls);
      doors = new ArrayList<>();
      makePath();
   }

   /**
    * Can assign title, description.  Used when splitting a room
    * @param title the title of the room
    * @param desc the description of the room
    * @param walls the walls of the room
    */
   public Room(String title, String desc, ArrayList<Wall> walls){
      this.walls = walls;
      ROOMID = idCount;
      idCount++;
      this.title = title;
      this.desc = desc;
      pointList = new ArrayList<>();
      makeList(walls);
      doors = new ArrayList<>();
      makePath();
   }

   /**
    * Constructor for making rooms when detecting rooms, allows for creation of a room without assigning an ID
    * @param walls the walls of the room
    * @param assignId used to overload the constructor
    */
   public Room(ArrayList<Wall> walls, boolean assignId){
      this.walls = walls;
      pointList = new ArrayList<>();
      makeList(walls);
      doors = new ArrayList<>();
      makePath();
   }

	private void makeList(ArrayList<Wall> walls) {
		walls.forEach(wall->{
         pointList.add(new Point((int)wall.getX2(),(int)wall.getY2()));
         pointList.add(new Point((int)wall.getX1(),(int)wall.getY1()));
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
		if (path == null) {
			path = new GeneralPath();
		}
		if (path.getCurrentPoint() != null) {
         path.reset();
      }
      addWallsInOrder();

	}

   /**
    * Helper method for makePath, adds walls in the correct order to avoid errors with GeneralPath
    */
   private void addWallsInOrder() {
      ArrayList<Wall> addedWalls = new ArrayList<>();
      boolean isFirstWall = true;
      Point lastAddedPoint = null;
      while(addedWalls.size() != walls.size()){
         if(isFirstWall){
            isFirstWall = false;
            Wall firstWall = walls.get(0);
            path.moveTo(firstWall.getX1(),firstWall.getY1());
            path.lineTo(firstWall.getX2(),firstWall.getY2());
            lastAddedPoint = new Point((int)firstWall.getX2(),(int)firstWall.getY2());
            addedWalls.add(firstWall);
            for(int i = 1;i<walls.size();i++){
               Wall wall = walls.get(i);
               lastAddedPoint = addWallToPath(addedWalls, lastAddedPoint, wall);
            }
         }else{
            for (Wall wall : walls) {
               lastAddedPoint = addWallToPath(addedWalls, lastAddedPoint, wall);
            }
         }
      }
   }

   /**
    * Helper method for addWallsInOrder, adds the wall to the path if it's the next wall in order
    * @param addedWalls the list of addedWalls
    * @param lastAddedPoint the last point added to path
    * @param wall the wall to check
    * @return the last added point
    */
   private Point addWallToPath(ArrayList<Wall> addedWalls, Point lastAddedPoint, Wall wall) {
      if(!addedWalls.contains(wall)) {
         if (wall.getP1().equals(lastAddedPoint)) {
            path.lineTo(wall.getX2(), wall.getY2());
            addedWalls.add(wall);
            lastAddedPoint = new Point((int)wall.getX2(),(int)wall.getY2());
         } else if (wall.getP2().equals(lastAddedPoint)) {
            path.lineTo(wall.getX1(), wall.getY1());
            addedWalls.add(wall);
            lastAddedPoint = new Point((int)wall.getX1(),(int)wall.getY1());
         }
      }
      return lastAddedPoint;
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
      return this.path.contains(p);
		//return(onBoundary(p));
	}

   /**
    * Checks if this contains and shares a wall with r
    * @param r the Room to check is contained by this
    * @return true if r is contained by and shares a wall with this
    */
	public boolean sharesWallAndContains(Room r){
      boolean hasSharedWall = false;
      for(Point p:r.pointList){
         if(this.onBoundary(p)){
            hasSharedWall = true;
            break;
         }
      }
      if(hasSharedWall){
         return contains(r);
      }else{
         return false;
      }
   }

   /**
    * Checks if this contains the specified Room r
    * @param r the Room to check is contained by this
    * @return true if r is contained by this
    */
	private boolean contains (Room r){
	   //for each point p of r, check that each point immediately northwest, northeast, southeast, and southwest of p is
      // contained by r.  If p is contained by r but not by this, then this does not contain r
      for (Point p : r.pointList) {
         if(!(bothContain(r,p,-1,-1)
            && bothContain(r,p,1,-1)
            && bothContain(r,p,1,1)
            && bothContain(r,p,-1,1))){
            return false;
         }
      }
      return true;
	}

   /**
    * Helper method for contains(Room r).  Checks that r and this contain the point p offset by xOffset and yOffset
    * @param r the Room to check is contained by this
    * @param point the current point to check
    * @param xOffset the x offset for the point
    * @param yOffset the y offset for the point
    * @return true if r does not contain point offset by xOffset and yOffset, or both r and this contain point offset
    * by xOffset and yOffset
    */
	private boolean bothContain(Room r, Point point, int xOffset, int yOffset){
	   Point p = new Point(point.x+xOffset,point.y+yOffset);
	   if(r.contains(p)){
         return this.contains(p);
      }
	   return true;
   }

	@Override
	public String toString() {
		if (this.path == null) {
			return "*Room#" + this.ROOMID + "-" + this.title;
		} else {
			return "Room#" + this.ROOMID + "-" + this.title;
		}
	}

   /**
    * Checks if the specified point lies on the boundary of this
    * @param p the point to check
    * @return true if the specified point lies on the boundary of this
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
		for (int i = 0; i < RoomList.getInstance().list.size(); i++) {
			Room r = RoomList.getInstance().list.get(i);
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
