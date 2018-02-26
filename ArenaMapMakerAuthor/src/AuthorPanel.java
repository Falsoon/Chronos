import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/*
 * Used for encapsulating the author UI
 */

public class AuthorPanel extends JPanel {
	private MapPanel mapPanel;
	private Action undoAction;

/*	public AuthorPanel(MapPanel mp) {
		mapPanel = mp;

		getInputMap().put(KeyStroke.getKeyStroke('Z'), "z");
		getActionMap().put("z", undoAction);
	}

	private class UndoAction extends AbstractAction {

		@Override
		public void actionPerformed(ActionEvent e) {
			mapPanel.undo();
			grabFocus();
		}

	}*/
}
