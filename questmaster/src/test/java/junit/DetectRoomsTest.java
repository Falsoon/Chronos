package junit;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import civ.CIV;
import pdc.Room;
import pdc.RoomList;

/**
 * This is the tests for Use Case #1 Drawing rooms on map and display to author
 * 
 * @author Daniel
 *
 */
class DetectRoomsTest {
	private CIV civ;

	@BeforeEach
	public void setup() {
		civ = new CIV();
	}

	@AfterEach
	public void tearDown() {
		civ = null;
		RoomList.getInstance().reset();
	}

	// N - User completes a square room shape
	@Test
	void testN1() {
		civ.outlining();
		Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 75);
      Point point3 = new Point(75, 75);
      Point point4 = new Point(75, 15);

		civ.mousePressed(point1, false, true,false);
		civ.mousePressed(point2, false, true,false);
		civ.mousePressed(point3, false, true,false);
      civ.mousePressed(point4, false, true,false);
      civ.mousePressed(point1, false, true,false);

		assertEquals(1, civ.getRoomList().size());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
	}


   // N - User completes a square room shape and then clicks another point
   @Test
   void testN2() {
      civ.outlining();
      Point point1 = new Point(15, 30);
      Point point2 = new Point(15, 75);
      Point point3 = new Point(75, 75);
      Point point4 = new Point(75, 30);
      Point point5 = new Point(15, 15);

      civ.mousePressed(point1, false, true,false);
      civ.mousePressed(point2, false, true,false);
      civ.mousePressed(point3, false, true,false);
      civ.mousePressed(point4, false, true,false);
      civ.mousePressed(point1, false, true,false);
      civ.mousePressed(point5, false, true,false);

      assertEquals(1, civ.getRoomList().size());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertTrue(civ.map.mapLayer.pointList.contains(point5));
   }

   // N - User clicks the corners to make an L-shaped room
   @Test
   void testN3() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 150);
      Point point3 = new Point(150, 150);
      Point point4 = new Point(150, 75);
      Point point5 = new Point(75, 75);
      Point point6 = new Point(75, 15);

      civ.mousePressed(point1, false, true,false);
      civ.mousePressed(point2, false, true,false);
      civ.mousePressed(point3, false, true,false);
      civ.mousePressed(point4, false, true,false);
      civ.mousePressed(point5, false, true,false);
      civ.mousePressed(point6, false, true,false);
      civ.mousePressed(point1, false, true,false);

      assertEquals(1, civ.getRoomList().size());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertTrue(civ.map.mapLayer.pointList.contains(point5));
      assertTrue(civ.map.mapLayer.pointList.contains(point6));
   }

   // N - User clicks the corners to make a +-shaped room
   @Test
   void testN4() {
      civ.outlining();
      Point point1 = new Point(60, 60);
      Point point2 = new Point(75, 60);
      Point point3 = new Point(75, 90);
      Point point4 = new Point(105, 90);
      Point point5 = new Point(105, 105);
      Point point6 = new Point(75, 105);
      Point point7 = new Point(75, 135);
      Point point8 = new Point(60, 135);
      Point point9 = new Point(60, 105);
      Point point10 = new Point(30, 105);
      Point point11 = new Point(30, 90);
      Point point12 = new Point(60, 90);

      civ.mousePressed(point1, false, true,false);
      civ.mousePressed(point2, false, true,false);
      civ.mousePressed(point3, false, true,false);
      civ.mousePressed(point4, false, true,false);
      civ.mousePressed(point5, false, true,false);
      civ.mousePressed(point6, false, true,false);
      civ.mousePressed(point7, false, true,false);
      civ.mousePressed(point8, false, true,false);
      civ.mousePressed(point9, false, true,false);
      civ.mousePressed(point10, false, true,false);
      civ.mousePressed(point11, false, true,false);
      civ.mousePressed(point12, false, true,false);
      civ.mousePressed(point1, false, true,false);


      assertEquals(1, civ.getRoomList().size());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertTrue(civ.map.mapLayer.pointList.contains(point5));
      assertTrue(civ.map.mapLayer.pointList.contains(point6));
      assertTrue(civ.map.mapLayer.pointList.contains(point7));
      assertTrue(civ.map.mapLayer.pointList.contains(point8));
      assertTrue(civ.map.mapLayer.pointList.contains(point9));
      assertTrue(civ.map.mapLayer.pointList.contains(point10));
      assertTrue(civ.map.mapLayer.pointList.contains(point11));
      assertTrue(civ.map.mapLayer.pointList.contains(point12));
      assertFalse(civ.map.mapLayer.pointList.contains(new Point(300, 90)));
   }

   // N User draws on to an uncompleted room
   @Test
   void testN5() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 45);
      Point point3 = new Point(90, 30);
      Point point4 = new Point(300, 30);

      civ.mousePressed(point1, false, true,false);
      civ.mousePressed(point2, false, true,false);

      civ.mousePressed(point3, false, true,false);
      // stops without finishing shape
      civ.stopDrawing();
      // then continues
      civ.outlining();
      civ.mousePressed(point3, false, true,false);
      civ.mousePressed(point4, false, true,false);

      assertEquals(0, civ.getRoomList().size());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
   }

   // N - User draws room then draws a second room
   @Test
   void testN6() {
      civ.outlining();
      //completes one shape
      Point point1 = new Point(15,15);
      Point point2 = new Point(15,90);
      Point point3 = new Point(90,90);
      Point point4 = new Point(90,15);
      Point point5 = new Point(160,15);
      Point point6 = new Point(160,90);
      //first
      civ.mousePressed(point1, false, true,false);
      civ.mousePressed(point2, false, true,false);
      civ.mousePressed(point3, false, true,false);
      civ.mousePressed(point4, false, true,false);
      civ.mousePressed(point1, false, true,false);

      civ.stopDrawing();
      civ.outlining();
      //second
      civ.mousePressed(point4, false, true,false);
      civ.mousePressed(point5, false, true,false);
      civ.mousePressed(point6, false, true,false);
      civ.mousePressed(point3, false, true,false);

      assertEquals(2, civ.getRoomList().size());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertTrue(civ.map.mapLayer.pointList.contains(point5));
      assertTrue(civ.map.mapLayer.pointList.contains(point6));
   }

   // N - User draws room then splits it with one line
   @Test
   void testN7() {
      civ.outlining();
      //completes one shape
      Point point1 = new Point(15,15);
      Point point2 = new Point(15,90);
      Point point3 = new Point(90,90);
      Point point4 = new Point(90,15);
      Point point5 = new Point(15,60);
      Point point6 = new Point(90,60);
      //first
      civ.mousePressed(point1, false, true,false);
      civ.mousePressed(point2, false, true,false);
      civ.mousePressed(point3, false, true,false);
      civ.mousePressed(point4, false, true,false);
      civ.mousePressed(point1, false, true,false);

      assertEquals(1, civ.getRoomList().size());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));

      civ.stopDrawing();
      civ.outlining();

      //second
      civ.mousePressed(point5, false, true,false);
      civ.mousePressed(point6, false, true,false);

      assertEquals(2, civ.getRoomList().size());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertTrue(civ.map.mapLayer.pointList.contains(point5));
      assertTrue(civ.map.mapLayer.pointList.contains(point6));
   }

   // N - User draws an unfinished room then draws a complete second room
   @Test
   void testN8() {
      civ.outlining();
      civ.mousePressed(new Point(15, 15), false, true,false);
      civ.mousePressed(new Point(15, 45), false, true,false);

      civ.mousePressed(new Point(90, 30), false, true,false);
      // stops without finishing shape
      civ.stopDrawing();
      // then continues
      civ.outlining();
      civ.mousePressed(new Point(105,90), false, true,false);
      civ.mousePressed(new Point(300, 90), false, true,false);
      civ.mousePressed(new Point(300,300), false, true,false);
      civ.mousePressed(new Point(105,300), false, true,false);
      civ.mousePressed(new Point(105,90), false, true,false);

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

   //N - Author draws a room with continuous lines
   @Test
   void testN9() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 75);
      Point point3 = new Point(75, 75);
      Point point4 = new Point(75, 15);

      civ.mousePressed(point1, false, true,false);
      civ.mousePressed(point2, false, true,false);
      civ.mousePressed(point3, false, true,false);
      civ.mousePressed(point4, false, true,false);
      civ.mousePressed(point1, false, true,false);

      assertEquals(1, civ.getRoomList().size());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
   }

	// B - User makes a small room (1x1)
	@Test
	void testB1() {
		civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 30);
      Point point3 = new Point(30, 30);
      Point point4 = new Point(30, 15);

      civ.mousePressed(point1, false, true,false);
      civ.mousePressed(point2, false, true,false);
      civ.mousePressed(point3, false, true,false);
      civ.mousePressed(point4, false, true,false);
      civ.mousePressed(point1, false, true,false);

		assertEquals(1, civ.getRoomList().size());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertFalse(civ.map.mapLayer.pointList.contains(new Point(300, 90)));
	}

	// B - User clicks the four corners of the map to make largest room possible
	@Test
	void testB2() {
		civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(15, Integer.MAX_VALUE);
      Point point3 = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
      Point point4 = new Point(Integer.MAX_VALUE, 15);
		civ.mousePressed(point1, false, true,false);
		civ.mousePressed(point2, false, true,false);

      civ.mousePressed(point2, false, true,false);
		civ.mousePressed(point3, false, true,false);

      civ.mousePressed(point3, false, true,false);
		civ.mousePressed(point4, false, true,false);

      civ.mousePressed(point4, false, true,false);
		civ.mousePressed(point1, false, true,false);


		assertEquals(1, civ.getRoomList().size());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
	}

   //B - Room 1 is entirely contained in the corner of Room 2, Room 1 is drawn first
   @Test
   void testB3() {
      Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 75);
      Point point3 = new Point(75, 75);
      Point point4 = new Point(75, 15);

      Point point5 = new Point(15,150);
      Point point6 = new Point(150,150);
      Point point7 = new Point(150,15);

      civ.outlining();

      //Room 1
      civ.mousePressed(point1, false, true,false);
      civ.mousePressed(point2, false, true,false);
      civ.mousePressed(point3, false, true,false);
      civ.mousePressed(point4, false, true,false);
      civ.mousePressed(point1, false, true,false);

      civ.stopDrawing();
      civ.outlining();

      //Room 2
      civ.mousePressed(point2, false, true,false);
      civ.mousePressed(point5, false, true,false);
      civ.mousePressed(point6, false, true,false);
      civ.mousePressed(point7, false, true,false);
      civ.mousePressed(point4, false, true,false);

      assertEquals(2, civ.getRoomList().size());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertTrue(civ.map.mapLayer.pointList.contains(point5));
      assertTrue(civ.map.mapLayer.pointList.contains(point6));
      assertTrue(civ.map.mapLayer.pointList.contains(point7));
   }

   //B - Room 2 is entirely contained in the corner of Room 1, Room 1 is drawn first
   @Test
   void testB4() {
      Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 75);
      Point point3 = new Point(75, 75);
      Point point4 = new Point(75, 15);

      Point point5 = new Point(15,150);
      Point point6 = new Point(150,150);
      Point point7 = new Point(150,15);

      civ.outlining();

      //Room 1
      civ.mousePressed(point2, false, true,false);
      civ.mousePressed(point5, false, true,false);
      civ.mousePressed(point6, false, true,false);
      civ.mousePressed(point7, false, true,false);
      civ.mousePressed(point4, false, true,false);

      civ.stopDrawing();
      civ.outlining();

      //Room 2
      civ.mousePressed(point1, false, true,false);
      civ.mousePressed(point2, false, true,false);
      civ.mousePressed(point3, false, true,false);
      civ.mousePressed(point4, false, true,false);
      civ.mousePressed(point1, false, true,false);

      assertEquals(2, civ.getRoomList().size());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertTrue(civ.map.mapLayer.pointList.contains(point5));
      assertTrue(civ.map.mapLayer.pointList.contains(point6));
      assertTrue(civ.map.mapLayer.pointList.contains(point7));
   }

   //B - Room 1 is entirely contained in Room 2, and one wall of Room 1 is shared with Room 2.  Room 1 drawn first
   @Test
   void testB5() {
      civ.outlining();
      Point point1 = new Point(15, 30);
      Point point2 = new Point(15, 75);
      Point point3 = new Point(75, 75);
      Point point4 = new Point(75, 30);

      Point point5 = new Point(15, 15);
      Point point6 = new Point(150,15);
      Point point7 = new Point(150,150);
      Point point8 = new Point(15,150);

      //Room 1
      civ.mousePressed(point1, false, true,false);
      civ.mousePressed(point2, false, true,false);
      civ.mousePressed(point3, false, true,false);
      civ.mousePressed(point4, false, true,false);
      civ.mousePressed(point1, false, true,false);

      civ.stopDrawing();
      civ.outlining();

      //Room 2
      civ.mousePressed(point1, false, true,false);
      civ.mousePressed(point5, false, true,false);
      civ.mousePressed(point6, false, true,false);
      civ.mousePressed(point7, false, true,false);
      civ.mousePressed(point8, false, true,false);
      civ.mousePressed(point2, false, true,false);

      assertEquals(2, civ.getRoomList().size());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertTrue(civ.map.mapLayer.pointList.contains(point5));
      assertTrue(civ.map.mapLayer.pointList.contains(point6));
      assertTrue(civ.map.mapLayer.pointList.contains(point7));
      assertTrue(civ.map.mapLayer.pointList.contains(point8));
   }

   //B - Room 1 is entirely contained in Room 2, and one wall of Room 1 is shared with Room 2.  Room 2 drawn first
   @Test
   void testB6() {
	   civ.outlining();
      Point point1 = new Point(15, 30);
      Point point2 = new Point(15, 75);
      Point point3 = new Point(75, 75);
      Point point4 = new Point(75, 30);

      Point point5 = new Point(15, 15);
      Point point6 = new Point(150,15);
      Point point7 = new Point(150,150);
      Point point8 = new Point(15,150);

      //Room 1
      civ.mousePressed(point1, false, true,false);
      civ.mousePressed(point5, false, true,false);
      civ.mousePressed(point6, false, true,false);
      civ.mousePressed(point7, false, true,false);
      civ.mousePressed(point8, false, true,false);
      civ.mousePressed(point2, false, true,false);

      civ.stopDrawing();
      civ.outlining();

      //Room 2
      civ.mousePressed(point1, false, true,false);
      civ.mousePressed(point2, false, true,false);
      civ.mousePressed(point3, false, true,false);
      civ.mousePressed(point4, false, true,false);
      civ.mousePressed(point1, false, true,false);

      assertEquals(2, civ.getRoomList().size());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertTrue(civ.map.mapLayer.pointList.contains(point5));
      assertTrue(civ.map.mapLayer.pointList.contains(point6));
      assertTrue(civ.map.mapLayer.pointList.contains(point7));
      assertTrue(civ.map.mapLayer.pointList.contains(point8));
   }

   //B - Room 1 is entirely contained in Room 2, and one wall of Room 1 is shared with Room 2.  A large box-shaped room
   //is drawn first, and then Room 1 is drawn inside of it
   @Test
   void testB7() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 150);
      Point point3 = new Point(150, 150);
      Point point4 = new Point(150, 15);

      Point point5 = new Point(15, 30);
      Point point6 = new Point(30,30);
      Point point7 = new Point(30,75);
      Point point8 = new Point(15,75);

      //Large room
      civ.mousePressed(point1, false, true,false);
      civ.mousePressed(point2, false, true,false);
      civ.mousePressed(point3, false, true,false);
      civ.mousePressed(point4, false, true,false);
      civ.mousePressed(point1, false, true,false);

      civ.stopDrawing();
      civ.outlining();

      //Smaller room
      civ.mousePressed(point5, false, true,false);
      civ.mousePressed(point6, false, true,false);
      civ.mousePressed(point7, false, true,false);
      civ.mousePressed(point8, false, true,false);

      assertEquals(2, civ.getRoomList().size());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertTrue(civ.map.mapLayer.pointList.contains(point5));
      assertTrue(civ.map.mapLayer.pointList.contains(point6));
      assertTrue(civ.map.mapLayer.pointList.contains(point7));
      assertTrue(civ.map.mapLayer.pointList.contains(point8));
   }

   //B - Room 1 is entirely contained in the center of Room 2
   @Test
   void testB8() {
      civ.outlining();
      Point point1 = new Point(30, 30);
      Point point2 = new Point(30, 45);
      Point point3 = new Point(45, 45);
      Point point4 = new Point(45, 30);

      Point point5 = new Point(15,15);
      Point point6 = new Point(150,15);
      Point point7 = new Point(150,150);
      Point point8 = new Point(15,150);

      //Room 1
      civ.mousePressed(point1, false, true,false);
      civ.mousePressed(point2, false, true,false);
      civ.mousePressed(point3, false, true,false);
      civ.mousePressed(point4, false, true,false);
      civ.mousePressed(point1, false, true,false);

      civ.stopDrawing();
      civ.outlining();

      //Room 2
      civ.mousePressed(point5, false, true,false);
      civ.mousePressed(point6, false, true,false);
      civ.mousePressed(point7, false, true,false);
      civ.mousePressed(point8, false, true,false);
      civ.mousePressed(point5, false, true,false);

      assertEquals(2, civ.getRoomList().size());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertTrue(civ.map.mapLayer.pointList.contains(point5));
      assertTrue(civ.map.mapLayer.pointList.contains(point6));
      assertTrue(civ.map.mapLayer.pointList.contains(point7));
      assertTrue(civ.map.mapLayer.pointList.contains(point8));
   }

   //B - Author draws duplicate walls over existing walls (e.g. by double clicking)
   @Test
   void testB9() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 75);
      Point point3 = new Point(75, 75);
      Point point4 = new Point(75, 15);

      civ.mousePressed(point1, false, true,false);
      civ.mousePressed(point2, false, true,false);

      civ.mousePressed(point2, false, true,false);
      civ.mousePressed(point3, false, true,false);

      civ.mousePressed(point3, false, true,false);
      civ.mousePressed(point4, false, true,false);

      civ.mousePressed(point4, false, true,false);
      civ.mousePressed(point1, false, true,false);

      assertEquals(1, civ.getRoomList().size());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertEquals(5,civ.map.mapLayer.pointList.size());
   }

}
