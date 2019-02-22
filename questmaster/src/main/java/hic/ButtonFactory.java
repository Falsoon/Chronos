package hic;

import civ.FormCiv;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * This class is used to encapsulate the creation of buttons and the logic behind
 * action listeners of each button 
 * 
 */
public class ButtonFactory implements ActionListener  {
	
	private AuthorWindow authorWindow;
	public JButton start, placeStart, undoButton, btnClear, addRoombtn1, addRoombtn2;
	
	public ButtonFactory(AuthorWindow aw) {
		this.authorWindow = aw;
		initialize();
	}
	
	private void initialize() {
		authorWindow.authorPanel = new AuthorPanel();
		//splitPane.setLeftComponent(authorPanel);
		JComboBox<String> authorModeBox = new JComboBox<String>(authorWindow.authorModes);
		authorModeBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (authorWindow.modeSelected == 0) {
					authorModeBox.removeItemAt(0);
				}
				@SuppressWarnings("unchecked")
				JComboBox<String> cb = (JComboBox<String>) e.getSource();
				switch (cb.getSelectedItem().toString()) {
				case "Author Room descriptions":
					authorWindow.modeSelected = 1;
					setMode1();
					authorWindow.authorPanel.update();
					break;
				case "Draw Rooms":
					authorWindow.modeSelected = 2;
					setMode2();
					authorWindow.authorPanel.update();
					break;
				}
			}
		});
		authorWindow.authorPanel.add(authorModeBox);

		authorWindow.wallCombo = new JComboBox<String>(authorWindow.wallTypes);
		authorWindow.wallCombo.addActionListener(this);
		authorWindow.wallCombo.setVisible(false);
		authorWindow.authorPanel.add(authorWindow.wallCombo);

		authorWindow.portalCombo = new JComboBox<String>(authorWindow.portalTypes);
		authorWindow.portalCombo.addActionListener(this);
		authorWindow.authorPanel.add(authorWindow.portalCombo);
		authorWindow.portalCombo.setVisible(false);

		// button resets the map
		btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				authorWindow.mapPanel.clear();
				authorWindow.authorPanel.Rooms.setSelectedIndex(0);
				authorWindow.wallCombo.setSelectedItem(authorWindow.wallTypes[0]);
				authorWindow.authorPanel.grabFocus();
			}
		});
		authorWindow.authorPanel.add(btnClear);
		btnClear.setVisible(false);

		// button allows author to undo last action.
		// ctrl+z is preferred design
		undoButton = new JButton("Undo");
		undoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				authorWindow.mapPanel.undo();
				authorWindow.authorPanel.grabFocus();
			}
		});
		authorWindow.authorPanel.add(undoButton);
		undoButton.setVisible(false);

		placeStart = new JButton("Place Start Point");
		placeStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				authorWindow.mapPanel.placePlayerStart();
			}
		});
		authorWindow.authorPanel.add(placeStart);
		placeStart.setVisible(false);

		start = new JButton("Start Playing");
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (authorWindow.mapPanel.placedPlayer()) {
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								PlayerWindow window = new PlayerWindow(authorWindow.mapPanel);
								window.frame.setVisible(true);
								AuthorWindow.civ.setSelectedRoom(null);
								authorWindow.mapPanel.repaint();
								AuthorWindow.civ.outputStory();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					authorWindow.mapPanel.startGame();
					authorWindow.frame.setVisible(false);
				}
			}
		});
		authorWindow.authorPanel.add(start);
		start.setVisible(false);

		authorWindow.authorPanel.Rooms = new JComboBox<String>();
		authorWindow.authorPanel.Rooms.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<String> cb = (JComboBox<String>) e.getSource();
				if (cb.getItemCount() > 1) {
					final String str = (String) cb.getSelectedItem();
					FormCiv fc = new FormCiv();
					if (!str.equals("Select Room")) {
						if (authorWindow.modeSelected == 1) {
							EventQueue.invokeLater(new Runnable() {
								public void run() {
									try {
										fc.setRoomReference(str);
										FormWindow window = new FormWindow(fc,
												fc.getRoomDrawn(str));
										window.frame.setVisible(true);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});
						}
						authorWindow.mapPanel.setSelectedRoom(str);		
					}else {
						authorWindow.mapPanel.setSelectedRoom(null);		
					}			
					authorWindow.mapPanel.repaint();
				}
			}

		});
		authorWindow.authorPanel.add(authorWindow.authorPanel.Rooms);

		addRoombtn1 = new JButton("Add Room");
		authorWindow.authorPanel.add(addRoombtn1);
		addRoombtn2 = new JButton("Add Room");
		authorWindow.authorPanel.add(addRoombtn2);
		addRoombtn2.setVisible(false);
		addRoombtn1.setVisible(false);
		addRoombtn1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							FormWindow window = new FormWindow(new FormCiv(), false);
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
				String str = (String) authorWindow.authorPanel.Rooms.getSelectedItem();
				if (str.equals("Select Room")) {
					authorWindow.mapPanel.paintRooms();
				} else {
					authorWindow.mapPanel.drawRoom(str);
				}
			}
		});
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		@SuppressWarnings("unchecked")
		JComboBox<String> cb = (JComboBox<String>) e.getSource();
		switch (cb.getSelectedItem().toString()) {
		case "Opaque":
			authorWindow.mapPanel.paintRooms();
			break;
		case "Transparent":
			authorWindow.mapPanel.paintWalls();
			break;
		case "Walls":
			authorWindow.mapPanel.stopDrawing();
			break;
		case "Doors":
			authorWindow.mapPanel.paintDoors();
			break;
		case "Archway":
         authorWindow.mapPanel.paintArchway();
         break;
		case "Portals":
			authorWindow.mapPanel.stopDrawing();
			break;
		default:
			System.err.println("ComboBox Error");
		}
		authorWindow.authorPanel.update();
	}
	
	public void setMode2() {
		start.setVisible(true);
		placeStart.setVisible(true);
		undoButton.setVisible(true);
		btnClear.setVisible(true);
		authorWindow.wallCombo.setVisible(true);
		authorWindow.portalCombo.setVisible(true);
		addRoombtn1.setVisible(false);
		addRoombtn2.setVisible(true);
	}

	public void setMode1() {
		start.setVisible(false);
		placeStart.setVisible(false);
		undoButton.setVisible(false);
		btnClear.setVisible(false);
		authorWindow.wallCombo.setVisible(false);
		authorWindow.portalCombo.setVisible(false);
		addRoombtn2.setVisible(false);
		addRoombtn1.setVisible(true);
	}
	
}
