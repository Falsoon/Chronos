package junit;

import civ.CIV;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import pdc.RoomList;
import pdc.Wall;
import pdc.WallType;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static pdc.Constants.PLAYER_X_OFFSET;
import static pdc.Constants.PLAYER_Y_OFFSET;

public class TeleportThroughPortalsTest {
   private CIV civ;
   private Point northPortalPoint = new Point(200,150);
   private Point eastPortalPoint = new Point(300, 200);
   private Point southPortalPoint = new Point(200, 300);
   private Point westPortalPoint = new Point(150, 200);

   @BeforeEach
   public void setup() {
      civ = new CIV();

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

      Point playerStartingPosition = new Point(200,200);

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
      civ.placeStart();
      civ.mousePressed(playerStartingPosition,false,true,false);
      civ.stopPlacingPlayer();
   }

   @AfterEach
   public void tearDown() {
      civ = null;
      RoomList.getInstance().reset();
   }

   @Nested
   class Archways {

      @BeforeEach
      void beforeEach() {
         civ.archwayAdd();
         civ.mousePressed(northPortalPoint,false,true,false);
         civ.mousePressed(eastPortalPoint,false,true,false);
         civ.mousePressed(southPortalPoint,false,true,false);
         civ.mousePressed(westPortalPoint,false,true,false);
         civ.stopDrawing();
         civ.startGame();
      }

      // N - User teleports through East Archway
      @Test
      void testNorthArchway() {
         Point playerPosition = new Point(195+PLAYER_X_OFFSET,135+PLAYER_Y_OFFSET);
         civ.teleportThroughNorthPortal();
         assertEquals(playerPosition,civ.map.player.getPosition());
      }

      // N - User teleports through East Archway
      @Test
      void testSouthArchway() {
         Point playerPosition = new Point(180+PLAYER_X_OFFSET,330-PLAYER_Y_OFFSET);
         civ.teleportThroughSouthPortal();
         assertEquals(playerPosition,civ.map.player.getPosition());
      }

      // N - User teleports through East Archway
      @Test
      void testEastArchway() {
         Point playerPosition = new Point(315+PLAYER_X_OFFSET,210-PLAYER_Y_OFFSET);
         civ.teleportThroughEastPortal();
         assertEquals(playerPosition,civ.map.player.getPosition());
      }

      // N - User teleports through East Archway
      @Test
      void testWestArchway() {
         Point playerPosition = new Point(120+PLAYER_X_OFFSET,195-PLAYER_Y_OFFSET);
         civ.teleportThroughWestPortal();
         assertEquals(playerPosition,civ.map.player.getPosition());
      }

   }

   @Nested
   class ClosedDoors {

      @BeforeEach
      void beforeEach() {
         civ.doorAdd();
         civ.mousePressed(northPortalPoint,false,true,false);
         civ.mousePressed(eastPortalPoint,false,true,false);
         civ.mousePressed(southPortalPoint,false,true,false);
         civ.mousePressed(westPortalPoint,false,true,false);
         civ.stopDrawing();
         civ.startGame();
      }

      // N - User teleports north
      @Test
      void testTeleportNorth() {
         Point playerPosition = new Point(195+PLAYER_X_OFFSET,180-PLAYER_Y_OFFSET);
         civ.teleportThroughNorthPortal();
         assertEquals(playerPosition,civ.map.player.getPosition());
      }

      // N - User teleports south
      @Test
      void testTeleportSouth() {
         Point playerPosition = new Point(180+PLAYER_X_OFFSET,285+PLAYER_Y_OFFSET);
         civ.teleportThroughSouthPortal();
         assertEquals(playerPosition,civ.map.player.getPosition());
      }

      // N - User teleports east
      @Test
      void testTeleportEast() {
         Point playerPosition = new Point(270+PLAYER_X_OFFSET,210-PLAYER_Y_OFFSET);
         civ.teleportThroughEastPortal();
         assertEquals(playerPosition,civ.map.player.getPosition());
      }

      // N - User teleports through west
      @Test
      void testTeleportWest() {
         Point playerPosition = new Point(165+PLAYER_X_OFFSET,195-PLAYER_Y_OFFSET);
         civ.teleportThroughWestPortal();
         assertEquals(playerPosition,civ.map.player.getPosition());
      }

   }

   @Nested
   class OpenDoors {

      @BeforeEach
      void beforeEach() {
         civ.doorAdd();
         civ.mousePressed(northPortalPoint,false,true,false);
         civ.mousePressed(eastPortalPoint,false,true,false);
         civ.mousePressed(southPortalPoint,false,true,false);
         civ.mousePressed(westPortalPoint,false,true,false);
         civ.stopDrawing();
         civ.startGame();

         for(Wall wall: civ.map.mapLayer.wallList){
            wall.setType(WallType.OPEN_DOOR);
         }
      }

      // N - User teleports through East Archway
      @Test
      void testTeleportNorth() {
         Point playerPosition = new Point(195+PLAYER_X_OFFSET,135+PLAYER_Y_OFFSET);
         civ.teleportThroughNorthPortal();
         assertEquals(playerPosition,civ.map.player.getPosition());
      }

      // N - User teleports through East Archway
      @Test
      void testTeleportSouth() {
         Point playerPosition = new Point(180+PLAYER_X_OFFSET,330-PLAYER_Y_OFFSET);
         civ.teleportThroughSouthPortal();
         assertEquals(playerPosition,civ.map.player.getPosition());
      }

      // N - User teleports through East Archway
      @Test
      void testTeleportEast() {
         Point playerPosition = new Point(315+PLAYER_X_OFFSET,210-PLAYER_Y_OFFSET);
         civ.teleportThroughEastPortal();
         assertEquals(playerPosition,civ.map.player.getPosition());
      }

      // N - User teleports through East Archway
      @Test
      void testTeleportWest() {
         Point playerPosition = new Point(120+PLAYER_X_OFFSET,195-PLAYER_Y_OFFSET);
         civ.teleportThroughWestPortal();
         assertEquals(playerPosition,civ.map.player.getPosition());
      }

   }

   @Nested
   class LockedDoors {

      @BeforeEach
      void beforeEach() {
         civ.lockedDoorAdd();
         civ.mousePressed(northPortalPoint,false,true,false);
         civ.mousePressed(eastPortalPoint,false,true,false);
         civ.mousePressed(southPortalPoint,false,true,false);
         civ.mousePressed(westPortalPoint,false,true,false);
         civ.stopDrawing();
         civ.startGame();
      }

      // N - User teleports north
      @Test
      void testTeleportNorth() {
         Point playerPosition = new Point(195+PLAYER_X_OFFSET,180-PLAYER_Y_OFFSET);
         civ.teleportThroughNorthPortal();
         assertEquals(playerPosition,civ.map.player.getPosition());
      }

      // N - User teleports south
      @Test
      void testTeleportSouth() {
         Point playerPosition = new Point(180+PLAYER_X_OFFSET,285+PLAYER_Y_OFFSET);
         civ.teleportThroughSouthPortal();
         assertEquals(playerPosition,civ.map.player.getPosition());
      }

      // N - User teleports east
      @Test
      void testTeleportEast() {
         Point playerPosition = new Point(270+PLAYER_X_OFFSET,210-PLAYER_Y_OFFSET);
         civ.teleportThroughEastPortal();
         assertEquals(playerPosition,civ.map.player.getPosition());
      }

      // N - User teleports through west
      @Test
      void testTeleportWest() {
         Point playerPosition = new Point(165+PLAYER_X_OFFSET,195-PLAYER_Y_OFFSET);
         civ.teleportThroughWestPortal();
         assertEquals(playerPosition,civ.map.player.getPosition());
      }

   }

   @Nested
   class OpenLockedDoors {

      @BeforeEach
      void beforeEach() {
         civ.doorAdd();
         civ.mousePressed(northPortalPoint,false,true,false);
         civ.mousePressed(eastPortalPoint,false,true,false);
         civ.mousePressed(southPortalPoint,false,true,false);
         civ.mousePressed(westPortalPoint,false,true,false);
         civ.stopDrawing();
         civ.startGame();

         for(Wall wall: civ.map.mapLayer.wallList){
            wall.setType(WallType.OPEN_LOCKED_DOOR);
         }
      }

      // N - User teleports through East Archway
      @Test
      void testTeleportNorth() {
         Point playerPosition = new Point(195+PLAYER_X_OFFSET,135+PLAYER_Y_OFFSET);
         civ.teleportThroughNorthPortal();
         assertEquals(playerPosition,civ.map.player.getPosition());
      }

      // N - User teleports through East Archway
      @Test
      void testTeleportSouth() {
         Point playerPosition = new Point(180+PLAYER_X_OFFSET,330-PLAYER_Y_OFFSET);
         civ.teleportThroughSouthPortal();
         assertEquals(playerPosition,civ.map.player.getPosition());
      }

      // N - User teleports through East Archway
      @Test
      void testTeleportEast() {
         Point playerPosition = new Point(315+PLAYER_X_OFFSET,210-PLAYER_Y_OFFSET);
         civ.teleportThroughEastPortal();
         assertEquals(playerPosition,civ.map.player.getPosition());
      }

      // N - User teleports through East Archway
      @Test
      void testTeleportWest() {
         Point playerPosition = new Point(120+PLAYER_X_OFFSET,195-PLAYER_Y_OFFSET);
         civ.teleportThroughWestPortal();
         assertEquals(playerPosition,civ.map.player.getPosition());
      }

   }
}
