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
	private final int GRIDDISTANCE = 15;
	private boolean drawing, outlining, creatingWalls;
	public GeneralPath path = new GeneralPath();
	public GeneralPath wallPath = new GeneralPath();
	public Point start;
	public Room room;
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
				map.mousePressed(e);
				repaint();
/*				StateEdit stateEdit = new StateEdit(MapPanel.this);
				//if a point needs to be drawn
				if (outlining || creatingWalls) {
					Point p = e.getPoint();
					//round to nearest grid point
					p.setLocation(Math.round(((double)p.x) / GRIDDISTANCE) * GRIDDISTANCE, Math.round(((double)p.y) / GRIDDISTANCE) * GRIDDISTANCE);
					boolean first = !drawing;
					//at the first point, start a new path
					if (!drawing) {
						path = new GeneralPath();
						path.moveTo(p.x, p.y);
						start = p;//save start to compare later
						drawing = true;
						room = new Room(p);//create room
					} else {
						//if not the first point, add to path
						path.lineTo(p.x, p.y);
						room.add(p);
					}
					//if the path has returned to start, the end outline
					if (!first && p.equals(start)) {
						outlining = false;
						creatingWalls = false;
					}
				}
				
				repaint();
				//stateEdit is used for undo
				stateEdit.end();
				manager.addEdit(stateEdit);*/
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
		map.outlining();
	}
	
	/**
	 * Changes state of MapPanel to add walls
	 */
	public void paintWalls() {
		creatingWalls = true;
	}

	/**
	 * Resets state of MapPanel
	 */
	public void clear() {
		outlining = false;
		creatingWalls = false;
		drawing = false;
		path = null;
		room = null;
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
		
		
		/*if (creatingWalls) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(Color.BLACK);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
			if (path != null) {
				g2d.draw(path);
			}
			Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
	        g2d.setStroke(dashed);
	        if (path != null) {
	        	//currently over riding entire path, need to learn how to split paths into sections
	        	//only draw added section
	        	Point p2 = room.removeLast();
	        	Point p1 = room.removeLast();
	        	g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
	        	room.add(p1);
	        	room.add(p2);
	        }
		}
		//if outlining, then draw the path so far 
		else if (outlining || drawing) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(Color.BLACK);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
			if (path != null) {
				g2d.draw(path);
			}
		}*/
	
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
		state.put(MAP_KEY, getPath());
	}

	/**
	 * This method is a helper method to get a copy of the current path rather than a reference to the path itself.
	 * @return Copy of Path
	 */
	private GeneralPath getPath() {
		GeneralPath returnValue = new GeneralPath();
		if (room != null && !room.points.isEmpty()) {
			ArrayList<Point> pts = room.points;
			returnValue.moveTo(pts.get(0).x, pts.get(0).y);
			for (int i = 1; pts.size() > i; i++) {
				returnValue.lineTo(pts.get(i).x, pts.get(i).y);
			}
		}
		return returnValue;
	}

	/**
	 * Called when the UndoMananger needs to undo to get to an earlier state.
	 */
	public void restoreState(Hashtable state) {
		GeneralPath newP = (GeneralPath) state.get(MAP_KEY);
		if (newP != null) {
			if (newP.getCurrentPoint() == null) {
				drawing = false;
			}
			if (path != null) {
				if (path.getCurrentPoint().equals(start)) {
					outlining = true;
				}
			}
			path = newP;
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
			room.removeLast();
		} else {
			JOptionPane.showMessageDialog(this, "Cannot Undo");
		}
		repaint();
	}
}
