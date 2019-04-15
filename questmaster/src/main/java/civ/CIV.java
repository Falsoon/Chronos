package civ;

import hic.AuthorWindow;
import hic.FormWindow;
import pdc.*;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.swing.undo.StateEdit;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEditSupport;

/**
 * This class is used as the civ/presenter class for map
 */
public class CIV {
	public Map map;
	public FormCiv formCiv;
	private UndoableEditSupport undoSupport;
	private UndoManager manager;
	private AuthorWindow authorWindow;

	public CIV() {
		map = new Map();
		formCiv = new FormCiv();
		undoSupport = new UndoableEditSupport(this);
		manager = new UndoManager();
		undoSupport.addUndoableEditListener(manager);
		authorWindow = null;
	}

	public CIV(AuthorWindow aw) {
		map = new Map();
		formCiv = new FormCiv();
		undoSupport = new UndoableEditSupport(this);
		manager = new UndoManager();
		undoSupport.addUndoableEditListener(manager);
		authorWindow = aw;
	}

   /**
    * Handles the mouse click event in a Swing component
    * @param point the point where the mouse was clicked
    * @param isAltDown if the alt button is down.  If so, the author can draw off the grid
    * @param isLeftButton if the left mouse button was pressed.  If so, draw under the current drawing mode
    * @param isRightButton if the right mouse button was pressed.  If so, stop drawing
    */
	public void mousePressed(Point point,boolean isAltDown, boolean isLeftButton, boolean isRightButton){
	   //keep the original point in case the author is deleting
	   Point originalPoint = new Point(point);
		if (!isAltDown) {
			point.setLocation(Math.round(((double) point.x) / Constants.GRIDDISTANCE) * Constants.GRIDDISTANCE,
					Math.round(((double) point.y) / Constants.GRIDDISTANCE) * Constants.GRIDDISTANCE);
		}
		if (isLeftButton) {
			if (map.isCreating()) {
				StateEdit stateEdit = new StateEdit(map);
				map.mousePressed(point);
				stateEdit.end();
				manager.addEdit(stateEdit);
			} else if(map.isDeleting()){
			   map.mousePressed(originalPoint);
         } else {
				if (!map.getPlayer().isPlaying()) {
					Room room = RoomList.getInstance().getRoom(point);
					if (room != null) {
						EventQueue.invokeLater(() -> {
							try {
								formCiv.setRoomReference(room.toString());
								authorWindow.buttonFactory.rdi.updateRoom();
								map.setSelectedRoom(room.toString());
								//FormWindow window = new FormWindow(formCiv, true);
								//window.frame.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						});
					}
				}
			}
		} else if(isRightButton){
        	map.stopDrawing();
        } else {
		    //some other mouse button was pressed (scrollwheel, side buttons)
		    System.out.println("Mouse Position: " + point);
			map.stopDrawing();
		}
	}

	public boolean undo() {
		boolean undid = false;
		if (manager.canUndo()) {
			manager.undo();
			//map.mapLayer.undo();
			//RoomList.getInstance().undo();
			DoorList.undo();
			undid = true;
		}
		return undid;
	}

	public boolean clear() {
		map = new Map();
		RoomList.getInstance().reset();
		return true;
	}

	public void save() {
		String filename = "savedMap.ser";
		ArrayList<Room> roomsToAdd = new ArrayList<>(RoomList.getInstance().list);
		map.rooms.addAll(roomsToAdd);
		try {
			FileOutputStream file = new FileOutputStream(filename);
			ObjectOutputStream out = new ObjectOutputStream(file);
			out.writeObject(map);
			out.close();
			file.close();

			System.out.println("Map has been saved");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void restore() {

		try {
		   FileInputStream file = new FileInputStream("savedMap.ser");
		   ObjectInputStream in = new ObjectInputStream(file);
		   map = (Map)in.readObject();
            //UndoableEditSupport undoSupport = new UndoableEditSupport(map);
        	//UndoManager manager = new UndoManager();
        	//map.addUndoableEditListener(manager);
         in.close();
         file.close();
         RoomList.getInstance().list = map.rooms;
            //map.mapLayer.detectRooms();

         System.out.println("Map has been restored ");
        } catch(IOException ex) {
        	ex.printStackTrace();
        } catch(ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught");
        }
	}

	public void draw(Graphics g) {
		map.draw(g);
	}

	public void outlining() {
		stopDrawing();
		stopPlacingPlayer();
		map.opaqueWalling();
	}

	public void walling() {
		stopDrawing();
		stopPlacingPlayer();
		map.transparentWalling();
	}

	public void startGame() {
		stopDrawing();
		stopPlacingPlayer();
		map.startGame();
	}

	public void placeStart() {
		stopDrawing();
		stopPlacingPlayer();
		map.placePlayerStart();
	}

	public void stopPlacingPlayer()  {
		map.stopPlacingPlayer();
	}

	public boolean placedPlayer() {
		return map.getPlayer().isPlaced();
	}

	public void goUp() {
		map.getPlayer().goUp();
	}

	public void goDown() {
		map.getPlayer().goDown();
	}

	public void goLeft() {
		map.getPlayer().goLeft();
	}

	public void goRight() {
		map.getPlayer().goRight();
	}

	public String getRoomName() {
		return map.getPlayer().getRoomName();
	}

	public String getRoomDesc() {
		return map.getPlayer().getRoomDesc();
	}

	public void stopDrawing() {
		map.stopDrawing();
	}

	public void archwayAdd() {
		stopDrawing();
		stopPlacingPlayer();
		map.archwayAdd();
	}
    public void doorAdd() {
		stopDrawing();
		stopPlacingPlayer();
		map.dooring();
	}

	public void drawRoom(String str) {
		map.drawRoom(str);
	}

	public void setSelectedRoom(String str) {
		map.setSelectedRoom(str);
	}

	public ArrayList<String> getRoomList() {
		ArrayList<String> rList = new ArrayList<String>();
		for (int i = 0; i < RoomList.getInstance().list.size(); i++) {
			rList.add(RoomList.getInstance().list.get(i).toString());
		}
		return rList;
	}

	public ArrayList<Room> getRooms(){
	   return RoomList.getInstance().list;
   }

	public Rectangle getRoomBounds(String str) {
		Room r = RoomList.getInstance().getRoomByStr(str);
		if (r == null || r.path == null) {
			return null;
		} else {
			return r.path.getBounds();
		}
	}

	/*

	NOTE: deliberately not deleted to be left as an output hook

	public void outputStory() {
		File out = new File("INFORM_Source/output.ni");
		PrintStream output = null;
		try {
			output = new PrintStream(out);
			System.setOut(output);
			StoryBuilder.build(map);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			output.close();
		}
	}
	*/

   /**
    * Method to set whether the MapLayer is for the player mode
    * @param setting the value to give to player mode
    */
   public void setPlayerMode(boolean setting) {
      map.setPlayerMode(setting);
   }

   /**
    * Method to delete walls and passageways
    */
   public void delete() {
      map.delete();
   }
}
