package junit;

import civ.CIV;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pdc.CardinalDirection;
import pdc.Room;
import pdc.RoomList;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DirectionalPortalsTest {
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

   // N - Author draws 2 rooms connected by an archway
   @Test
   void testN1() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 75);
      Point point3 = new Point(75, 75);
      Point point4 = new Point(75, 15);
      Point point5 = new Point(150,15);
      Point point6 = new Point(150,75);
      Point point7 = new Point(75,40);

      civ.mousePressed(point1, false, true,false);
      civ.mousePressed(point2, false, true,false);
      civ.mousePressed(point3, false, true,false);
      civ.mousePressed(point4, false, true,false);
      civ.mousePressed(point1, false, true,false);
      civ.stopDrawing();
      civ.outlining();
      civ.mousePressed(point4, false, true,false);
      civ.mousePressed(point5, false, true,false);
      civ.mousePressed(point6, false, true,false);
      civ.mousePressed(point3, false, true,false);
      civ.stopDrawing();
      civ.archwayAdd();
      civ.mousePressed(point7, false, true,false);

      assertEquals(2, civ.getRoomList().size());
      ArrayList<Room> rooms = civ.getRooms();
      Room room1 = rooms.get(0);
      assertTrue(room1.hasPortalInDirection(CardinalDirection.EAST));
      Room room2 = rooms.get(1);
      assertTrue(room2.hasPortalInDirection(CardinalDirection.WEST));
   }

   // N - Author draws 3 rooms connected by an archways
   @Test
   void testN2() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 75);
      Point point3 = new Point(75, 75);
      Point point4 = new Point(75, 15);

      Point point5 = new Point(150,15);
      Point point6 = new Point(150,75);

      Point point7 = new Point(75, 150);
      Point point8 = new Point(15, 150);
      Point archway1 = new Point(75,40);
      Point archway2 = new Point(40, 75);

      civ.mousePressed(point1, false, true,false);
      civ.mousePressed(point2, false, true,false);
      civ.mousePressed(point3, false, true,false);
      civ.mousePressed(point4, false, true,false);
      civ.mousePressed(point1, false, true,false);
      civ.stopDrawing();
      civ.outlining();
      civ.mousePressed(point4, false, true,false);
      civ.mousePressed(point5, false, true,false);
      civ.mousePressed(point6, false, true,false);
      civ.mousePressed(point3, false, true,false);
      civ.stopDrawing();
      civ.outlining();
      civ.mousePressed(point3, false, true,false);
      civ.mousePressed(point7, false, true,false);
      civ.mousePressed(point8, false, true,false);
      civ.mousePressed(point2, false, true,false);
      civ.archwayAdd();
      civ.mousePressed(archway1, false, true,false);
      civ.mousePressed(archway2, false, true,false);

      assertEquals(3, civ.getRoomList().size());
      ArrayList<Room> rooms = civ.getRooms();
      Room room1 = rooms.get(0);
      assertTrue(room1.hasPortalInDirection(CardinalDirection.EAST));
      assertTrue(room1.hasPortalInDirection(CardinalDirection.SOUTH));
      assertFalse(room1.hasPortalInDirection(CardinalDirection.NORTH));
      assertFalse(room1.hasPortalInDirection(CardinalDirection.WEST));
      assertFalse(room1.hasPortalInDirection(CardinalDirection.UP));
      assertFalse(room1.hasPortalInDirection(CardinalDirection.DOWN));
      Room room2 = rooms.get(1);
      assertTrue(room2.hasPortalInDirection(CardinalDirection.WEST));
      assertFalse(room2.hasPortalInDirection(CardinalDirection.SOUTH));
      assertFalse(room2.hasPortalInDirection(CardinalDirection.EAST));
      assertFalse(room2.hasPortalInDirection(CardinalDirection.NORTH));
      assertFalse(room2.hasPortalInDirection(CardinalDirection.UP));
      assertFalse(room2.hasPortalInDirection(CardinalDirection.DOWN));
      Room room3 = rooms.get(2);
      assertTrue(room3.hasPortalInDirection(CardinalDirection.NORTH));
      assertFalse(room3.hasPortalInDirection(CardinalDirection.SOUTH));
      assertFalse(room3.hasPortalInDirection(CardinalDirection.EAST));
      assertFalse(room3.hasPortalInDirection(CardinalDirection.WEST));
      assertFalse(room3.hasPortalInDirection(CardinalDirection.UP));
      assertFalse(room3.hasPortalInDirection(CardinalDirection.DOWN));
   }

   // N - Author draws 4 rooms, the first 3 connected by archways
   @Test
   void testN3() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 75);
      Point point3 = new Point(75, 75);
      Point point4 = new Point(75, 15);

      Point point5 = new Point(150,15);
      Point point6 = new Point(150,75);

      Point point7 = new Point(75, 150);
      Point point8 = new Point(15, 150);

      Point point9 = new Point(150, 150);
      Point archway1 = new Point(75,40);
      Point archway2 = new Point(40, 75);

      civ.mousePressed(point1, false, true,false);
      civ.mousePressed(point2, false, true,false);
      civ.mousePressed(point3, false, true,false);
      civ.mousePressed(point4, false, true,false);
      civ.mousePressed(point1, false, true,false);
      civ.stopDrawing();
      civ.outlining();
      civ.mousePressed(point4, false, true,false);
      civ.mousePressed(point5, false, true,false);
      civ.mousePressed(point6, false, true,false);
      civ.mousePressed(point3, false, true,false);
      civ.stopDrawing();
      civ.outlining();
      civ.mousePressed(point3, false, true,false);
      civ.mousePressed(point7, false, true,false);
      civ.mousePressed(point8, false, true,false);
      civ.mousePressed(point2, false, true,false);
      civ.stopDrawing();
      civ.outlining();
      civ.mousePressed(point6, false, true,false);
      civ.mousePressed(point9, false, true,false);
      civ.mousePressed(point7, false, true,false);
      civ.stopDrawing();
      civ.archwayAdd();
      civ.mousePressed(archway1, false, true,false);
      civ.mousePressed(archway2, false, true,false);

      assertEquals(4, civ.getRoomList().size());
      ArrayList<Room> rooms = civ.getRooms();
      Room room1 = rooms.get(0);
      assertTrue(room1.hasPortalInDirection(CardinalDirection.EAST));
      assertTrue(room1.hasPortalInDirection(CardinalDirection.SOUTH));
      assertFalse(room1.hasPortalInDirection(CardinalDirection.NORTH));
      assertFalse(room1.hasPortalInDirection(CardinalDirection.WEST));
      assertFalse(room1.hasPortalInDirection(CardinalDirection.UP));
      assertFalse(room1.hasPortalInDirection(CardinalDirection.DOWN));
      Room room2 = rooms.get(1);
      assertTrue(room2.hasPortalInDirection(CardinalDirection.WEST));
      assertFalse(room2.hasPortalInDirection(CardinalDirection.SOUTH));
      assertFalse(room2.hasPortalInDirection(CardinalDirection.EAST));
      assertFalse(room2.hasPortalInDirection(CardinalDirection.NORTH));
      assertFalse(room2.hasPortalInDirection(CardinalDirection.UP));
      assertFalse(room2.hasPortalInDirection(CardinalDirection.DOWN));
      Room room3 = rooms.get(2);
      assertTrue(room3.hasPortalInDirection(CardinalDirection.NORTH));
      assertFalse(room3.hasPortalInDirection(CardinalDirection.SOUTH));
      assertFalse(room3.hasPortalInDirection(CardinalDirection.EAST));
      assertFalse(room3.hasPortalInDirection(CardinalDirection.WEST));
      assertFalse(room3.hasPortalInDirection(CardinalDirection.UP));
      assertFalse(room3.hasPortalInDirection(CardinalDirection.DOWN));
      Room room4 = rooms.get(3);
      assertFalse(room4.hasPortalInDirection(CardinalDirection.NORTH));
      assertFalse(room4.hasPortalInDirection(CardinalDirection.SOUTH));
      assertFalse(room4.hasPortalInDirection(CardinalDirection.EAST));
      assertFalse(room4.hasPortalInDirection(CardinalDirection.WEST));
      assertFalse(room4.hasPortalInDirection(CardinalDirection.UP));
      assertFalse(room4.hasPortalInDirection(CardinalDirection.DOWN));
   }

   // N - Author draws 5 rooms in a cross shape, the outer 4 connected to the central room
   @Test
   void testN4() {
      civ.outlining();
      Point point1 = new Point(300, 150);
      Point point2 = new Point(300, 300);
      Point point3 = new Point(150, 300);
      Point point4 = new Point(150, 150);

      Point point5 = new Point(150, 15);
      Point point6 = new Point(300, 15);

      Point point7 = new Point(450, 150);
      Point point8 = new Point(450, 300);

      Point point9 = new Point(300, 450);
      Point point10 = new Point(150, 450);

      Point point11 = new Point(15, 300);
      Point point12 = new Point(15, 150);

      Point archway1 = new Point(200,150);
      Point archway2 = new Point(300, 200);
      Point archway3 = new Point(200, 300);
      Point archway4 = new Point(150, 200);


      civ.mousePressed(point1, false, true,false);
      civ.mousePressed(point2, false, true,false);
      civ.mousePressed(point3, false, true,false);
      civ.mousePressed(point4, false, true,false);
      civ.mousePressed(point1, false, true,false);
      civ.stopDrawing();
      civ.outlining();
      civ.mousePressed(point4, false, true,false);
      civ.mousePressed(point5, false, true,false);
      civ.mousePressed(point6, false, true,false);
      civ.mousePressed(point1, false, true,false);
      civ.mousePressed(point7, false, true,false);
      civ.mousePressed(point8, false, true,false);
      civ.mousePressed(point2, false, true,false);
      civ.mousePressed(point9, false, true,false);
      civ.mousePressed(point10, false, true,false);
      civ.mousePressed(point3, false, true,false);
      civ.mousePressed(point11, false, true,false);
      civ.mousePressed(point12, false, true,false);
      civ.mousePressed(point4, false, true,false);
      civ.stopDrawing();
      civ.archwayAdd();
      civ.mousePressed(archway1, false, true,false);
      civ.mousePressed(archway2, false, true,false);
      civ.mousePressed(archway3, false, true,false);
      civ.mousePressed(archway4, false, true,false);

      assertEquals(5, civ.getRoomList().size());
      ArrayList<Room> rooms = civ.getRooms();
      Room room1 = rooms.get(0);
      assertTrue(room1.hasPortalInDirection(CardinalDirection.EAST));
      assertTrue(room1.hasPortalInDirection(CardinalDirection.SOUTH));
      assertTrue(room1.hasPortalInDirection(CardinalDirection.NORTH));
      assertTrue(room1.hasPortalInDirection(CardinalDirection.WEST));
      assertFalse(room1.hasPortalInDirection(CardinalDirection.UP));
      assertFalse(room1.hasPortalInDirection(CardinalDirection.DOWN));
      Room room2 = rooms.get(1);
      assertTrue(room2.hasPortalInDirection(CardinalDirection.SOUTH));
      assertFalse(room2.hasPortalInDirection(CardinalDirection.WEST));
      assertFalse(room2.hasPortalInDirection(CardinalDirection.EAST));
      assertFalse(room2.hasPortalInDirection(CardinalDirection.NORTH));
      assertFalse(room2.hasPortalInDirection(CardinalDirection.UP));
      assertFalse(room2.hasPortalInDirection(CardinalDirection.DOWN));
      Room room3 = rooms.get(2);
      assertTrue(room3.hasPortalInDirection(CardinalDirection.WEST));
      assertFalse(room3.hasPortalInDirection(CardinalDirection.SOUTH));
      assertFalse(room3.hasPortalInDirection(CardinalDirection.EAST));
      assertFalse(room3.hasPortalInDirection(CardinalDirection.NORTH));
      assertFalse(room3.hasPortalInDirection(CardinalDirection.UP));
      assertFalse(room3.hasPortalInDirection(CardinalDirection.DOWN));
      Room room4 = rooms.get(3);
      assertTrue(room4.hasPortalInDirection(CardinalDirection.NORTH));
      assertFalse(room4.hasPortalInDirection(CardinalDirection.SOUTH));
      assertFalse(room4.hasPortalInDirection(CardinalDirection.EAST));
      assertFalse(room4.hasPortalInDirection(CardinalDirection.WEST));
      assertFalse(room4.hasPortalInDirection(CardinalDirection.UP));
      assertFalse(room4.hasPortalInDirection(CardinalDirection.DOWN));
      Room room5 = rooms.get(4);
      assertTrue(room5.hasPortalInDirection(CardinalDirection.EAST));
      assertFalse(room5.hasPortalInDirection(CardinalDirection.SOUTH));
      assertFalse(room5.hasPortalInDirection(CardinalDirection.NORTH));
      assertFalse(room5.hasPortalInDirection(CardinalDirection.WEST));
      assertFalse(room5.hasPortalInDirection(CardinalDirection.UP));
      assertFalse(room5.hasPortalInDirection(CardinalDirection.DOWN));
   }
}
