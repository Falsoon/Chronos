import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import civ.FormCiv;
import hic.FormWindow;
import pdc.Room;
import pdc.RoomList;

class DescriptionFormTesting {

	
	private Room room = new Room();
	private FormCiv fc = new FormCiv();
	private FormWindow fw = new FormWindow(fc, false);
	
	@Test
	void testRoomID() {
		RoomList.add(room);
		fc.setRoomReference(room.toString());
		int ans = room.ROOMID;
		
		assertEquals(ans, fc.getRoomID() );
		
	}
	
	@Test
	void testRoomTitle(){
		room.title = "hello";
		
	}

}
