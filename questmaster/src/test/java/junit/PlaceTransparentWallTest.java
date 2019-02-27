package junit;

import civ.CIV;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pdc.RoomList;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlaceTransparentWallTest {

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

   // N - User draws a room and splits it with a transparent line
   @Test
   void testN1() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 75);
      Point point3 = new Point(75, 75);
      Point point4 = new Point(75, 15);

      Point point5 = new Point(15,60);
      Point point6 = new Point(75,60);

      civ.mousePressed(point1, false, true, false);
      civ.mousePressed(point2, false, true, false);

      civ.mousePressed(point2, false, true, false);
      civ.mousePressed(point3, false, true, false);

      civ.mousePressed(point3, false, true, false);
      civ.mousePressed(point4, false, true, false);

      civ.mousePressed(point4, false, true, false);
      civ.mousePressed(point1, false, true, false);

      assertEquals(1, civ.getRoomList().size());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));

      civ.walling();

      civ.mousePressed(point5,false,true, false);
      civ.mousePressed(point6,false,true, false);

      assertEquals(2, civ.getRoomList().size());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertTrue(civ.map.mapLayer.pointList.contains(point5));
      assertTrue(civ.map.mapLayer.pointList.contains(point6));
   }

   // N -  Right mouse button stops drawing
   @Test
   void testN2() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 75);
      Point point3 = new Point(75, 75);
      Point point4 = new Point(75, 15);

      Point point5 = new Point(15,60);
      Point point6 = new Point(75,60);

      civ.mousePressed(point1, false, true, false);
      civ.mousePressed(point2, false, true, false);

      civ.mousePressed(point2, false, true, false);
      civ.mousePressed(point3, false, true, false);

      civ.mousePressed(point3, false, true, false);
      civ.mousePressed(point4, false, true, false);

      civ.mousePressed(point4, false, true, false);
      civ.mousePressed(point1, false, true, false);

      assertEquals(1, civ.getRoomList().size());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));

      civ.walling();

      civ.mousePressed(point5,false,true, false);
      civ.mousePressed(point6,false,false, true);
      civ.mousePressed(point6,false,true, false);

      assertEquals(1, civ.getRoomList().size());
      assertFalse(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertFalse(civ.map.mapLayer.pointList.contains(point5));
      assertFalse(civ.map.mapLayer.pointList.contains(point6));
   }

   //E - User draws an invalid transparent line
   @Test
   void testE1() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 75);
      Point point3 = new Point(75, 75);
      Point point4 = new Point(75, 15);

      Point point5 = new Point(15,60);
      Point point6 = new Point(100,100);

      civ.mousePressed(point1, false, true, false);
      civ.mousePressed(point2, false, true, false);

      civ.mousePressed(point2, false, true, false);
      civ.mousePressed(point3, false, true, false);

      civ.mousePressed(point3, false, true, false);
      civ.mousePressed(point4, false, true, false);

      civ.mousePressed(point4, false, true, false);
      civ.mousePressed(point1, false, true, false);

      assertEquals(1, civ.getRoomList().size());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));

      civ.walling();

      civ.mousePressed(point5,false,true, false);
      civ.mousePressed(point6,false,true, false);

      assertEquals(1, civ.getRoomList().size());
      assertFalse(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertFalse(civ.map.mapLayer.pointList.contains(point5));
      assertFalse(civ.map.mapLayer.pointList.contains(point6));
   }

   //E - User draws an invalid transparent line where the second click is invalid but not the first
   @Test
   void testE2() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 75);
      Point point3 = new Point(75, 75);
      Point point4 = new Point(75, 15);

      Point point5 = new Point(15,30);
      Point point6 = new Point(100,100);

      civ.mousePressed(point1, false, true, false);
      civ.mousePressed(point2, false, true, false);

      civ.mousePressed(point2, false, true, false);
      civ.mousePressed(point3, false, true, false);

      civ.mousePressed(point3, false, true, false);
      civ.mousePressed(point4, false, true, false);

      civ.mousePressed(point4, false, true, false);
      civ.mousePressed(point1, false, true, false);

      assertEquals(1, civ.getRoomList().size());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));

      civ.walling();

      civ.mousePressed(point5,false,true, false);
      civ.mousePressed(point6,false,true, false);

      assertEquals(1, civ.getRoomList().size());
      assertFalse(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertFalse(civ.map.mapLayer.pointList.contains(point5));
      assertFalse(civ.map.mapLayer.pointList.contains(point6));
   }

   //E - User attempts to draw a transparent line in empty space
   @Test
   void testE3() {
      Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 75);

      civ.walling();

      civ.mousePressed(point1, false, true, false);
      civ.mousePressed(point2, false, true, false);


      assertTrue(civ.getRoomList().isEmpty());
      assertFalse(civ.map.isCreating());
      assertFalse(civ.map.mapLayer.pointList.contains(point1));
      assertFalse(civ.map.mapLayer.pointList.contains(point2));
   }
}
