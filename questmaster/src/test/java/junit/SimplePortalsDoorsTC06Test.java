package test.java.junit;

import main.java.civ.CIV;
import main.java.hic.AuthorWindow;
import main.java.hic.ButtonFactory;
import main.java.pdc.Map;
import main.java.pdc.RoomList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

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
		RoomList.reset();
	}

	@Test
	void testAddDoorToMap() throws Throwable {
		bf = new ButtonFactory(aw);
		bf.setModeDraw();

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
		bf.setModeDraw();

		civ.outlining();
		civ.mousePressed(new Point(50, 50), false, true);
		civ.mousePressed(new Point(50, 100), false, true);

		civ.dooring();
		civ.mousePressed(new Point(50, 75), false, true);

		assertEquals(1, civ.numOfDoors());

		//TODO need to add in setting door open logic
	}

}
