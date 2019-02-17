package hic;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import java.awt.EventQueue;
import civ.CIV;

/**
 * handles UI of playing (moving player around) on the map
 */
public class PlayerWindow {

	public JFrame frame;
	private MapPanel mapPanel;

	/**
	 * Create the application.
	 * 
	 * @param mapPanel
	 */
	public PlayerWindow(MapPanel mp) {
		mapPanel = mp;
		initialize();
	}
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PlayerWindow window = new PlayerWindow(new MapPanel());
					window.frame.setTitle("ArenaMapMaker");
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		//frame.setBounds(200, 200, 800, 450);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("PlayerWindow");

		JSplitPane splitPane = new JSplitPane();
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);
		splitPane.setDividerLocation(800);
		splitPane.setRightComponent(mapPanel);
		
		StoryPanel storyPanel = new StoryPanel(mapPanel);
		
		splitPane.setLeftComponent(storyPanel);
	}
}
