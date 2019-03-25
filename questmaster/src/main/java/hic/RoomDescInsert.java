package hic;

import civ.FormCiv;
import pdc.Door;
import pdc.DoorList;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Handles UI of form window
 */

public class RoomDescInsert extends JPanel{

    public FormCiv formCiv;
    public JTextArea titleText, descArea;
    JTextField roomIdText;

    public RoomDescInsert(AuthorWindow aw) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        titleText = new JTextArea();
        roomIdText = new JTextField();
        descArea = new JTextArea();
        formCiv = AuthorWindow.civ.formCiv;
        initialize();
    }
    
    public void initialize() {

        JLabel lblRoomTitle = new JLabel("Room Title");
		lblRoomTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleText.setMaximumSize(new Dimension(5000, 30));
		JLabel lblRoomId = new JLabel("Room ID");
		lblRoomId.setAlignmentX(Component.CENTER_ALIGNMENT);
		roomIdText.setEditable(false);
        roomIdText.setMaximumSize(new Dimension(5000, 30));
		JLabel lblRoomDescription = new JLabel("Room Description");
		lblRoomDescription.setAlignmentX(Component.CENTER_ALIGNMENT);
		JScrollPane scrollPane = new JScrollPane();
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
		descArea.setRows(25);
        scrollPane.setViewportView(descArea);
        
        this.add(lblRoomTitle);
        this.add(titleText);
        this.add(lblRoomId);
        this.add(roomIdText);
        this.add(lblRoomDescription);
        this.add(scrollPane);

        titleText.getDocument().addDocumentListener(new DocumentListener(){
        
            @Override
            public void removeUpdate(DocumentEvent e) {
                formCiv.adjustRoomTitle(titleText.getText());
            }
        
            @Override
            public void insertUpdate(DocumentEvent e) {
                formCiv.adjustRoomTitle(titleText.getText());
            }
        
            @Override
            public void changedUpdate(DocumentEvent e) {
                formCiv.adjustRoomTitle(titleText.getText());
            }
        });

        descArea.getDocument().addDocumentListener(new DocumentListener(){
        
            @Override
            public void removeUpdate(DocumentEvent e) {
                formCiv.adjustRoomDesc(descArea.getText());
            }
        
            @Override
            public void insertUpdate(DocumentEvent e) {
                formCiv.adjustRoomDesc(descArea.getText());
            }
        
            @Override
            public void changedUpdate(DocumentEvent e) {
                formCiv.adjustRoomDesc(descArea.getText());
            }
        });

    }

    public void updateRoom() {
        titleText.setText(formCiv.getRoomTitle());
        roomIdText.setText(Integer.toString(formCiv.getRoomID()));
        //descArea.setText("");
        System.out.println(formCiv.getRoomDesc());
        descArea.setText(formCiv.getRoomDesc());
    }
}
