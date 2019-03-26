package pdc;

import javax.swing.undo.StateEditable;
import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Encapsulates all the rooms into a list
 */
@SuppressWarnings("serial")
public class RoomList implements StateEditable, Serializable {
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

	/* public void undo() {
      if(undoManager.canUndo()){
         undoManager.undo();
      }
	} */

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

   /**
    * Get the Room that contains the Player's starting position
    * @param playerStartingPosition the Player's starting position
    * @return the Room containing the Player's starting position
    */
   public Room getPlayerCurrentRoom(Point playerStartingPosition){
      Room currentRoom = null;
      for(Room room : list){
         if(room.contains(playerStartingPosition)){
            //make sure the player is in the right room if it's the case that Room 1 is contained by Room 2 and the
            //two rooms do not share a wall
            currentRoom = getInnerMostRoomContainingPlayer(room,playerStartingPosition);
            break;
         }
      }
      return currentRoom;
   }

   /**
    * Returns the intermost Room containing the Player's starting position.
    * @param room the Room to check for contained rooms
    * @param playerStartingPosition the Player's starting position
    * @return the intermost Room containing the Player's starting position
    */
   private Room getInnerMostRoomContainingPlayer(Room room,Point playerStartingPosition){
      Room innerMostRoom = room;
      ArrayList<Room> containedRoomsContainingPlayer = room.getContainedRooms().stream().filter(
         containedRoom->containedRoom.contains(playerStartingPosition)
      ).collect(Collectors.toCollection(ArrayList::new));
      if(containedRoomsContainingPlayer.size()>0){
         innerMostRoom = getInnerMostRoomContainingPlayer(containedRoomsContainingPlayer.get(0),playerStartingPosition);
      }
      return innerMostRoom;
   }

   /**
    * Returns an ArrayList\<Room\> of all Rooms accessible from the Player's starting Room.
    * An "accessible room" is defined here as a room that the player can walk to by going through portals and
    * transparent walls belonging to rooms within the map
    * @param playerStartingPosition the player's starting position
    * @return an ArrayList\<Room\> of all Rooms accessible from the Player's starting Room
    */
   public ArrayList<Room> getAccessibleRooms(Point playerStartingPosition){
      HashSet<Room> accessibleRooms = new HashSet<>();
      Room startingRoom = getPlayerCurrentRoom(playerStartingPosition);
      accessibleRooms.add(startingRoom);

      ArrayList<Room> currentAccessibleRooms = startingRoom.getAccessibleRooms();
      accessibleRooms.addAll(currentAccessibleRooms);

      Stack<Room> roomsToCheck = new Stack<>();
      roomsToCheck.addAll(currentAccessibleRooms);

      while(roomsToCheck.size()>0){
         Room room = roomsToCheck.pop();
         currentAccessibleRooms = room.getAccessibleRooms();
         //add all rooms adjacent to the current room that haven't been checked yet
         roomsToCheck.addAll(currentAccessibleRooms.stream().filter(accessibleRoom->!accessibleRooms.contains(accessibleRoom)).collect(Collectors.toCollection(HashSet::new)));
         accessibleRooms.addAll(room.getAccessibleRooms());
      }
      return new ArrayList<>(accessibleRooms);
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
