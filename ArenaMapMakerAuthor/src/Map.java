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
	private MapLayer mapLayer2;
	private boolean walling;

	public Map() {
		layers = new ArrayList<MapLayer>();
		drawing = false;
		outlining = false;
		mapLayer = new MapOutlineLayer();
		mapLayer2 = new MapWallingLayer();
	}

	public void draw(Graphics g) {
		mapLayer.draw(g);
		mapLayer2.draw(g);
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
		Point p = e.getPoint();
		// round to nearest grid point
		p.setLocation(Math.round(((double) p.x) / GRIDDISTANCE) * GRIDDISTANCE,
				Math.round(((double) p.y) / GRIDDISTANCE) * GRIDDISTANCE);
		if (outlining) {
			if (mapLayer == null) {
				mapLayer = new MapOutlineLayer();
			}
			outlining = mapLayer.outline(p);
			if (!outlining) {
				layers.add(mapLayer);
			}
		}
		if (walling) {
			if (mapLayer2 == null) {
				mapLayer2 = new MapWallingLayer();
			}
			walling = mapLayer2.transWalling(p);
			if(!walling) {
				layers.add(mapLayer2);
			}
		}
		
		// stateEdit is used for undo
		// stateEdit.end();
		// manager.addEdit(stateEdit);
	}

	public void walling() {
		walling = true;
	}
}
