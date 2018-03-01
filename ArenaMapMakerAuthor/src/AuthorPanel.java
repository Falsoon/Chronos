import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class AuthorPanel extends JPanel implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		int id;
		JComboBox<Room> cb = (JComboBox<Room>) e.getSource();
		if (cb.getItemCount() > 1) {
			Room r = (Room) cb.getSelectedItem();
			id = r.ROOMID;

			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						// TODO Should not access RoomList
						FormWindow window = new FormWindow(RoomList.getRoomById(id));
						window.frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

}
