package hic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

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

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		//frame.setBounds(200, 200, 800, 450);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		//TODO: figure out how to not crash the map when returning to the editor
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setTitle("PlayerWindow");

		JSplitPane splitPane = new JSplitPane();
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);
		splitPane.setDividerLocation(800);
		splitPane.setRightComponent(mapPanel);
		
		StoryPanel storyPanel = new StoryPanel(mapPanel);
		
		splitPane.setLeftComponent(storyPanel);


		WindowListener wl = new WindowListener(){
		
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
			public void windowClosing(WindowEvent e) {
				
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							mapPanel = null;
							frame = null;
							AuthorWindow window = new AuthorWindow();
							window.frame.setTitle("ArenaMapMaker");
							window.frame.setVisible(true);
							window.mapPanel.restore();
							window.mapPanel.civ.stopDrawing();
							window.civ.setPlayerMode(false);
							window.civ.map.player.stopPlaying();
							window.civ.setSelectedRoom(null);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
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
}
