import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.undo.StateEdit;
import javax.swing.undo.StateEditable;

public class Map {
	private static final double GRIDDISTANCE = 15;
	private ArrayList<MapLayer> layers;
	private boolean outlining;
	private boolean drawing;
	private MapLayer mapLayer;

	public Map() {
		layers = new ArrayList<MapLayer>();
		drawing = false;
		outlining = false;
		mapLayer = new MapLayer();
	}

	public void draw(Graphics g) {
		mapLayer.draw(g);
		/*for (int i = 0; i < layers.size(); i++) {
			layers.get(i).draw(g);
		}*/
	}

	public void outlining() {
		outlining = true;
	}

	public void mousePressed(MouseEvent e) {
		// StateEdit stateEdit = new StateEdit(this);
		// if a point needs to be drawn
		if (outlining) {
			Point p = e.getPoint();
			// round to nearest grid point
			p.setLocation(Math.round(((double) p.x) / GRIDDISTANCE) * GRIDDISTANCE,
					Math.round(((double) p.y) / GRIDDISTANCE) * GRIDDISTANCE);
			if (mapLayer == null) {
				mapLayer = new MapLayer();
			}
			
			outlining = mapLayer.outline(p);
			if (!outlining) {
				layers.add(mapLayer);
			}

		}
		// stateEdit is used for undo
		// stateEdit.end();
		// manager.addEdit(stateEdit);
	}
}
