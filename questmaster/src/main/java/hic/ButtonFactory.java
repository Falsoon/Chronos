package hic;

import civ.FormCiv;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
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
		/*
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
            setMode1();
            authorWindow.authorPanel.update();
            break;
			case "Draw Rooms":
			
            authorWindow.modeSelected = 2;
            setMode2();
				authorWindow.authorPanel.update();
				
            break;
         }
		});
		
		authorWindow.authorPanel.add(authorModeBox);
		*/

		// @author Ryan - and now, the proper buttons
		JToggleButton btnOpaqueWalls = new JToggleButton("Opaque Walls");
		btnOpaqueWalls.setAlignmentX(Component.CENTER_ALIGNMENT);
		JToggleButton btnTransWalls = new JToggleButton("Transparent Walls");
		btnTransWalls.setAlignmentX(Component.CENTER_ALIGNMENT);
		JToggleButton btnArchways = new JToggleButton("Archways");
		btnArchways.setAlignmentX(Component.CENTER_ALIGNMENT);
		JToggleButton btnProp = new JToggleButton("Set Properties");
		btnProp.setAlignmentX(Component.CENTER_ALIGNMENT);

		JButton btnPlayer = new JButton("Set Start Point");
		btnPlayer.setAlignmentX(Component.CENTER_ALIGNMENT);

		JSeparator line1 = new JSeparator(SwingConstants.HORIZONTAL);

		JButton btnVoid = new JButton("area to be filled later");
		btnVoid.setAlignmentX(Component.CENTER_ALIGNMENT);

		JSeparator line2 = new JSeparator(SwingConstants.HORIZONTAL);

		JButton btnUndo = new JButton("Undo");
		btnUndo.setAlignmentX(Component.CENTER_ALIGNMENT);
		JButton btnClear = new JButton("Clear");
		btnClear.setAlignmentX(Component.CENTER_ALIGNMENT);

		authorWindow.authorPanel.add(btnOpaqueWalls);
		authorWindow.authorPanel.add(btnTransWalls);
		authorWindow.authorPanel.add(btnArchways);
		authorWindow.authorPanel.add(btnPlayer);
		authorWindow.authorPanel.add(btnProp);
		authorWindow.authorPanel.add(line1);
		authorWindow.authorPanel.add(btnVoid);
		authorWindow.authorPanel.add(line2);
		authorWindow.authorPanel.add(btnUndo);
		authorWindow.authorPanel.add(btnClear);

		btnOpaqueWalls.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				authorWindow.mapPanel.stopDrawing();
				authorWindow.mapPanel.paintRooms();
				btnTransWalls.setSelected(false);
				btnArchways.setSelected(false);
				btnProp.setSelected(false);
			}
		});

		btnTransWalls.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				authorWindow.mapPanel.stopDrawing();
				authorWindow.mapPanel.paintWalls();
				btnOpaqueWalls.setSelected(false);
				btnArchways.setSelected(false);
				btnProp.setSelected(false);
			}
		});

		btnArchways.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				authorWindow.mapPanel.stopDrawing();
				authorWindow.mapPanel.paintArchway();
				btnOpaqueWalls.setSelected(false);
				btnTransWalls.setSelected(false);
				btnProp.setSelected(false);
			}
		});

		btnProp.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				authorWindow.mapPanel.stopDrawing();
				btnOpaqueWalls.setSelected(false);
				btnTransWalls.setSelected(false);
				btnArchways.setSelected(false);
			}
		});

		btnPlayer.addActionListener(e -> {
			authorWindow.mapPanel.placePlayerStart();
			authorWindow.mapPanel.stopDrawing();
			btnOpaqueWalls.setSelected(false);
			btnTransWalls.setSelected(false);
			btnArchways.setSelected(false);
		});

		


		// btnWalls.addActionListener(e -> {
		// 	authorWindow.mapPanel.paintRooms();
		// 	authorWindow.authorPanel.update();
		// });

		/*
		authorWindow.wallCombo = new JComboBox<>(authorWindow.wallTypes);
		authorWindow.wallCombo.addActionListener(this);
		authorWindow.wallCombo.setVisible(false);
		authorWindow.authorPanel.add(authorWindow.wallCombo);

		authorWindow.portalCombo = new JComboBox<>(authorWindow.portalTypes);
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
                  authorWindow.mapPanel.civ.stopDrawing();
                  authorWindow.mapPanel.setPlayerMode(true);
                  PlayerWindow window = new PlayerWindow(authorWindow.mapPanel);
                  window.frame.setVisible(true);
                  AuthorWindow.civ.setSelectedRoom(null);
                  authorWindow.mapPanel.repaint();

               } catch (Exception e13) {
                  e13.printStackTrace();
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
            if (!"Select Room".equals(str)) {
               if (authorWindow.modeSelected == 1) {
                  EventQueue.invokeLater(() -> {
                     try {
                        fc.setRoomReference(str);
                        FormWindow window = new FormWindow(fc,
                              fc.getRoomDrawn(str));
                        window.frame.setVisible(true);
                     } catch (Exception e1) {
                        e1.printStackTrace();
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

		addRoombtn1 = new JButton("Add Room");
		authorWindow.authorPanel.add(addRoombtn1);
		addRoombtn2 = new JButton("Add Room");
		authorWindow.authorPanel.add(addRoombtn2);
		addRoombtn2.setVisible(false);
		addRoombtn1.setVisible(false);
		addRoombtn1.addActionListener(e -> EventQueue.invokeLater(() -> {
         try {
            FormWindow window = new FormWindow(new FormCiv(), false);
            window.frame.setVisible(true);
         } catch (Exception e12) {
            e12.printStackTrace();
         }
      }));
		addRoombtn2.addActionListener(e -> {
         String str = (String) authorWindow.authorPanel.Rooms.getSelectedItem();
         if ("Select Room".equals(str)) {
            authorWindow.mapPanel.paintRooms();
         } else {
            authorWindow.mapPanel.drawRoom(str);
         }
		});
		*/
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
