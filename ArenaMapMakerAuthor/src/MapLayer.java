import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Iterator;

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
	protected ArrayList<Point> pointList;
	protected GeneralPath path;
	private boolean walling;

	public MapLayer() {
		pathList = new ArrayList<GeneralPath>();
		pointList = new ArrayList<Point>();
		drawing = false;
	}

	public void addPath(GeneralPath path) {
		pathList.add(path);
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
		// at the first point, start a new path
		if (!drawing) {
			path = new GeneralPath();
			path.moveTo(p.x, p.y);
			start = p;// save start to compare later
			drawing = true;
			pathList.add(path);
		} else {
			// if not the first point, add to path
			path.lineTo(p.x, p.y);
		}
		// if the path has returned to start, the end outline
		if (!first && p.equals(start)) {
			outlining = false;
		}
		return outlining;
	}
	
	/*
	 * transWalling is used to encapsulate the logic behind
	 * implementing a transparent wall.
	 * 
	 * going forward, we could ID whether the layer is a walling
	 * and whether the previous layer is a outline layer
	 * before calling this method 
	 */
	//public boolean transWalling(Point p, MapLayer previousLayer)
	public boolean transWalling(Point p) {
		walling = true;
		
		//below is some logic for snapping transparent wall end point to solid wall
		/*double min = 100000.0;
		int indexOfMin = 0;
		GeneralPath layerPath = previousLayer.pathList.get(0);
		
		for (int i = 0; i < previousLayer.pointList.size(); i = i+2) {
			Point a = previousLayer.pointList.get(i);
			Point b = null;
			if( i+1 < previousLayer.pointList.size() )
				b = previousLayer.pointList.get(i+1);
			//may need to move this before the for loop, and encap the for loop in this if
			if(!layerPath.contains(p)) {
				
				double normalLength = Math.sqrt( (b.x - a.x) * (b.x - a.x) + (b.y - a.y) * (b.y - a.y) );
				double tempMin = Math.abs((p.x - a.x )*(b.y - a.y) - (p.y - a.y)*(b.x-a.x))/normalLength;
				if(tempMin < min) {
					min = tempMin;
					indexOfMin = i;
				}
				//now modify point
				if( i == previousLayer.pathList.size() - 1) {
					GeneralPath closestPath = previousLayer.pathList.get(indexOfMin);
					double hypo = Math.sqrt((a.x - p.x) * (a.x - p.x) + (a.y - p.y) * (a.y - p.y));
					double distanceFromClosestPtOnPath = hypo * hypo - min * min;
					int slopeOfPath = (b.y - a.y) / (b.x - a.x);
					int deltaX = (int) (distanceFromClosestPtOnPath / slopeOfPath);
					int newX = a.x + deltaX;
					int newY = a.y + (deltaX * slopeOfPath);
					p.move(newX, newY);
				}
				
			}
			
		}*/
		
		if (!drawing) {
			path = new GeneralPath();
			path.moveTo(p.x, p.y);
			drawing = true;
			//add logic for pushing to closest call point here
			pathList.add(path);
		} else {
			path.lineTo(p.x, p.y);
			drawing = false;
		}
		return walling;
	}
	
	//below are going to be private methods called for snapping transparent wall end point to solid wall
	/*private void adjustPointToNearestPath(Point p, GeneralPath previousPath) {
		Rectangle bounds = layerPath.getBounds();
	}
	
	private int indexOfMinDistance(Point p, GeneralPath previousPath) {
		
		return 0;
	}*/

	public abstract MapLayer copy();

	public abstract void undo();

	public Room getRoom(Point p) {
		GeneralPath curr = null;
		Iterator<GeneralPath> itr = pathList.iterator();
		boolean found = false;
		while (!found &&itr.hasNext()) {
			curr = itr.next();
			found = curr.contains(p);
		}
		if(found) {
			return RoomList.getRoom(curr);
		}else {
			return null;
		}
	} 
}
