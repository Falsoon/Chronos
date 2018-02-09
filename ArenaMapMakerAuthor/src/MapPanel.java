import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.*;
import java.awt.geom.GeneralPath;

import javax.swing.JPanel;

public class MapPanel extends JPanel {

	private boolean drawing, creating;
	private MouseHandler mousehandler = new MouseHandler();
	public GeneralPath path;

	public MapPanel() {
		addMouseListener(mousehandler);
		addMouseMotionListener(mousehandler);
	}

	public void paintRooms() {
		creating = true;
	}

	public void clear() {
		creating = false;
		drawing = false;
		path = null;
		repaint();
	}

	public void paintComponent(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, 600, 600);
		g.setColor(Color.white);
		for (int i = 0; i < 600; i++) {
			for (int j = 0; j < 600; j++) {
				g.drawLine(10 * (i + 1), 10 * (j + 1), 10 * (i + 1), 10 * (j + 1));
			}
		}
		if (creating) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(Color.BLACK);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
			if (path != null) {
				g2d.draw(path);
			}
		}
	}

	private class MouseHandler extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			if (creating) {
				Point p = e.getPoint();
				if (!drawing) {
					path = new GeneralPath();
					path.moveTo(p.x, p.y);
					drawing = true;
				} else {
					path.lineTo(p.x, p.y);
				}
				repaint();
			}
		}
	}
}
