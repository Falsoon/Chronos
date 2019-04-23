package pdc;

public class StoryBuilder {

	public static void build(Map map) {
		if (RoomList.getInstance().list.size() > 0) {
			System.out.println("Section - Global Actions\r\n" + "\r\n"
					+ "When play begins: now the command prompt is \"> \". \r\n" + "\r\n" + "\r\n"
					+ "Section - Story\r\n" + "\r\n");
			Room r = RoomList.getInstance().getRoom(map.player.getPosition());
			System.out.println(generateRoomStory(r));
			for (int i = 0; i < RoomList.getInstance().list.size(); i++) {
				if(!RoomList.getInstance().list.get(i).equals(r)) {
					System.out.println(generateRoomStory(RoomList.getInstance().list.get(i)));
				}
			}
		}
	}

	private static String generateRoomStory(Room room) {
		String roomInfo;
		if(!room.getAdjacents().equals("")) {
			roomInfo = room.title + room.getAdjacents() + ". Description is " + "\"" + room.desc + "\".";
		}else {
			roomInfo = "Description of "+ room.title + " is \"" + room.desc + "\".";
		}
		return roomInfo;
	}
}
