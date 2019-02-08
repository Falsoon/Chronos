package hic;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import civ.*;
import pdc.Door;
import pdc.DoorList;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Handles UI of form window
 */

public class FormWindow  {

	public JFrame frame;
	private JTextField roomIdText;
	private JTextArea textArea, titleText;
	private JComboBox<String> portalCombo;
	//private Room room;
	private boolean drawnRoom;
	private FormCiv formCiv;

	public FormWindow(FormCiv fc, boolean drawn) {
		//room  = r;
		formCiv = fc;
		drawnRoom = drawn;
		initialize();
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
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
		titleText.setText(formCiv.getRoomTitle());
		titleText.setLineWrap(true);
		
		JLabel lblRoomDescription = new JLabel("Room Description");
		lblRoomDescription.setBounds(10, 75, 150, 15);
		frame.getContentPane().add(lblRoomDescription);
		
		JLabel lblRoomId = new JLabel("Room ID");
		lblRoomId.setBounds(10, 50, 75, 15);
		frame.getContentPane().add(lblRoomId);
		
		roomIdText = new JTextField();
		roomIdText.setBounds(115, 50, 90, 20);
		
		roomIdText.setText(formCiv.getRoomID() + "");
		frame.getContentPane().add(roomIdText);
		roomIdText.setColumns(10);
		roomIdText.setEditable(false);
		
		portalCombo.addItem("Select Door");
		ArrayList<Door> doorList = formCiv.getRoomDoors();
		for (int i = 0; i < doorList.size();i++) {
			portalCombo.addItem(DoorList.list.get(i).toString());
		}
		portalCombo.setBounds(250, 50, 90, 20);
		portalCombo.addActionListener(new ActionListener( ) {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!portalCombo.getSelectedItem().equals("Select Door")) {
					DoorFormWindow window = new DoorFormWindow(formCiv);
					formCiv.setDoorReference((String)portalCombo.getSelectedItem());
					window.frame.setVisible(true);
				}
			}
		});
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.setBounds(180, 510, 100, 25);
		btnSubmit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				formCiv.adjustRoomTitleAndDesc(titleText.getText(), textArea.getText() );
				frame.setVisible(false);
				if(!drawnRoom) {
					formCiv.addRoomToRoomList();
				}
			}
			
		});
		frame.getContentPane().add(btnSubmit);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 100, 300, 400);
		frame.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setText(formCiv.getRoomDesc());
		scrollPane.setViewportView(textArea);
	}
		
}
