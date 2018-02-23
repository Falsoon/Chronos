import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEditSupport;

public class Map{
	private static final double GRIDDISTANCE = 15;
	private ArrayList<MapLayer> layers;
	boolean outlining;
	private MapLayer mapLayer;
	private MapLayer mapLayer2;
	private boolean walling;
	UndoableEditSupport undoSupport = new UndoableEditSupport(this);
	UndoManager manager = new UndoManager();

	public Map() {
		layers = new ArrayList<MapLayer>();
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
		walling = false;
		mapLayer.start = null;
		mapLayer.drawing = false;
	}

	public boolean mousePressed(MouseEvent e) {
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
			walling = mapLayer2.transWalling(p, mapLayer);
			if(!walling) {
				layers.add(mapLayer2);
			}
		}
		return outlining||walling;
	}

	public void walling() {
		walling = true;
		outlining = false;
	}

	public Map copy() {
		Map copy = new Map();
		copy.mapLayer = mapLayer.copy();
		copy.mapLayer2 = mapLayer2.copy();
		copy.outlining = outlining;
		copy.walling = walling;
		return copy;
	}

	public void undo() {
		mapLayer.undo();
	}
}
