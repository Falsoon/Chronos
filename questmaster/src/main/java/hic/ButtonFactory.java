package hic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
/**
 * This class is used to encapsulate the creation of buttons and the logic behind
 * action listeners of each button 
 * 
 * @author Ryan Wires - I done fixed it good
 */
public class ButtonFactory {
	
	private AuthorWindow authorWindow;
	public RoomDescInsert rdi;
	public JButton start, placeStart, undoButton, btnClear, addRoombtn1, addRoombtn2;
	
	public ButtonFactory(AuthorWindow aw) {
		this.authorWindow = aw;
		rdi = new RoomDescInsert(aw);
		initialize();
	}
	
	private void initialize() {
		authorWindow.authorPanel = new AuthorPanel();

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
		JButton btnPlay = new JButton("Play");
		btnPlay.setAlignmentX(Component.CENTER_ALIGNMENT);

		JSeparator line3 = new JSeparator(SwingConstants.HORIZONTAL);

		JButton btnClear = new JButton("Clear");
		btnClear.setAlignmentX(Component.CENTER_ALIGNMENT);

		authorWindow.authorPanel.add(btnOpaqueWalls);
		authorWindow.authorPanel.add(btnTransWalls);
		authorWindow.authorPanel.add(btnArchways);
		authorWindow.authorPanel.add(btnProp);
		authorWindow.authorPanel.add(btnPlayer);
		authorWindow.authorPanel.add(line1);

		authorWindow.authorPanel.add(rdi);

		authorWindow.authorPanel.add(line2);
		authorWindow.authorPanel.add(btnUndo);
		authorWindow.authorPanel.add(line2);
		authorWindow.authorPanel.add(btnPlay);
		authorWindow.authorPanel.add(line3);
		authorWindow.authorPanel.add(btnClear);

		ButtonGroup btnList = new ButtonGroup();
		btnList.add(btnOpaqueWalls);
		btnList.add(btnTransWalls);
		btnList.add(btnArchways);
		btnList.add(btnProp);

		btnOpaqueWalls.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				authorWindow.mapPanel.stopDrawing();
				authorWindow.mapPanel.stopPlacingPlayer();
				authorWindow.mapPanel.paintRooms();
			}
		});

		btnTransWalls.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				authorWindow.mapPanel.stopDrawing();
				authorWindow.mapPanel.stopPlacingPlayer();
				authorWindow.mapPanel.paintWalls();
			}
		});

		btnArchways.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				authorWindow.mapPanel.stopDrawing();
				authorWindow.mapPanel.stopPlacingPlayer();
				authorWindow.mapPanel.paintArchway();
			}
		});

		btnProp.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				authorWindow.mapPanel.stopDrawing();
				authorWindow.mapPanel.stopPlacingPlayer();
			}
		});

		btnPlayer.addActionListener(e -> {
			btnProp.doClick();
			authorWindow.mapPanel.placePlayerStart();
			
		});

		btnUndo.addActionListener(e -> {
         authorWindow.mapPanel.undo();
         authorWindow.authorPanel.grabFocus();
		});
		
		btnPlay.addActionListener(e -> {
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
         }
		});
		
		btnUndo.addActionListener(e -> {
         authorWindow.mapPanel.undo();
         authorWindow.authorPanel.grabFocus();
		});
		
		btnClear.addActionListener(e -> {
         authorWindow.mapPanel.clear();
         authorWindow.authorPanel.Rooms.setSelectedIndex(0);
         authorWindow.wallCombo.setSelectedItem(authorWindow.wallTypes[0]);
         authorWindow.authorPanel.grabFocus();
		});
	}
}
