package pdc;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;


/*
 * handles the logic behind drawing, coping and undoing an opaque wall
 */
public class MapOpaqueWallLayer extends MapLayer {

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
		//pathList.forEach(g2d::draw);
		wallList.forEach(wall-> {
		   if(wall.getType().equals(Type.OPAQUE)){
            g2d.draw(wall.getLineRepresentation());
         }
		});
		if (selectedRoom != null) {
			g2d.setColor(Color.RED);
			if (selectedRoom.path != null) {
				g2d.draw(selectedRoom.path);
			}
		}
	}

	@Override
	public MapLayer copy() {
		MapLayer copy = new MapOpaqueWallLayer();
		copy.start = start;
		/*
		if (copy.pathList == null) {
			copy.pathList = new ArrayList<GeneralPath>();
		}
		if (pathList == null) {
			pathList = new ArrayList<GeneralPath>();
		}
		for (int i = 0; i < pathList.size(); i++) {
			copy.pathList.add((GeneralPath) pathList.get(i).clone());
		}
		*/
		if(!pointList.isEmpty()) {
			copy.pointList = (ArrayList<Point>) pointList.clone();
		}
			
		return copy;
	}

	@Override
	public void undo() {
	   //TODO need to fix this now that we've changed how rooms are created
      /*
		if (!pathList.isEmpty()) {
			guiPath = pathList.get(pathList.size() - 1);
			drawing = true;
		}
		*/
	}
}
