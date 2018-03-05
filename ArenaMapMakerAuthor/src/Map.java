import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.event.UndoableEditListener;
import javax.swing.undo.StateEdit;
import javax.swing.undo.StateEditable;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEditSupport;

/*
 * Handles the data of map overall (not the same as map layer)
 */

public class Map implements StateEditable {
	private ArrayList<MapLayer> layers;
	private Player player;
	private MapLayer mapLayer;
	private MapLayer mapLayer2;
	private MapDoorLayer mapLayer3;
	private boolean walling, outlining, dooring;
	UndoableEditSupport undoSupport = new UndoableEditSupport(this);
	UndoManager manager = new UndoManager();
	private final Object MAP_KEY = "MAPKEY";
	private Room room;
	private boolean paused;

	public Map() {
		layers = new ArrayList<MapLayer>();
		outlining = false;
		mapLayer = new MapOutlineLayer();
		mapLayer2 = new MapWallingLayer();
		mapLayer3 = new MapDoorLayer();
		addUndoableEditListener(manager);
		player = new Player(mapLayer);
	}

	public void addUndoableEditListener(UndoableEditListener undoableEditListener) {
		undoSupport.addUndoableEditListener(undoableEditListener);
	}

	public void draw(Graphics g) {
		mapLayer.draw(g);
		mapLayer2.draw(g);
		mapLayer3.draw(g);
		player.draw(g);
		/*
		 * for (int i = 0; i < layers.size(); i++) { layers.get(i).draw(g); }
		 */
	}

	public void outlining() {
		room = null;
		outlining = true;
		walling = false;
		dooring = false;
		mapLayer.start = null;
		mapLayer.drawing = false;
	}

	public void mousePressed(Point p) throws Throwable {
		StateEdit stateEdit = new StateEdit(Map.this);
		if (outlining) {
			if (mapLayer == null) {
				mapLayer = new MapOutlineLayer();
			}
			if (room == null) {
				outlining = mapLayer.outline(p);
			} else {
				outlining = mapLayer.outline(p, room);
			}
			if (!outlining) {
				layers.add(mapLayer);
			}
		}
		if (walling) {
			if (getRoom(p) != null) {

				if (mapLayer2 == null) {
					mapLayer2 = new MapWallingLayer();
				}
				walling = mapLayer2.transWalling(p, mapLayer);
				if (!walling) {
					layers.add(mapLayer2);
				}
			}else {
				Throwable e = new Throwable("Can only Draw Transparent Walls in a bounded room");
				throw e;
			}
		}
		if (dooring) {
			mapLayer3.placeDoor(p);
		}
		if (player.isPlacing()) {
			player.place(p);
		}
		stateEdit.end();
		manager.addEdit(stateEdit);
		if (player.isPlaced()) {
			player.rePlace();
		}
	}

	public void walling() {
		walling = true;
		outlining = false;
		mapLayer2.start = null;
		mapLayer2.drawing = false;
		dooring = false;
	}

	public void dooring() {
		dooring = true;
		walling = false;
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
		return walling || outlining || dooring || player.isPlacing();
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
			this.player = newP.player;
		}
	}

	public Player getPlayer() {
		return player;
	}

	public void placePlayerStart() {
		player.startPlacing();
		outlining = false;
		walling = false;
		dooring = false;
	}

	public void startGame() {
		player.startPlaying();
	}

	public Room getRoom(Point p) {
		return mapLayer.getRoom(p);
	}

	public void stopDrawing() {
		outlining = false;
		walling = false;
	}

	public void drawRoom(Room r) {
		room = r;
		outlining = true;
		walling = false;
		dooring = false;
		mapLayer.start = null;
		mapLayer.drawing = false;
	}

	public void setSelectedRoom(Room r) {
		mapLayer.setSelectedRoom(r);
	}

	public void pauseDrawing() {
		outlining = false;
		walling = false;
		paused = true;
	}

	public void resumeDrawing() {
		if(paused) {
			outlining = true;
			paused = false;
		}
	}
}
