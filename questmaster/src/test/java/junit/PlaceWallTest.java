package junit;

import civ.CIV;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pdc.RoomList;

import java.awt.*;
import java.awt.geom.Point2D;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlaceWallTest {
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

   // N - User clicks opaque wall option, then clicks two points on map to draw a line
   @Test
   void testN1() {
      civ.outlining();
      Point point1 = new Point(15,15);
      Point point2 = new Point(30, 15);
      civ.mousePressed(point1, false, true, false);
      civ.mousePressed(point2, false, true, false);
      assertTrue(civ.map.isCreating());
      assertTrue(civ.getRoomList().isEmpty());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
   }

   // N -  User clicks opaque wall option, then clicks two points on map to draw a line
   @Test
   void testN2() {
      civ.outlining();
      civ.mousePressed(new Point(15,15), false, true, false);
      civ.mousePressed(new Point(75, 15), false, true, false);
      civ.mousePressed(new Point(90, 90), false, true, false);
      civ.mousePressed(new Point(90, 105), false, true, false);
      assertTrue(civ.getRoomList().isEmpty());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(new Point(15,15)));
      assertTrue(civ.map.mapLayer.pointList.contains(new Point(75, 15)));
   }

   // N -  User clicks opaque wall option, then clicks four points on map to draw 2 lines
   @Test
   void testN3() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(75, 15);
      Point point3 = new Point(90,90);
      Point point4 = new Point(90,105);
      civ.mousePressed(point1, false, true, false);
      civ.mousePressed(point2, false, true, false);
      civ.mousePressed(point3, false, true, false);
      civ.mousePressed(point4, false, true, false);
      assertTrue(civ.getRoomList().isEmpty());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
   }

   // N -  User draws 2 lines that intersect
   @Test
   void testN4() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(75, 15);
      Point point3 = new Point(75,30);
      civ.mousePressed(point1, false, true, false);
      civ.mousePressed(point2, false, true, false);
      civ.mousePressed(point2, false, true, false);
      civ.mousePressed(point3, false, true, false);
      assertTrue(civ.getRoomList().isEmpty());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
   }

   // N -  Right mouse button stops drawing
   @Test
   void testN5() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(75, 15);
      Point point3 = new Point(75,30);
      civ.mousePressed(point1, false, true, false);
      civ.mousePressed(point2, false, true, false);
      civ.mousePressed(point2, false, false, true);
      civ.mousePressed(point3, false, true, false);
      assertTrue(civ.getRoomList().isEmpty());
      assertFalse(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertFalse(civ.map.mapLayer.pointList.contains(point3));
   }

   // E - User clicks points on map without pressing draw button
   @Test
   void testE1() {
      civ.mousePressed(new Point(15,15), false, true, false);
      civ.mousePressed(new Point(15, 30), false, true, false);
      assertTrue(civ.getRoomList().isEmpty());
      assertFalse(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.isEmpty());
   }

   // B - User draws a long wall
   @Test
   void testB1() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(Integer.MAX_VALUE, 15);
      civ.mousePressed(point1, false, true, false);
      civ.mousePressed(point2, false, true, false);
      assertTrue(civ.getRoomList().isEmpty());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
   }

   // B - User draws a long wall
   @Test
   void testB2() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(15, Integer.MAX_VALUE);
      civ.mousePressed(point1, false, true, false);
      civ.mousePressed(point2, false, true, false);
      assertTrue(civ.getRoomList().isEmpty());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
   }

}
