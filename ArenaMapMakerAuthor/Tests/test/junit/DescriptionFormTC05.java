package test.junit;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import civ.CIV;
import civ.FormCiv;
import hic.AuthorWindow;
import hic.ButtonFactory;
import hic.FormWindow;
import pdc.Room;
import pdc.RoomList;

class DescriptionFormTC05 {

	
	private Room room = new Room();
	private FormCiv fc = new FormCiv() ;
	private FormWindow fw = new FormWindow(fc, false);
	private AuthorWindow aw = new AuthorWindow();
	private ButtonFactory bf = new ButtonFactory(aw);
	private JTextArea textArea, titleText;
	private JFrame frame;

	@AfterEach
	public void tearDown() {
		room = null;
		fc = null;
		fw = null;
		aw = null;
		bf = null;
		RoomList.reset();
	}
	
	@Test
	void testAuthorSelectsDescriptionMode() {
		bf = new ButtonFactory(aw);
		bf.setMode1();
		assertEquals(false, aw.wallCombo.isVisible());
		assertEquals(false, aw.portalCombo.isVisible());
	}
	
	@Test
	void testAuthorSelectsDrawingMode() {
		bf = new ButtonFactory(aw);
		bf.setMode2();
		assertEquals(true, aw.wallCombo.isVisible());
		assertEquals(true, aw.portalCombo.isVisible());
	}
	
	@Test
	void testCreateRoomViaForm() {
		bf = new ButtonFactory(aw);
		bf.setMode1();
		bf.addRoombtn1.doClick();
		assertEquals(true, fw.frame.isVisible());
		
	}
	
	@Test
	void testClosesFormBeforeSubmit() {
		//the room title and room description should not save
		RoomList.add(room);
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
		//author brings up form by clicking room in drawing mode
	}
	
	@Test
	void testFormEditingViaDrawMode() {
		//author can edit room desc and title when in drawing mode
	}
	
	@Test
	void testCreateRoomDescThenDrawRoom() {
		//author creates room description and then draws room
	}
	
	@Test
	void testRoomID() {
		RoomList.add(room);
		int roomID = room.ROOMID;
		fc.setRoomReference(room.toString());
		assertEquals(roomID, fc.getRoomID() );		
	}
	
	@Test
	void testNumberInputForDescAndTitle(){
		
		RoomList.add(room);
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
