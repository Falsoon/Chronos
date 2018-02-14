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
	private boolean drawing, creating, creatingWalls;
	public GeneralPath path = new GeneralPath();
	public Point start;
	public Room room;
	UndoableEditSupport undoSupport = new UndoableEditSupport(this);
	UndoManager manager = new UndoManager();

	public MapPanel() {
		MouseListener mousehandler = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				StateEdit stateEdit = new StateEdit(MapPanel.this);
				if (creating || creatingWalls) {
					Point p = e.getPoint();
					p.setLocation(Math.round(p.x / 10) * 10, Math.round(p.y / 10) * 10);
					boolean first = !drawing;
					if (!drawing) {
						path = new GeneralPath();
						path.moveTo(p.x, p.y);
						start = p;
						drawing = true;
						room = new Room(p);
					} else {
						path.lineTo(p.x, p.y);
						room.add(p);
					}
					if (!first && p.equals(start)) {
						creating = false;
						creatingWalls = false;
					}
					repaint();
					stateEdit.end();
					manager.addEdit(stateEdit);

					repaint();
				}
			}
		};
		addKeyListener(this);
		addUndoableEditListener(manager);
		addMouseListener(mousehandler);
	}

	public void paintRooms() {
		creating = true;
		creatingWalls = false;
	}
	
	public void paintWalls() {
		creatingWalls = true;
		creating = true;
	}

	public void clear() {
		creating = false;
		creatingWalls = false;
		drawing = false;
		path = null;
		room = null;
		repaint();
	}

	public void paintComponent(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, 1200, 1200);
		g.setColor(Color.white);
		for (int i = 0; i < 600; i++) {
			for (int j = 0; j < 600; j++) {
				g.drawLine(10 * (i + 1), 10 * (j + 1), 10 * (i + 1), 10 * (j + 1));
			}
		}
		
		
		if (creatingWalls) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(Color.BLACK);
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
		else if (creating || drawing) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(Color.BLACK);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
			if (path != null) {
				g2d.draw(path);
			}
		}
	
	}

	public void addUndoableEditListener(UndoableEditListener undoableEditListener) {
		undoSupport.addUndoableEditListener(undoableEditListener);
	}

	public void storeState(Hashtable state) {
		state.put(MAP_KEY, getPath());
	}

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

	public void restoreState(Hashtable state) {
		GeneralPath newP = (GeneralPath) state.get(MAP_KEY);
		if (newP != null) {
			if (newP.getCurrentPoint() == null) {
				drawing = false;
			}
			if (path != null) {
				if (path.getCurrentPoint().equals(start)) {
					creating = true;
				}
			}
			path = newP;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

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
