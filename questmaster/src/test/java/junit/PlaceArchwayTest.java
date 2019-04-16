package junit;

import civ.CIV;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pdc.RoomList;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlaceArchwayTest {

   private CIV civ;

   @BeforeEach
   public void setup() {
      civ = new CIV();
      civ.map.mapLayer.throwAlerts = false;
   }

   @AfterEach
   public void tearDown() {
      civ = null;
      RoomList.getInstance().reset();
   }

   // N - User draws a room and places an archway on left wall
   @Test
   void testN1() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 75);
      Point point3 = new Point(75, 75);
      Point point4 = new Point(75, 15);
      Point point5 = new Point(15, 45);

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

      civ.archwayAdd();

      civ.mousePressed(point5, false, true,false);

      assertEquals(1, civ.getRoomList().size());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertEquals(6, civ.map.mapLayer.getWallList().size());
   }

   // N - User draws a room and places an archway on right wall
   @Test
   void testN2() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 75);
      Point point3 = new Point(75, 75);
      Point point4 = new Point(75, 15);

      Point point5 = new Point(75, 45);

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

      civ.archwayAdd();

      civ.mousePressed(point5, false, true,false);

      assertEquals(1, civ.getRoomList().size());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertEquals(6, civ.map.mapLayer.getWallList().size());
   }

   // N - User draws a room and places an archway on top wall
   @Test
   void testN3() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 75);
      Point point3 = new Point(75, 75);
      Point point4 = new Point(75, 15);

      Point point5 = new Point(45, 15);

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

      civ.archwayAdd();

      civ.mousePressed(point5, false, true,false);

      assertEquals(1, civ.getRoomList().size());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertEquals(6, civ.map.mapLayer.getWallList().size());
   }

   // N - User draws a room and places an archway on Bottom wall
   @Test
   void testN4() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 75);
      Point point3 = new Point(75, 75);
      Point point4 = new Point(75, 15);

      Point point5 = new Point(45, 75);

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

      civ.archwayAdd();

      civ.mousePressed(point5, false, true,false);

      assertEquals(1, civ.getRoomList().size());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertEquals(6, civ.map.mapLayer.getWallList().size());
   }


   // N - User draws a room and places an archway on a wall splitting two walls
   @Test
   void testN5() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 75);
      Point point3 = new Point(75, 75);
      Point point4 = new Point(75, 15);

      Point point5 = new Point(150, 75);
      Point point6 = new Point(150, 15);


      Point point7 = new Point(105, 75);

      civ.mousePressed(point1, false, true, false);
      civ.mousePressed(point2, false, true, false);
      civ.mousePressed(point3, false, true, false);
      civ.mousePressed(point4, false, true, false);
      civ.mousePressed(point1, false, true, false);

      civ.stopDrawing();
      civ.outlining();

      civ.mousePressed(point3, false, true, false);
      civ.mousePressed(point5, false, true, false);

      civ.stopDrawing();
      civ.outlining();

      civ.mousePressed(point5, false, true, false);
      civ.mousePressed(point6, false, true, false);

      civ.stopDrawing();
      civ.outlining();

      civ.mousePressed(point6, false, true, false);
      civ.mousePressed(point4, false, true, false);

      assertEquals(2, civ.getRoomList().size());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertTrue(civ.map.mapLayer.pointList.contains(point5));
      assertTrue(civ.map.mapLayer.pointList.contains(point6));

      civ.archwayAdd();

      civ.mousePressed(point7, false, true, false);

      assertEquals(2, civ.getRoomList().size());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertTrue(civ.map.mapLayer.pointList.contains(point5));
      assertTrue(civ.map.mapLayer.pointList.contains(point6));
      assertEquals(9, civ.map.mapLayer.getWallList().size());
   }
   //E - User tries to place an archway not on a wall
   @Test
   void testE1() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 75);
      Point point3 = new Point(75, 75);
      Point point4 = new Point(75, 15);

      Point point5 = new Point(10, 0);

      civ.mousePressed(point1, false, true, false);
      civ.mousePressed(point2, false, true, false);
      civ.mousePressed(point3, false, true, false);
      civ.mousePressed(point4, false, true, false);
      civ.mousePressed(point1, false, true, false);

      assertEquals(1, civ.getRoomList().size());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));

      civ.archwayAdd();

      civ.mousePressed(point5, false, true, false);

      assertEquals(1, civ.getRoomList().size());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertEquals(4, civ.map.mapLayer.getWallList().size());
      // assertThrows()
   }

   //E - User tries to place an archway in a corner
   @Test
   void testE2() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 75);
      Point point3 = new Point(75, 75);
      Point point4 = new Point(75, 15);

      Point point5 = new Point(15, 20);

      civ.mousePressed(point1, false, true, false);
      civ.mousePressed(point2, false, true, false);
      civ.mousePressed(point3, false, true, false);
      civ.mousePressed(point4, false, true, false);
      civ.mousePressed(point1, false, true, false);

      assertEquals(1, civ.getRoomList().size());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));

      civ.archwayAdd();

      civ.mousePressed(point5, false, true, false);

      assertEquals(1, civ.getRoomList().size());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertEquals(4, civ.map.mapLayer.getWallList().size());
      // assertThrows()
   }
}


