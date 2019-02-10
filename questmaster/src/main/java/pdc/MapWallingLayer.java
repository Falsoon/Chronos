package main.java.pdc;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

/*
 * Handles the logic behind drawing, coping and undoing a transparent wall
 */
public class MapWallingLayer extends MapLayer {

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, 0);
		g2d.setStroke(dashed);
		if (pathList == null) {
			pathList = new ArrayList<GeneralPath>();
		}
		for (int i = 0; i < pathList.size(); i++) {
			g2d.draw(pathList.get(i));
		}
	}

	@Override
	public MapLayer copy() {
		MapLayer copy = new MapWallingLayer();
		if(copy.pathList==null) {
			copy.pathList = new ArrayList<GeneralPath>();
		}
		if(pathList==null) {
			pathList = new ArrayList<GeneralPath>();
		}
		for(int i=0; i< pathList.size();i++) {
			copy.pathList.add((GeneralPath)pathList.get(i).clone());
		}

		return copy;
	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub

	}

}
