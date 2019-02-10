package main.java.hic;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JPanel;
/**
 * Used for encapsulating the author UI
 */
@SuppressWarnings("serial")
public class AuthorPanel extends JPanel {

	public JComboBox<String> Rooms;


	public void update() {
		int index = Rooms.getSelectedIndex();
		if(index == -1) {index = 0;}
		Rooms.removeAllItems();
		Rooms.addItem("Select Room");
		// TODO Should not get Room List Directly
		ArrayList<String> roomList = AuthorWindow.civ.getRoomList();
		for (int i = 0; i < roomList.size();i++) {
			Rooms.addItem(roomList.get(i));
		}
		//temporarily remove action listeners
		ActionListener[] al = Rooms.getActionListeners();
		Rooms.removeActionListener(al[0]);
		Rooms.setSelectedIndex(index);
		Rooms.addActionListener(al[0]);
	}


	public void reset() {

	}
}