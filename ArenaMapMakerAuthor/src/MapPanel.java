import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.*;
import java.awt.geom.GeneralPath;
import java.util.Hashtable;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.StateEdit;
import javax.swing.undo.StateEditable;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEditSupport;

public class MapPanel extends JPanel implements StateEditable {

	private static final Object MAP_KEY = "MapKey";
	private boolean isPlaying, placingPlayer, creating;
	private final int GRIDDISTANCE = 15;
	public GeneralPath path = new GeneralPath();
	public GeneralPath wallPath = new GeneralPath();
	public Point start;
	public Point playerPos;
	public Room room;
	UndoableEditSupport undoSupport = new UndoableEditSupport(this);
	UndoManager manager = new UndoManager();
	private Map map;

	/**
	 * Constructor of MapPanel adds the appropriate action listeners
	 */
	public MapPanel() {
		map = new Map();
		// Anonymous class was used to access MapPanel fields
		MouseListener mousehandler = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (creating) {
					StateEdit stateEdit = new StateEdit(MapPanel.this);
					creating = map.mousePressed(e);
					repaint();
					// stateEdit is used for undo
					stateEdit.end();
					manager.addEdit(stateEdit);
				}
				if (placingPlayer) {
					playerPos = e.getPoint();
					playerPos.setLocation(Math.round(playerPos.x / GRIDDISTANCE) * GRIDDISTANCE,
							Math.round(playerPos.y / GRIDDISTANCE) * GRIDDISTANCE);
					repaint();
				}
			}
		};
		// add listeners
		//addKeyListener(this);
		addUndoableEditListener(manager);
		addMouseListener(mousehandler);
	}

	/**
	 * Changes state of MapPanel to draw Outline
	 */
	public void paintRooms() {
		placingPlayer = false;
		map.outlining();
		creating = true;
	}

	/**
	 * Changes state of MapPanel to add walls
	 */
	public void paintWalls() {
		placingPlayer = false;
		map.walling();
		creating =true;
	}

	/**
	 * Resets state of MapPanel
	 */
	public void clear() {
		map = new Map();
		placingPlayer = false;
		repaint();
		creating = false;
	}

	/**
	 * This method is inherited by JPanel paintComponents will draw on the panel
	 * each time repaint() is called
	 */
	public void paintComponent(Graphics g) {
		// Background
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, 1200, 1200);
		// Grid points
		g.setColor(Color.white);
		for (int i = 0; i < 600; i++) {
			for (int j = 0; j < 600; j++) {
				g.drawLine(GRIDDISTANCE * (i + 1), GRIDDISTANCE * (j + 1), GRIDDISTANCE * (i + 1),
						GRIDDISTANCE * (j + 1));
			}
		}
		map.draw(g);
		if (placingPlayer || isPlaying) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(Color.BLACK);
			g2d.drawString("¶", playerPos.x, playerPos.y);
			placingPlayer = false;
		}
	}

	/**
	 * Helps with undo support
	 * 
	 * @param undoableEditListener
	 */
	public void addUndoableEditListener(UndoableEditListener undoableEditListener) {
		undoSupport.addUndoableEditListener(undoableEditListener);
	}

	/**
	 * Required by StateEditable interface for undo support. This method is called
	 * when a state edit is created and when an edit ends
	 */
	public void storeState(Hashtable state) {
		state.put(MAP_KEY, getMap());
	}

	/**
	 * This method is a helper method to get a copy of the current path rather than
	 * a reference to the path itself.
	 * 
	 * @return Copy of Path
	 */
	private Map getMap() {
		return map.copy();
	}

	/**
	 * Called when the UndoMananger needs to undo to get to an earlier state.
	 */
	public void restoreState(Hashtable state) {
		Map newP = (Map) state.get(MAP_KEY);
		if (newP != null) {
			map = newP;
		}
	}

	/**
	 * performs undo
	 */
	public void undo() {
		if (manager.canUndo()) {
			manager.undo();
			creating = true;
			map.undo();
		} else {
			JOptionPane.showMessageDialog(this, "Cannot Undo");
		}
		repaint();
	}

	public void placePlayerStart() {
		placingPlayer = true;
	}

	public void startGame() {
		isPlaying = !isPlaying;
		creating = false;
	}

	public void goUp() {
		if (isPlaying) {
			playerPos.move(playerPos.x, playerPos.y - GRIDDISTANCE);
			repaint();
		}
	}

	public void goDown() {
		if (isPlaying) {
			playerPos.move(playerPos.x, playerPos.y + GRIDDISTANCE);
			repaint();
		}
	}

	public void goLeft() {
		if (isPlaying) {
			playerPos.move(playerPos.x - GRIDDISTANCE, playerPos.y);
			repaint();
		}
	}

	public void goRight() {
		if (isPlaying) {
			playerPos.move(playerPos.x + GRIDDISTANCE, playerPos.y);
			repaint();
		}
	}
}
