package pdc;

import javax.swing.event.UndoableEditListener;
import javax.swing.undo.StateEditable;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Handles the data of map overall including data of 3 mapLayers
 */

@SuppressWarnings("serial")
public class Map implements StateEditable, Serializable {
	public ArrayList<Room> rooms;
	public ArrayList<MapLayer> layers;
	public Player player;
	public Key key;
	public ArrayList<Key> keyList;
	public MapLayer mapLayer;
	public MapLayer mapLayer3;
	private boolean transparentWallMode, opaqueWallMode, dooring, archwayAdd, deleting, keyPlace, lockedDooring,  stairing;
	//UndoableEditSupport undoSupport = new UndoableEditSupport(this);
	//UndoManager manager = new UndoManager();
	private final Object MAP_KEY = "MAPKEY";

	public Map() {
		layers = new ArrayList<>();
		keyList = new ArrayList<>();
		mapLayer = new MapWallLayer();
		mapLayer3 = new MapDoorLayer();
		//addUndoableEditListener(manager);
		player = new Player(mapLayer);
		rooms = new ArrayList<>();
	}

	public void addUndoableEditListener(UndoableEditListener undoableEditListener) {
		//undoSupport.addUndoableEditListener(undoableEditListener);
	}

	public void draw(Graphics g) {
		mapLayer.draw(g);
		mapLayer3.draw(g);
		player.draw(g);
		for (Key K: mapLayer.keyList){
		    System.out.println(K.getPosition());
		    K.draw(g);
        }
		/*
		 * for (int i = 0; i < layers.size(); i++) { layers.get(i).draw(g); }
		 */
	}

	public void opaqueWalling() {
	   opaqueWallMode = true;
	   transparentWallMode = false;
	   dooring = false;
	   lockedDooring =false;
	   mapLayer.start = null;
	   mapLayer.drawingTransparent = false;
	   archwayAdd = false;
		deleting = false;
		stairing = false;
	}

	public void mousePressed(Point p) {
		//StateEdit stateEdit = new StateEdit(Map.this);
		if (opaqueWallMode) {
			mapLayer.drawOpaqueWalls(p);
		}
		if (transparentWallMode) {
		   transparentWallMode = mapLayer.drawTransparentWalls(p);
		}
//		if (dooring) {
//			try {
//				mapLayer3.placeDoor(p);
//			} catch (Throwable e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
      if (archwayAdd) {
         try {
            mapLayer.placeArchway(p);
         } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
      if (dooring) {
         try {
            mapLayer.placeDoor(p);
         } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
        if (lockedDooring) {
            try {
                mapLayer.placeLockedDoor(p);
            } catch (Throwable e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
		if (player.isPlacing()) {
			player.place(p);
			mapLayer.setPlayerPosition(p);
		}
		if (keyPlace) {
			mapLayer.placeKey(p);
		}
		//stateEdit.end();
		//manager.addEdit(stateEdit);
		if(deleting){
		   mapLayer.delete(p);
		}
		if (player.isPlaced()) {
			player.rePlace();
		}
		if (stairing) {
			mapLayer.placeStairs(p);
		}
	}

	public void transparentWalling() {
		transparentWallMode = true;
		opaqueWallMode = false;
		mapLayer.start = null;
		dooring = false;
        lockedDooring =false;
        keyPlace = false;
		archwayAdd = false;
      mapLayer.drawingTransparent = false;
		deleting = false;
		stairing = false;
	}

	public void dooring() {
		dooring = true;
		transparentWallMode = false;
        lockedDooring =false;
        keyPlace = false;
		opaqueWallMode = false;
		archwayAdd = false;
		deleting = false;
		stairing = false;
	}

    public void lockedDooring() {
	    lockedDooring = true;
        dooring = false;
        transparentWallMode = false;
        keyPlace = false;
        opaqueWallMode = false;
        archwayAdd = false;
        deleting = false;
       stairing = false;
    }
    public void keyAdd() {
        keyPlace = true;
	    dooring = false;
        lockedDooring =false;
        transparentWallMode = false;
        opaqueWallMode = false;
        archwayAdd = false;
        deleting = false;
       stairing = false;
    }
   public void archwayAdd() {
      dooring = false;
      lockedDooring =false;
      keyPlace = false;
      archwayAdd = true;
      opaqueWallMode = false;
      transparentWallMode = false;
		deleting = false;
		stairing = false;
	}
	public void stairing() {
      dooring = false;
      archwayAdd = false;
      opaqueWallMode = false;
		transparentWallMode = false;
		stairing = true;
		deleting = false;
      lockedDooring =false;
      keyPlace = false;
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

	/*public boolean undo() {
		boolean undid = false;
		if (manager.canUndo()) {
			manager.undo();
			mapLayer.undo();
			RoomList.getInstance().undo();
			DoorList.undo();
			undid = true;
		}
		return undid;
	} */

	public boolean isCreating() {
		return transparentWallMode || opaqueWallMode || archwayAdd || keyPlace|| dooring || lockedDooring || stairing || player.isPlacing();
	}

	public boolean isDeleting(){
      return deleting;
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
        lockedDooring =false;
        keyPlace = false;
        stairing = false;
		dooring = false;
		archwayAdd = false;
		deleting = false;
	}

	public void stopPlacingPlayer() {
		player.stopPlacing();
		opaqueWallMode = false;
		transparentWallMode = false;
		dooring = false;
		archwayAdd = false;
        lockedDooring =false;
        keyPlace = false;
		deleting = false;
		stairing = false;
	}

	public void startGame() {
		player.startPlaying();
	}

	public Room getRoom(Point p) {
		return mapLayer.getRoom(p);
	}

	public void stopDrawing() {
		//opaqueWallMode = false;
		//transparentWallMode = false;
		dooring = false;
		archwayAdd = false;
        lockedDooring =false;
        keyPlace = false;
		deleting = false;
		stairing = false;
		mapLayer.stopDrawing();
	}

	public void drawRoom(String str) {
		opaqueWallMode = true;
		transparentWallMode = false;
        lockedDooring =false;
        keyPlace = false;
		dooring = false;
		archwayAdd = false;
		mapLayer.start = null;
      mapLayer.drawingTransparent = false;
		deleting = false;
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

   public void delete() {
      deleting = true;
      opaqueWallMode = false;
      transparentWallMode = false;
       lockedDooring =false;
       keyPlace = false;
      dooring = false;
      archwayAdd = false;
      mapLayer.start = null;
      mapLayer.drawingTransparent = false;
   }
}
