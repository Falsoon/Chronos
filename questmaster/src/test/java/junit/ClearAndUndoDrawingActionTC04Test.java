package junit;

import civ.CIV;
import hic.AuthorWindow;
import hic.ButtonFactory;
import pdc.RoomList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;

class ClearAndUndoDrawingActionTC04Test {

	private CIV civ = new CIV();
	private AuthorWindow aw = new AuthorWindow();
	private ButtonFactory bf = new ButtonFactory(aw);

	@AfterEach
	public void tearDown() {
		aw = null;
		bf = null;
		civ = null;
		RoomList.reset();
	}

	@Test
	void testUndoLastWallOfCompleteRoom() throws Throwable {
		bf = new ButtonFactory(aw);
		bf.setModeDraw();

		Point p0 = new Point(50, 50);
		Point p1 = new Point(87, 95);
		Point p2 = new Point(300, 90);
		Point p3 = new Point(50, 50);


		civ.outlining();
		civ.mousePressed(p0, false, true);
		civ.mousePressed(p1, true, true);
		civ.mousePressed(p2, false, true);
		civ.mousePressed(p3, false, true);

		assertEquals(true, civ.undo());
		assertEquals(true, civ.map.mapLayer.pointList.contains(p0));
		assertEquals(true, civ.map.mapLayer.pointList.contains(p1));
		assertEquals(true, civ.map.mapLayer.pointList.contains(p2));
		assertEquals(3, civ.map.mapLayer.pointList.size());

	}

	@Test
	void testUndoSinglePoint() throws Throwable {
		bf = new ButtonFactory(aw);
		bf.setModeDraw();

		civ.outlining();
		civ.mousePressed(new Point(50, 50), true, true);

		assertEquals(true, civ.undo());
		assertEquals(false, civ.map.mapLayer.pointList.contains(new Point(50, 50)));


	}

	@Test
	void testUndoLastWallOfIncompleteRoom() throws Throwable {
		bf = new ButtonFactory(aw);
		bf.setModeDraw();

		Point p0 = new Point(50, 50);
		Point p1 = new Point(87, 95);
		Point p2 = new Point(300, 90);


		civ.outlining();
		civ.mousePressed(p0, false, true);
		civ.mousePressed(p1, true, true);
		civ.mousePressed(p2, false, true);

		assertEquals(true, civ.undo());
		assertEquals(true, civ.map.mapLayer.pointList.contains(p0));
		assertEquals(true, civ.map.mapLayer.pointList.contains(p1));
		assertEquals(false, civ.map.mapLayer.pointList.contains(p2));


	}

	@Test
	void testUndoOnEmptyMap() {
		bf = new ButtonFactory(aw);
		bf.setModeDraw();

		assertEquals(false, civ.undo());
	}

	@Test
	void testUndoPlayerStartPt() throws Throwable {
		bf = new ButtonFactory(aw);
		bf.setModeDraw();

		Point p0 = new Point(50, 50);
		Point p1 = new Point(87, 95);
		Point p2 = new Point(300, 15);
		Point p3 = new Point(50, 50);

		civ.outlining();
		civ.mousePressed(p0, false, true);
		civ.mousePressed(p1, true, true);
		civ.mousePressed(p2, false, true);
		civ.mousePressed(p3, false, true);

		civ.placedPlayer();
		civ.mousePressed(new Point(75, 35), false, true);

		assertEquals(true, civ.undo());
		assertEquals(true, civ.map.mapLayer.pointList.contains(p0));
		assertEquals(true, civ.map.mapLayer.pointList.contains(p1));
		assertEquals(true, civ.map.mapLayer.pointList.contains(p2));
		assertEquals(true, civ.map.mapLayer.pointList.contains(p3));
		assertEquals(false, civ.placedPlayer());

	}

	@Test
	void testClearOnEmptyMap() {
		bf = new ButtonFactory(aw);
		bf.setModeDraw();

		assertEquals(true, civ.clear());
	}

	@Test
	void testClearMapWithCompleteRoom() throws Throwable {
		bf = new ButtonFactory(aw);
		bf.setModeDraw();

		Point p0 = new Point(50, 50);
		Point p1 = new Point(87, 95);
		Point p2 = new Point(300, 90);
		Point p3 = new Point(50, 50);


		civ.outlining();
		civ.mousePressed(p0, false, true);
		civ.mousePressed(p1, true, true);
		civ.mousePressed(p2, false, true);
		civ.mousePressed(p3, false, true);

		assertEquals(true, civ.clear());
		assertEquals(false, civ.map.mapLayer.pointList.contains(p0));
		assertEquals(false, civ.map.mapLayer.pointList.contains(p1));
		assertEquals(false, civ.map.mapLayer.pointList.contains(p2));
		assertEquals(false, civ.map.mapLayer.pointList.contains(p3));
	}

	@Test
	void testClearMapWithIncompleteRoom() throws Throwable {
		bf = new ButtonFactory(aw);
		bf.setModeDraw();

		Point p0 = new Point(50, 50);
		Point p1 = new Point(87, 95);
		Point p2 = new Point(300, 90);


		civ.outlining();
		civ.mousePressed(p0, false, true);
		civ.mousePressed(p1, true, true);
		civ.mousePressed(p2, false, true);

		assertEquals(true, civ.clear());
		assertEquals(false, civ.map.mapLayer.pointList.contains(p0));
		assertEquals(false, civ.map.mapLayer.pointList.contains(p1));
		assertEquals(false, civ.map.mapLayer.pointList.contains(p2));
	}

	@Test
	void testClearMapWithMultipleRooms() throws Throwable {
		bf = new ButtonFactory(aw);
		bf.setModeDraw();

		Point p0 = new Point(50, 50);
		Point p1 = new Point(87, 95);
		Point p2 = new Point(300, 90);
		Point p3 = new Point(50, 50);

		Point pA = new Point(150, 150);
		Point pB = new Point(187, 195);
		Point pC = new Point(200, 190);
		Point pD = new Point(150, 150);


		civ.outlining();
		civ.mousePressed(p0, false, true);
		civ.mousePressed(p1, true, true);
		civ.mousePressed(p2, false, true);
		civ.mousePressed(p3, false, true);

		civ.mousePressed(pA, false, true);
		civ.mousePressed(pB, true, true);
		civ.mousePressed(pC, false, true);
		civ.mousePressed(pD, false, true);

		assertEquals(true, civ.clear());
		assertEquals(false, civ.map.mapLayer.pointList.contains(p0));
		assertEquals(false, civ.map.mapLayer.pointList.contains(p1));
		assertEquals(false, civ.map.mapLayer.pointList.contains(p2));
		assertEquals(false, civ.map.mapLayer.pointList.contains(p3));
		assertEquals(false, civ.map.mapLayer.pointList.contains(pA));
		assertEquals(false, civ.map.mapLayer.pointList.contains(pB));
		assertEquals(false, civ.map.mapLayer.pointList.contains(pC));
		assertEquals(false, civ.map.mapLayer.pointList.contains(pD));
	}

	@Test
	void testClearMapWithPlayerStartPtAndRooms() throws Throwable {
		bf = new ButtonFactory(aw);
		bf.setModeDraw();

		Point p0 = new Point(50, 50);
		Point p1 = new Point(87, 95);
		Point p2 = new Point(300, 15);
		Point p3 = new Point(50, 50);

		civ.outlining();
		civ.mousePressed(p0, false, true);
		civ.mousePressed(p1, true, true);
		civ.mousePressed(p2, false, true);
		civ.mousePressed(p3, false, true);

		civ.placedPlayer();
		civ.mousePressed(new Point(75, 35), false, true);

		assertEquals(true, civ.clear());
		assertEquals(false, civ.map.mapLayer.pointList.contains(p0));
		assertEquals(false, civ.map.mapLayer.pointList.contains(p1));
		assertEquals(false, civ.map.mapLayer.pointList.contains(p2));
		assertEquals(false, civ.map.mapLayer.pointList.contains(p3));
	}

}
