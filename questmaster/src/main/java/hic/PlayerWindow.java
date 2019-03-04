package hic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * handles UI of playing (moving player around) on the map
 */
public class PlayerWindow {

	public JFrame frame;
	private MapPanel mapPanel;
	private StoryPanel storyPanel;

	private static final String MOVE_NORTH = "move north";
   private static final String MOVE_SOUTH = "move south";
   private static final String MOVE_EAST = "move east";
   private static final String MOVE_WEST = "move west";

   private static final char moveNorthKey = 'w';
   private static final char moveSouthKey = 's';
   private static final char moveEastKey = 'd';
   private static final char moveWestKey = 'a';

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
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("PlayerWindow");

		JSplitPane splitPane = new JSplitPane();
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);
		splitPane.setDividerLocation(800);
		splitPane.setRightComponent(mapPanel);
		
		storyPanel = new StoryPanel(mapPanel);
		
		splitPane.setLeftComponent(storyPanel);
      InputMap splitPaneInputMap = splitPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
      splitPaneInputMap.put(KeyStroke.getKeyStroke(moveNorthKey),
         MOVE_NORTH);
      splitPaneInputMap.put(KeyStroke.getKeyStroke(moveWestKey),
         MOVE_WEST);
      splitPaneInputMap.put(KeyStroke.getKeyStroke(moveSouthKey),
         MOVE_SOUTH);
      splitPaneInputMap.put(KeyStroke.getKeyStroke(moveEastKey),
         MOVE_EAST);

      splitPane.getActionMap().put(MOVE_NORTH,
         new MoveAndPrintAction(moveNorthKey));
      splitPane.getActionMap().put(MOVE_WEST,
         new MoveAndPrintAction(moveWestKey));
      splitPane.getActionMap().put(MOVE_SOUTH,
         new MoveAndPrintAction(moveSouthKey));
      splitPane.getActionMap().put(MOVE_EAST,
         new MoveAndPrintAction(moveEastKey));
	}

   /**
    * Custom class to handle actions for key bindings
    */
   private class MoveAndPrintAction extends AbstractAction {
	   private char keyStroke;

	   MoveAndPrintAction(char keyStroke){
	      this.keyStroke = keyStroke;
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         switch (keyStroke){
            case moveWestKey:
               mapPanel.goLeft();
               storyPanel.printDetails(mapPanel.getRoomName(), mapPanel.getRoomDesc());
               break;
            case moveEastKey:
               mapPanel.goRight();
               storyPanel.printDetails(mapPanel.getRoomName(), mapPanel.getRoomDesc());
               break;
            case moveNorthKey:
               mapPanel.goUp();
               storyPanel.printDetails(mapPanel.getRoomName(), mapPanel.getRoomDesc());
               break;
            case moveSouthKey:
               mapPanel.goDown();
               storyPanel.printDetails(mapPanel.getRoomName(), mapPanel.getRoomDesc());
               break;
            default:
               break;
         }
      }

   }
}
