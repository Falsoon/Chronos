package junit;

import civ.CIV;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pdc.RoomList;

import java.awt.*;

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
      civ.stopDrawing();
      civ.outlining();
      civ.mousePressed(point3, false, true, false);
      civ.mousePressed(point4, false, true, false);
      assertTrue(civ.getRoomList().isEmpty());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertEquals(2,civ.map.mapLayer.wallList.size());
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
      civ.mousePressed(point3, false, true, false);
      assertTrue(civ.getRoomList().isEmpty());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertEquals(2,civ.map.mapLayer.wallList.size());
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
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertFalse(civ.map.mapLayer.pointList.contains(point3));
   }

   // N - Author places two walls with functionally identical line representations
   @Test
   void testN6() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(75, 15);
      civ.mousePressed(point1, false, true, false);
      civ.mousePressed(point2, false, true, false);
      civ.mousePressed(point1, false, true, false);

      assertTrue(civ.getRoomList().isEmpty());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertEquals(1,civ.map.mapLayer.wallList.size());
   }

   // N - Author places a wall, then places a second collinear wall that is longer than the first and shares an endpoint
   @Test
   void testN7() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(75, 15);
      Point point3 = new Point(90,15);
      civ.mousePressed(point1, false, true, false);
      civ.mousePressed(point2, false, true, false);
      civ.stopDrawing();
      civ.outlining();
      civ.mousePressed(point1, false, true, false);
      civ.mousePressed(point3, false, true, false);

      assertTrue(civ.getRoomList().isEmpty());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertEquals(1,civ.map.mapLayer.wallList.size());
   }

   // N - Author places a wall, then places a second collinear wall that is shorter than the first and shares an endpoint
   @Test
   void testN8() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(75, 15);
      Point point3 = new Point(90,15);
      civ.mousePressed(point1, false, true, false);
      civ.mousePressed(point3, false, true, false);
      civ.stopDrawing();
      civ.outlining();
      civ.mousePressed(point1, false, true, false);
      civ.mousePressed(point2, false, true, false);

      assertTrue(civ.getRoomList().isEmpty());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertEquals(1,civ.map.mapLayer.wallList.size());
   }

   // N - Author places a wall, then places a second collinear wall that contains the first but does not share an endpoint
   @Test
   void testN9() {
      civ.outlining();
      Point point1 = new Point(30, 15);
      Point point2 = new Point(75, 15);
      Point point3 = new Point(15,15);
      Point point4 = new Point(90,15);
      civ.mousePressed(point1, false, true, false);
      civ.mousePressed(point2, false, true, false);
      civ.stopDrawing();
      civ.outlining();
      civ.mousePressed(point3, false, true, false);
      civ.mousePressed(point4, false, true, false);

      assertTrue(civ.getRoomList().isEmpty());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertEquals(1,civ.map.mapLayer.wallList.size());
   }

   // N - Author places a wall, then places a second collinear wall that is contained by the first but does not share an endpoint
   @Test
   void testN10() {
      civ.outlining();
      Point point1 = new Point(30, 15);
      Point point2 = new Point(75, 15);
      Point point3 = new Point(15,15);
      Point point4 = new Point(90,15);
      civ.mousePressed(point3, false, true, false);
      civ.mousePressed(point4, false, true, false);
      civ.stopDrawing();
      civ.outlining();
      civ.mousePressed(point1, false, true, false);
      civ.mousePressed(point2, false, true, false);

      assertTrue(civ.getRoomList().isEmpty());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertEquals(1,civ.map.mapLayer.wallList.size());
   }

   // N - Author places a wall, then places a second collinear wall that partially overlaps the first
   @Test
   void testN11() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(45, 15);
      Point point3 = new Point(30,15);
      Point point4 = new Point(90,15);
      civ.mousePressed(point1, false, true, false);
      civ.mousePressed(point2, false, true, false);
      civ.stopDrawing();
      civ.outlining();
      civ.mousePressed(point3, false, true, false);
      civ.mousePressed(point4, false, true, false);

      assertTrue(civ.getRoomList().isEmpty());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertEquals(1,civ.map.mapLayer.wallList.size());
   }

   // N - Author places a wall, then places a second collinear wall that is partially overlapped by the first
   @Test
   void testN12() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(45, 15);
      Point point3 = new Point(30,15);
      Point point4 = new Point(90,15);
      civ.mousePressed(point3, false, true, false);
      civ.mousePressed(point4, false, true, false);
      civ.stopDrawing();
      civ.outlining();
      civ.mousePressed(point1, false, true, false);
      civ.mousePressed(point2, false, true, false);

      assertTrue(civ.getRoomList().isEmpty());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertEquals(1,civ.map.mapLayer.wallList.size());
   }

   // N - Author places a wall, then places a second collinear wall that shares one endpoint
   @Test
   void testN13() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(45, 15);
      Point point3 = new Point(60,15);
      civ.mousePressed(point1, false, true, false);
      civ.mousePressed(point2, false, true, false);
      civ.mousePressed(point3, false, true, false);

      assertTrue(civ.getRoomList().isEmpty());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertEquals(1,civ.map.mapLayer.wallList.size());
   }

   // N - Author places 2 perpendicular walls that share an endpoint wall, then places a third wall that is collinear
   // to one of the other walls and shares the common endpoint
   @Test
   void testN14() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(45, 15);
      Point point3 = new Point(45,30);
      Point point4 = new Point(60,15);
      civ.mousePressed(point1, false, true, false);
      civ.mousePressed(point2, false, true, false);
      civ.mousePressed(point3, false, true, false);
      civ.stopDrawing();
      civ.outlining();
      civ.mousePressed(point4, false, true, false);
      civ.mousePressed(point3, false, true, false);
      assertTrue(civ.getRoomList().isEmpty());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertEquals(3,civ.map.mapLayer.wallList.size());
   }

   // N - Author places 2 perpendicular walls that share an endpoint , then places a third wall that is collinear to
   // one of the other walls and overlaps it
   @Test
   void testN15() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(45, 15);
      Point point3 = new Point(45,30);
      Point point4 = new Point(60,15);
      Point point5 = new Point(30,15);
      civ.mousePressed(point1, false, true, false);
      civ.mousePressed(point2, false, true, false);
      civ.mousePressed(point3, false, true, false);
      civ.stopDrawing();
      civ.outlining();
      civ.mousePressed(point4, false, true, false);
      civ.mousePressed(point5, false, true, false);
      assertTrue(civ.getRoomList().isEmpty());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point2));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertEquals(3,civ.map.mapLayer.wallList.size());
   }

   // N - Author places 2 perpendicular walls that intersect but do not share an endpoint when initially drawn, then
   // places a third wall that is collinear to one of the other walls and shares the generated intersection point
   @Test
   void testN16() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(45, 15);
      Point point3 = new Point(30,15);
      Point point4 = new Point(30,30);
      Point point5 = new Point(60,15);
      civ.mousePressed(point1, false, true, false);
      civ.mousePressed(point2, false, true, false);
      civ.stopDrawing();
      civ.outlining();
      civ.mousePressed(point3, false, true, false);
      civ.mousePressed(point4, false, true, false);
      civ.stopDrawing();
      civ.outlining();
      civ.mousePressed(point5, false, true, false);
      civ.mousePressed(point3, false, true, false);
      assertTrue(civ.getRoomList().isEmpty());
      assertTrue(civ.map.isCreating());
      assertTrue(civ.map.mapLayer.pointList.contains(point1));
      assertTrue(civ.map.mapLayer.pointList.contains(point3));
      assertTrue(civ.map.mapLayer.pointList.contains(point4));
      assertTrue(civ.map.mapLayer.pointList.contains(point5));
      assertEquals(3,civ.map.mapLayer.wallList.size());
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
