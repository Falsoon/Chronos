package pdc;

import javafx.geometry.BoundingBox;
import javafx.util.Pair;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.*;


/**
 * Layer of the map
 *
 * @author Daniel
 *
 */
public abstract class MapLayer {
	protected boolean drawing;
	protected Point start;
	private boolean outlining;
	protected ArrayList<GeneralPath> pathList;
	public ArrayList<Point> pointList;
	public GeneralPath guiPath;
	private boolean walling;
	protected Room selectedRoom;
	protected ArrayList<Wall> wallList;
	private boolean firstClick;
	private Point firstPoint;
   private Wall lastWall;
   Room roomToDivide;

   public MapLayer() {
		this.pathList = new ArrayList<>();
		this.pointList = new ArrayList<>();
		wallList = new ArrayList<>();
		this.drawing = false;
		this.selectedRoom = null;
		firstClick = false;
		roomToDivide = null;
	}

	/*
	public void addPath(GeneralPath guiPath) {
		this.pathList.add(guiPath);
	}

	public GeneralPath removeLastPath() {
		if (!this.pathList.isEmpty()) {
			return this.pathList.remove(this.pathList.size() - 1);
		} else {
			return null;
		}
	}
*/
	public abstract void draw(Graphics g);

	public void drawOpaqueWalls(Point p) {

	   firstClick = !firstClick;
	   if(firstClick){
	      firstPoint = p;
      }else{
	      lastWall = new Wall(new Line2D.Double(firstPoint,p),Type.OPAQUE);
	      wallList.add(lastWall);
         detectRooms();
      }
	   pointList.add(p);

	   /*
		this.opaqueWallMode = true;

		this.pointList.add(p);
		boolean first = !this.drawing;
		// at the first point, start a newguiPath
		if (!this.drawing) {
			this.start = p;// save start to compare later
			this.guiPath = this.getPath(p);// get path with point. sets start
			if (this.guiPath.getCurrentPoint() == null) {
				this.guiPath.moveTo(p.x, p.y);
			}
			this.drawing = true;
		} else {
			// if not the first point, add toguiPath
			this.guiPath.lineTo(p.x, p.y);
		}
		// if the guiPath has returned to start, the end drawOpaqueWalls
		if (!first && p.equals(this.start)) {
			this.opaqueWallMode = false;
			this.guiPath.closePath();
			RoomList.add(new Room((GeneralPath) this.guiPath.clone()));
		}
		return this.opaqueWallMode;
		*/
	}


	private void detectRooms(){
      breakUpWallsAtIntersections();

      //map intersections to vertices on a graph and walls to edges

      ArrayList<Room> tempRoomList = getRooms();
      //tempRoomList now contains all the valid rooms and only the valid rooms

      //TODO if any existing room contains rooms from templist, that means it's been split.  remove it, give one of
      // the contained rooms the original ID, assign new ones to the others, give all the same name and desc as
      // original room
      //if tempRoomList is bigger, there's a new room in town
      if(tempRoomList.size()>RoomList.list.size()) {
         //check if the new room is from splitting a room, handle that if so
         for (int i = 0; i < RoomList.list.size(); i++) {
            ArrayList<Room> containedRooms = new ArrayList<>();
            Room roomA = new Room();
            for (int j = 0; j < RoomList.list.size(); j++) {
               if (j == i) {
                  continue;
               }
               roomA = RoomList.list.get(i);
               Room roomB = RoomList.list.get(j);
               if (roomA.contains(roomB)) {
                  containedRooms.add(roomB);
               }
            }
            if (containedRooms.size() > 0) {
               RoomList.remove(roomA);
               for(Room containedRoom : containedRooms) {
                  RoomList.add(new Room(roomA.ROOMID, roomA.title, roomA.desc, containedRoom.walls));
                  tempRoomList.remove(containedRoom);
               }
            }
         }
         //if there are still remaining rooms, they're new rooms.  Simply add them

      }
      //TODO only add rooms new rooms
      tempRoomList.forEach(RoomList::add);

   }

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
      cyclesAsEdges.forEach(cycle-> tempRoomList.add(new Room(cycle)));

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

   private boolean linesMatch(Line2D wallA, Line2D wallB){
	   return xMatch(wallA,wallB)&&yMatch(wallA,wallB);
   }
   
   private boolean xMatch(Line2D wallA, Line2D wallB){
	   return (wallA.getX1()==wallB.getX1()
         ||wallA.getX1()==wallB.getX2())
         &&
         (wallA.getX2()==wallB.getX1()
         ||wallA.getX2()==wallB.getX2());
   }
   
   private boolean yMatch(Line2D wallA, Line2D wallB){
      return (wallA.getY1()==wallB.getY1()
         ||wallA.getY1()==wallB.getY2())
         &&
         (wallA.getY2()==wallB.getY1()
         ||wallA.getY2()==wallB.getY2());
   }

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

   /*
   private GeneralPath getPath(Point p) {
		int index = 0;
		boolean found = false;
		GeneralPath path;
		for (int i = 0; i < this.pathList.size() && !found; i++) {
			found = this.onPath(this.pathList.get(i), p);
			if (found) {
				index = i;
			}
		}
		// if point on a path
		if (found) {
			path = this.pathList.get(index);
			// setStart(path);
		} else {
			path = new GeneralPath();
			this.pathList.add(path);
		}
		return path;
	}
	*/

	/**
	 *
	 * @param path
	 *            - a path to search for the point
	 * @param p
	 *            - a point to find on the path
	 * @return found - if the point is a part of the path
	 */
	private boolean onPath(GeneralPath path, Point p) {
		boolean found = false;
		Point point = new Point();
		double[] coords = new double[6];
		PathIterator pi = path.getPathIterator(null);
		if (!pi.isDone()) {
			pi.currentSegment(coords);
			point.setLocation((int) coords[0], (int) coords[1]);
			found = p.equals(point);
		}
		if (found) {
			this.start.setLocation(path.getCurrentPoint());
		}
		if (!found) {
			found = p.equals(path.getCurrentPoint());
			if (found) {
				this.start.setLocation(point);
			}
		}
		return found;
	}


	/*
	 * drawTransparentWalls is used to encapsulate the logic behind implementing a
	 * transparent wall.
	 *
	 * going forward, we could ID whether the layer is a transparentWallMode and whether the
	 * previous layer is a drawOpaqueWalls layer before calling this method
	 */

	public boolean drawTransparentWalls(Point p) {
		this.walling = true;
      firstClick = !firstClick;

      if (firstClick) {
         //check that the player has clicked on the boundary of a room
         for(int i = 0; roomToDivide==null && i<RoomList.list.size();i++){
            Room room = RoomList.list.get(i);
            if(room.onBoundary(p)){
               roomToDivide = room;
            }
         }
         if(roomToDivide==null){
            //TODO error message that the author must divide a room with transparent walls
            firstClick = false;
         }else {
            firstPoint = p;
         }
      } else {
         if(roomToDivide.onBoundary(p)) {
            lastWall = new Wall(new Line2D.Double(firstPoint, p), Type.TRANSPARENT);
            wallList.add(lastWall);
            detectRooms();
            System.out.println("ROOMS: " + RoomList.list.size());
         }else {
            //TODO error message that the author must divide a room with transparent walls
         }
         roomToDivide=null;
      }
      pointList.add(p);

//		Room r1 = null, r2 = RoomList.getRoom(p);
//		//if r2 null then just draw
//		if(r2 != null) {
//         if (r2.onBoundary(p)) {
//            boolean first = this.pointList.isEmpty();
//            this.pointList.add(p);
//            if (r2 != null) {
//               //r1 = r2.split(this.pointList);
//            }
//            if (r1 != null) {
//               RoomList.add(r1);
//            }
//            if (!first) {
//               this.walling = false;
//               this.pointList.clear();
//            }
//         } else {
//            if (!this.pointList.isEmpty()) {
//               this.pointList.add(p);
//            }
//         }
//      }
//		if (!this.drawing) {
//			this.guiPath = new GeneralPath();
//			this.guiPath.moveTo(p.x, p.y);
//			this.drawing = true;
//			//TODO add logic for pushing to closest call point here
//			this.pathList.add(this.guiPath);
//		} else {
//			this.guiPath.lineTo(p.x, p.y);
//			//Calculate for midpoint, removing them in case they cross over another line
//			/*Point p2 = this.pointList.remove(0);
//			Point p1 = this.pointList.remove(1);
//			Point midp = new Point();
//			midp.x = (int) Math.sqrt( (p1.x + p2.x)/2.0);
//			midp.y = (int) Math.sqrt((p2.y + p1.y )/2.0);*/
//			//Get room from midpoint by roomlist
//			//don't have to worry about r being null, we take care of this earlier
//			/*Room r = RoomList.getRoom(midp);
//			if( r != null) {
//				//split room
//			}*/
//			//snap end points of line to 2 wall opaque walls that have been crossed
//				//make the line an infinite line to see if it crosses 2 opaque walls of the room
//			/*double slope = (p2.y - p1.y) / (p2.x - p1.x) * 1.0; */
//			/*while(!r.contains(p2)) {
//
//			}*/
//				//if so, adjust end points to wall boundary
//			//p1 = newEndPoint1;
//			//p2 = newEndPoint2;
//				//double check that no other transparent walls are being crossed over,
//			/*boolean onOtherTrans = false;
//			for(int i = 0; this.pathList.size() > 0 ; i++) {
//				GeneralPath exisitingP = this.pathList.get(i);
//				exisitingP.c
//			}*/
//				//if so delete this line, and display error message
//			//add new path to pathlist
//			drawing = false;
//		}

		return this.walling;
	}



	public abstract MapLayer copy();

	public abstract void undo();

	public Room getRoom(Point p) {
		return RoomList.getRoom(p);
	}

	/*
	public boolean drawOpaqueWalls(Point p, Room room) {
		this.opaqueWallMode = true;
		this.pointList.add(p);
		boolean first = !this.drawing;
		// at the first point, start a newguiPath
		if (!this.drawing) {
			this.guiPath = new GeneralPath();
			this.guiPath.moveTo(p.x, p.y);
			this.start = p;// save start to compare later
			this.drawing = true;
			this.pathList.add(this.guiPath);
		} else {
			// if not the first point, add toguiPath
			this.guiPath.lineTo(p.x, p.y);
		}
		// if the guiPath has returned to start, the end drawOpaqueWalls
		if (!first && p.equals(this.start)) {
			this.opaqueWallMode = false;
			this.guiPath.closePath();
			room.setPath((GeneralPath) this.guiPath.clone());
		}
		return this.opaqueWallMode;
	}
	*/

    public void setSelectedRoom(Room r) {
        this.selectedRoom = r;
    }

    public void placeDoor(Point p) throws Throwable {
        this.guiPath = new GeneralPath();
        ArrayList<Room> l = RoomList.list;
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
