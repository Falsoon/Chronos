import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;

public class AuthorWindow extends JPanel implements ActionListener {

	private JFrame frame;
	private AuthorPanel authorPanel;
	private MapPanel mapPanel;
	
	
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
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.BLACK);
		
		JSplitPane splitPane = new JSplitPane();
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);
		splitPane.setDividerLocation(300);
		//Author panel holds the buttons for authors use
		authorPanel = new AuthorPanel();
		splitPane.setLeftComponent(authorPanel);
		
		//button indicates that the outline of the room is to be drawn
		JButton btnAddRooms = new JButton("Draw Outline(Solid Wall)");
		btnAddRooms.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mapPanel.paintRooms();
			}
		});
		authorPanel.add(btnAddRooms);
		
		//button indicates that the outline is ready to be split into rooms
		JButton wallButton = new JButton("Split Room(Transparent Wall)");
		wallButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mapPanel.paintWalls();
			}
		});
		authorPanel.add(wallButton);
		
		//button resets the map
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mapPanel.clear();
			}
		});
		authorPanel.add(btnClear);
		//button allows author to undo last action.
		//ctrl+z is preferred design
		JButton undoButton = new JButton("Undo");
		undoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mapPanel.undo();
			}
		});
		authorPanel.add(undoButton);
		
		JButton placeStart = new JButton("Place Start Point");
		placeStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mapPanel.placePlayerStart();
			}
		});
		authorPanel.add(placeStart);
		
		JButton start = new JButton("Start/Stop Playing");
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mapPanel.startGame();
			}
		});
		authorPanel.add(start);
		
		JButton goUp = new JButton("↑");
		goUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mapPanel.goUp();
			}
		});
		authorPanel.add(goUp);
		
		JButton goDown = new JButton("↓");
		goDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mapPanel.goDown();
			}
		});
		authorPanel.add(goDown);
		
		JButton goLeft = new JButton("←");
		goLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mapPanel.goLeft();
			}
		});
		authorPanel.add(goLeft);

		JButton goRight = new JButton("→");
		goRight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mapPanel.goRight();
			}
		});
		authorPanel.add(goRight);
		
		//mapPanel holds the graphics of the map
		mapPanel = new MapPanel();
		
		splitPane.setRightComponent(mapPanel);
		
		frame.setBounds(200, 200, 650, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		e.getSource();
		mapPanel.paintRooms();
	}
}
