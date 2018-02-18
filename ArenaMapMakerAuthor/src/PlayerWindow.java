import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSplitPane;

public class PlayerWindow {

	JFrame frame;
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
		frame.setBounds(200, 200, 800, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("PlayerWindow");
		
		JSplitPane splitPane = new JSplitPane();
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);
		splitPane.setDividerLocation(300);
		splitPane.setRightComponent(mapPanel);

		StoryPanel storyPanel = new StoryPanel(mapPanel);

		JButton goUp = new JButton("Up");
		goUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mapPanel.goUp();
			}
		});
		storyPanel.add(goUp);

		JButton goDown = new JButton("Down");
		goDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mapPanel.goDown();
			}
		});
		storyPanel.add(goDown);

		JButton goLeft = new JButton("Left");
		goLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mapPanel.goLeft();
			}
		});
		storyPanel.add(goLeft);

		JButton goRight = new JButton("Right");
		goRight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mapPanel.goRight();
			}
		});
		storyPanel.add(goRight);

		splitPane.setLeftComponent(storyPanel);
	}
}
