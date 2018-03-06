package civ;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/**
 * Presenter class used to update playerWindow and player data.
 */
public class StoryPanel extends JPanel {
	private LeftAction leftAction = new LeftAction();
	private RightAction rightAction = new RightAction();
	private UpAction upAction = new UpAction();
	private DownAction downAction = new DownAction();
	private MapPanel mapPanel;
	private JTextField title = new JTextField();
	private JTextArea desc = new JTextArea();

	public StoryPanel(MapPanel mp) {
		mapPanel = mp;
		this.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "LEFT");
		this.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "RIGHT");
		this.getInputMap().put(KeyStroke.getKeyStroke("UP"), "UP");
		this.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "DOWN");
		this.getActionMap().put("LEFT", leftAction);
		this.getActionMap().put("RIGHT", rightAction);
		this.getActionMap().put("UP", upAction);
		this.getActionMap().put("DOWN", downAction);

		title.setEditable(false);
		this.add(title);

		desc.setEditable(false);
		this.add(desc);

		updateText();
	}

	private void updateText() {
		if (mapPanel.getRoom() != null) {
			desc.setText(mapPanel.getRoom().desc);
			title.setText(mapPanel.getRoom().title);
		}
	}

	private class LeftAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			mapPanel.goLeft();
			updateText();
		}
	}

	private class RightAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			mapPanel.goRight();
			updateText();
		}
	}

	private class UpAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			mapPanel.goUp();
			updateText();
		}
	}

	private class DownAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			mapPanel.goDown();
			updateText();
		}
	}
}
