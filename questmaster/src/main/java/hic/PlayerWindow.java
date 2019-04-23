package hic;

import pdc.CardinalDirection;
import pdc.Player;

import javax.swing.*;
import java.awt.*;
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
	private KeyEventDispatcher dispatcher;

	private static final int KEYCODE_W = 87;
   private static final int KEYCODE_A = 65;
   private static final int KEYCODE_S = 83;
   private static final int KEYCODE_D = 68;
   private static final int KEYCODE_L = 76;
   private static final int KEYCODE_K = 75;

   private static final int KEYCODE_LEFT_ARROW = 37;
   private static final int KEYCODE_UP_ARROW = 38;
   private static final int KEYCODE_RIGHT_ARROW = 39;
   private static final int KEYCODE_DOWN_ARROW = 40;

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
		storyPanel.updateExits(mapPanel.getRoom(mapPanel.civ.map.getPlayer().getPosition()));
		
		dispatcher = e -> {
			if(e.getID() == KeyEvent.KEY_PRESSED) {
				switch (e.getKeyCode()) {
					case KEYCODE_A:
					case KEYCODE_LEFT_ARROW:
						goDirection(CardinalDirection.WEST);
						return true;
					case KEYCODE_W:
					case KEYCODE_UP_ARROW:
						goDirection(CardinalDirection.NORTH);
						return true;
					case KEYCODE_RIGHT_ARROW:
						goDirection(CardinalDirection.EAST);
						return true;
					case KEYCODE_S:
                  mapPanel.moveStair();
                  return true;
					case KEYCODE_DOWN_ARROW:
						goDirection(CardinalDirection.SOUTH);
						return true;
					case KEYCODE_D:
						mapPanel.dropKey();
						return true;
               case KEYCODE_K:
						mapPanel.pickUpKey();
						return true;
					case KEYCODE_L:
						mapPanel.lockDoor();
						return true;
				}
			}
			//return false if not key pressed or not one of the reserved keys so other components can grab the key
			return false;
		};

      KeyboardFocusManager.getCurrentKeyboardFocusManager()
         .addKeyEventDispatcher(dispatcher);
		
		splitPane.setLeftComponent(storyPanel);

		WindowListener wl = new WindowListener(){

			@Override
			public void windowClosing(WindowEvent e) {
				EventQueue.invokeLater(() -> {
					try {
						mapPanel.aw.nukeVariables();
						mapPanel = null;
						frame = null;
						storyPanel = null;
						KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(dispatcher);
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
    * Move player character in the direction specified
    * @param direction the direction code
    */
	private void goDirection(CardinalDirection direction){
      switch (direction){
         case WEST:
            mapPanel.goLeft();
            break;
         case EAST:
            mapPanel.goRight();
            break;
         case NORTH:
            mapPanel.goUp();
            break;
         case SOUTH:
            mapPanel.goDown();
            break;
         default:
            break;
      }
      storyPanel.printDetails(mapPanel.getRoomName(), mapPanel.getRoomDesc());
      storyPanel.updateExits(mapPanel.getRoom(mapPanel.civ.map.getPlayer().getPosition()));
   }
}
