import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.Scrollable;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;

/**
 * UI class for author side of application. Contains main Method
 */

public class AuthorWindow extends JPanel implements ActionListener {

	private static final Room selectRoom = new SelectRoom();
	private JFrame frame; 
	private AuthorPanel authorPanel;
	private MapPanel mapPanel;
	private String[] wallTypes = { "Walls", "Opaque", "Transparent" };
	private String[] portalTypes = { "Portals", "Doors" };
	private String[] authorModes = { "Select Authoring Mode", "Author Room descriptions", "Draw Rooms" };
	JComboBox<Room> Rooms;
	JComboBox<String> wallCombo, portalCombo;
	JButton start, placeStart, undoButton, btnClear, addRoombtn1, addRoombtn2;
	private int modeSelected = 0;
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
		initialize();
		update();
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
		splitPane.setLeftComponent(authorPanel);

		JComboBox<String> authorModeBox = new JComboBox<String>(authorModes);
		authorModeBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (modeSelected == 0) {
					authorModeBox.removeItemAt(0);
				}
				JComboBox<String> cb = (JComboBox<String>) e.getSource();
				switch (cb.getSelectedItem().toString()) {
				case "Author Room descriptions":
					modeSelected = 1;
					setMode1();
					update();
					break;
				case "Draw Rooms":
					modeSelected = 2;
					setMode2();
					update();
					break;
				}
			}
		});
		authorPanel.add(authorModeBox);

		wallCombo = new JComboBox<String>(wallTypes);
		wallCombo.addActionListener(this);
		wallCombo.setVisible(false);
		authorPanel.add(wallCombo);

		portalCombo = new JComboBox<String>(portalTypes);
		portalCombo.addActionListener(this);
		authorPanel.add(portalCombo);
		portalCombo.setVisible(false);

		// button resets the map
		btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mapPanel.clear();
				Rooms.setSelectedIndex(0);
				wallCombo.setSelectedItem(wallTypes[0]);
				
				authorPanel.grabFocus();
			}
		});
		authorPanel.add(btnClear);
		btnClear.setVisible(false);

		// button allows author to undo last action.
		// ctrl+z is preferred design
		undoButton = new JButton("Undo");
		undoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mapPanel.undo();
				authorPanel.grabFocus();
			}
		});
		authorPanel.add(undoButton);
		undoButton.setVisible(false);

		placeStart = new JButton("Place Start Point");
		placeStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mapPanel.placePlayerStart();
			}
		});
		authorPanel.add(placeStart);
		placeStart.setVisible(false);

		start = new JButton("Start Playing");
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (mapPanel.placedPlayer()) {
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								PlayerWindow window = new PlayerWindow(mapPanel);
								window.frame.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					mapPanel.startGame();
					frame.setVisible(false);
				}
			}
		});
		authorPanel.add(start);
		start.setVisible(false);

		Rooms = new JComboBox<Room>();
		Rooms.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int id;
				JComboBox<Room> cb = (JComboBox<Room>) e.getSource();
				if (cb.getItemCount() > 1) {
					Room r = (Room) cb.getSelectedItem();
					id = r.ROOMID;
					if (id != -1) {
						if (modeSelected == 1) {
							EventQueue.invokeLater(new Runnable() {
								public void run() {
									try {
										// TODO Should not access RoomList
										FormWindow window = new FormWindow(RoomList.getRoomById(id),
												RoomList.getRoomById(id).path != null);
										window.frame.setVisible(true);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});
						}
					}else {
						r = null;
					}
					mapPanel.setSelectedRoom(r);					
					mapPanel.repaint();
				}
			}

		});
		authorPanel.add(Rooms);

		addRoombtn1 = new JButton("Add Room");
		authorPanel.add(addRoombtn1);
		addRoombtn2 = new JButton("Add Room");
		authorPanel.add(addRoombtn2);
		addRoombtn2.setVisible(false);
		addRoombtn1.setVisible(false);
		addRoombtn1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							FormWindow window = new FormWindow(new Room(), false);
							window.frame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		addRoombtn2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Room r = (Room) Rooms.getSelectedItem();
				if (r.ROOMID == -1) {
					mapPanel.paintRooms();
				} else {
					mapPanel.drawRoom(r);
				}
			}
		});

	}

	private void setMode2() {
		start.setVisible(true);
		placeStart.setVisible(true);
		undoButton.setVisible(true);
		btnClear.setVisible(true);
		wallCombo.setVisible(true);
		portalCombo.setVisible(true);
		addRoombtn1.setVisible(false);
		addRoombtn2.setVisible(true);
	}

	private void setMode1() {
		start.setVisible(false);
		placeStart.setVisible(false);
		undoButton.setVisible(false);
		btnClear.setVisible(false);
		wallCombo.setVisible(false);
		portalCombo.setVisible(false);
		addRoombtn2.setVisible(false);
		addRoombtn1.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JComboBox<String> cb = (JComboBox<String>) e.getSource();
		switch (cb.getSelectedItem().toString()) {
		case "Opaque":
			mapPanel.paintRooms();
			break;
		case "Transparent":
			mapPanel.paintWalls();
			break;
		case "Walls":
			mapPanel.stopDrawing();
			break;
		case "Doors":
			mapPanel.paintDoors();
			break;
		case "Portals":
			mapPanel.stopDrawing();
			break;
		default:
			System.err.println("ComboBox Error");
		}
		update();
	}

	void update() {
		int index = Rooms.getSelectedIndex();
		if(index == -1) {index = 0;}
		Rooms.removeAllItems();
		Rooms.addItem(selectRoom);
		// TODO Should not get Room List Directly
		for (int i = 0; i < RoomList.list.size(); i++) {
			Rooms.addItem(RoomList.list.get(i));
		}
		Rooms.setSelectedIndex(index);
	}
}
