package test.junit;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import civ.CIV;
import civ.FormCiv;
import hic.FormWindow;
import pdc.Room;
import pdc.RoomList;

class DescriptionFormTC05 {

	
	private Room room = new Room();
	private FormCiv fc = new FormCiv() ;
	private FormWindow fw = new FormWindow(fc, false);
	private JTextArea textArea, titleText;
	private JFrame frame;

	@AfterEach
	public void tearDown() {
		room = null;
		fc = null;
		fw = null;
		RoomList.reset();
		System.out.print("yo i done");
	}
	
	@Test
	void testAuthorSelectsDescriptionMode() {
		
	}
	
	@Test
	void testAuthorSelectsDrawingMode() {
		
	}
	
	@Test
	void testCreateRoomViaForm() {
		
	}
	
	@Test
	void testClosesFormBeforeSubmit() {
		//the room title and room description should not save
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
