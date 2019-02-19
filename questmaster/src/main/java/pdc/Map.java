package pdc;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.event.UndoableEditListener;
import javax.swing.undo.StateEdit;
import javax.swing.undo.StateEditable;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEditSupport;

/**
 * Handles the data of map overall including data of 3 mapLayers
 */

public class Map implements StateEditable {
	public ArrayList<MapLayer> layers;
	public Player player;
	public MapLayer mapLayer;
	public MapLayer mapLayer2;
	public MapLayer mapLayer3;
	public boolean transparentWallMode, opaqueWallMode, dooring;
	UndoableEditSupport undoSupport = new UndoableEditSupport(this);
	UndoManager manager = new UndoManager();
	private final Object MAP_KEY = "MAPKEY";
	private Room room;

	public Map() {
		layers = new ArrayList<MapLayer>();
		opaqueWallMode = false;
		dooring = false;
		mapLayer = new MapOpaqueWallLayer();
		mapLayer2 = new MapTransparentWallLayer();
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
		opaqueWallMode = true;
		transparentWallMode = false;
		dooring = false;
		mapLayer.start = null;
		mapLayer.drawing = false;
	}

	public void mousePressed(Point p) {
		StateEdit stateEdit = new StateEdit(Map.this);
		if (opaqueWallMode) {
			if (mapLayer == null) {
				mapLayer = new MapOpaqueWallLayer();
			}
			if (room == null) {
				mapLayer.outline(p);
			} else {
				//opaqueWallMode = mapLayer.outline(p, room);
			}
			if (!opaqueWallMode) {
				layers.add(mapLayer);
			}
		}
		if (transparentWallMode) {
			/*if (getRoom(p) != null) {
*/
				if (mapLayer2 == null) {
					mapLayer2 = new MapTransparentWallLayer();
				}
				transparentWallMode = mapLayer2.transWalling(p);
				if (!transparentWallMode) {
					layers.add(mapLayer2);
				}
			/*}else {
				mapLayer2.pointList.clear();
				Throwable e = new Throwable("Transparent walls must be drawn in bounded rooms");
				throw e;
			}*/
		}
		if (dooring) {
			try {
				mapLayer3.placeDoor(p);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		transparentWallMode = true;
		opaqueWallMode = false;
		mapLayer2.start = null;
		mapLayer2.drawing = false;
		dooring = false;
	}

	public void dooring() {
		dooring = true;
		transparentWallMode = false;
		opaqueWallMode = false;
	}
	public int numOfDoors() {
		return DoorList.list.size();
	}

	public Map copy() {
		Map copy = new Map();
		copy.mapLayer = mapLayer.copy();
		copy.mapLayer2 = mapLayer2.copy();
		copy.mapLayer3 = mapLayer3.copy();
		copy.opaqueWallMode = opaqueWallMode;
		copy.transparentWallMode = transparentWallMode;
		return copy;
	}

	public boolean undo() {
		boolean undid = false;
		if (manager.canUndo()) {
			manager.undo();
			mapLayer.undo();
			RoomList.undo();
			DoorList.undo();
			undid = true;
		}
		return undid;
	}

	public boolean isCreating() {
		return transparentWallMode || opaqueWallMode || dooring || player.isPlacing();
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
			this.mapLayer3 = newP.mapLayer3;
			this.opaqueWallMode = newP.opaqueWallMode;
			this.transparentWallMode = newP.transparentWallMode;
			this.player = newP.player;
		}
	}

	public Player getPlayer() {
		return player;
	}

	public void placePlayerStart() {
		player.startPlacing();
		opaqueWallMode = false;
		transparentWallMode = false;
		dooring = false;
	}

	public void startGame() {
		player.startPlaying();
	}

	public Room getRoom(Point p) {
		return mapLayer.getRoom(p);
	}

	public void stopDrawing() {
		opaqueWallMode = false;
		transparentWallMode = false;
		dooring = false;
	}

	public void drawRoom(String str) {
		room = RoomList.getRoomByStr(str);
		opaqueWallMode = true;
		transparentWallMode = false;
		dooring = false;
		mapLayer.start = null;
		mapLayer.drawing = false;
	}

	public void setSelectedRoom(String str) {
		mapLayer.setSelectedRoom(RoomList.getRoomByStr(str));
	}
}
