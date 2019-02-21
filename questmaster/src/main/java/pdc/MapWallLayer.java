package pdc;

import java.awt.*;
import java.util.ArrayList;


/*
 * handles the logic behind drawingTransparent, coping and undoing an opaque wall
 */
public class MapWallLayer extends MapLayer {

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		wallList.forEach(wall-> {
         setDrawMode(g2d,wall.getType());
         g2d.draw(wall.getLineRepresentation());
		});
		if (selectedRoom != null) {
			g2d.setColor(Color.RED);
			setDrawMode(g2d,Type.OPAQUE);
			selectedRoom.walls.forEach(wall -> g2d.draw(wall.getLineRepresentation()));
		}

	}

   private void setDrawMode(Graphics2D g2d,Type type) {
	   if(type.equals(Type.OPAQUE)){
         g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
      }else {
         g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
         g2d.setStroke(dashed);
      }
   }

   @Override
	public MapLayer copy() {
		MapLayer copy = new MapWallLayer();
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
			drawingTransparent = true;
		}
		*/
	}
}
