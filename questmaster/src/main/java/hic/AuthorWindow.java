package hic;

import civ.CIV;

import javax.swing.*;
import java.awt.*;

/**
 * UI class for entire application
 */

@SuppressWarnings("serial")
public class AuthorWindow extends JPanel {

	public static CIV civ;
	public JFrame frame;
	public AuthorPanel authorPanel;
	public MapPanel mapPanel;
	public String[] wallTypes = { "Walls", "Opaque", "Transparent" };
	public String[] portalTypes = { "Portals", "Doors" };
	public String[] authorModes = { "Select Authoring Mode", "Author Room descriptions", "Draw Rooms" };
	public JComboBox<String> wallCombo, portalCombo;
	public JButton start, placeStart, undoButton, btnClear, addRoombtn1, addRoombtn2;
	public int modeSelected = 0;
	private JScrollPane scrollPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AuthorWindow window = new AuthorWindow();
					window.frame.setTitle("ArenaMapMaker");
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AuthorWindow() {
		civ = new CIV();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		frame.getContentPane().setBackground(Color.BLACK);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		//frame.setBounds(200, 100, 1000, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JSplitPane splitPane = new JSplitPane();
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);
		splitPane.setDividerLocation(300);

		// mapPanel holds the graphics of the map
		mapPanel = new MapPanel(this);
		scrollPane = new JScrollPane(mapPanel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		splitPane.setRightComponent(scrollPane);

		// Author panel holds the buttons for authors use
		authorPanel = new AuthorPanel();
		new ButtonFactory(this);
		splitPane.setLeftComponent(authorPanel);
		authorPanel.update();

	}
}
