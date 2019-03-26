package pdc;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.undo.StateEdit;
import javax.swing.undo.StateEditable;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.stream.Collectors;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;


/**
 * Layer of the map
 *
 * @author Daniel
 *
 */
@SuppressWarnings("serial")
public abstract class MapLayer implements StateEditable, Serializable {
   protected boolean drawingTransparent;
   protected Point start;
   protected ArrayList<GeneralPath> pathList;
   public ArrayList<Point> pointList;
   public GeneralPath guiPath;
   public boolean throwAlerts;
   private boolean walling;
   protected Room selectedRoom;
   protected ArrayList<Wall> wallList;
   private boolean firstClick;
   private Wall lastWall;
   //private UndoableEditSupport undoSupport;
   //private UndoManager undoManager;
   //private StateEdit stateEdit;
   private Point lastPoint;
   private boolean wasFirstClick;
   private ArrayList<Room> candidateRoomsForTransparent;

   public MapLayer() {

        /* undoSupport = new UndoableEditSupport(this);
        undoManager = new UndoManager();
        undoSupport.addUndoableEditListener(undoManager); */

		this.pathList = new ArrayList<>();
		this.pointList = new ArrayList<>();
		wallList = new ArrayList<>();
      throwAlerts = true;
		this.selectedRoom = null;
		firstClick = true;
		candidateRoomsForTransparent = new ArrayList<>();
	}

	public abstract void draw(Graphics g);

   public abstract void setPlayerStartingPosition(Point p);

   /**
    * Method called to draw opaque walls
    * @param p the point the author clicked on
    */
	public void drawOpaqueWalls(Point p) {

	   if(firstClick){
	      lastPoint = p;
	      firstClick = false;
	      wasFirstClick = true;
      }else{
	      //check that the last 2 points clicked are not the same
         if(!lastPoint.equals(p)) {
         //startStateEdit();

         lastWall = new Wall(new Line2D.Double(lastPoint, p), WallType.OPAQUE);
         wallList.add(lastWall);
         detectRooms();
         //add lastPoint if it hasn't been added yet
         if (wasFirstClick) {
            pointList.add(lastPoint);
            wasFirstClick = false;
         }
         pointList.add(p);
         lastPoint = p;

         //endStateEdit();
         }
      }

	}

   /*private void startStateEdit(){
      stateEdit = new StateEdit(MapLayer.this);
      RoomList.getInstance().startStateEdit();
   }

   private void endStateEdit(){
      stateEdit.end();
      RoomList.getInstance().endStateEdit();
      undoManager.addEdit(stateEdit);
   }*/

   /**
    * Method called to detect rooms from lines drawn on the map
    */
	public void detectRooms(){
      breakUpWallsAtIntersections();

      ArrayList<Room> tempRoomList = getRooms();
      //tempRoomList now contains all the valid rooms and only the valid rooms

      //if tempRoomList is bigger than the RoomList, there's a new room in town
      if(tempRoomList.size()>RoomList.getInstance().list.size()) {
         addNewRooms(tempRoomList);
      }

   }

   /**
    * Method to add new rooms to RoomList
    * @param tempRoomList the temporary list of rooms detected after a wall has been drawn
    */
   private void addNewRooms(ArrayList<Room> tempRoomList) {
      //find the new rooms
      ArrayList<Room> newRooms = new ArrayList<>();
      for (Room roomA : tempRoomList) {
         boolean unique = true;
         for (int j = 0; unique && j < RoomList.getInstance().list.size(); j++) {
            Room roomB = RoomList.getInstance().list.get(j);
            if (roomA.walls.size()==roomB.walls.size()&&roomA.walls.containsAll(roomB.walls)) {
               unique = false;
            }
         }
         if (unique) {
            newRooms.add(roomA);
         }
      }
      //check which if any of the new rooms are from splitting a room, handle that if so
      ArrayList<Room> roomsToRemove = new ArrayList<>();
      ArrayList<Room> subRooms = new ArrayList<>();
      for (int i = 0; i < RoomList.getInstance().list.size(); i++) {
         ArrayList<Room> containedRooms = new ArrayList<>();
         Room roomA = RoomList.getInstance().list.get(i);
         for (Room roomB : newRooms) {
            if (roomA.sharesWallAndContains(roomB)) {
               containedRooms.add(roomB);
            }
         }
         if (containedRooms.size() > 0) {
            roomsToRemove.add(roomA);
            boolean first = true;
            for(Room containedRoom : containedRooms) {
               if(first) {
                  subRooms.add(new Room(roomA.ROOMID, roomA.title, roomA.desc, containedRoom.walls));
                  first = false;
               }else{
                  subRooms.add(new Room(roomA.title, roomA.desc, containedRoom.walls));
               }
               newRooms.remove(containedRoom);
            }
         }
      }
      //remove the old rooms, add the subRooms
      RoomList.getInstance().list.removeAll(roomsToRemove);
      RoomList.getInstance().list.addAll(subRooms);
      //if there are still remaining rooms, they're regular new rooms.  Simply add them
      newRooms.forEach(room-> RoomList.getInstance().add(new Room(room.walls)));
   }

   /**
    * Method to find all rooms on the map after a line has been drawn
    * @return a list of all rooms detected on the map
    */
   private ArrayList<Room> getRooms() {
      Graph graph = new Graph(wallList.size());
      wallList.forEach(wall-> graph.addEdge(wall.getP1(),wall.getP2()));
      graph.findCycles();
      ArrayList<Point2D[]> cycles = graph.cycles;
      //convert cycles from vertices to edges
      ArrayList<ArrayList<Wall>> cyclesAsEdges = new ArrayList<>();
      cycles.forEach(cycle->{
         ArrayList<Wall> currentCycle = new ArrayList<>();
         for(int i = 0;i<cycle.length-1;i++){
            Line2D line = new Line2D.Double(cycle[i],cycle[i+1]);
            for(Wall wall : wallList){
               if(linesMatch(wall.getLineRepresentation(),line)){
                  currentCycle.add(wall);
               }
            }
         }
         //add edge back to the starting vertex
         Line2D line = new Line2D.Double(cycle[cycle.length-1],cycle[0]);
         for(Wall wall : wallList){
            if(linesMatch(wall.getLineRepresentation(),line)){
               currentCycle.add(wall);
            }
         }
         cyclesAsEdges.add(currentCycle);
      });
      //create new rooms from the cycles, put in a temporary array before sorting out non-rooms
      ArrayList<Room> tempRoomList = new ArrayList<>();
      ArrayList<Room> roomsToRemove = new ArrayList<>();
      //add potential rooms to the list, but don't assign them IDs until they're verified to be desired rooms
      cyclesAsEdges.forEach(cycle-> tempRoomList.add(new Room(cycle,false)));

      //remove all rooms that contain other rooms
      for(int i = 0; i<tempRoomList.size()-1;i++){
         for(int j = i+1;j<tempRoomList.size();j++){
            Room roomA = tempRoomList.get(i);
            Room roomB = tempRoomList.get(j);
            if (!roomsToRemove.contains(roomB)&&roomB.sharesWallAndContains(roomA)) {

               roomsToRemove.add(roomB);
            }else if(!roomsToRemove.contains(roomA)&&roomA.sharesWallAndContains(roomB)){
               roomsToRemove.add(roomA);
            }
         }
      }
      tempRoomList.removeAll(roomsToRemove);
      return tempRoomList;
   }

   /**
    * Helper method to determine if 2 lines are the same
    * @param lineA the first line
    * @param lineB the second line
    * @return true if lineA and lineB have the same coordinates in either order
    */
   private boolean linesMatch(Line2D lineA, Line2D lineB){
	   return xMatch(lineA,lineB)&&yMatch(lineA,lineB);
   }

   /**
    * Helper method to determine if the x coordinates of the 2 lines are the same
    * @param lineA the first line
    * @param lineB the second line
    * @return true of lineA and lineB have the same x coordinate in either order
    */
   private boolean xMatch(Line2D lineA, Line2D lineB){
	   return (lineA.getX1()==lineB.getX1()
         ||lineA.getX1()==lineB.getX2())
         &&
         (lineA.getX2()==lineB.getX1()
         ||lineA.getX2()==lineB.getX2());
   }

   /**
    * Helper method to determine if the y coordinates of the 2 lines are the same
    * @param lineA the first line
    * @param lineB the second line
    * @return true of lineA and lineB have the same y coordinate in either order
    */
   private boolean yMatch(Line2D lineA, Line2D lineB){
      return (lineA.getY1()==lineB.getY1()
         ||lineA.getY1()==lineB.getY2())
         &&
         (lineA.getY2()==lineB.getY1()
         ||lineA.getY2()==lineB.getY2());
   }

   /**
    * Breaks up walls that intersect into separate line segments so that they are easier to work with
    */
   private void breakUpWallsAtIntersections() {
      HashMap<Wall, ArrayList<Point>> wallsToBreak = new HashMap<>();
      for (Wall wallA : wallList) {
         if (Line2D.linesIntersect(wallA.getX1(), wallA.getY1(), wallA.getX2(), wallA.getY2(),
            lastWall.getX1(), lastWall.getY1(), lastWall.getX2(), lastWall.getY2())) {
            Optional<Point> intersection = intersectionPoint(wallA.getLineRepresentation(), lastWall.getLineRepresentation());
            intersection.ifPresent(point -> {
               if ((wallA.getP1().equals(point) || wallA.getP2().equals(point))
                  && !(lastWall.getP1().equals(point) || lastWall.getP2().equals(point))) {
                  //case 1: wall A intersects at an endpoint, need to break up lastWall and add its new segments
                  if(!wallsToBreak.containsKey(lastWall)) {
                     wallsToBreak.put(lastWall, new ArrayList<>());
                  }
                  wallsToBreak.get(lastWall).add(point);
               } else if ((lastWall.getP1().equals(point) || lastWall.getP2().equals(point))
                  && !(wallA.getP1().equals(point) || wallA.getP2().equals(point))) {
                  //case 2: lastWall intersects at an endpoint, need to remove wallA, break it up, and add the new segments
                  if(!wallsToBreak.containsKey(wallA)) {
                     wallsToBreak.put(wallA, new ArrayList<>());
                  }
                  wallsToBreak.get(wallA).add(point);
               } else if (!(lastWall.getP1().equals(point) || lastWall.getP2().equals(point))
                  && !(wallA.getP1().equals(point) || wallA.getP2().equals(point))) {
                  //case 3: neither wall intersects at an endpoint, need to break up both walls and add their new segments
                  if(!wallsToBreak.containsKey(wallA)) {
                     wallsToBreak.put(wallA, new ArrayList<>());
                  }
                  wallsToBreak.get(wallA).add(point);

                  if(!wallsToBreak.containsKey(lastWall)) {
                     wallsToBreak.put(lastWall, new ArrayList<>());
                  }
                  wallsToBreak.get(lastWall).add(point);
               }
            });
         }
      }
      wallsToBreak.forEach((key, value) -> {
         wallList.remove(key);
         breakUpWallAndAddToList(key, value);
      });
      //remove duplicate walls
      wallList = (ArrayList<Wall>) wallList.stream().distinct().collect(Collectors.toList());
   }

   /**
    * Method to break line into multiple line segments at the specified points and add them to the walllist
    * @param points the points at which the wall will be broken up
    * @param wall the wall to be broken up
    */
   private void breakUpWallAndAddToList(Wall wall, ArrayList<Point> points) {
      PointSort ps = new PointSort();
      points.sort(ps);
      Point lastPoint = null;
      ArrayList<Point> wallPoints = new ArrayList<>();
      wallPoints.add(new Point((int)wall.getX1(),(int)wall.getY1()));
      wallPoints.add(new Point((int)wall.getX2(),(int)wall.getY2()));
      wallPoints.sort(ps);
      for(Point point : points){
         if(lastPoint == null){
            lastPoint = point;
            wallList.add(new Wall(wallPoints.get(0),lastPoint,wall.getWallType()));
         }else{
            wallList.add(new Wall(lastPoint,point,wall.getWallType()));
            lastPoint = point;
         }
      }
      //add last segment to complete the wall
      wallList.add(new Wall(lastPoint,wallPoints.get(1),wall.getWallType()));
   }

   //TODO: improve Java Doc, detect if the wall divides two rooms?
   /**
    * Method to add archway onto map.
    * @param point the point that was clicked
    * @throws Throwable if the author attempts to place an archway at an inappropriate point
    * @param point the point that was clicked
    * @throws Throwable if the author attempts to place an archway at an invalid point
    */
   public void placeArchway(Point point) throws Throwable {
      Line2D archwayWall= new Line2D.Double();
      Wall archwayWallObj = null;
      ArrayList<Room> roomsToUpdate = new ArrayList<>();
      boolean flag = false;
      int flagerror = 0;
      for(int i = 0; i< this.wallList.size(); i++) {
         if (this.wallList.get(i).getDistance(point) ==0) {
            archwayWallObj = this.wallList.get(i);
            archwayWall = archwayWallObj.getLineRepresentation();
            if(Math.sqrt( ( ( archwayWall.getX2() - archwayWall.getX1() ) * ( archwayWall.getX2() - archwayWall.getX1() ) ) + ( ( archwayWall.getY2() - archwayWall.getY1() ) * ( archwayWall.getY2() - archwayWall.getY1() ) ) )>= 15) {
               if((archwayWall.getX1() == archwayWall.getX2())) {
                  if ((Math.abs(point.getY() - archwayWall.getY1()) > 15) && (Math.abs(point.getY() - archwayWall.getY2()) > 15)) {
                     this.wallList.remove(archwayWallObj);
                     for(Room room : RoomList.getInstance().list){
                        if(room.walls.contains(archwayWallObj)){
                           roomsToUpdate.add(room);
                        }
                     }
                     flag = true;
                     break;
                  } else {
                     flagerror = 1;
                  }
               } else if(archwayWall.getY1() == archwayWall.getY2()) {
                  if ((Math.abs(point.getX() - archwayWall.getX1()) > 15) && (Math.abs(point.getX() - archwayWall.getX2()) > 15)) {
                     this.wallList.remove(archwayWallObj);
                     for(Room room : RoomList.getInstance().list){
                        if(room.walls.contains(archwayWallObj)){
                           roomsToUpdate.add(room);
                        }
                     }
                     flag = true;
                     break;
                  } else {
                     flagerror = 1;
                  }
               } else {
                  flagerror = 1;
               }
               }
               else
               {
                  flagerror = 1; //rectilinear
               }
            }

      }
      if(flag) {
         Point2D start = archwayWall.getP1();
         Point2D end = archwayWall.getP2();
         Wall newStartWall = new Wall(new Line2D.Double(start, point),WallType.OPAQUE);
         //TODO: Limit number of archways on wall?
         Point2D endArchway;
         if(archwayWall.getX1() == archwayWall.getX2()) {
            endArchway = new Point2D.Double(archwayWall.getX2(), point.getY()-15);
            if(start.getY() < end.getY()) {
              endArchway = new Point2D.Double(archwayWall.getX2(), point.getY()+15);
            }
         } else {
            endArchway = new Point2D.Double(point.getX()-15, archwayWall.getY2());
            if(start.getX() < end.getX()) {
               endArchway = new Point2D.Double(point.getX()+15, archwayWall.getY2());
            }
         }
         Wall newEndWall = new Wall(new Line2D.Double(endArchway, end),WallType.OPAQUE);
         Wall archwaySeg = new Wall(new Line2D.Double(point, endArchway),WallType.ARCHWAY);
         this.wallList.add(newStartWall);
         this.wallList.add(archwaySeg);
         this.wallList.add(newEndWall);
         for(Room room:roomsToUpdate){
            room.walls.remove(archwayWallObj);
            ArrayList<Wall> newWallList = room.walls;
            newWallList.add(newStartWall);
            newWallList.add(archwaySeg);
            newWallList.add(newEndWall);
            room.updatePath(newWallList);
         }
      } else {
         if(flagerror ==1)
         {
            dialog("Archway cannot be placed here.");
         }
         else {
            dialog("Archway must be placed on a wall.");

         }
      }
   }

   /**
    * Determines where the specified Line2Ds intersect
    * Adapted from https://www.geeksforgeeks.org/program-for-point-of-intersection-of-two-lines/
    * and https://www.baeldung.com/java-intersection-of-two-lines
    * @param lineA the first line
    * @param lineB the second line
    * @return the intersection as a point if the lines intersect, Optional.Empty() if not
    */
   private Optional<Point> intersectionPoint(Line2D lineA, Line2D lineB) {
      // Line AB represented as a1x + b1y = c1
      double a1 = lineA.getY2() - lineA.getY1();
      double b1 = lineA.getX1() - lineA.getX2();
      double c1 = a1*(lineA.getX1()) + b1*(lineA.getY1());

      // Line CD represented as a2x + b2y = c2
      double a2 = lineB.getY2() - lineB.getY1();
      double b2 = lineB.getX1() - lineB.getX2();
      double c2 = a2*(lineB.getX1())+ b2*(lineB.getY1());

      double determinant = a1*b2 - a2*b1;

      if (determinant == 0)
      {
         // The lines are parallel
            return Optional.empty();
      }
      else
      {
         double x = (b2*c1 - b1*c2)/determinant;
         double y = (a1*c2 - a2*c1)/determinant;
         return Optional.of(new Point((int)x, (int)y));
      }
   }

   /**
    * Method called when drawingTransparent transparent walls
    * @param p the point the author clicked
    * @return true if the author is still drawingTransparent transparent walls
    */
	public boolean drawTransparentWalls(Point p) {
      this.walling = true;
      if (firstClick) {
         firstClick = false;
         //check that the player has clicked on the boundary of a room
         for(int i = 0; i<RoomList.getInstance().list.size();i++){
            Room room = RoomList.getInstance().list.get(i);
            if(room.onBoundary(p)){
               candidateRoomsForTransparent.add(room);
            }
         }
         if(candidateRoomsForTransparent.size()==0){
            walling = false;
         }else {
            lastPoint = p;
         }
      } else {
         if(candidateRoomsForTransparent.size()>0) {
            boolean secondClickValid=false;
            for(int i = 0;!secondClickValid&&i<candidateRoomsForTransparent.size();i++){
               secondClickValid = candidateRoomsForTransparent.get(i).onBoundary(p);
            }
            if(secondClickValid) {
               StateEdit stateEdit = new StateEdit(MapLayer.this);
               pointList.add(lastPoint);
               pointList.add(p);
               lastWall = new Wall(new Line2D.Double(lastPoint, p), WallType.TRANSPARENT);
               wallList.add(lastWall);
               detectRooms();
               stateEdit.end();
               //undoManager.addEdit(stateEdit);
            }else{
               walling = false;
            }
         }else {
            walling = false;
         }
      }
		return this.walling;
	}


	public abstract MapLayer copy();

	/*public void undo(){
	   if(undoManager.canUndo()){
	      undoManager.undo();
      }
   }*/

	public Room getRoom(Point p) {
		return RoomList.getInstance().getRoom(p);
	}

	public void setSelectedRoom(Room r) {
       this.selectedRoom = r;
	}

   public void placeDoor(Point point) throws Throwable
   {
      Line2D doorWall= new Line2D.Double();
      ArrayList<Room> roomsToUpdate = new ArrayList<>();
      Wall doorWallObj = null;
      boolean flag = false;
      int flagerror = 0;
      for(int i = 0; i< this.wallList.size(); i++) {
         if (this.wallList.get(i).getDistance(point) ==0) {
            doorWallObj = this.wallList.get(i);
            doorWall = doorWallObj.getLineRepresentation();
            //System.out.println(Math.sqrt( ( ( archwayWall.getX2() - archwayWall.getX1() ) * ( doorWall.getX2() -
            // doorWall.getX1() ) ) + ( ( doorWall.getY2() - doorWall.getY1() ) * ( doorWall.getY2() - doorWall.getY1() ) ) ));
            if(Math.sqrt( ( ( doorWall.getX2() - doorWall.getX1() ) * ( doorWall.getX2() - doorWall.getX1() ) ) + ( ( doorWall.getY2() - doorWall.getY1() ) * ( doorWall.getY2() - doorWall.getY1() ) ) )>= 15){
               if((doorWall.getX1() == doorWall.getX2())) {
                  if ((Math.abs(point.getY() - doorWall.getY1()) > 15) && (Math.abs(point.getY() - doorWall.getY2()) > 15)) {
                     this.wallList.remove(doorWallObj);
                     for(Room room : RoomList.getInstance().list){
                        if(room.walls.contains(doorWallObj)){
                           roomsToUpdate.add(room);
                        }
                     }
                     flag = true;
                     break;
                  } else {
                     flagerror = 1;
                  }
               } else if(doorWall.getY1() == doorWall.getY2()) {
                  if ((Math.abs(point.getX() - doorWall.getX1()) > 15) && (Math.abs(point.getX() - doorWall.getX2()) > 15)) {
                     this.wallList.remove(doorWallObj);
                     for(Room room : RoomList.getInstance().list){
                        if(room.walls.contains(doorWallObj)){
                           roomsToUpdate.add(room);
                        }
                     }
                     flag = true;
                     break;
                  } else {
                     flagerror = 1;
                  }
               } else {
                  flagerror = 1;
               }
            } else {
               flagerror = 1;
            }

         }
      }
      if(flag) {
         Point2D start = doorWall.getP1();
         Point2D end = doorWall.getP2();
         Wall newStartWall = new Wall(new Line2D.Double(start, point),WallType.OPAQUE);
         //TODO: Limit number of doors on wall?
         Point2D endDoor;
         if(doorWall.getX1() == doorWall.getX2()) {
            endDoor = new Point2D.Double(doorWall.getX2(), point.getY()-15);
            if(start.getY() < end.getY()) {
               endDoor = new Point2D.Double(doorWall.getX2(), point.getY()+15);
            }
         } else {
            endDoor = new Point2D.Double(point.getX()-15, doorWall.getY2());
            if(start.getX() < end.getX()) {
               endDoor = new Point2D.Double(point.getX()+15, doorWall.getY2());
            }
         }
         Wall newEndWall = new Wall(new Line2D.Double(endDoor, end),WallType.OPAQUE);
         Wall doorSeg = new Wall(new Line2D.Double(point, endDoor),WallType.CLOSEDDOOR);
         this.wallList.add(newStartWall);
         this.wallList.add(doorSeg);
         this.wallList.add(newEndWall);
         for(Room room:roomsToUpdate){
            room.walls.remove(doorWallObj);
            ArrayList<Wall> newWallList = room.walls;
            newWallList.add(newStartWall);
            newWallList.add(doorSeg);
            newWallList.add(newEndWall);
            room.updatePath(newWallList);
         }
      } else {
         if(flagerror ==1) {
            dialog("Door cannot be placed here.");
         } else {
            dialog("Door must be placed on a wall.");
         }
      }
   }

    public ArrayList<Wall> getWallList()
    {
       return wallList;
    }

   /**
    * Represents changes of state when the author stops drawing
    */
   public void stopDrawing() {
	   firstClick = true;
	   lastPoint = null;
	   wasFirstClick = false;
   }

    /** Abstract method to set whether the MapLayer is for the player mode
    * @param setting the value to give to player mode
    */
   public abstract void setPlayerMode(boolean setting);

   /**
    * Deletes the wall or passageway at p, if there is a wall or passageway at p
    * @param p the point of the wall or passageway to delete
    */
   public void delete(Point p) {
      ArrayList<Wall> wallsToRemove = new ArrayList<>();
      ArrayList<Wall> portalsToRemove = new ArrayList<>();
      for(Wall wall:wallList){
         if(wall.getDistance(p)==0){
            //TODO find a good way to do this.
            // Handle clicking a portal vs a wall. If the author clicks a wall that contains an archway, need to handle
            // deleting that entire wall (check if wall shares intersection with passageway, delete the wall,
            // the passageway, and the wall on the other side of the passageway)
            //if the author clicked on the endpoint of 2 walls, ignore it.  The endpoint of a wall is often shared by
            // 2 walls, so it's ambiguous which wall was clicked.
            if(!wall.hasEndpoint(p)){
               WallType wallType = wall.getWallType();
               if(wallType.equals(WallType.OPAQUE)|| wallType.equals(WallType.TRANSPARENT)){
                  wallsToRemove.add(wall);
                  //remove any portals that are connected to the wall
                  //TODO ask if this is desired
                  /*
                  Point wallP1 = (Point) wall.getP1();
                  Point wallP2 = (Point) wall.getP2();
                  for(Wall portal : wallList){
                     if(portal.getWallType().equals(WallType.ARCHWAY)||portal.getWallType().equals(WallType.DOOR)||){
                        if(portal.hasEndpoint(wallP1)||portal.hasEndpoint(wallP2)){
                           wallsToRemove.add(portal);
                        }
                     }
                  }
                  */
                  ArrayList<Room> roomsToRemove = new ArrayList<>();
                  for(Room room: RoomList.getInstance().list){
                     if(room.walls.contains(wall)){
                        roomsToRemove.add(room);
                     }
                  }
                  RoomList.getInstance().list.removeAll(roomsToRemove);
               }else if(wall.isTraversable()){
                  portalsToRemove.add(wall);
               }
            }
         }
      }
      for(Wall portal : portalsToRemove){
         portal.setWallType(WallType.OPAQUE);
      }
      if(wallsToRemove.size()>0) {
         wallList.removeAll(wallsToRemove);
         detectRooms();
      }

   }

   /**
    * Custom comparator used to sort points when breaking up walls.
    */
   class PointSort implements Comparator<Point>{

       @Override
       public int compare(Point o1, Point o2) {
          if(o1.getX()!=o2.getX()){
             return (int) (o1.getX() - o2.getX());
          }else{
             return (int) (o1.getY() - o2.getY());
          }
       }
   }

   private void dialog(String message) {
      if(throwAlerts) {
         JOptionPane jop = new JOptionPane(message);
         final JDialog d = jop.createDialog("Error");
         d.setLocation(250, 250);
         d.setVisible(true);
      }
   }
}
