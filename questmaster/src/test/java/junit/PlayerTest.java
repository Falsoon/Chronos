package junit;

import civ.CIV;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pdc.RoomList;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerTest {

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

   // N - User draws a room and places an archway on left wall
   @Test
   void testN1() {
      civ.outlining();
      Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 75);
      Point point3 = new Point(75, 75);
      Point point4 = new Point(75, 15);

      Point playerPoint = new Point(30, 30);

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

      civ.placeStart();

      civ.mousePressed(playerPoint, false, true);

      assertTrue(civ.placedPlayer());
   }
}