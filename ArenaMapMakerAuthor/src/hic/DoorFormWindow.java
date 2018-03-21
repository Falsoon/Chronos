package hic;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import civ.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Handles UI of form window
 */

public class DoorFormWindow  {

	public JFrame frame;
	private JTextField doorIdText;
	private JTextArea textArea, titleText;
	private JCheckBox doorOpen;
	private FormCiv formCiv;

	public DoorFormWindow(FormCiv fc) {
		formCiv = fc;
		initialize();
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(200, 100, 350, 600);
		frame.setTitle("Door Description");
		frame.getContentPane().setLayout(null);
		
		JLabel lblDoorTitle = new JLabel("Door Title");
		lblDoorTitle.setBounds(10, 25, 100, 15);
		frame.getContentPane().add(lblDoorTitle);
		
		titleText = new JTextArea();
		titleText.setBounds(110, 20, 200, 30);
		frame.getContentPane().add(titleText);
		titleText.setColumns(10);
		titleText.setText(formCiv.getDoorTitle());
		titleText.setLineWrap(true);
		
		JLabel lblDoorDescription = new JLabel("Door Description");
		lblDoorDescription.setBounds(10, 75, 150, 15);
		frame.getContentPane().add(lblDoorDescription);
		
		JLabel lblDoorId = new JLabel("Door ID");
		lblDoorId.setBounds(10, 50, 75, 15);
		frame.getContentPane().add(lblDoorId);
		
		doorIdText = new JTextField();
		doorIdText.setBounds(115, 50, 90, 20);
		
		doorIdText.setText(formCiv.getDoorID() + "");
		frame.getContentPane().add(doorIdText);
		doorIdText.setColumns(10);
		doorIdText.setEditable(false);
		
		doorOpen = new JCheckBox("Open?");
		doorOpen.setBounds(220, 50, 90, 20);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.setBounds(180, 510, 100, 25);
		btnSubmit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				formCiv.adjustDoorTitleAndDesc(titleText.getText(), textArea.getText() );
				formCiv.setDoorOpen(doorOpen.isSelected());
				frame.setVisible(false);
			}
			
		});
		frame.getContentPane().add(btnSubmit);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 100, 300, 400);
		frame.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setText(formCiv.getDoorDesc());
		scrollPane.setViewportView(textArea);
	}
		
}
