package pdc;

import javax.swing.undo.StateEdit;
import javax.swing.undo.StateEditable;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEditSupport;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
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
public abstract class MapLayer implements StateEditable {
	protected boolean drawingTransparent;
	protected Point start;
	private boolean drawingOpaque;
	protected ArrayList<GeneralPath> pathList;
	public ArrayList<Point> pointList;
	public GeneralPath guiPath;
	private boolean walling;
	protected Room selectedRoom;
	protected ArrayList<Wall> wallList;
	private boolean firstClick;
	private Point firstPoint;
   private Wall lastWall;
   private Room roomToDivide;
   private UndoableEditSupport undoSupport;
   private UndoManager undoManager;
   private StateEdit stateEdit;

   public MapLayer() {
      undoSupport = new UndoableEditSupport(this);
      undoManager = new UndoManager();
      undoSupport.addUndoableEditListener(undoManager);
		this.pathList = new ArrayList<>();
		this.pointList = new ArrayList<>();
		wallList = new ArrayList<>();
		this.drawingTransparent = false;
		this.selectedRoom = null;
		firstClick = false;
		roomToDivide = null;
	}

	public abstract void draw(Graphics g);

   /**
    * Method called to draw opaque walls
    * @param p the point the author clicked on
    */
	public void drawOpaqueWalls(Point p) {

	   firstClick = !firstClick;
	   if(firstClick){
	      firstPoint = p;
      }else{
         startStateEdit();
	      lastWall = new Wall(new Line2D.Double(firstPoint,p),Type.OPAQUE);
	      wallList.add(lastWall);
         detectRooms();
         pointList.add(firstPoint);
         pointList.add(p);
         endStateEdit();
      }


	}

	private void startStateEdit(){
      stateEdit = new StateEdit(MapLayer.this);
      RoomList.getInstance().startStateEdit();
   }

   private void endStateEdit(){
      stateEdit.end();
      RoomList.getInstance().endStateEdit();
      undoManager.addEdit(stateEdit);
   }

   /**
    * Method called to detect rooms from lines drawn on the map
    */
	private void detectRooms(){
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
            if (roomA.pointList.containsAll(roomB.pointList)) {
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
            if (roomA.contains(roomB)) {
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
            if (!roomsToRemove.contains(roomB)&&roomB.contains(roomA)) {

               roomsToRemove.add(roomB);
            }else if(!roomsToRemove.contains(roomA)&&roomA.contains(roomB)){
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
            wallList.add(new Wall(wallPoints.get(0),lastPoint,wall.getType()));
         }else{
            wallList.add(new Wall(lastPoint,point,wall.getType()));
            lastPoint = point;
         }
      }
      //add last segment to complete the wall
      wallList.add(new Wall(lastPoint,wallPoints.get(1),wall.getType()));
   }

   //TODO: improve Java Doc, detect if the wall divides two rooms?
   /**
    * Method to add archway onto map.
    * @param point
    * @throws Throwable
    */
   public void placeArchway(Point point) throws Throwable
   {
      Line2D archwayWall= new Line2D.Double();
      boolean flag = false;
      for(int i = 0; i< this.wallList.size(); i++)
      {
         if (this.wallList.get(i).getDistance(point) ==0)
         {
            Wall archwayWallObj = this.wallList.get(i);
            archwayWall = archwayWallObj.getLineRepresentation();
            System.out.println(Math.sqrt( ( ( archwayWall.getX2() - archwayWall.getX1() ) * ( archwayWall.getX2() - archwayWall.getX1() ) ) + ( ( archwayWall.getY2() - archwayWall.getY1() ) * ( archwayWall.getY2() - archwayWall.getY1() ) ) ));
            if(Math.sqrt( ( ( archwayWall.getX2() - archwayWall.getX1() ) * ( archwayWall.getX2() - archwayWall.getX1() ) ) + ( ( archwayWall.getY2() - archwayWall.getY1() ) * ( archwayWall.getY2() - archwayWall.getY1() ) ) )>= 15)
            {
               if((archwayWall.getX1() == archwayWall.getX2())  || (archwayWall.getY1() == archwayWall.getY2())) {
                  this.wallList.remove(archwayWallObj);
                  flag = true;
                  break;
               }
               else
               {
                  throw new Throwable("Archway must be placed on a rectilinear wall");
               }
            }
            else
            {
               throw new Throwable("Archway must be placed on a large enough wall");
            }

         }
      }
      if(flag)
      {

         Point2D start = archwayWall.getP1();
         Point2D end = archwayWall.getP2();
         Wall newStartWall = new Wall(new Line2D.Double(start, point),Type.OPAQUE);
         //TODO: Limit number of archways on wall?
         Point2D endArchway;
         if(archwayWall.getX1() == archwayWall.getX2())
         {
            endArchway = new Point2D.Double(archwayWall.getX2(), point.getY()-15);
            if(start.getY() < end.getY())
            {
              endArchway = new Point2D.Double(archwayWall.getX2(), point.getY()+15);
            }
         }
         else
         {
            endArchway = new Point2D.Double(point.getX()-15, archwayWall.getY2());
            if(start.getX() < end.getX())
            {
               endArchway = new Point2D.Double(point.getX()+15, archwayWall.getY2());
            }
         }
         Wall newEndWall = new Wall(new Line2D.Double(endArchway, end),Type.OPAQUE);
         Wall archwaySeg = new Wall(new Line2D.Double(point, endArchway),Type.ARCHWAY);
         //System.out.println("Start: " + start + "\nPoint: " + point + "\nEndArch: " + endArchway + "\nEnd: " + end);
         //System.out.println("StartWall: " + newStartWall.getLineRepresentation() + "\nArchway:" + archwaySeg
         // .getLineRepresentation() + "\nEnd: "+ newEndWall.getLineRepresentation());
         this.wallList.add(newStartWall);
         this.wallList.add(archwaySeg);
         this.wallList.add(newEndWall);
      }
      else
      {
         throw new Throwable("Archway must be placed on a wall");
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
      firstClick = !firstClick;

      if (firstClick) {
         //check that the player has clicked on the boundary of a room
         for(int i = 0; roomToDivide==null && i<RoomList.getInstance().list.size();i++){
            Room room = RoomList.getInstance().list.get(i);
            if(room.onBoundary(p)){
               roomToDivide = room;
            }
         }
         if(roomToDivide==null){
            //TODO error message that the author must divide a room with transparent walls
            walling = false;
            firstClick = false;
         }else {
            firstPoint = p;
         }
      } else {
         if(roomToDivide.onBoundary(p)) {
            StateEdit stateEdit = new StateEdit(MapLayer.this);
            pointList.add(firstPoint);
            pointList.add(p);
            lastWall = new Wall(new Line2D.Double(firstPoint, p), Type.TRANSPARENT);
            wallList.add(lastWall);
            detectRooms();
            stateEdit.end();
            undoManager.addEdit(stateEdit);
         }else {
            //TODO error message that the author must divide a room with transparent walls
            walling = false;
         }
         roomToDivide=null;
      }


		return this.walling;
	}


	public abstract MapLayer copy();

	public void undo(){
	   if(undoManager.canUndo()){
	      undoManager.undo();
      }
   }

	public Room getRoom(Point p) {
		return RoomList.getInstance().getRoom(p);
	}

	public void setSelectedRoom(Room r) {
       this.selectedRoom = r;
	}

   public void placeDoor(Point p) throws Throwable {
       this.guiPath = new GeneralPath();
       ArrayList<Room> l = RoomList.getInstance().list;
       for (int j = 0; j < l.size(); j++) { // For each room
           Room r = l.get(j);
           if (r.doorCount() < 8) { // Limit to 8 doors per room
               ArrayList<Point> list = r.pointList;
               if(list.contains(p)) {
               	throw new Throwable("Door must be placed on a wall");
               }
               for (int i = 0; i < list.size(); i++) { // Iterate over each pair of points
                   Point a = list.get(i);
                   Point b;
                   if (i == list.size() - 1) {
                       b = list.get(0);
                   } else {
                       b = list.get(i + 1);
                   }
                   // Create a shape matching the line between by points a and b
                   GeneralPath line = new GeneralPath();
                   double m;
                   line.moveTo(a.x, a.y);
                   line.lineTo(b.x, b.y);
                   Stroke s = new BasicStroke(4, BasicStroke.CAP_ROUND,
                           BasicStroke.JOIN_BEVEL);
                   Shape sh = s.createStrokedShape(line);
                   if (sh.contains(p)) { // Point is on the line
                       if (b.x != a.x) { // Case the line is not vertical
                           // M = slope between a and b
                           m = ((double)b.y - a.y) / ((double)b.x - a.x);
                           // Theta = angle between line with slope m and x axis
                           double theta = Math.toDegrees(Math.atan(m));
                           // Start path at point clicked
                           this.guiPath.moveTo(p.x, p.y);
                           this.guiPath.lineTo(
                                   p.x + Constants.GRIDDISTANCE
                                           * Math.cos(Math.toRadians(theta)),
                                   p.y + Constants.GRIDDISTANCE
                                           * Math.sin(Math.toRadians(theta)));
                           // Add path to doorList
                       } else { // Case the line is vertical
                           System.out.println("here");
                           this.guiPath.moveTo(p.x, p.y);
                           this.guiPath.lineTo(p.x,
                                   p.y + Constants.GRIDDISTANCE);
                       }
                       //TODO: Make door objects and store them
                       Door d = new Door(this.guiPath);
                       d.room = r;
                       d.title = "Room Title";
                       DoorList.add(d);
                       r.addDoor(d);
                   }
                }
            }
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
}
