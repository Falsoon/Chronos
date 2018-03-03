import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.*;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class MapPanel extends JPanel  {

	public CIV civ;
	private final int GRIDDISTANCE = Constants.GRIDDISTANCE;
	public Point playerPos;
	private AuthorWindow aw;

	/**
	 * Constructor of MapPanel adds the appropriate action listeners
	 * @param authorWindow 
	 */
	public MapPanel(AuthorWindow authorWindow) {
		civ = new CIV();
		aw = authorWindow;
		// Anonymous class was used to access MapPanel fields
		MouseListener mousehandler = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				
				try {
					civ.mousePressed(e);
				} catch (Throwable error) {
					dialog(error.getMessage());
				}
				repaint();
				aw.update();
			}
		};
		addMouseListener(mousehandler);
	}

	protected void dialog(String message) {
		JOptionPane.showMessageDialog(this, message);
	}

	/**
	 * Changes state of MapPanel to draw Outline
	 */
	public void paintRooms() {
		civ.outlining();
	}

	/**
	 * Changes state of MapPanel to add walls
	 */
	public void paintWalls() {
		civ.walling();
	}
	
	/**
	 * Changes state of MapPanel to add doors
	 */
	public void paintDoors() {
		civ.dooring();
	}

	/**
	 * Resets state of MapPanel
	 */
	public void clear() {
		civ.clear();
		repaint();
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
		civ.draw(g);
	}

	/**
	 * performs undo
	 */
	public void undo() {
		// if undo fails throw dialog
		if (!civ.undo()) {
			JOptionPane.showMessageDialog(this, "Cannot Undo");
		}
		repaint();
	}

	public void startGame() {
		civ.startGame();
	}

	public void placePlayerStart() {
		civ.placeStart();
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

	public Room getRoom() {
		return civ.getRoom();
	}

	public void stopDrawing() {
		civ.stopDrawing();
	}

	public void drawRoom(Room r) {
		civ.drawRoom(r);
	}

	public void setSelectedRoom(Room r) {
		civ.setSelectedRoom(r);
	}
}