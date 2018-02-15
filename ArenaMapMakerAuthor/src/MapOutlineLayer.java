import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

public class MapOutlineLayer extends MapLayer {

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
		if (pathList == null) {
			pathList = new ArrayList<GeneralPath>();
		}
		for (int i = 0; i < pathList.size(); i++) {
			g2d.draw(pathList.get(i));
		}
	}

	@Override
	public MapLayer copy() {
		MapLayer copy = new MapOutlineLayer();
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

}
