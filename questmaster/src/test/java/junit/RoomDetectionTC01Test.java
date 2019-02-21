package junit;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import civ.CIV;
import pdc.RoomList;

/**
 * This is the tests for Use Case #1 Drawing rooms on map and display to author
 * 
 * @author Daniel
 *
 */
class RoomDetectionTC01Test {
	private CIV civ;

	@BeforeEach
	public void setup() {
		civ = new CIV();
	}

	@AfterEach
	public void tearDown() {
		civ = null;
		RoomList.reset();
	}

	// TC01-1 User clicks points on map without pressing draw button
	@Test
	void test01_1() {
		civ.mousePressed(new Point(), false, true);
		civ.mousePressed(new Point(200, 100), true, true);
      assertTrue(civ.getRoomList().isEmpty());
      assertFalse(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.isEmpty());
	}

	// TC01-2 User clicks opaque wall option, then clicks two points on map to draw
	// a line
	@Test
	void test01_2() {
		civ.outlining();
		civ.mousePressed(new Point(), false, true);
		civ.mousePressed(new Point(200, 100), false, true);
      assertTrue(civ.map.isCreating());
      assertTrue(civ.getRoomList().isEmpty());
      assertTrue(civ.map.mapLayer.pointList.contains(new Point()));
      assertTrue(civ.map.mapLayer.pointList.contains(new Point(195, 105)));
	}

	// TC01-3 User clicks opaque wall option, then clicks two
	// points on map to draw a line while holding alt
	@Test
	void test01_3() {
		civ.outlining();
		civ.mousePressed(new Point(), true, true);
		civ.mousePressed(new Point(87, 95), true, true);
      assertTrue(civ.getRoomList().isEmpty());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(new Point()));
      assertTrue(civ.map.mapLayer.pointList.contains(new Point(87, 95)));
	}

	// TC01-4 User completes a square room shape
	@Test
	void test01_4() {
		civ.outlining();
		Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 75);
      Point point3 = new Point(75, 75);
      Point point4 = new Point(75, 15);

		civ.mousePressed(point1, false, true);
		civ.mousePressed(point2, false, true);

      civ.mousePressed(point2, false, true);
		civ.mousePressed(point3, false, true);

      civ.mousePressed(point3, false, true);
      civ.mousePressed(point4, false, true);

      civ.mousePressed(point4, false, true);
      civ.mousePressed(point1, false, true);

		assertEquals(1, civ.getRoomList().size());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
	}

   // TC01-5 User completes a square room shape and then clicks another point
   @Test
   void test01_5() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 75);
      Point point3 = new Point(75, 75);
      Point point4 = new Point(75, 15);
      Point point5 = new Point(150, 150);

      civ.mousePressed(point1, false, true);
      civ.mousePressed(point2, false, true);

      civ.mousePressed(point2, false, true);
      civ.mousePressed(point3, false, true);

      civ.mousePressed(point3, false, true);
      civ.mousePressed(point4, false, true);

      civ.mousePressed(point4, false, true);
      civ.mousePressed(point1, false, true);

      civ.mousePressed(point5, false, true);

      assertEquals(1, civ.getRoomList().size());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertFalse(civ.map.mapLayer.pointList.contains(point5));
   }

	// TC01-6 User makes a small room (1x1)
	@Test
	void test01_6() {
		civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 30);
      Point point3 = new Point(30, 30);
      Point point4 = new Point(30, 15);

      civ.mousePressed(point1, false, true);
      civ.mousePressed(point2, false, true);

      civ.mousePressed(point2, false, true);
      civ.mousePressed(point3, false, true);

      civ.mousePressed(point3, false, true);
      civ.mousePressed(point4, false, true);

      civ.mousePressed(point4, false, true);
      civ.mousePressed(point1, false, true);

		assertEquals(1, civ.getRoomList().size());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertFalse(civ.map.mapLayer.pointList.contains(new Point(300, 90)));
	}

	// TC01-7 User clicks the four corners of the map to make largest room possible
	@Test
	void test01_7() {
		civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(15, Integer.MAX_VALUE);
      Point point3 = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
      Point point4 = new Point(Integer.MAX_VALUE, 15);
		civ.mousePressed(point1, false, true);
		civ.mousePressed(point2, false, true);

      civ.mousePressed(point2, false, true);
		civ.mousePressed(point3, false, true);

      civ.mousePressed(point3, false, true);
		civ.mousePressed(point4, false, true);

      civ.mousePressed(point4, false, true);
		civ.mousePressed(point1, false, true);


		assertEquals(1, civ.getRoomList().size());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
	}

	// TC01-8 User clicks the corners to make an L-shaped room
	@Test
	void test01_8() {
		civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 150);
      Point point3 = new Point(150, 150);
      Point point4 = new Point(150, 75);
      Point point5 = new Point(75, 75);
      Point point6 = new Point(75, 15);

      civ.mousePressed(point1, false, true);
      civ.mousePressed(point2, false, true);

      civ.mousePressed(point2, false, true);
      civ.mousePressed(point3, false, true);

      civ.mousePressed(point3, false, true);
      civ.mousePressed(point4, false, true);

      civ.mousePressed(point4, false, true);
      civ.mousePressed(point5, false, true);

      civ.mousePressed(point5, false, true);
      civ.mousePressed(point6, false, true);

      civ.mousePressed(point6, false, true);
      civ.mousePressed(point1, false, true);

		assertEquals(1, civ.getRoomList().size());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertTrue(civ.map.mapLayer.pointList.contains(point5));
      assertTrue(civ.map.mapLayer.pointList.contains(point6));
	}

	// TC01-9 User clicks the corners to make a +-shaped room
	@Test
	void test01_9() {
		civ.outlining();
		civ.mousePressed(new Point(60, 60), false, true);
		civ.mousePressed(new Point(75, 60), false, true);

      civ.mousePressed(new Point(75, 60), false, true);
		civ.mousePressed(new Point(75, 90), false, true);

      civ.mousePressed(new Point(75, 90), false, true);
		civ.mousePressed(new Point(105, 90), false, true);

      civ.mousePressed(new Point(105, 90), false, true);
		civ.mousePressed(new Point(105, 105), false, true);

      civ.mousePressed(new Point(105, 105), false, true);
		civ.mousePressed(new Point(75, 105), false, true);

      civ.mousePressed(new Point(75, 105), false, true);
		civ.mousePressed(new Point(75, 135), false, true);

      civ.mousePressed(new Point(75, 135), false, true);
		civ.mousePressed(new Point(60, 135), false, true);

      civ.mousePressed(new Point(60, 135), false, true);
		civ.mousePressed(new Point(60, 105), false, true);

      civ.mousePressed(new Point(60, 105), false, true);
		civ.mousePressed(new Point(30, 105), false, true);

      civ.mousePressed(new Point(30, 105), false, true);
		civ.mousePressed(new Point(30, 90), false, true);

      civ.mousePressed(new Point(30, 90), false, true);
		civ.mousePressed(new Point(60, 90), false, true);

      civ.mousePressed(new Point(60, 90), false, true);
		civ.mousePressed(new Point(60, 60), false, true);


		assertEquals(1, civ.getRoomList().size());
      assertTrue(civ.map.mapLayer.pointList.contains(new Point(60, 60)));
      assertTrue(civ.map.mapLayer.pointList.contains(new Point(75, 135)));
      assertTrue(civ.map.mapLayer.pointList.contains(new Point(30, 90)));
      assertFalse(civ.map.mapLayer.pointList.contains(new Point(300, 90)));
	}

	//TC01-10 User draws an invalid transparent line
   @Test
   void test01_10() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 75);
      Point point3 = new Point(75, 75);
      Point point4 = new Point(75, 15);

      Point point5 = new Point(15,60);
      Point point6 = new Point(100,100);

      civ.mousePressed(point1, false, true);
      civ.mousePressed(point2, false, true);

      civ.mousePressed(point2, false, true);
      civ.mousePressed(point3, false, true);

      civ.mousePressed(point3, false, true);
      civ.mousePressed(point4, false, true);

      civ.mousePressed(point4, false, true);
      civ.mousePressed(point1, false, true);

      assertEquals(1, civ.getRoomList().size());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));

      civ.walling();

      civ.mousePressed(point5,false,true);
      civ.mousePressed(point6,false,true);

      assertEquals(1, civ.getRoomList().size());
      assertFalse(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertFalse(civ.map.mapLayer.pointList.contains(point5));
      assertFalse(civ.map.mapLayer.pointList.contains(point6));
   }
	// TC01-11 User draws on to an uncompleted room
	@Test
	void test01_11() {
		civ.outlining();
		civ.mousePressed(new Point(15, 15), false, true);
		civ.mousePressed(new Point(15, 45), false, true);
      civ.mousePressed(new Point(15, 45), false, true);
		civ.mousePressed(new Point(90, 30), false, true);
		// stops without finishing shape
		civ.stopDrawing();
		// then continues
		civ.outlining();
		civ.mousePressed(new Point(90, 30), false, true);
		civ.mousePressed(new Point(300, 90), false, true);

		assertEquals(0, civ.getRoomList().size());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(new Point(15, 15)));
      assertTrue(civ.map.mapLayer.pointList.contains(new Point(15, 45)));
      assertTrue(civ.map.mapLayer.pointList.contains(new Point(90, 30)));
      assertTrue(civ.map.mapLayer.pointList.contains(new Point(300, 90)));
	}

	// TC01-12 User draws a room and splits it with a transparent line
	@Test
	void test01_12() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 75);
      Point point3 = new Point(75, 75);
      Point point4 = new Point(75, 15);

      Point point5 = new Point(15,60);
      Point point6 = new Point(75,60);

      civ.mousePressed(point1, false, true);
      civ.mousePressed(point2, false, true);

      civ.mousePressed(point2, false, true);
      civ.mousePressed(point3, false, true);

      civ.mousePressed(point3, false, true);
      civ.mousePressed(point4, false, true);

      civ.mousePressed(point4, false, true);
      civ.mousePressed(point1, false, true);

      assertEquals(1, civ.getRoomList().size());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));

      civ.walling();

      civ.mousePressed(point5,false,true);
      civ.mousePressed(point6,false,true);

      assertEquals(2, civ.getRoomList().size());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertTrue(civ.map.mapLayer.pointList.contains(point5));
      assertTrue(civ.map.mapLayer.pointList.contains(point6));
	}

	// TC01-13 User draws an unfinished room then draws a complete second room 
	@Test
	void test01_13() {
		civ.outlining();
		civ.mousePressed(new Point(15, 15), false, true);
		civ.mousePressed(new Point(15, 45), false, true);

      civ.mousePressed(new Point(15, 45), false, true);
		civ.mousePressed(new Point(90, 30), false, true);
		// stops without finishing shape
		civ.stopDrawing();
		// then continues
		civ.outlining();
		civ.mousePressed(new Point(105,90), false, true);
		civ.mousePressed(new Point(300, 90), false, true);

      civ.mousePressed(new Point(300, 90), false, true);
		civ.mousePressed(new Point(300,300), false, true);

      civ.mousePressed(new Point(300,300), false, true);
		civ.mousePressed(new Point(105,300), false, true);

      civ.mousePressed(new Point(105,300), false, true);
      civ.mousePressed(new Point(105,90), false, true);

		assertEquals(1, civ.getRoomList().size());
      assertTrue(civ.map.mapLayer.pointList.contains(new Point(15, 15)));
      assertTrue(civ.map.mapLayer.pointList.contains(new Point(15, 45)));
      assertTrue(civ.map.mapLayer.pointList.contains(new Point(90, 30)));
      assertTrue(civ.map.mapLayer.pointList.contains(new Point(105, 90)));
      assertTrue(civ.map.mapLayer.pointList.contains(new Point(300, 90)));
      assertTrue(civ.map.mapLayer.pointList.contains(new Point(300, 300)));
      assertTrue(civ.map.mapLayer.pointList.contains(new Point(105, 300)));
      assertTrue(civ.map.mapLayer.pointList.contains(new Point(105, 90)));
	}
	// TC01-14 User draws room then draws a second room 
	@Test
	void test01_14() {
		civ.outlining();
		//completes one shape
      Point point1 = new Point(15,15);
      Point point2 = new Point(15,90);
      Point point3 = new Point(90,90);
      Point point4 = new Point(90,15);
      Point point5 = new Point(160,15);
      Point point6 = new Point(160,90);
      //first
		civ.mousePressed(point1, false, true);
		civ.mousePressed(point2, false, true);

      civ.mousePressed(point2, false, true);
      civ.mousePressed(point3, false, true);

      civ.mousePressed(point3, false, true);
      civ.mousePressed(point4, false, true);

      civ.mousePressed(point4, false, true);
      civ.mousePressed(point1, false, true);

      //second
      civ.mousePressed(point4, false, true);
      civ.mousePressed(point5, false, true);

      civ.mousePressed(point5, false, true);
      civ.mousePressed(point6, false, true);

      civ.mousePressed(point6, false, true);
      civ.mousePressed(point3, false, true);

		assertEquals(2, civ.getRoomList().size());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertTrue(civ.map.mapLayer.pointList.contains(point5));
      assertTrue(civ.map.mapLayer.pointList.contains(point6));
	}
   // TC01-15 User draws room then splits it with one line
   @Test
   void test01_15() {
      civ.outlining();
      //completes one shape
      Point point1 = new Point(15,15);
      Point point2 = new Point(15,90);
      Point point3 = new Point(90,90);
      Point point4 = new Point(90,15);
      Point point5 = new Point(15,60);
      Point point6 = new Point(90,60);
      //first
      civ.mousePressed(point1, false, true);
      civ.mousePressed(point2, false, true);

      civ.mousePressed(point2, false, true);
      civ.mousePressed(point3, false, true);

      civ.mousePressed(point3, false, true);
      civ.mousePressed(point4, false, true);

      civ.mousePressed(point4, false, true);
      civ.mousePressed(point1, false, true);

      assertEquals(1, civ.getRoomList().size());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));

      //second
      civ.mousePressed(point5, false, true);
      civ.mousePressed(point6, false, true);

      assertEquals(2, civ.getRoomList().size());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertTrue(civ.map.mapLayer.pointList.contains(point5));
      assertTrue(civ.map.mapLayer.pointList.contains(point6));
   }
	/*
	 * 
	 * 
	 * 
	 *  
	 * 
	 * 
	 * 
	 */
}
