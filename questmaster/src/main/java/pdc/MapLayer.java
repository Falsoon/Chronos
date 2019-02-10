package main.java.pdc;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.util.ArrayList;


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

	public MapLayer() {
		this.pathList = new ArrayList<GeneralPath>();
		this.pointList = new ArrayList<Point>();
		this.drawing = false;
		this.selectedRoom = null;
	}

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

	public abstract void draw(Graphics g);

	public boolean outline(Point p) {
		this.outlining = true;

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
		// if the guiPath has returned to start, the end outline
		if (!first && p.equals(this.start)) {
			this.outlining = false;
			this.guiPath.closePath();
			RoomList.add(new Room((GeneralPath) this.guiPath.clone()));
		}
		return this.outlining;
	}

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
	 * transWalling is used to encapsulate the logic behind implementing a
	 * transparent wall.
	 *
	 * going forward, we could ID whether the layer is a walling and whether the
	 * previous layer is a outline layer before calling this method
	 */
	public boolean transWalling(Point p, MapLayer previousLayer) {
		this.walling = true;
		Room r1 = null, r2 = RoomList.getRoom(p);
		//if r2 null then just draw
		if(r2 == null) {
			//just draw
		}
		else if (r2.onBoundary(p)) {
			boolean first = this.pointList.isEmpty();
			this.pointList.add(p);
			if (r2 != null) {
				r1 = r2.split(this.pointList);
			}
			if (r1 != null) {
				RoomList.add(r1);
			}
			if (!first) {
				this.walling = false;
				this.pointList.clear();
			}
		} else {
			if (!this.pointList.isEmpty()) {
				this.pointList.add(p);
			}
		}
		if (!this.drawing) {
			this.guiPath = new GeneralPath();
			this.guiPath.moveTo(p.x, p.y);
			this.drawing = true;
			//TODO add logic for pushing to closest call point here
			this.pathList.add(this.guiPath);
		} else {
			this.guiPath.lineTo(p.x, p.y);
			//Calculate for midpoint, removing them in case they cross over another line
			/*Point p2 = this.pointList.remove(0);
			Point p1 = this.pointList.remove(1);
			Point midp = new Point();
			midp.x = (int) Math.sqrt( (p1.x + p2.x)/2.0);
			midp.y = (int) Math.sqrt((p2.y + p1.y )/2.0);*/
			//Get room from midpoint by roomlist
			//don't have to worry about r being null, we take care of this earlier
			/*Room r = RoomList.getRoom(midp);
			if( r != null) {
				//split room
			}*/
			//snap end points of line to 2 wall opaque walls that have been crossed
				//make the line an infinite line to see if it crosses 2 opaque walls of the room
			/*double slope = (p2.y - p1.y) / (p2.x - p1.x) * 1.0; */
			/*while(!r.contains(p2)) {

			}*/
				//if so, adjust end points to wall boundary
			//p1 = newEndPoint1;
			//p2 = newEndPoint2;
				//double check that no other transparent walls are being crossed over,
			/*boolean onOtherTrans = false;
			for(int i = 0; this.pathList.size() > 0 ; i++) {
				GeneralPath exisitingP = this.pathList.get(i);
				exisitingP.c
			}*/
				//if so delete this line, and display error message
			//add new path to pathlist
			drawing = false;
		}
		return this.walling;
	}

	public abstract MapLayer copy();

	public abstract void undo();

	public Room getRoom(Point p) {
		return RoomList.getRoom(p);
	}

	public boolean outline(Point p, Room room) {
		this.outlining = true;
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
		// if the guiPath has returned to start, the end outline
		if (!first && p.equals(this.start)) {
			this.outlining = false;
			this.guiPath.closePath();
			room.setPath((GeneralPath) this.guiPath.clone());
		}
		return this.outlining;
	}

    public void setSelectedRoom(Room r) {
        this.selectedRoom = r;
    }

    public void placeDoor(Point p) throws Throwable {
        this.guiPath = new GeneralPath();
        ArrayList<Room> l = RoomList.list;
        for (int j = 0; j < l.size(); j++) { // For each room
            Room r = l.get(j);
            if (r.doorCount() < 8) { // Limit to 8 doors per room
                ArrayList<Point> list = r.list;
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
}