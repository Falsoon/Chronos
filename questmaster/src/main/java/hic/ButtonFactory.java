package main.java.hic;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;

import main.java.civ.*;
/**
 * This class is used to encapsulate the creation of buttons and the logic behind
 * action listeners of each button
 *
 */
public class ButtonFactory implements ActionListener  {

	private AuthorWindow authorWindow;
	public JButton start, placeStart, undoButton, btnClear, addRoombtnDescription, addRoombtnDraw;

	public ButtonFactory(AuthorWindow aw) {
		this.authorWindow = aw;
		initialize();
	}

	private void initialize() {
		authorWindow.authorPanel = new AuthorPanel();
		//splitPane.setLeftComponent(authorPanel);
		JComboBox<String> authorModeBox = new JComboBox<String>(authorWindow.authorModes);
		authorModeBox.addActionListener(e -> {
            if (authorWindow.modeSelected == 0) {
                authorModeBox.removeItemAt(0);
            }
            @SuppressWarnings("unchecked")
            JComboBox<String> cb = (JComboBox<String>) e.getSource();
            switch (cb.getSelectedItem().toString()) {
            case "Author Room descriptions":
                authorWindow.modeSelected = 1;
                setModeDescription();
                authorWindow.authorPanel.update();
                break;
            case "Draw Rooms":
                authorWindow.modeSelected = 2;
                setModeDraw();
                authorWindow.authorPanel.update();
                break;
            }
        });
		authorWindow.authorPanel.add(authorModeBox);

		authorWindow.wallCombo = new JComboBox<>(authorWindow.wallTypes);
		authorWindow.wallCombo.addActionListener(this);
		authorWindow.wallCombo.setVisible(false);
		authorWindow.authorPanel.add(authorWindow.wallCombo);

		authorWindow.portalCombo = new JComboBox<String>(authorWindow.portalTypes);
		authorWindow.portalCombo.addActionListener(this);
		authorWindow.authorPanel.add(authorWindow.portalCombo);
		authorWindow.portalCombo.setVisible(false);

		// button resets the map
		btnClear = new JButton("Clear");
		btnClear.addActionListener(e -> {
            authorWindow.mapPanel.clear();
            authorWindow.authorPanel.Rooms.setSelectedIndex(0);
            authorWindow.wallCombo.setSelectedItem(authorWindow.wallTypes[0]);
            authorWindow.authorPanel.grabFocus();
        });
		authorWindow.authorPanel.add(btnClear);
		btnClear.setVisible(false);

		// button allows author to undo last action.
		// ctrl+z is preferred design
		undoButton = new JButton("Undo");
		undoButton.addActionListener(e -> {
            authorWindow.mapPanel.undo();
            authorWindow.authorPanel.grabFocus();
        });
		authorWindow.authorPanel.add(undoButton);
		undoButton.setVisible(false);

		placeStart = new JButton("Place Start Point");
		placeStart.addActionListener(e -> authorWindow.mapPanel.placePlayerStart());
		authorWindow.authorPanel.add(placeStart);
		placeStart.setVisible(false);

		start = new JButton("Start Playing");
		start.addActionListener(e -> {
            if (authorWindow.mapPanel.placedPlayer()) {
                EventQueue.invokeLater(() -> {
                    try {
                        PlayerWindow window = new PlayerWindow(authorWindow.mapPanel);
                        window.frame.setVisible(true);
                        AuthorWindow.civ.setSelectedRoom(null);
                        authorWindow.mapPanel.repaint();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                });
                authorWindow.mapPanel.startGame();
                authorWindow.frame.setVisible(false);
            }
        });
		authorWindow.authorPanel.add(start);
		start.setVisible(false);

		authorWindow.authorPanel.Rooms = new JComboBox<>();
		authorWindow.authorPanel.Rooms.addActionListener(e -> {
            @SuppressWarnings("unchecked")
            JComboBox<String> cb = (JComboBox<String>) e.getSource();
            if (cb.getItemCount() > 1) {
                final String str = (String) cb.getSelectedItem();
                FormCiv fc = new FormCiv();
                if (!str.equals("Select Room")) {
                    if (authorWindow.modeSelected == 1) {
                        EventQueue.invokeLater(() -> {
                            try {
                                fc.setRoomReference(str);
                                FormWindow window = new FormWindow(fc,
                                        fc.getRoomDrawn(str));
                                window.frame.setVisible(true);
                            } catch (Exception e12) {
                                e12.printStackTrace();
                            }
                        });
                    }
                    authorWindow.mapPanel.setSelectedRoom(str);
                }else {
                    authorWindow.mapPanel.setSelectedRoom(null);
                }
                authorWindow.mapPanel.repaint();
            }
        });
		authorWindow.authorPanel.add(authorWindow.authorPanel.Rooms);

		addRoombtnDescription = new JButton("Add Room");
		authorWindow.authorPanel.add(addRoombtnDescription);
		addRoombtnDraw = new JButton("Add Room");
		authorWindow.authorPanel.add(addRoombtnDraw);
		addRoombtnDraw.setVisible(false);
		addRoombtnDescription.setVisible(false);
		addRoombtnDescription.addActionListener(e -> EventQueue.invokeLater(() -> {
            try {
                FormWindow window = new FormWindow(new FormCiv(), false);
                window.frame.setVisible(true);
            } catch (Exception e13) {
                e13.printStackTrace();
            }
        }));
		addRoombtnDraw.addActionListener(e -> {
            String str = (String) authorWindow.authorPanel.Rooms.getSelectedItem();
            if (str.equals("Select Room")) {
                authorWindow.mapPanel.paintRooms();
            } else {
                authorWindow.mapPanel.drawRoom(str);
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
		case "Portals":
			authorWindow.mapPanel.stopDrawing();
			break;
		default:
			System.err.println("ComboBox Error");
		}
		authorWindow.authorPanel.update();
	}

	public void setModeDraw() {
		start.setVisible(true);
		placeStart.setVisible(true);
		undoButton.setVisible(true);
		btnClear.setVisible(true);
		authorWindow.wallCombo.setVisible(true);
		authorWindow.portalCombo.setVisible(true);
		addRoombtnDescription.setVisible(false);
		addRoombtnDraw.setVisible(true);
	}

	public void setModeDescription() {
		start.setVisible(false);
		placeStart.setVisible(false);
		undoButton.setVisible(false);
		btnClear.setVisible(false);
		authorWindow.wallCombo.setVisible(false);
		authorWindow.portalCombo.setVisible(false);
		addRoombtnDraw.setVisible(false);
		addRoombtnDescription.setVisible(true);
	}

}
