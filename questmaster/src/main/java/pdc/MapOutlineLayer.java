package main.java.pdc;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;


/*
 * handles the logic behind drawing, coping and undoing an opaque wall
 */
public class MapOutlineLayer extends MapLayer {

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
		if (pathList == null) {
			pathList = new ArrayList<GeneralPath>();
		}
		for (int i = 0; i < pathList.size(); i++) {
			g2d.draw(pathList.get(i));
		}
		if (selectedRoom != null) {
			g2d.setColor(Color.RED);
			if (selectedRoom.path != null) {
				g2d.draw(selectedRoom.path);
			}
		}
		///TODO remove this when done working on room detection
		if(bb!=null) {
		   g2d.setColor(Color.GREEN);
         g2d.drawRect((int) bb.getMinX(), (int) bb.getMinY(), (int) bb.getWidth(), (int) bb.getHeight());
      }
      g2d.setColor(Color.CYAN);
      if(currentPoints!=null){
		   for(javafx.geometry.Point2D currentPoint : currentPoints) {
            g2d.drawString("o", (int) currentPoint.getX(), (int) currentPoint.getY());
         }
      }
	}

	@Override
	public MapLayer copy() {
		MapLayer copy = new MapOutlineLayer();
		copy.start = start;
		if (copy.pathList == null) {
			copy.pathList = new ArrayList<GeneralPath>();
		}
		if (pathList == null) {
			pathList = new ArrayList<GeneralPath>();
		}
		for (int i = 0; i < pathList.size(); i++) {
			copy.pathList.add((GeneralPath) pathList.get(i).clone());
		}
		if(!pointList.isEmpty()) {
			copy.pointList = (ArrayList<Point>) pointList.clone();
		}

		return copy;
	}

	@Override
	public void undo() {
		if (!pathList.isEmpty()) {
			guiPath = pathList.get(pathList.size() - 1);
			drawing = true;
		}
	}
}
