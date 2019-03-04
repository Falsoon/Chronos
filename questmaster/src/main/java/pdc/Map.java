package pdc;

import javax.swing.event.UndoableEditListener;
import javax.swing.undo.StateEdit;
import javax.swing.undo.StateEditable;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEditSupport;
import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Handles the data of map overall including data of 3 mapLayers
 */

public class Map implements StateEditable {
	public ArrayList<MapLayer> layers;
	public Player player;
	public MapLayer mapLayer;
	public MapLayer mapLayer3;
	public boolean transparentWallMode, opaqueWallMode, dooring, archwayAdd;
	UndoableEditSupport undoSupport = new UndoableEditSupport(this);
	UndoManager manager = new UndoManager();
	private final Object MAP_KEY = "MAPKEY";
	private Room room;

	public Map() {
		layers = new ArrayList<MapLayer>();
		opaqueWallMode = false;
		dooring = false;
		mapLayer = new MapWallLayer();
		mapLayer3 = new MapDoorLayer();
		addUndoableEditListener(manager);
		player = new Player(mapLayer);
	}

	public void addUndoableEditListener(UndoableEditListener undoableEditListener) {
		undoSupport.addUndoableEditListener(undoableEditListener);
	}

	public void draw(Graphics g) {
		mapLayer.draw(g);
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
		mapLayer.drawingTransparent = false;
	}

	public void mousePressed(Point p) {
		StateEdit stateEdit = new StateEdit(Map.this);
		if (opaqueWallMode) {
			if (mapLayer == null) {
				mapLayer = new MapWallLayer();
			}

			mapLayer.drawOpaqueWalls(p);
			if (!opaqueWallMode) {
				layers.add(mapLayer);
			}
		}
		if (transparentWallMode) {
			/*if (getRoom(p) != null) {
			* */
				transparentWallMode = mapLayer.drawTransparentWalls(p);
				/*
				if (!transparentWallMode) {
					layers.add(mapLayer2);
				}
				*/
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
      if (archwayAdd) {
         try {
            mapLayer.placeArchway(p);
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
		mapLayer.start = null;
		mapLayer.drawingTransparent = false;
		dooring = false;
		archwayAdd = false;
	}

	public void dooring() {
		dooring = true;
		transparentWallMode = false;
		opaqueWallMode = false;
		archwayAdd = false;
	}
   public void archwayAdd() {
      dooring = false;
      archwayAdd = true;
      opaqueWallMode = false;
      transparentWallMode = false;
   }
	public int numOfDoors() {
		return DoorList.list.size();
	}

	public Map copy() {
		Map copy = new Map();
		copy.mapLayer = mapLayer.copy();
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
			RoomList.getInstance().undo();
			DoorList.undo();
			undid = true;
		}
		return undid;
	}

	public boolean isCreating() {
		return transparentWallMode || opaqueWallMode || archwayAdd || dooring || player.isPlacing();
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
		archwayAdd = false;
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
		archwayAdd = false;
	}

	public void drawRoom(String str) {
		room = RoomList.getInstance().getRoomByStr(str);
		opaqueWallMode = true;
		transparentWallMode = false;
		dooring = false;
		archwayAdd = false;
		mapLayer.start = null;
		mapLayer.drawingTransparent = false;
	}

	public void setSelectedRoom(String str) {
		mapLayer.setSelectedRoom(RoomList.getInstance().getRoomByStr(str));
	}

   /**
    * Method to set whether the MapLayer is for the player mode
    * @param setting the value to give to player mode
    */
   public void setPlayerMode(boolean setting) {
      mapLayer.setPlayerMode(setting);
   }
}
