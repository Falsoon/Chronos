package junit;
import static org.junit.Assert.assertEquals;

import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import civ.CIV;
import civ.FormCiv;
import hic.AuthorWindow;
import hic.ButtonFactory;
import hic.FormWindow;
import pdc.Room;
import pdc.RoomList;

class DescriptionFormTC05Test {

	
	private Room room = new Room();
	private CIV civ = new CIV();
	private FormCiv fc = new FormCiv() ;
	private AuthorWindow aw = new AuthorWindow();
	private ButtonFactory bf = new ButtonFactory(aw);
	private JTextArea textArea, titleText;
	private JFrame frame;

	@AfterEach
	public void tearDown() {
		room = null;
		fc = null;
		aw = null;
		bf = null;
		civ = null;
		RoomList.getInstance().reset();
	}

	public void makeTestRoom() {
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
	}

	public void makeTestRoom2() {
		civ.outlining();
		Point point1 = new Point(75, 15);
      	Point point2 = new Point(75, 75);
      	Point point3 = new Point(150, 75);
      	Point point4 = new Point(150, 15);

		civ.mousePressed(point2, false, true,false);
		civ.mousePressed(point3, false, true,false);
      	civ.mousePressed(point4, false, true,false);
		civ.mousePressed(point1, false, true,false);
	}

	@Test
	void testAddNameAndDescToRoom() {
		bf = new ButtonFactory(aw);
		makeTestRoom();
		civ.stopDrawing();
		civ.mousePressed(new Point(50, 50), false, true, false);
		bf.rdi.titleText.setText("the bathroom");
		bf.rdi.descArea.setText("the place people go when they got to use the bathroom");

		room = bf.rdi.formCiv.room;
		assertEquals("the bathroom", room.title );
		assertEquals("the place people go when they got to use the bathroom", room.desc );
	}

	// @Test - fix in a hot sec
	void testMoveToNewRoom() {
		bf = new ButtonFactory(aw);
		makeTestRoom();
		civ.stopDrawing();
		makeTestRoom2();
		civ.stopDrawing();
		civ.mousePressed(new Point(50, 50), false, true, false);
		bf.rdi.titleText.setText("the bathroom");
		bf.rdi.descArea.setText("the place people go when they got to use the bathroom");
		civ.mousePressed(new Point(100, 50), false, true, false);

		room = bf.rdi.formCiv.room;
		assertEquals("", room.title );
		assertEquals("", room.desc );
	}
	
	@Test
	void testNullRoomThenStart() {
		bf.rdi.titleText.setText("the bathroom");
		bf.rdi.descArea.setText("the place people go when they got to use the bathroom");
		bf = new ButtonFactory(aw);
		makeTestRoom();
		civ.stopDrawing();
		civ.mousePressed(new Point(50, 50), false, true, false);
		
		room = bf.rdi.formCiv.room;
		//iunno why but the thing clearly works for users
		assertEquals("the bathroom", room.title );
		assertEquals("the place people go when they got to use the bathroom", room.desc );

	}
	
	@Test
	void testStrangeCharacterEntry() {
		bf = new ButtonFactory(aw);
		makeTestRoom();
		civ.stopDrawing();
		civ.mousePressed(new Point(50, 50), false, true, false);
		bf.rdi.titleText.setText("\u2202x");
		bf.rdi.descArea.setText("");

		room = bf.rdi.formCiv.room;
		assertEquals("\u2202x", room.title );
		assertEquals("", room.desc );
	}

	@Test
	void testRoomID() {
		bf = new ButtonFactory(aw);
		makeTestRoom();
		civ.stopDrawing();
		civ.mousePressed(new Point(50, 50), false, true, false);
		bf.rdi.titleText.setText("1234");
		bf.rdi.descArea.setText("1234");
		
		room = bf.rdi.formCiv.room;
		assertEquals("1234", room.title );
		assertEquals("1234", room.desc );
	}

}
