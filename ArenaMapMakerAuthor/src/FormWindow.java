
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JEditorPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/*
 * Handles UI of form window
 */

public class FormWindow {

	protected JFrame frame;
	private JTextField titleText;
	private JTextField roomIdText;
	private JTextArea textArea;
	private Room room;

	public FormWindow(Room r) {
		room  = r;
		initialize();
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 200, 300, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Room Description");
		frame.getContentPane().setLayout(null);
		
		JLabel lblRoomTitle = new JLabel("Room Title");
		lblRoomTitle.setBounds(33, 24, 68, 14);
		frame.getContentPane().add(lblRoomTitle);
		
		titleText = new JTextField();
		titleText.setBounds(111, 21, 125, 20);
		frame.getContentPane().add(titleText);
		titleText.setColumns(10);
		titleText.setText(room.title);
		
		JLabel lblRoomDescription = new JLabel("Room Description");
		lblRoomDescription.setBounds(10, 74, 83, 14);
		frame.getContentPane().add(lblRoomDescription);
		
		JLabel lblRoomId = new JLabel("Room ID");
		lblRoomId.setBounds(33, 49, 46, 14);
		frame.getContentPane().add(lblRoomId);
		
		roomIdText = new JTextField();
		roomIdText.setBounds(111, 52, 86, 20);
		roomIdText.setText(room.ROOMID+"");
		frame.getContentPane().add(roomIdText);
		roomIdText.setColumns(10);
		roomIdText.setEditable(false);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.setBounds(179, 343, 89, 23);
		btnSubmit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				room.title = titleText.getText();
				room.desc = textArea.getText();
				frame.setVisible(false);
			}
			
		});
		frame.getContentPane().add(btnSubmit);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 99, 230, 220);
		frame.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setText(room.desc);
		scrollPane.setViewportView(textArea);

	}
}
