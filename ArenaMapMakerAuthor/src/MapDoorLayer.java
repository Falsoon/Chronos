import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

public class MapDoorLayer extends MapLayer {
	private ArrayList<GeneralPath> doorList;
	private final int GRIDDISTANCE = 15;
	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Stroke door = new BasicStroke(8, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0);
		g2d.setStroke(door);
		if (doorList == null) {
			doorList = new ArrayList<GeneralPath>();
		}
		for (int i = 0; i < doorList.size(); i++) {
			g2d.draw(doorList.get(i));
		}
	}

	@Override
	public MapLayer copy() {
		MapLayer copy = new MapDoorLayer();
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
	
	public void placeDoor(Point p) {
		guiPath = new GeneralPath();
		guiPath.moveTo(p.x, p.y);
		guiPath.lineTo(p.x + GRIDDISTANCE, p.y);
		doorList.add(guiPath);
	}

}
