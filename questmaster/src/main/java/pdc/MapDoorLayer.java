package main.java.pdc;
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
	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Stroke door = new BasicStroke(8, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0);
		g2d.setStroke(door);
		for (int i = 0; i < DoorList.list.size(); i++) {
			Door d = DoorList.list.get(i);
			if (!d.open)
				g2d.draw(d.path);
			else {
				g2d.rotate(Math.PI/2);
				g2d.draw(d.path);
			}
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
	}



}
