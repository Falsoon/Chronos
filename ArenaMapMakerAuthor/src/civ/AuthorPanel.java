package civ;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import pdc.*;

/**
 * Used for encapsulating the author UI
 */
@SuppressWarnings("serial")
public class AuthorPanel extends JPanel {

	public static final Room selectRoom = new SelectRoom();
	public JComboBox<Room> Rooms;
	
	
	public void update() {
		int index = Rooms.getSelectedIndex();
		if(index == -1) {index = 0;}
		Rooms.removeAllItems();
		Rooms.addItem(selectRoom);
		// TODO Should not get Room List Directly
		for (int i = 0; i < RoomList.list.size(); i++) {
			Rooms.addItem(RoomList.list.get(i));
		}
		Rooms.setSelectedIndex(index);
	}
}
