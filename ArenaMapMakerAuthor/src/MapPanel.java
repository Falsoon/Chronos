import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.StateEdit;
import javax.swing.undo.StateEditable;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEditSupport;

public class MapPanel extends JPanel implements StateEditable, KeyListener {

	private static final Object MAP_KEY = "MapKey";
	private boolean isPlaying, placingPlayer, outlining, creatingWalls, drawing;
	private final int GRIDDISTANCE = 15;
	public GeneralPath path = new GeneralPath();
	public GeneralPath wallPath = new GeneralPath();
	public Point start;
	//public Room room;
	public Point playerPos;
	UndoableEditSupport undoSupport = new UndoableEditSupport(this);
	UndoManager manager = new UndoManager();
	private Map map;
/**
 * Constructor of MapPanel adds the appropriate action listeners
 */
	public MapPanel() {
		map = new Map();
		//Anonymous class was used to access MapPanel fields
		MouseListener mousehandler = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				StateEdit stateEdit = new StateEdit(MapPanel.this);
				map.mousePressed(e);
				repaint();
				//stateEdit is used for undo
				stateEdit.end();
				manager.addEdit(stateEdit);
				if (placingPlayer) {
					playerPos = e.getPoint();
					playerPos.setLocation(Math.round(playerPos.x / 10) * 10, Math.round(playerPos.y / 10) * 10);
					repaint();
				}
			}
		};
		//add listeners
		addKeyListener(this);
		addUndoableEditListener(manager);
		addMouseListener(mousehandler);
	}

	/**
	 * Changes state of MapPanel to draw Outline
	 */
	public void paintRooms() {
		outlining = true;
		creatingWalls = false;
		placingPlayer = false;
		map.outlining();
	}
	
	/**
	 * Changes state of MapPanel to add walls
	 */
	public void paintWalls() {
		creatingWalls = true;
		placingPlayer = false;
		map.walling();
	}

	/**
	 * Resets state of MapPanel
	 */
	public void clear() {
		outlining = false;
		creatingWalls = false;
		drawing = false;
		placingPlayer = false;
		path = null;
		//room = null;
		repaint();
	}

	/**
	 * This method is inherited by JPanel
	 * paintComponents will draw on the panel each time repaint() is called
	 */
	public void paintComponent(Graphics g) {
		//Background
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, 1200, 1200);
		//Grid points
		g.setColor(Color.white);
		for (int i = 0; i < 600; i++) {
			for (int j = 0; j < 600; j++) {
				g.drawLine(GRIDDISTANCE * (i + 1), GRIDDISTANCE * (j + 1), GRIDDISTANCE * (i + 1), GRIDDISTANCE * (j + 1));
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
	 * Required by StateEditable interface for undo support. 
	 * This method is called when a state edit is created and when an edit ends
	 */
	public void storeState(Hashtable state) {
		state.put(MAP_KEY, getMap());
	}

	/**
	 * This method is a helper method to get a copy of the current path rather than a reference to the path itself.
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

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
/**
 * ctrl+z for undo is not yet working
 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.isControlDown() && e.getKeyChar() == 'z') {
			manager.undo();
			repaint();
		}
		if (isPlaying) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				// Move player up
				playerPos.move(playerPos.x, playerPos.y+10);
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				// Move player down
				playerPos.move(playerPos.x, playerPos.y-10);
			}
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				// Move player left
				playerPos.move(playerPos.x-10, playerPos.y);
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				// Move player right
				playerPos.move(playerPos.x+10, playerPos.y);
			}
			repaint();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * performs undo
	 */
	public void undo() {
		if (manager.canUndo()) {
			manager.undo();
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
	}
	
	
}
