package hic;

import civ.FormCiv;
import pdc.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Handles UI of form window
 */

public class RoomDescInsert extends JPanel{

    public FormCiv formCiv;
    public JTextArea titleText, descArea;
    JTextField roomIdText;
    private AuthorTopBar topBar;

    public RoomDescInsert(AuthorWindow aw) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        titleText = new JTextArea();
        roomIdText = new JTextField();
        descArea = new JTextArea();
        formCiv = aw.civ.formCiv;
        initialize();
    }
    
    public void initialize() {

       topBar = new AuthorTopBar();
       JPanel topBarJPanel = topBar.getMainJPanel();

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

       this.add(topBarJPanel);
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
        System.out.println(formCiv.getRoomDesc());
        descArea.setText(formCiv.getRoomDesc());
        resetButtons();
        Set<Map.Entry<Room, CardinalDirection>> connectedRooms = formCiv.getConnectedRooms().entrySet();
        for(Map.Entry<Room, CardinalDirection> entry : connectedRooms){
           topBar.setRoomIdForButton(entry.getValue(),entry.getKey().ROOMID);
        }

    }

    public void clear(){
       titleText.setText("");
       roomIdText.setText("");
       descArea.setText("");
       resetButtons();
    }

    public void resetButtons(){
       topBar.resetButtons();
    }
}
