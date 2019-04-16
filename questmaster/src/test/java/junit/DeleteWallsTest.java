package junit;

import civ.CIV;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pdc.RoomList;
import pdc.Wall;
import pdc.WallType;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteWallsTest {
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

    // N - User draws opaque wall then deletes it
    @Test
    void testN1() {
        civ.outlining();
        Point point1 = new Point(15,15);
        Point point2 = new Point(30, 15);
        Point point3 = new Point(20, 15);
        civ.mousePressed(point1, false, true, false);
        civ.mousePressed(point2, false, true, false);
        civ.stopDrawing();
        civ.delete();
        civ.mousePressed(point3,false,true,false);
        assertTrue(civ.map.isDeleting());
        assertTrue(civ.getRoomList().isEmpty());
        assertEquals(0, civ.map.mapLayer.getWallList().size());
    }

    // N -  User draws 2 lines, then deletes one
    @Test
    void testN2() {
        civ.outlining();
        Point point1 = new Point(15, 15);
        Point point2 = new Point(30, 15);
        Point point3 = new Point(30,90);
        Point point4 = new Point(20,15);
        civ.mousePressed(point1, false, true, false);
        civ.mousePressed(point2, false, true, false);
        civ.mousePressed(point3, false, true, false);
        civ.stopDrawing();
        civ.delete();
        civ.mousePressed(point4,false,true,false);
        assertTrue(civ.map.isDeleting());
        assertTrue(civ.getRoomList().isEmpty());
        assertEquals(1, civ.map.mapLayer.getWallList().size());
    }

    // N -  User draws a wall, draws an archway on it, then deletes the archway
    @Test
    void testN3() {
        civ.outlining();
        Point point1 = new Point(15, 15);
        Point point2 = new Point(150, 15);
        Point point3 = new Point(75,15);
        Point point4 = new Point(80,15);
        civ.mousePressed(point1, false, true, false);
        civ.mousePressed(point2, false, true, false);
        civ.archwayAdd();
        civ.mousePressed(point3,false,true,false);

        ArrayList<Wall> wallList = civ.map.mapLayer.getWallList();
        boolean hasArchway = false;
        for(Wall wall:wallList){
            if(wall.getWallType().equals(WallType.ARCHWAY)){
                hasArchway = true;
            }
        }
        assertTrue(hasArchway);
        assertEquals(3,wallList.size());
        civ.delete();
        civ.mousePressed(point4,false,true,false);
        assertTrue(civ.map.isDeleting());
        assertTrue(civ.getRoomList().isEmpty());
        wallList = civ.map.mapLayer.getWallList();
        hasArchway = false;
        for(Wall wall:wallList){
            if(wall.getWallType().equals(WallType.ARCHWAY)){
                hasArchway = true;
            }
        }
        assertFalse(hasArchway);
        assertEquals(3, civ.map.mapLayer.getWallList().size());
    }

    // N -  User draws a wall, draws a door on it, then deletes the door
    @Test
    void testN4() {
        civ.outlining();
        Point point1 = new Point(15, 15);
        Point point2 = new Point(150, 15);
        Point point3 = new Point(75,15);
        Point point4 = new Point(80,15);
        civ.mousePressed(point1, false, true, false);
        civ.mousePressed(point2, false, true, false);
        civ.doorAdd();
        civ.mousePressed(point3,false,true,false);

        ArrayList<Wall> wallList = civ.map.mapLayer.getWallList();
        boolean hasDoor = false;
        for(Wall wall:wallList){
            if(wall.getWallType().equals(WallType.CLOSED_DOOR)||wall.getWallType().equals(WallType.LOCKED_DOOR)||wall.getWallType().equals(WallType.OPEN_LOCKED_DOOR)){
                hasDoor = true;
            }
        }
        assertTrue(hasDoor);
        assertEquals(3,wallList.size());
        civ.delete();
        civ.mousePressed(point4,false,true,false);
        assertTrue(civ.map.isDeleting());
        assertTrue(civ.getRoomList().isEmpty());
        wallList = civ.map.mapLayer.getWallList();
        hasDoor = false;
        for(Wall wall:wallList){
            if(wall.getWallType().equals(WallType.ARCHWAY)){
                hasDoor = true;
            }
        }
        assertFalse(hasDoor);
        assertEquals(3, civ.map.mapLayer.getWallList().size());
    }

    // N -  User draws a room, then deletes one of its walls
    @Test
    void testN5() {
        civ.outlining();
        Point point1 = new Point(15, 15);
        Point point2 = new Point(150, 15);
        Point point3 = new Point(150,150);
        Point point4 = new Point(15,150);
        Point point5 = new Point(130,150);
        civ.mousePressed(point1, false, true, false);
        civ.mousePressed(point2, false, true, false);
        civ.mousePressed(point3, false, true, false);
        civ.mousePressed(point4, false, true, false);
        civ.mousePressed(point1, false, true, false);
        assertEquals(1,civ.getRoomList().size());
        assertEquals(4,civ.map.mapLayer.getWallList().size());
        civ.delete();
        civ.mousePressed(point5,false,true,false);
        assertTrue(civ.map.isDeleting());
        assertTrue(civ.getRoomList().isEmpty());
        assertEquals(3, civ.map.mapLayer.getWallList().size());
    }

    // N -  User draws a room, adds an archway, then deletes the archway
    @Test
    void testN6() {
        civ.outlining();
        Point point1 = new Point(15, 15);
        Point point2 = new Point(150, 15);
        Point point3 = new Point(150,150);
        Point point4 = new Point(15,150);
        Point point5 = new Point(45,150);
        Point point6 = new Point(40,150);
        civ.mousePressed(point1, false, true, false);
        civ.mousePressed(point2, false, true, false);
        civ.mousePressed(point3, false, true, false);
        civ.mousePressed(point4, false, true, false);
        civ.mousePressed(point1, false, true, false);
        assertEquals(1,civ.getRoomList().size());
        assertEquals(4,civ.map.mapLayer.getWallList().size());

        civ.archwayAdd();
        civ.mousePressed(point5,false,true,false);
        assertEquals(1,civ.getRoomList().size());
        assertEquals(6,civ.map.mapLayer.getWallList().size());
        ArrayList<Wall> wallList = civ.map.mapLayer.getWallList();
        boolean hasArchway = false;
        for(Wall wall:wallList){
            if(wall.getWallType().equals(WallType.ARCHWAY)){
                hasArchway = true;
            }
        }
        assertTrue(hasArchway);

        civ.delete();
        civ.mousePressed(point6,false,true,false);
        assertTrue(civ.map.isDeleting());
        assertEquals(1,civ.getRoomList().size());
        assertEquals(6, civ.map.mapLayer.getWallList().size());
    }

}
