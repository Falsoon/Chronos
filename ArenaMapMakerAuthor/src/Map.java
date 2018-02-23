import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.event.UndoableEditListener;
import javax.swing.undo.StateEdit;
import javax.swing.undo.StateEditable;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEditSupport;

public class Map implements StateEditable {
	private static final int GRIDDISTANCE = Constants.GRIDDISTANCE;
	private ArrayList<MapLayer> layers;
	private MapLayer mapLayer;
	private MapLayer mapLayer2;
	private boolean walling, outlining;
	UndoableEditSupport undoSupport = new UndoableEditSupport(this);
	UndoManager manager = new UndoManager();
	private final Object MAP_KEY = "MAPKEY";
	private Point playerPos;
	private boolean placingPlayer;
	private boolean isPlaying;
	boolean playerPlaced;
	private GeneralPath playerRoom;

	public Map() {
		layers = new ArrayList<MapLayer>();
		outlining = false;
		mapLayer = new MapOutlineLayer();
		mapLayer2 = new MapWallingLayer();
		addUndoableEditListener(manager);
	}

	public void addUndoableEditListener(UndoableEditListener undoableEditListener) {
		undoSupport.addUndoableEditListener(undoableEditListener);
	}

	public void draw(Graphics g) {
		mapLayer.draw(g);
		mapLayer2.draw(g);
		/*
		 * for (int i = 0; i < layers.size(); i++) { layers.get(i).draw(g); }
		 */
		if (playerPlaced) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(Color.BLACK);
			g2d.drawString("¶", playerPos.x, playerPos.y);
			placingPlayer = false;
		}
	}

	public void outlining() {
		outlining = true;
		walling = false;
		mapLayer.start = null;
		mapLayer.drawing = false;
	}

	public void mousePressed(Point p) {
		StateEdit stateEdit = new StateEdit(Map.this);
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
			if (!walling) {
				layers.add(mapLayer2);
			}
		}
		if (placingPlayer) {
			playerPos = p;
			playerPos.setLocation(Math.round(playerPos.x / GRIDDISTANCE) * GRIDDISTANCE,
					Math.round(playerPos.y / GRIDDISTANCE) * GRIDDISTANCE);
			playerPlaced = true;
			Iterator<GeneralPath> itr = mapLayer.pathList.iterator();
			while (itr.hasNext()) {
				GeneralPath curr = itr.next();
				if (curr.contains(playerPos)) {
					playerRoom = curr;
				}
			}
		}
		stateEdit.end();
		manager.addEdit(stateEdit);
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

	public boolean undo() {
		boolean undid = false;
		if (manager.canUndo()) {
			manager.undo();
			mapLayer.undo();
			undid = true;
		}
		return undid;
	}

	public boolean isCreating() {
		return walling || outlining || placingPlayer;
	}

	@Override
	public void storeState(Hashtable<Object, Object> state) {
		state.put(MAP_KEY, getMap());
	}

	private Map getMap() {
		return this.copy();
	}

	@Override
	public void restoreState(Hashtable<?, ?> state) {
		Map newP = (Map) state.get(MAP_KEY);
		if (newP != null) {
			this.layers = newP.layers;
			this.mapLayer = newP.mapLayer;
			this.mapLayer2 = newP.mapLayer2;
			this.outlining = newP.outlining;
			this.walling = newP.walling;
			// whatever else matters
		}
	}

	public void goUp() {
		if (isPlaying) {
			boolean inside = false;
			playerPos.move(playerPos.x, playerPos.y - GRIDDISTANCE);
			Iterator<GeneralPath> itr = mapLayer.pathList.iterator();
			while (itr.hasNext() && !inside) {
				GeneralPath curr = itr.next();
				if (curr.equals(playerRoom) && curr.contains(playerPos.x + GRIDDISTANCE/2, playerPos.y - GRIDDISTANCE/2)) {
					inside = true;
				} 
			}
			if (!inside) {
				playerPos.move(playerPos.x, playerPos.y + GRIDDISTANCE);
			}
		}
	}

	public void goDown() {
		if (isPlaying) {
			boolean inside = false;
			playerPos.move(playerPos.x, playerPos.y + GRIDDISTANCE);
			Iterator<GeneralPath> itr = mapLayer.pathList.iterator();
			while (itr.hasNext() && !inside) {
				GeneralPath curr = itr.next();
				if (curr.equals(playerRoom) && curr.contains(playerPos.x + GRIDDISTANCE/2, playerPos.y - GRIDDISTANCE/2)) {
					inside = true;
				} 
			}
			if (!inside) {
				playerPos.move(playerPos.x, playerPos.y - GRIDDISTANCE);
			}
		}
	}

	public void goLeft() {
		if (isPlaying) {
			boolean inside = false;
			playerPos.move(playerPos.x - GRIDDISTANCE, playerPos.y);
			Iterator<GeneralPath> itr = mapLayer.pathList.iterator();
			while (itr.hasNext() && !inside) {
				GeneralPath curr = itr.next();
				if (curr.equals(playerRoom) && curr.contains(playerPos.x + GRIDDISTANCE/2, playerPos.y - GRIDDISTANCE/2)) {
					inside = true;
				} 
			}
			if (!inside) {
				playerPos.move(playerPos.x + GRIDDISTANCE, playerPos.y);
			}
		}
	}

	public void goRight() {
		if (isPlaying) {
			boolean inside = false;
			playerPos.move(playerPos.x + GRIDDISTANCE, playerPos.y);
			Iterator<GeneralPath> itr = mapLayer.pathList.iterator();
			while (itr.hasNext() && !inside) {
				GeneralPath curr = itr.next();
				if (curr.equals(playerRoom) && curr.contains(playerPos.x + GRIDDISTANCE/2, playerPos.y - GRIDDISTANCE/2)) {
					inside = true;
				} 
			}
			if (!inside) {
				playerPos.move(playerPos.x - GRIDDISTANCE, playerPos.y);
			}
		}
	}

	public void placePlayerStart() {
		placingPlayer = true;
	}

	public void startGame() {
		isPlaying = !isPlaying;
	}

	public Room getRoom(Point p) {
		return mapLayer.getRoom(p);
	}
}
