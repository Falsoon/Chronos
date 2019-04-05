package junit;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import civ.CIV;
import pdc.Room;
import pdc.RoomList;
import hic.ButtonFactory;
import hic.AuthorWindow;

/**
 * Attempting to test save and restore quickly
 * 
 * @author Ryan
 *
 */
class SaveRestoreTest {
   private CIV civ;
   private AuthorWindow aw;
   private ButtonFactory bf;

	@BeforeEach
	public void setup() {
      civ = new CIV();
      aw = new AuthorWindow();
	}

	@AfterEach
	public void tearDown() {
      civ = null;
      aw = null;
		RoomList.getInstance().reset();
   }
   
   @Test
	void testOneRoom() {
      civ.outlining();
		Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 75);
      Point point3 = new Point(75, 75);
      Point point4 = new Point(75, 15);

		civ.mousePressed(point1, false, true,false);
		civ.mousePressed(point2, false, true,false);
		civ.mousePressed(point3, false, true,false);
      civ.mousePressed(point4, false, true,false);
      civ.mousePressed(point1, false, true,false);

      assertEquals(1, civ.getRoomList().size());
      
      civ.save();
      civ.clear();
      assertEquals(0, civ.getRoomList().size());
      
      civ.restore();
      assertEquals(1, civ.getRoomList().size());
   }

   // @Test - fix in a hot sec
   void testWithDescription() {
      bf = new ButtonFactory(aw);
      civ.outlining();
		Point point1 = new Point(15, 15);
      Point point2 = new Point(15, 75);
      Point point3 = new Point(75, 75);
      Point point4 = new Point(75, 15);
      Point mid = new Point(50, 50);

		civ.mousePressed(point1, false, true,false);
		civ.mousePressed(point2, false, true,false);
		civ.mousePressed(point3, false, true,false);
      civ.mousePressed(point4, false, true,false);
      civ.mousePressed(point1, false, true,false);

      civ.stopDrawing();
		civ.mousePressed(mid, false, true, false);
		bf.rdi.titleText.setText("the bathroom");
		bf.rdi.descArea.setText("the place people go when they got to use the bathroom");

		Room room = bf.rdi.formCiv.room;
		assertEquals("the bathroom", room.title );
		assertEquals("the place people go when they got to use the bathroom", room.desc );
      assertEquals(1, civ.getRoomList().size());
      
      civ.save();
      civ.clear();
      assertEquals(0, civ.getRoomList().size());
      assertEquals("", bf.rdi.titleText.getText());
      assertEquals("", bf.rdi.descArea.getText());
      
      civ.restore();
      civ.mousePressed(mid, false, true, false);
      assertEquals(1, civ.getRoomList().size());
      assertEquals("the bathroom", bf.rdi.titleText.getText());
      assertEquals("the place people go when they got to use the bathroom",
            bf.rdi.descArea.getText());
   }
}