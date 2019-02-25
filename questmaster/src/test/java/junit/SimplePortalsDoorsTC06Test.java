package junit;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import civ.CIV;
import hic.AuthorWindow;
import hic.ButtonFactory;
import pdc.Door;
import pdc.Map;
import pdc.MapLayer;
import pdc.RoomList;
import pdc.DoorList;

class SimplePortalsDoorsTC06Test {

	private CIV civ = new CIV();
	private Map map = new Map();
	private AuthorWindow aw = new AuthorWindow();
	private ButtonFactory bf = new ButtonFactory(aw);
	
	@AfterEach
	public void tearDown() {
		aw = null;
		map = null;
		bf = null;
		civ = null;
		RoomList.getInstance().reset();
	}
	
	@Test
	void testAddDoorToMap() throws Throwable {
		bf = new ButtonFactory(aw);
		bf.setMode2();
		
		civ.outlining();
		civ.mousePressed(new Point(50, 50), false, true);
		civ.mousePressed(new Point(50, 100), false, true);
		
		civ.dooring();
		civ.mousePressed(new Point(50, 75), false, true);
		
		assertEquals(1, civ.numOfDoors());
	}
	
	@Test
	void testAddDoorAndSetOpen() throws Throwable {
		bf = new ButtonFactory(aw);
		bf.setMode2();
		
		civ.outlining();
		civ.mousePressed(new Point(50, 50), false, true);
		civ.mousePressed(new Point(50, 100), false, true);
		
		civ.dooring();
		civ.mousePressed(new Point(50, 75), false, true);
		
		assertEquals(1, civ.numOfDoors());
		
		//TODO need to add in setting door open logic
	}

}
