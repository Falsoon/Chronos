package pdc;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

import static pdc.Geometry.point2DToPoint;

/**
 * Represents a room on the map
 */
@SuppressWarnings("serial")
public class Room implements Serializable{
	public ArrayList<Wall> walls;
	public ArrayList<Point> pointList;
	public String desc = "", title = "";
	public int ROOMID;
	public static int idCount = 1;
	private ArrayList<Door> doors;
	public GeneralPath path;
	private HashMap<CardinalDirection,Wall> portals = new HashMap<>();

   /**
    * Create a room from a list of walls, typical case
    * @param walls the walls of the room
    */
	public Room(ArrayList<Wall> walls) {
		this.walls = walls;
		ROOMID = idCount;
		idCount++;
      pointList = new ArrayList<>();
		makePointList(walls);
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
      makePointList(walls);
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
      makePointList(walls);
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
      makePointList(walls);
      doors = new ArrayList<>();
      makePath();
   }

	private void makePointList(ArrayList<Wall> walls) {
		walls.forEach(wall->{
         pointList.add(point2DToPoint(wall.getP1()));
         pointList.add(point2DToPoint(wall.getP2()));
      });
	}

	public ArrayList<Door> getDoors() {
		return this.doors;
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
            Point p1 = point2DToPoint(firstWall.getP1());
            Point p2 = point2DToPoint(firstWall.getP2());
            path.moveTo(p1.x,p1.y);
            path.lineTo(p2.x,p2.y);
            lastAddedPoint = p2;
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
      //addedWalls has the walls in a nice sorted order, so give its value to walls
      walls = addedWalls;
      for(Wall wall : walls){
         if(wall.isPortal()){
            setPortalDirection(wall);
         }
      }
   }

   /**
    * Determines if the Walls of the Room were added in clockwise order or not based on the direction changes, then
    * sets the cardinal directions of this' Walls appropriately
    * @param directionChanges the changes of direction
    * @return true if the walls were drawn in clockwise order
    * @throws IllegalArgumentException if the orientation of the room cannot be determined from the provided arguments
    */
   private boolean isClockwiseOrientation(ArrayList<DirectionChange> directionChanges) throws IllegalArgumentException{
      long rights = directionChanges.stream().filter(directionChange -> directionChange.equals(DirectionChange.RIGHT)).count();
      long lefts = directionChanges.stream().filter(directionChange -> directionChange.equals(DirectionChange.LEFT)).count();
      if(lefts!=rights) {
         return rights > lefts;
      } else throw new IllegalArgumentException("Cannot determine room orientation from provided direction changes");

   }

   /**
    * Determine the direction the Wall represented by Points p1 and p2 is drawn.  Used to determine portal cardinal
    * direction
    * @param p1 the first point
    * @param p2 the next point
    * @return the absolute direction of the line
    * @throws IllegalArgumentException if p1 and p2 are the same point
    */
   private AbsoluteDirection getWallAbsoluteDirection(Point2D p1, Point2D p2) throws IllegalArgumentException{
      double xDelta = p2.getX() - p1.getX();
      if(xDelta > 0){
         return AbsoluteDirection.WEST_EAST;
      }else if(xDelta < 0){
         return AbsoluteDirection.EAST_WEST;
      }
      double yDelta = p2.getY() - p1.getY();
      if(yDelta > 0){
         return AbsoluteDirection.NORTH_SOUTH;
      }else if(yDelta < 0){
         return AbsoluteDirection.SOUTH_NORTH;
      }

      throw new IllegalArgumentException("Points p1 and p2 are the same.");
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
            path.lineTo(wall.getP2().getX(), wall.getP2().getY());
            Point p2 = point2DToPoint(wall.getP2());
            addedWalls.add(wall);
            lastAddedPoint = p2;
         } else if (wall.getP2().equals(lastAddedPoint)) {
            path.lineTo(wall.getP1().getX(), wall.getP1().getY());
            Point p1 = point2DToPoint(wall.getP1());
            addedWalls.add(wall);
            lastAddedPoint = p1;
         }
      }
      return lastAddedPoint;
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
	public boolean contains (Room r){
	   //for each point p of r, check that each point immediately northwest, northeast, southeast, and southwest of p is
      // contained by r.  If p is contained by r but not by this, then this does not contain r
      ArrayList<Point> pointsToCheck = new ArrayList<>();
      for (Point p : r.pointList) {
         int xOffset = -1;
         int yOffset = -1;
         Point offsetPoint = new Point(p.x+xOffset,p.y+yOffset);
         if(r.contains(offsetPoint)){
            pointsToCheck.add(offsetPoint);
         }
         xOffset = 1;
         offsetPoint = new Point(p.x+xOffset,p.y+yOffset);
         if(r.contains(offsetPoint)){
            pointsToCheck.add(offsetPoint);
         }
         yOffset = 1;
         offsetPoint = new Point(p.x+xOffset,p.y+yOffset);
         if(r.contains(offsetPoint)){
            pointsToCheck.add(offsetPoint);
         }
         xOffset = -1;
         offsetPoint = new Point(p.x+xOffset,p.y+yOffset);
         if(r.contains(offsetPoint)){
            pointsToCheck.add(offsetPoint);
         }
      }
      for(Point p : pointsToCheck){
         if(!this.contains(p)){
            return false;
         }
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
		for(Wall wall : walls){
		   if(wall.containsPoint(p)){
		      return true;
         }
      }
		return false;
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

	public ArrayList<Room> getAdjacentRoomsAsList(){
	   ArrayList<Room> adjacentRooms = new ArrayList<>();
	   for(Room room: RoomList.getInstance().list){
	      if(isAdjacent(room)){
	         adjacentRooms.add(room);
         }
      }
	   return adjacentRooms;
   }

	private String getDirection(Room r) {
		Point roomCenter, otherCenter;
		roomCenter = new Point((int)this.path.getBounds2D().getCenterX(), (int)this.path.getBounds2D().getCenterY());
		otherCenter = new Point((int)r.path.getBounds2D().getCenterX(), (int)r.path.getBounds2D().getCenterY());
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
         return pOnBoundary >= 2;
		}
	}

   /**
    * Updates the walls of the room (e.g. if a portal is added to the room)
    * @param newWalls the new list of walls
    */
	public void updatePath(ArrayList<Wall> newWalls){
	   walls = newWalls;
	   pointList.clear();
	   makePointList(walls);
	   makePath();
   }

   /**
    * Returns all Rooms internal to this that do not share a wall with this
    * @return An ArrayList\<Room\> containing all Rooms contained by this which do not share a wall with this
    */
   public ArrayList<Room> getContainedRooms(){
	   return RoomList.getInstance().list.stream().filter(room->!room.equals(this)&&this.contains(room)).collect(Collectors.toCollection(ArrayList::new));
   }

   /**
    * Determines if this has any traversable Walls
    * @return true if this.walls contains any Walls of type ARCHWAY, DOOR, or TRANSPARENT
    */
   public boolean hasTraversableWall(){
      return walls.stream().anyMatch(Wall::isTraversable);
   }

   /**
    * Get the accessible Rooms adjacent to this
    * An "accessible room" is defined here as a room that the player can walk to by going through portals and
    * transparent walls belonging to rooms within the map
    * @return an ArrayList\<Room\> of Rooms adjacent to and accessible from this
    */
   private ArrayList<Room> getAccessibleAdjacentRooms() {
      ArrayList<Room> adjacentRoomsWithTraversableWalls = getAdjacentRoomsAsList().stream().filter(
         Room::hasTraversableWall
      ).collect(Collectors.toCollection(ArrayList::new));
      return adjacentRoomsWithTraversableWalls.stream().filter(
         room -> room.walls.stream().anyMatch(wall->wall.isTraversable()&&this.walls.contains(wall))
      ).collect(Collectors.toCollection(ArrayList::new));
   }

    /**
     * Get the accessible Rooms contained in this
     * An "accessible room" is defined here as a room that the player can walk to by going through portals and
     * transparent walls belonging to rooms within the map
     * @return an ArrayList\<Room\> of Rooms contained within and accessible from this
     */
    private ArrayList<Room> getAccessibleInnerRooms(){
        //if room has a traversable Wall that is not shared with any of its adjacent Rooms, then that traversable
        //Wall connects to the outer Room (this)
        return RoomList.getInstance().list.stream().filter(this::hasAccessibleInnerRoom).collect(Collectors.toCollection(ArrayList::new));
   }

    /**
     * Get the accessible Rooms that contain this
     * An "accessible room" is defined here as a room that the player can walk to by going through portals and
     * transparent walls belonging to rooms within the map
     * @return an ArrayList\<Room\> of Rooms that contain and are accessible from this
     */
    private ArrayList<Room> getAccessibleOuterRooms() {
        return RoomList.getInstance().list.stream().filter(room->{
            //if this has a traversable Wall that is not shared with any of its adjacent Rooms, then that traversable
            //Wall connects to the outer Room
            return room.hasAccessibleInnerRoom(this);
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Determines if room is an accessible inner Room of this
     * An "accessible room" is defined here as a room that the player can walk to by going through portals and
     * transparent walls belonging to rooms within the map
     * @param room the room to check
     * @return true if room is an accessible inner Room of this
     */
    private boolean hasAccessibleInnerRoom(Room room){
        if(!this.equals(room)&&this.contains(room)&&room.hasTraversableWall()){
            boolean isImmediatelyInside = true;
            for(Room otherRoom:RoomList.getInstance().list){
                //if otherRoom:
                // 1. is not one of the 2 rooms in question,
                // 2. contains room, and
                // 3. is contained by this
                //then room is not immediately inside this
                if(!otherRoom.equals(this)
                        &&!otherRoom.equals(room)
                        &&otherRoom.contains(room)
                        &&this.contains(otherRoom)){
                    isImmediatelyInside = false;
                    break;
                }
            }
            if(isImmediatelyInside) {
                ArrayList<Wall> traversableWalls = room.walls.stream().filter(
                        Wall::isTraversable
                ).collect(Collectors.toCollection(ArrayList::new));
                return room.hasOuterWall(traversableWalls);
            }
        }
        return false;
    }

    /**
     * determines if this has an outer Wall, defined as a Wall that is not shared by any other Room
     * @param traversableWalls the traversable walls of this
     * @return true is this has an outer Wall not shared by any other Room
     */
    private boolean hasOuterWall(ArrayList<Wall> traversableWalls){
        for(Wall traversableWall:traversableWalls){
            boolean isOuterWall = true;
            for(Room adjacentRoom:getAdjacentRoomsAsList()){
                if(adjacentRoom.walls.contains(traversableWall)){
                    isOuterWall = false;
                    break;
                }
            }
            if(isOuterWall){
                return true;
            }
        }
        return false;
    }

    /**
     * Gets all rooms accessible from this
     * An "accessible room" is defined here as a room that the player can walk to by going through portals and
     * transparent walls belonging to rooms within the map
     * @return an ArrayList\<Room\> containing all Rooms accessible from this
     */
   public ArrayList<Room> getAccessibleRooms(){
       HashSet<Room> rooms = new HashSet<>();
       rooms.addAll(getAccessibleAdjacentRooms());
       rooms.addAll(getAccessibleInnerRooms());
       rooms.addAll(getAccessibleOuterRooms());
       return new ArrayList<>(rooms);
   }

   /**
    * Sets the cardinal direction of the specified Wall portal.
    * @param portal the Wall representing a portal
    */
   private void setPortalDirection(Wall portal) {
      CardinalDirection direction = getWallDirection(portal);
      updateRoomPortals(portal,direction);
   }

   private DirectionChange getDirectionChange(AbsoluteDirection previousDirection, AbsoluteDirection nextDirection){
      switch (previousDirection){
         case WEST_EAST:
            switch (nextDirection) {
               case NORTH_SOUTH:
                  return DirectionChange.RIGHT;
               case SOUTH_NORTH:
                  return DirectionChange.LEFT;
            }
         case EAST_WEST:
            switch (nextDirection) {
               case NORTH_SOUTH:
                  return DirectionChange.LEFT;
               case SOUTH_NORTH:
                  return DirectionChange.RIGHT;
            }
         case SOUTH_NORTH:
            switch (nextDirection) {
               case WEST_EAST:
                  return DirectionChange.RIGHT;
               case EAST_WEST:
                  return DirectionChange.LEFT;
            }
         case NORTH_SOUTH:
            switch (nextDirection) {
               case WEST_EAST:
                  return DirectionChange.LEFT;
               case EAST_WEST:
                  return DirectionChange.RIGHT;
            }
      }
      throw new IllegalArgumentException("Invalid direction change: "+previousDirection.toString()+" to "+nextDirection.toString());
   }

   /**
    * Updates this' directional portals based on the CardinalDirection of Wall portal
    * @param portal the Wall Object representing a portal
    * @param direction the Cardinal of portal
    */
   private void updateRoomPortals(Wall portal, CardinalDirection direction){
      if(!portals.containsKey(direction)) {
         portals.put(direction, portal);
      }else if (!portals.get(direction).equals(portal)){
         throw new IllegalStateException("There is already a "+direction.toString()+" portal.");
      }

   }

   public HashMap<CardinalDirection,Wall> getPortals(){
      return portals;
   }

   public boolean hasPortalInDirection(CardinalDirection direction){
      return portals.containsKey(direction);
   }

   public boolean canPlacePortalOnWall(Wall wall){
      return(hasPortalInDirection(getWallDirection(wall)));
   }

   public CardinalDirection getWallDirection(Wall wallToCheck){
      AbsoluteDirection lastDirection = null;
      AbsoluteDirection wallAbsoluteDirection = null;
      AbsoluteDirection currentDirection;
      Point2D lastPoint = new Point2D.Double();
      ArrayList<DirectionChange> directionChanges = new ArrayList<>();
      boolean isFirstWall = true;
      for(Wall wall : walls){
         Point2D p1 = wall.getP1();
         Point2D p2 = wall.getP2();
         if(isFirstWall){
            isFirstWall = false;
         }else{
            if(wall.getP1().equals(lastPoint)){
               p1 = wall.getP1();
               p2 = wall.getP2();
            }else{
               p2 = wall.getP1();
               p1 = wall.getP2();
            }
         }
         lastPoint = p2;

         currentDirection = getWallAbsoluteDirection(p1,p2);
         if(wall.equals(wallToCheck)){
            wallAbsoluteDirection = currentDirection;
         }
         if(lastDirection != null && !currentDirection.equals(lastDirection)){
            directionChanges.add(getDirectionChange(lastDirection,currentDirection));
         }
         lastDirection = currentDirection;
      }
      boolean isClockwiseOrientation = isClockwiseOrientation(directionChanges);
      return getCardinalDirection(isClockwiseOrientation,wallAbsoluteDirection);
   }

   private CardinalDirection getCardinalDirection(boolean clockwise, AbsoluteDirection absoluteDirection){
      CardinalDirection direction = null;
      if(clockwise){
         switch (absoluteDirection){
            case NORTH_SOUTH:
               direction = CardinalDirection.EAST;
               break;
            case SOUTH_NORTH:
               direction = CardinalDirection.WEST;
               break;
            case EAST_WEST:
               direction = CardinalDirection.SOUTH;
               break;
            case WEST_EAST:
               direction = CardinalDirection.NORTH;
               break;
         }
      }else{
         switch (absoluteDirection){
            case NORTH_SOUTH:
               direction = CardinalDirection.WEST;
               break;
            case SOUTH_NORTH:
               direction = CardinalDirection.EAST;
               break;
            case EAST_WEST:
               direction = CardinalDirection.NORTH;
               break;
            case WEST_EAST:
               direction = CardinalDirection.SOUTH;
               break;
         }
      }
      return direction;
   }

}
