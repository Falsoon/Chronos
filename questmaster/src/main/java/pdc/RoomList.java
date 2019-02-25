package pdc;

import javax.swing.undo.StateEdit;
import javax.swing.undo.StateEditable;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;


/**
 * Encapsulates all the rooms into a list
 */
public class RoomList implements StateEditable {
	public ArrayList<Room> list = new ArrayList<>();
	private static final String listKey = "list";
   //private UndoManager undoManager;
   //private StateEdit stateEdit;

   private static RoomList instance;

   public static RoomList getInstance() {
      if (instance == null) {
         instance = new RoomList();
      }

      return instance;
   }

   private RoomList() {
      //undoManager = new UndoManager();
   }

	public Room getRoom(Point p) {
		Room room = null;
		Iterator<Room> itr = list.iterator();
		boolean found = false;
		while (!found && itr.hasNext()) {
			room = itr.next();
			found = room.contains(p);
		}
		if (!found) {
			room = null;
		}
		return room;
	}

	public void add(Room r) {
      list.add(r);
	}

	public void reset() {
		list.clear();
		Room.idCount = 1;
	}

	public Room getRoomById(int id) {
		Room room = null;
		for(int i=0; i< list.size();i++) {
			if(list.get(i).ROOMID==id) {
				room = list.get(i);
			}
		}
		return room;
	}

	public Room getRoomByStr(String str) {
		boolean found = false;
		Room r = null;
		for(int i=0;i<list.size()&&!found;i++) {
			if(list.get(i).toString().equals(str)) {
				r = list.get(i);
				found = true;
			}
		}
		return r;
	}

	public void undo() {
      /*
      if(undoManager.canUndo()){
         undoManager.undo();
      }
      */
	}

	public Iterator<Room> iterator(){
	   return list.iterator();
   }

   public void startStateEdit(){
      //stateEdit = new StateEdit(RoomList.getInstance());
   }

   public void endStateEdit(){
      //stateEdit = new StateEdit(RoomList.getInstance());
      //stateEdit.end();
      //undoManager.addEdit(stateEdit);
   }

   @Override
   public void storeState(Hashtable<Object, Object> state) {
      state.put(listKey,list);
   }

   @Override
   public void restoreState(Hashtable<?, ?> state) {
      if(state.contains(listKey)){
         list = (ArrayList) state.get(listKey);
      }
   }
}
