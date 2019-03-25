package pdc;

import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEditSupport;
import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;


/*
 * handles the logic behind drawingTransparent, coping and undoing an opaque wall
 */
@SuppressWarnings("serial")
public class MapWallLayer extends MapLayer {

   private boolean playerMode;

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		wallList.forEach(wall-> {
		   Type wallType = wall.getType();
		   //don't draw transparent walls if in player mode
		   if(!playerMode||(playerMode&&wallType.equals(Type.OPAQUE))) {
            setDrawMode(g2d, wallType);
            g2d.draw(wall.getLineRepresentation());
         }
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
      }
      else if(type.equals(Type.ARCHWAY)) {
         g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         Stroke arch = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{3}, 0);
         g2d.setStroke(arch);
      }
      else {
         g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
         g2d.setStroke(dashed);
      }
   }

   @SuppressWarnings("unchecked")
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
			copy.wallList =(ArrayList<Wall>) wallList.clone();
		}
			
		return copy;
	}
    /*
	@Override
	public void undo() {
	   //TODO need to fix this now that we've changed how rooms are created

	} */

   @Override
   public void setPlayerMode(boolean setting) {
      playerMode = setting;
   }

   @Override
   public void storeState(Hashtable<Object, Object> state) {

   }

   @Override
   public void restoreState(Hashtable<?, ?> state) {

   }
}
