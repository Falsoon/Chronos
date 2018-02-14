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
		JButton btnOutline = new JButton("Draw Map Outline");
		btnOutline.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mapPanel.mapOutline();
			}
		});
		authorPanel.add(btnOutline);
		
		//button indicates that the outline is ready to be split into rooms
		JButton btnWalls = new JButton("Transparent Wall");
		btnWalls.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mapPanel.addWalls();
			}
		});
		authorPanel.add(btnWalls);
		
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
		
		//mapPanel holds the graphics of the map
		mapPanel = new MapPanel();
		
		splitPane.setRightComponent(mapPanel);
		
		frame.setBounds(200, 200, 650, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		e.getSource();
		mapPanel.mapOutline();
	}
}
