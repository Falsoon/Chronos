package junit;

import civ.CIV;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pdc.RoomList;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pdc.Constants.PLAYER_X_OFFSET;
import static pdc.Constants.PLAYER_Y_OFFSET;

public class MoveLockDoorTest {
   private CIV civ;

   @BeforeEach
   public void setup() {
      civ = new CIV();
      civ.map.mapLayer.throwAlerts = false;

      Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 195);
      Point point3 = new Point(195, 195);
      Point point4 = new Point(195, 15);
      Point point5 = new Point(135, 195);
      Point point6 = new Point(135, 15);
      Point point7 = new Point(135, 120);

      civ.outlining();
      civ.mousePressed(point1, false, true,false);
      civ.mousePressed(point2, false, true,false);
      civ.mousePressed(point3, false, true,false);
      civ.mousePressed(point4, false, true,false);
      civ.mousePressed(point1, false, true,false);
      civ.stopDrawing();
      civ.outlining();
      civ.mousePressed(point5, false, true,false);
      civ.mousePressed(point6, false, true,false);

      civ.lockedDoorAdd();
      civ.mousePressed(point7, false, true,false);

      assertEquals(2,civ.getRooms().size());
   }

   @AfterEach
   public void tearDown() {
      civ = null;
      RoomList.getInstance().reset();
   }

   // N - User collides with a locked door
   @Test
   void collideLocked() {
      civ.placeStart();
      civ.mousePressed(new Point(105,120), false, true, false);

      civ.startGame();
      civ.goEast();

      assertEquals(105+PLAYER_X_OFFSET, civ.map.player.getPosition().x);
      assertEquals(120-PLAYER_Y_OFFSET, civ.map.player.getPosition().y);
   }

   // N - User collides with a locked door with key and goes through
   @Test
   void openKey() {
      civ.placeStart();
      civ.mousePressed(new Point(105,90), false, true, false);
      civ.keyAdd();
      civ.mousePressed(new Point(105,105), false, true, false);

      civ.startGame();
      civ.goSouth();
      civ.pickUpKey();
      civ.goSouth();
      civ.goEast();
      civ.goEast();
      civ.lockDoor();
      civ.goEast();
      civ.goEast();
      civ.goEast();

      assertEquals(150+PLAYER_X_OFFSET, civ.map.player.getPosition().x);
      assertEquals(120-PLAYER_Y_OFFSET, civ.map.player.getPosition().y);
   }

   // N - User collides with a locked door with no key and cannot go through
   @Test
   void openNoKey() {
      civ.placeStart();
      civ.mousePressed(new Point(105,120), false, true, false);
      civ.startGame();
      civ.goEast();
      civ.lockDoor();
      civ.goEast();
      civ.goEast();
      civ.goEast();

      assertEquals(105+PLAYER_X_OFFSET, civ.map.player.getPosition().x);
      assertEquals(120-PLAYER_Y_OFFSET, civ.map.player.getPosition().y);
   }

   // N - User goes through then locked the door
   @Test
   void lockKey() {
      civ.placeStart();
      civ.mousePressed(new Point(105,90), false, true, false);
      civ.keyAdd();
      civ.mousePressed(new Point(105,105), false, true, false);

      civ.startGame();
      civ.goSouth();
      civ.pickUpKey();
      civ.goSouth();
      civ.goEast();
      civ.goEast();
      civ.lockDoor();
      civ.goEast();
      civ.goEast();
      civ.goEast();
      civ.goEast();
      civ.goWest();
      civ.lockDoor();
      civ.goWest();

      assertEquals(150+PLAYER_X_OFFSET, civ.map.player.getPosition().x);
      assertEquals(120-PLAYER_Y_OFFSET, civ.map.player.getPosition().y);
   }

   // N - User tries to open without a key
   @Test
   void lockNoKey() {
      civ.placeStart();
      civ.mousePressed(new Point(105,90), false, true, false);
      civ.keyAdd();
      civ.mousePressed(new Point(105,105), false, true, false);

      civ.startGame();
      civ.goSouth();
      civ.pickUpKey();
      civ.goSouth();
      civ.goEast();
      civ.goEast();
      civ.lockDoor();
      civ.goEast();
      civ.goEast();
      civ.goEast();
      civ.goEast();
      civ.dropKey();
      civ.goWest();
      civ.lockDoor();
      civ.goWest();

      assertEquals(135+PLAYER_X_OFFSET, civ.map.player.getPosition().x);
      assertEquals(120-PLAYER_Y_OFFSET, civ.map.player.getPosition().y);
   }

   // N - User picks up then drops a key and tries to open
   @Test
   void  dropKey() {
      civ.placeStart();
      civ.mousePressed(new Point(105,90), false, true, false);
      civ.keyAdd();
      civ.mousePressed(new Point(105,105), false, true, false);

      civ.startGame();
      civ.goSouth();
      civ.pickUpKey();
      civ.dropKey();
      civ.goSouth();
      civ.goEast();
      civ.lockDoor();

      assertEquals(105+PLAYER_X_OFFSET, civ.map.player.getPosition().x);
      assertEquals(120-PLAYER_Y_OFFSET, civ.map.player.getPosition().y);
   }

   // N - User collides with a locked door ,opens it, then locks it again.
   @Test
   void openLock() {
      civ.placeStart();
      civ.mousePressed(new Point(105,90), false, true, false);
      civ.keyAdd();
      civ.mousePressed(new Point(105,105), false, true, false);

      civ.startGame();
      civ.goSouth();
      civ.pickUpKey();
      civ.goSouth();
      civ.goEast();
      civ.goEast();
      civ.lockDoor();
      civ.lockDoor();
      civ.goEast();

      assertEquals(105+PLAYER_X_OFFSET, civ.map.player.getPosition().x);
      assertEquals(120-PLAYER_Y_OFFSET, civ.map.player.getPosition().y);
   }
}
