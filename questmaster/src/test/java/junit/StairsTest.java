package junit;

import civ.CIV;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pdc.RoomList;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static pdc.Constants.PLAYER_X_OFFSET;
import static pdc.Constants.PLAYER_Y_OFFSET;

public class StairsTest {
   private CIV civ;

   @BeforeEach
   public void setup() {
      civ = new CIV();

      //create 2 rooms
      Point point1 = new Point(15,15);
      Point point2 = new Point(150,15);
      Point point3 = new Point(150,150);
      Point point4 = new Point(15,150);
      Point point5 = new Point(300,15);
      Point point6 = new Point(450,15);
      Point point7 = new Point(450,150);
      Point point8 = new Point(300,150);
      civ.outlining();
      civ.mousePressed(point1, false, true, false);
      civ.mousePressed(point2, false, true, false);
      civ.mousePressed(point3, false, true, false);
      civ.mousePressed(point4, false, true, false);
      civ.mousePressed(point1, false, true, false);
      civ.stopDrawing();
      civ.outlining();
      civ.mousePressed(point5, false, true, false);
      civ.mousePressed(point6, false, true, false);
      civ.mousePressed(point7, false, true, false);
      civ.mousePressed(point8, false, true, false);
      civ.mousePressed(point5, false, true, false);

      assertEquals(2,civ.getRooms().size());
   }

   @AfterEach
   public void tearDown() {
      civ = null;
      RoomList.getInstance().reset();
   }

   // N - User places a pair of stairs
   @Test
   void testPlaceStairs() {
      civ.stairsAdd();
      civ.mousePressed(new Point(30,30), false, true, false);
      civ.mousePressed(new Point(330,30), false, true, false);

      assertEquals(2, civ.map.mapLayer.stairList.size());
      assertEquals(civ.map.mapLayer.stairList.get(0).linkedStair, civ.map.mapLayer.stairList.get(1));
      assertEquals(civ.map.mapLayer.stairList.get(1).linkedStair, civ.map.mapLayer.stairList.get(0));
   }

   // N -  User clicks opaque wall option, then clicks two points on map to draw a line
   @Test
   void testPlaceStairsThroughDrawing() {
      civ.stairsAdd();
      civ.mousePressed(new Point(30,30), false, true, false);

      civ.outlining();
      civ.mousePressed(new Point(90,90), false, true, false);
      civ.mousePressed(new Point(90,105), false, true, false);
      
      civ.stairsAdd();
      civ.mousePressed(new Point(75,75), false, true, false);

      assertEquals(2, civ.map.mapLayer.stairList.size());
      assertEquals(civ.map.mapLayer.stairList.get(0).linkedStair, civ.map.mapLayer.stairList.get(1));
      assertEquals(civ.map.mapLayer.stairList.get(1).linkedStair, civ.map.mapLayer.stairList.get(0));
   }

   @Test
   void testDeleteStairs() {
      civ.stairsAdd();
      civ.mousePressed(new Point(30,30), false, true, false);
      civ.mousePressed(new Point(330,30), false, true, false);

      civ.mousePressed(new Point(360,30), false, true, false);
      civ.mousePressed(new Point(60,30), false, true, false);

      civ.stopDrawing();
      civ.delete();
      civ.mousePressed(new Point(30,30), false, true, false);
      assertEquals(2, civ.map.mapLayer.stairList.size());
   }

   @Test
   void testMoveOnStairs() {
      civ.stairsAdd();
      civ.mousePressed(new Point(45,45), false, true, false);
      civ.mousePressed(new Point(345,45), false, true, false);

      civ.placeStart();
      civ.mousePressed(new Point(45,60), false, true, false);

      civ.startGame();
      civ.goNorth();

      assertEquals(345+PLAYER_X_OFFSET, civ.map.player.getPosition().x);
      assertEquals(45-PLAYER_Y_OFFSET, civ.map.player.getPosition().y);

   }

   @Test
   void testTeleportDownStairs() {
      civ.stairsAdd();
      civ.mousePressed(new Point(45,45), false, true, false);
      civ.mousePressed(new Point(345,45), false, true, false);

      civ.placeStart();
      civ.mousePressed(new Point(45,60), false, true, false);

      civ.startGame();
      civ.teleportThroughDownPortal();

      assertEquals(345+PLAYER_X_OFFSET, civ.map.player.getPosition().x);
      assertEquals(45-PLAYER_Y_OFFSET, civ.map.player.getPosition().y);

   }

   @Test
   void testTeleportUpStairs() {
      civ.stairsAdd();
      civ.mousePressed(new Point(45,45), false, true, false);
      civ.mousePressed(new Point(345,45), false, true, false);

      civ.placeStart();
      civ.mousePressed(new Point(345,60), false, true, false);

      civ.startGame();
      civ.teleportThroughUpPortal();

      assertEquals(45+PLAYER_X_OFFSET, civ.map.player.getPosition().x);
      assertEquals(45-PLAYER_Y_OFFSET, civ.map.player.getPosition().y);

   }
}
