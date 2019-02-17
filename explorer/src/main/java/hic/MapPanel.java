package hic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.*;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import civ.CIV;
import pdc.Constants;

/**
 * the presenter class for the mapWindow handles updating both data on the map
 * and UI of the map
 */
@SuppressWarnings("serial")
public class MapPanel extends JPanel {

	public CIV civ;
	private final int GRIDDISTANCE = Constants.GRIDDISTANCE;
	public Point playerPos;

	public MapPanel() {
		civ = new CIV();
	}

	protected void dialog(String message) {
		JOptionPane jop = new JOptionPane(message);
		final JDialog d = jop.createDialog((JFrame) null, "Error");
		d.setLocation(250, 250);
		d.setVisible(true);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(3000, 3000);
	}

	/**
	 * This method is inherited by JPanel paintComponents will draw on the panel
	 * each time repaint() is called
	 */
	public void paintComponent(Graphics g) {
		// Background
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, 3000, 3000);
		// Grid points
		g.setColor(Color.white);
		for (int i = 0; i < 600; i++) {
			for (int j = 0; j < 600; j++) {
				g.drawLine(GRIDDISTANCE * (i + 1), GRIDDISTANCE * (j + 1), GRIDDISTANCE * (i + 1),
						GRIDDISTANCE * (j + 1));
			}
		}
		civ.draw(g);
	}

	public void startGame() {
		civ.startGame();
	}

	public boolean placedPlayer() {
		return civ.placedPlayer();
	}

	public void goUp() {
		civ.goUp();
		repaint();
	}

	public void goDown() {
		civ.goDown();
		repaint();
	}

	public void goLeft() {
		civ.goLeft();
		repaint();
	}

	public void goRight() {
		civ.goRight();
		repaint();
	}
}