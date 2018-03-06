package hic;
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
		Rooms.setSelectedIndex(index);
	}
}
