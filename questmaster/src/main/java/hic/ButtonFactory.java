package hic;

import javax.swing.*;
import java.awt.*;
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
	public JButton btnPlay, btnPlayer, btnUndo, btnClear;
	public JButton btnSave, btnRestore;
	public JToggleButton btnOpaqueWalls, btnTransWalls, btnArchways, btnProp, btnDoors, btnDelete;
	
	public ButtonFactory(AuthorWindow aw) {
		this.authorWindow = aw;
		rdi = new RoomDescInsert(aw);
		initialize();
	}
	
	private void initialize() {

		authorWindow.authorPanel = new AuthorPanel();

		btnOpaqueWalls = new JToggleButton("Opaque Walls");
		btnOpaqueWalls.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnTransWalls = new JToggleButton("Transparent Walls");
		btnTransWalls.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnArchways = new JToggleButton("Archways");
		btnArchways.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnDoors = new JToggleButton("Doors");
		btnDoors.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnProp = new JToggleButton("Set Properties");
		btnProp.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnDelete = new JToggleButton("Delete Walls/Passageways");
		btnDelete.setAlignmentX(Component.CENTER_ALIGNMENT);

		btnPlayer = new JButton("Set Start Point");
		btnPlayer.setAlignmentX(Component.CENTER_ALIGNMENT);

		JSeparator line1 = new JSeparator(SwingConstants.HORIZONTAL);

		JSeparator line2 = new JSeparator(SwingConstants.HORIZONTAL);

		btnUndo = new JButton("Undo");
		btnUndo.setAlignmentX(Component.CENTER_ALIGNMENT);

		btnSave = new JButton("Save Map");
		btnSave.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnRestore = new JButton("Restore Map");
		btnRestore.setAlignmentX(Component.CENTER_ALIGNMENT);

		JSeparator line3 = new JSeparator(SwingConstants.HORIZONTAL);

		btnPlay = new JButton("Play");
		btnPlay.setAlignmentX(Component.CENTER_ALIGNMENT);

		JSeparator line4 = new JSeparator(SwingConstants.HORIZONTAL);

		btnClear = new JButton("Clear");
		btnClear.setAlignmentX(Component.CENTER_ALIGNMENT);

		authorWindow.authorPanel.add(btnOpaqueWalls);
		authorWindow.authorPanel.add(btnTransWalls);
		authorWindow.authorPanel.add(btnArchways);
		authorWindow.authorPanel.add(btnDoors);
		authorWindow.authorPanel.add(btnProp);
		authorWindow.authorPanel.add(btnPlayer);
		authorWindow.authorPanel.add(btnDelete);
		authorWindow.authorPanel.add(line1);

		authorWindow.authorPanel.add(rdi);

		authorWindow.authorPanel.add(line2);
		//authorWindow.authorPanel.add(btnUndo);
		authorWindow.authorPanel.add(btnSave);
		authorWindow.authorPanel.add(btnRestore);
		authorWindow.authorPanel.add(line3);
		authorWindow.authorPanel.add(btnPlay);
		authorWindow.authorPanel.add(line4);
		authorWindow.authorPanel.add(btnClear);

		ButtonGroup btnList = new ButtonGroup();
		btnList.add(btnOpaqueWalls);
		btnList.add(btnTransWalls);
		btnList.add(btnArchways);
		btnList.add(btnDoors);
		btnList.add(btnProp);
		btnList.add(btnDelete);

		btnOpaqueWalls.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				//authorWindow.mapPanel.stopDrawing();
				//authorWindow.mapPanel.stopPlacingPlayer();
				authorWindow.mapPanel.paintRooms();
			}
		});

		btnTransWalls.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				//authorWindow.mapPanel.stopDrawing();
				//authorWindow.mapPanel.stopPlacingPlayer();
				authorWindow.mapPanel.paintWalls();
			}
		});

		btnArchways.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				//authorWindow.mapPanel.stopDrawing();
				//authorWindow.mapPanel.stopPlacingPlayer();
				authorWindow.mapPanel.paintArchway();
			}
		});

		btnArchways.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				//authorWindow.mapPanel.stopDrawing();
				//authorWindow.mapPanel.stopPlacingPlayer();
				authorWindow.mapPanel.paintArchway();
			}
		});

		btnDoors.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				//authorWindow.mapPanel.stopDrawing();
				//authorWindow.mapPanel.stopPlacingPlayer();
				authorWindow.mapPanel.paintDoors();
			}
		});

		btnProp.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				authorWindow.mapPanel.stopDrawing();
				authorWindow.mapPanel.stopPlacingPlayer();
			}
		});

		btnDelete.addItemListener(e->{
			if (e.getStateChange() == ItemEvent.SELECTED) {
				authorWindow.mapPanel.stopDrawing();
				authorWindow.mapPanel.stopPlacingPlayer();
				authorWindow.mapPanel.delete();
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

		btnSave.addActionListener(e -> {
			authorWindow.mapPanel.save();
			authorWindow.civ.setSelectedRoom(null);
			authorWindow.authorPanel.grabFocus();
			btnProp.doClick();
});

		btnRestore.addActionListener(e -> {
			authorWindow.mapPanel.restore();
			authorWindow.civ.setSelectedRoom(null);
			authorWindow.authorPanel.grabFocus();
			btnProp.doClick();
		});

		btnPlay.addActionListener(e -> {
         if (authorWindow.mapPanel.placedPlayer()) {
            EventQueue.invokeLater(() -> {
               try {
                  authorWindow.mapPanel.civ.stopDrawing();
				  authorWindow.mapPanel.setPlayerMode(true);
				  authorWindow.mapPanel.save();
                  PlayerWindow window = new PlayerWindow(authorWindow.mapPanel);
                  window.frame.setVisible(true);
                  authorWindow.civ.setSelectedRoom(null);
				  authorWindow.mapPanel.repaint();

               } catch (Exception e13) {
                  e13.printStackTrace();
               }
            });
			authorWindow.mapPanel.startGame();
			authorWindow.frame.setVisible(false);
         }
		});

		btnClear.addActionListener(e -> {
			int opt = JOptionPane.showConfirmDialog(null, "Are you sure you want to clear?", "Clear",
					JOptionPane.YES_NO_OPTION);
			if (opt == JOptionPane.YES_OPTION) {
				btnProp.doClick();
				authorWindow.mapPanel.clear();
				// authorWindow.authorPanel.Rooms.setSelectedIndex(0);
				// authorWindow.wallCombo.setSelectedItem(authorWindow.wallTypes[0]);
				authorWindow.authorPanel.grabFocus();
			}
		});
	}
}
