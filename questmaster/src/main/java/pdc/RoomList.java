package pdc;

import javax.swing.undo.StateEditable;
import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;


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

   public ArrayList<Room> getInaccessibleRooms(Point playerStartingPosition){
      HashSet<Room> accessibleRooms = new HashSet<>();
      Room startingRoom = RoomList.getInstance().getPlayerCurrentRoom(playerStartingPosition);
      accessibleRooms.add(startingRoom);
      ArrayList<Room> adjacentRoomsWithTraversableWalls = startingRoom.getAdjacentRoomsAsList().stream()
         .filter(Room::hasTraversableWall).collect(Collectors.toCollection(ArrayList::new));

      ArrayList<Room> accessibleAdjacentRooms = adjacentRoomsWithTraversableWalls.stream()
         .filter(room -> room.walls.stream().anyMatch(wall -> wall.isTraversable()&&startingRoom.walls.contains(wall))).collect(Collectors.toCollection(ArrayList::new));

      accessibleRooms.addAll(accessibleAdjacentRooms);
      Stack<Room> roomsToCheck = new Stack<>();
      roomsToCheck.addAll(accessibleAdjacentRooms);
      while(roomsToCheck.size()>0){
         Room room = roomsToCheck.pop();
         room.getAccessibleAdjacentRooms();
      }
      accessibleAdjacentRooms.addAll(getAccessibleRooms(accessibleRooms));
   }

   private HashSet<Room> getAccessibleRooms(HashSet<Room> accessibleRooms){
      for(Room room:accessibleRooms){

      }
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
