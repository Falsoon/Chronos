import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

/**
 * Layer of the map 
 * @author Daniel
 *
 */
public class MapLayer {
	private static boolean drawing;
	private static Point start;
	private static boolean outlining;
	private ArrayList<GeneralPath> pathList;
	private GeneralPath path; 
	
	public void MapLayer() {
		pathList = new ArrayList<GeneralPath>();
		drawing = false;
	}

	public void addPath(GeneralPath path) {
		pathList.add(path);
	}
	
	public GeneralPath removeLastPath() {
		if(!pathList.isEmpty()) {
			return pathList.remove(pathList.size()-1);
		}else {
			return null;
		}
	}

	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
		if(pathList==null) {
			pathList = new ArrayList<GeneralPath>();
		}
		for(int i =0; i< pathList.size();i++) {
			g2d.draw(pathList.get(i));
		}
	}

	public boolean outline(Point p) {
		outlining = true;
		boolean first = !drawing;
		//at the first point, start a new path
		if (!drawing) {
			path = new GeneralPath();
			path.moveTo(p.x, p.y);
			start = p;//save start to compare later
			drawing = true;
			pathList.add(path);
		} else {
			//if not the first point, add to path
			path.lineTo(p.x, p.y);
		}
		//if the path has returned to start, the end outline
		if (!first && p.equals(start)) {
			outlining = false;
		}
		return outlining;		
	}

/*	public void begin(Point p) {
		// TODO Auto-generated method stub
		GeneralPath path = new GeneralPath();
		path.moveTo(p.x, p.y);
		room = new Room(p);//create room
	}*/
}
