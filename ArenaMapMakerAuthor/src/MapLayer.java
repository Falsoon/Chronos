import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

/**
 * Layer of the map
 * 
 * @author Daniel
 *
 */
public abstract class MapLayer {
	private boolean drawing;
	private Point start;
	private boolean outlining;
	protected ArrayList<GeneralPath> pathList;
	private GeneralPath path;
	private boolean walling;

	public void MapLayer() {
		pathList = new ArrayList<GeneralPath>();
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

	public boolean transWalling(Point p) {
		walling = true;
		if (!drawing) {
			path = new GeneralPath();
			path.moveTo(p.x, p.y);
			drawing = true;
			pathList.add(path);
		} else {
			path.lineTo(p.x, p.y);
			drawing = false;
		}
		return walling;
	}

	public abstract MapLayer copy();

}
