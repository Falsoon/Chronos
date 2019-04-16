package junit;

import civ.CIV;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pdc.RoomList;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StairsTest {
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
   void testPlaceStairs() {
      civ.stairsAdd();
      civ.mousePressed(new Point(15,15), false, true, false);
      civ.mousePressed(new Point(75,75), false, true, false);

      assertEquals(2, civ.map.mapLayer.stairList.size());
      assertEquals(civ.map.mapLayer.stairList.get(0).linkedStair, civ.map.mapLayer.stairList.get(1));
      assertEquals(civ.map.mapLayer.stairList.get(1).linkedStair, civ.map.mapLayer.stairList.get(0));
   }

   // N -  User clicks opaque wall option, then clicks two points on map to draw a line
   @Test
   void testPlaceStairsThroughDrawing() {
      civ.stairsAdd();
      civ.mousePressed(new Point(15,15), false, true, false);

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
      civ.mousePressed(new Point(15,15), false, true, false);
      civ.mousePressed(new Point(75,75), false, true, false);

      civ.mousePressed(new Point(15,45), false, true, false);
      civ.mousePressed(new Point(75,45), false, true, false);

      civ.stopDrawing();
      civ.delete();
      civ.mousePressed(new Point(15,15), false, true, false);
      assertEquals(2, civ.map.mapLayer.stairList.size());
   }

   @Test
   void testMoveOnStairs() {
      civ.stairsAdd();
      civ.mousePressed(new Point(15,15), false, true, false);
      civ.mousePressed(new Point(75,75), false, true, false);

      civ.placeStart();
      civ.mousePressed(new Point(15,30), false, true, false);

      civ.startGame();
      civ.goUp();

      assertEquals(75 - 4, civ.map.player.getPosition().y);
   }
}