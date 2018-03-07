package pdc;
import java.awt.Graphics;
import java.awt.Point;
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
	protected ArrayList<GeneralPath> doorList;
	protected GeneralPath guiPath;
	private boolean walling;
	protected Room selectedRoom;
	
	public MapLayer() {
		pathList = new ArrayList<GeneralPath>();
		pointList = new ArrayList<Point>();
		drawing = false;
		selectedRoom = null;
	}

	public void addPath(GeneralPath guiPath) {
		pathList.add(guiPath);
	}

	public GeneralPath removeLastPath() {
		if (!pathList.isEmpty()) {
			return pathList.remove(pathList.size() - 1);
		} else {
			return null;
		}
	}

	public abstract void draw(Graphics g);

	public boolean outline(Point p) {
		outlining = true;

		pointList.add(p);
		boolean first = !drawing;
		// at the first point, start a newguiPath
		if (!drawing) {
			start = p;// save start to compare later
			guiPath = getPath(p);// get path with point. sets start
			if(guiPath.getCurrentPoint()==null) {
				guiPath.moveTo(p.x, p.y);
			}
			drawing = true;
		} else {
			// if not the first point, add toguiPath
			guiPath.lineTo(p.x, p.y);
		}
		// if the guiPath has returned to start, the end outline
		if (!first && p.equals(start)) {
			outlining = false;
			guiPath.closePath();
			RoomList.add(new Room((GeneralPath) guiPath.clone()));
		}
		return outlining;
	}

	private GeneralPath getPath(Point p) {
		int index = 0;
		boolean found = false;
		GeneralPath path;
		for (int i = 0; i < pathList.size() && !found; i++) {
			found = onPath(pathList.get(i), p);
			if (found) {
				index = i;
			}
		}
		// if point on a path
		if (found) {
			path = pathList.get(index);
			//setStart(path);
		} else {
			path = new GeneralPath();
			pathList.add(path);
		}
		return path;
	}

	/**
	 * 
	 * @param path - a path to search for the point
	 * @param p - a point to find on the path
	 * @return found - if the point is a part of the path
	 */
	private boolean onPath(GeneralPath path, Point p) {
		boolean found = false;
		Point point = new Point();
		double[] coords = new double[6];
		PathIterator pi = path.getPathIterator(null);
		if(!pi.isDone()) {
			pi.currentSegment(coords);
			point.setLocation((int) coords[0], (int) coords[1]);
			found = p.equals(point);
		}
		if(found) {
			start.setLocation(path.getCurrentPoint());
		}
		if(!found) {
			found = p.equals(path.getCurrentPoint());
			if(found) {
				start.setLocation(point);
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
		walling = true;
		Room r1 = null, r2 = RoomList.getRoom(p);
		if (r2.onBoundary(p)) {
			boolean first = pointList.isEmpty();
			pointList.add(p);
			if (r2 != null)
				r1 = r2.split(pointList);
			if (r1 != null) {
				RoomList.add(r1);
			}
			if (!first) {
				walling = false;
				pointList.clear();
			}
		} else {
			if (!pointList.isEmpty()) {
				pointList.add(p);
			}
		}

		if (!drawing) {
			guiPath = new GeneralPath();
			guiPath.moveTo(p.x, p.y);
			drawing = true;
			// add logic for pushing to closest call point here
			pathList.add(guiPath);
		} else {
			guiPath.lineTo(p.x, p.y);
			// drawing = false;
		}
		return walling;
	}

	// below are going to be private methods called for snapping transparent wall
	// end point to solid wall
	/*
	 * private void adjustPointToNearestPath(Point p, GeneralPath previousPath) {
	 * Rectangle bounds = layerPath.getBounds(); }
	 * 
	 * private int indexOfMinDistance(Point p, GeneralPath previousPath) {
	 * 
	 * return 0; }
	 */

	public abstract MapLayer copy();

	public abstract void undo();

	public Room getRoom(Point p) {
		return RoomList.getRoom(p);
	}

	public boolean outline(Point p, Room room) {
		outlining = true;
		pointList.add(p);
		boolean first = !drawing;
		// at the first point, start a newguiPath
		if (!drawing) {
			guiPath = new GeneralPath();
			guiPath.moveTo(p.x, p.y);
			start = p;// save start to compare later
			drawing = true;
			pathList.add(guiPath);
		} else {
			// if not the first point, add toguiPath
			guiPath.lineTo(p.x, p.y);
		}
		// if the guiPath has returned to start, the end outline
		if (!first && p.equals(start)) {
			outlining = false;
			guiPath.closePath();
			room.setPath((GeneralPath) guiPath.clone());
		}
		return outlining;
	}

	public void setSelectedRoom(Room r) {
		selectedRoom = r;
	}
	
		public void placeDoor(Point p) {
		guiPath = new GeneralPath();
		/*guiPath.moveTo(p.x, p.y);
		guiPath.lineTo(p.x + Constants.GRIDDISTANCE, p.y);
		doorList.add(guiPath);*/
		//Room r = this.getRoom(p);
		ArrayList<Room> l = RoomList.list;
		//if (r != null) {
		for (int j = 0; j < l.size(); j++) {
			Room r = l.get(j);
			ArrayList<Point> list = r.list;
			for (int i = 0; i < list.size(); i++) {
				Point a = list.get(i);
				Point b;
				if (i == list.size() - 1)
					b = list.get(0);
				else 
					b = list.get(i+1);
				GeneralPath rect= new GeneralPath();
				double m;
				if (b.x != a.x)
					m = (b.y-a.y) / (b.x - a.x);
				rect.moveTo(a.x+7, a.y-7);
				rect.lineTo(a.x-7, a.y-7);
				rect.lineTo(b.x-7, b.y+7);
				rect.lineTo(b.x+7, b.y+7);
				rect.lineTo(a.x+7, a.y-7);
				rect.closePath();
				doorList.add(rect);
				if (rect.contains(p)) {
					System.out.println("here");
					if (b.x != a.x) {
						m = (b.y-a.y) / (b.x - a.x);
						guiPath.moveTo(p.x, p.y);
						guiPath.lineTo(p.x+Constants.GRIDDISTANCE*m, p.y+Constants.GRIDDISTANCE*(1/m));
						doorList.add(guiPath);	
					} else {
						guiPath.moveTo(p.x, p.y);
						guiPath.lineTo(p.x, p.y+Constants.GRIDDISTANCE);
					}
				}
			}
		}

	}
}
