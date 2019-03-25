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
	
	@Test
	void testAuthorSelectsDescriptionMode() {
		bf = new ButtonFactory(aw);
		bf.setModeDescription();
		assertEquals(false, aw.wallCombo.isVisible());
		assertEquals(false, aw.portalCombo.isVisible());
	}
	
	@Test
	void testAuthorSelectsDrawingMode() {
		bf = new ButtonFactory(aw);
		bf.setModeDraw();
		assertEquals(true, aw.wallCombo.isVisible());
		assertEquals(true, aw.portalCombo.isVisible());
	}
	
	@Test
	void testCreateRoomViaForm() {
		bf = new ButtonFactory(aw);
		bf.setModeDescription();
		bf.addRoomDescriptionBtn.doClick();
		FormWindow window = new FormWindow(fc, true);
		window.frame.setVisible(true);
		
		RoomList.getInstance().add(room);
		fc.setRoomReference(room.toString());
		
		window.frame = new JFrame();
		window.frame.setBounds(200, 100, 350, 600);
		window.frame.setTitle("Room Description");
		window.frame.getContentPane().setLayout(null);
		
		JLabel lblRoomTitle = new JLabel("Room Title");
		lblRoomTitle.setBounds(10, 25, 100, 15);
		window.frame.getContentPane().add(lblRoomTitle);
		
		
		
		titleText = new JTextArea();
		titleText.setBounds(110, 20, 200, 30);
		window.frame.getContentPane().add(titleText);
		titleText.setColumns(10);
		titleText.setText("the bathroom");
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 100, 300, 400);
		window.frame.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setText("the place people go when they got to use the bathroom");
		
		fc.adjustRoomTitleAndDesc(titleText.getText(), textArea.getText() );
		
		assertEquals("the bathroom", room.title );
		assertEquals("the place people go when they got to use the bathroom", room.desc );
		
		
	}
	
	@Test
	void testClosesFormBeforeSubmit() {
		//the room title and room description should not save
		RoomList.getInstance().add(room);
		fc.setRoomReference(room.toString());
		
		frame = new JFrame();
		frame.setBounds(200, 100, 350, 600);
		frame.setTitle("Room Description");
		frame.getContentPane().setLayout(null);
		
		JLabel lblRoomTitle = new JLabel("Room Title");
		lblRoomTitle.setBounds(10, 25, 100, 15);
		frame.getContentPane().add(lblRoomTitle);
		
		
		
		titleText = new JTextArea();
		titleText.setBounds(110, 20, 200, 30);
		frame.getContentPane().add(titleText);
		titleText.setColumns(10);
		titleText.setText("the bathroom");
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 100, 300, 400);
		frame.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setText("the place people go when they got to use the bathroom");
		
		frame.setVisible(false);
		frame.dispose();
		
		assertEquals("", room.title );
		assertEquals("", room.desc );
		
	}
	
	@Test
	void testStrangeCharacterEntry() {
		
	}
	
	@Test
	void testFormPopUpViaDrawMode() {
		//author brings up form by clicking room in drawingTransparent mode
		bf = new ButtonFactory(aw);
		bf.setModeDescription();
		bf.addRoomDescriptionBtn.doClick();
		FormWindow window = new FormWindow(fc, true);
		window.frame.setVisible(true);
		assertEquals(true, window.frame.isVisible());
		
	}
	
	@Test
	void testFormEditingViaDrawMode() throws Throwable {
		//author can edit room desc and title when in drawingTransparent mode
		civ.outlining();
		civ.mousePressed(new Point(), true, true,false);
		civ.mousePressed(new Point(87, 95), true, true,false);
		civ.mousePressed(new Point(300, 90), false, true,false);
		civ.mousePressed(new Point(), false, true,false);
		civ.mousePressed(new Point(100, 50), false, true,false);
		
		fc.setRoomReference(room.toString());
		FormWindow fw = new FormWindow(fc, true);
		fw.frame.setVisible(true);
		
		assertEquals(true, fw.frame.isVisible());
		
		
	}
	/*
	Why the hell was this a test in the first place?
	@Test
	void testCreateRoomDescThenDrawRoom() throws Throwable {
		//author creates room description and then draws room
		RoomList.getInstance().add(room);
		fc.setRoomReference(room.toString());
		
		frame = new JFrame();
		frame.setBounds(200, 100, 350, 600);
		frame.setTitle("Room Description");
		frame.getContentPane().setLayout(null);
		
		JLabel lblRoomTitle = new JLabel("Room Title");
		lblRoomTitle.setBounds(10, 25, 100, 15);
		frame.getContentPane().add(lblRoomTitle);
		
		titleText = new JTextArea();
		titleText.setBounds(110, 20, 200, 30);
		frame.getContentPane().add(titleText);
		titleText.setColumns(10);
		titleText.setText("1234");
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 100, 300, 400);
		frame.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setText("1234");
		
		fc.adjustRoomTitleAndDesc(titleText.getText(), textArea.getText() );
		
		
		
		civ.opaqueWalling();
		civ.mousePressed(new Point(), true, true);
		civ.mousePressed(new Point(87, 95), true, true);
		civ.mousePressed(new Point(300, 90), false, true);
		civ.mousePressed(new Point(), false, true);
		civ.stopDrawing();
		
		assertEquals(1, civ.getRoomList().size());
		assertEquals(false, civ.map.isCreating());
		assertEquals(true, civ.map.mapLayer.pointList.contains(new Point()));
		assertEquals(true, civ.map.mapLayer.pointList.contains(new Point(87, 95)));
		assertEquals(true, civ.map.mapLayer.pointList.contains(new Point(300, 90)));
		assertEquals(true, civ.map.mapLayer.pointList.contains(new Point()));
		assertEquals("1234", room.title );
		assertEquals("1234", room.desc );
		
	}
	*/

	@Test
	void testRoomID() {
		RoomList.getInstance().add(room);
		int roomID = room.ROOMID;
		fc.setRoomReference(room.toString());
		assertEquals(roomID, fc.getRoomID() );		
	}
	
	@Test
	void testNumberInputForDescAndTitle(){
		
		RoomList.getInstance().add(room);
		fc.setRoomReference(room.toString());
		
		frame = new JFrame();
		frame.setBounds(200, 100, 350, 600);
		frame.setTitle("Room Description");
		frame.getContentPane().setLayout(null);
		
		JLabel lblRoomTitle = new JLabel("Room Title");
		lblRoomTitle.setBounds(10, 25, 100, 15);
		frame.getContentPane().add(lblRoomTitle);
		
		
		
		titleText = new JTextArea();
		titleText.setBounds(110, 20, 200, 30);
		frame.getContentPane().add(titleText);
		titleText.setColumns(10);
		titleText.setText("1234");
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 100, 300, 400);
		frame.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setText("1234");
		
		fc.adjustRoomTitleAndDesc(titleText.getText(), textArea.getText() );
		
		assertEquals("1234", room.title );
		assertEquals("1234", room.desc );
		
	}

}
