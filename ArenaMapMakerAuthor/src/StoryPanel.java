import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/*
 * Presenter class used to update playerWindow and player data.
 */
public class StoryPanel extends JPanel {
	private LeftAction leftAction = new LeftAction();
	private RightAction rightAction = new RightAction();
	private UpAction upAction = new UpAction();
	private DownAction downAction = new DownAction();
	private MapPanel mapPanel;

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

	}

	private class LeftAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			mapPanel.goLeft();
		}
	}

	private class RightAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			mapPanel.goRight();
		}
	}

	private class UpAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			mapPanel.goUp();
		}
	}

	private class DownAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			mapPanel.goDown();
		}
	}
}
