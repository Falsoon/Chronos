package hic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * handles UI of playing (moving player around) on the map
 */
public class PlayerWindow {

	public JFrame frame;
	private MapPanel mapPanel;
	private StoryPanel storyPanel;

	private static final char NORTH = 'N';
	private static final char SOUTH = 'S';
	private static final char EAST = 'E';
	private static final char WEST = 'W';

	private static final int moveNorthKey = KeyEvent.VK_W;
	private static final int moveSouthKey = KeyEvent.VK_S;
	private static final int moveEastKey = KeyEvent.VK_D;
	private static final int moveWestKey = KeyEvent.VK_A;

	//the following are used to define the Swing KeyStrokes for arrow keys.
	//The have been renamed here to distinguish between moving north/south and up/down
	private static final int moveNorthArrowKey = KeyEvent.VK_UP;
	private static final int moveSouthArrowKey = KeyEvent.VK_DOWN;
	private static final int moveEastArrowKey = KeyEvent.VK_RIGHT;
	private static final int moveWestArrowKey = KeyEvent.VK_LEFT;

	//use unitScroll<Direction> because sometimes the arrow keys get registered as such
	private static final String moveNorthArrowKeyAlternate = "unitScrollUp";
	private static final String moveSouthArrowKeyAlternate = "unitScrollDown";
	private static final String moveEastArrowKeyAlternate = "unitScrollRight";
	private static final String moveWestArrowKeyAlternate = "unitScrollLeft";

	/**
	 * Create the application.
	 * 
	 * @param mp
	 */
	public PlayerWindow(MapPanel mp) {
		mapPanel = mp;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		//TODO: figure out how to not crash the map when returning to the editor
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setTitle("Explorer");

		JSplitPane splitPane = new JSplitPane();
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);
		splitPane.setDividerLocation(800);
		splitPane.setRightComponent(mapPanel);
		
		storyPanel = new StoryPanel(mapPanel);
		
		splitPane.setLeftComponent(storyPanel);
		InputMap inputMap = splitPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

		inputMap.put(KeyStroke.getKeyStroke(moveNorthKey,0), NORTH);
		inputMap.put(KeyStroke.getKeyStroke(moveWestKey,0), WEST);
		inputMap.put(KeyStroke.getKeyStroke(moveSouthKey,0), SOUTH);
		inputMap.put(KeyStroke.getKeyStroke(moveEastKey,0), EAST);

		inputMap.put(KeyStroke.getKeyStroke(moveNorthArrowKey,0), NORTH);
		inputMap.put(KeyStroke.getKeyStroke(moveWestArrowKey,0), WEST);
		inputMap.put(KeyStroke.getKeyStroke(moveSouthArrowKey,0), SOUTH);
		inputMap.put(KeyStroke.getKeyStroke(moveEastArrowKey,0), EAST);


		ActionMap actionMap = splitPane.getActionMap();

		actionMap.put(NORTH,
				new MoveAndPrintAction(NORTH));
		actionMap.put(WEST,
				new MoveAndPrintAction(WEST));
		actionMap.put(SOUTH,
				new MoveAndPrintAction(SOUTH));
		actionMap.put(EAST,
				new MoveAndPrintAction(EAST));

		WindowListener wl = new WindowListener(){

			@Override
			public void windowClosing(WindowEvent e) {

				EventQueue.invokeLater(() -> {
					try {
						mapPanel = null;
						frame = null;
						AuthorWindow window = new AuthorWindow();
						window.frame.setTitle("QuestMaster");
						window.frame.setVisible(true);
						window.mapPanel.restore();
						window.mapPanel.civ.stopDrawing();
						window.civ.setPlayerMode(false);
						window.civ.map.player.stopPlaying();
						window.civ.setSelectedRoom(null);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				});
			}
		
			@Override
			public void windowOpened(WindowEvent e) {
				
			}
		
			@Override
			public void windowIconified(WindowEvent e) {
				
			}
		
			@Override
			public void windowDeiconified(WindowEvent e) {
				
			}
		
			@Override
			public void windowDeactivated(WindowEvent e) {
				
			}
		
			@Override
			public void windowClosed(WindowEvent e) {
				
			}
		
			@Override
			public void windowActivated(WindowEvent e) {
				
			}
		};
		frame.addWindowListener(wl);
	}

	/**
	 * Custom class to handle actions for key bindings
	 */
	private class MoveAndPrintAction extends AbstractAction {
		private char direction;

		MoveAndPrintAction(char direction){
			this.direction = direction;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			switch (direction){
				case WEST:
					mapPanel.goLeft();
					storyPanel.printDetails(mapPanel.getRoomName(), mapPanel.getRoomDesc());
					break;
				case EAST:
					mapPanel.goRight();
					storyPanel.printDetails(mapPanel.getRoomName(), mapPanel.getRoomDesc());
					break;
				case NORTH:
					mapPanel.goUp();
					storyPanel.printDetails(mapPanel.getRoomName(), mapPanel.getRoomDesc());
					break;
				case SOUTH:
					mapPanel.goDown();
					storyPanel.printDetails(mapPanel.getRoomName(), mapPanel.getRoomDesc());
					break;
				default:
					break;
			}
		}

	}
}
