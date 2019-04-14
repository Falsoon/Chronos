package hic;

import civ.CIV;
import pdc.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static pdc.Constants.GRIDDISTANCE;

/**
 * the presenter class for the mapWindow handles updating both data on the map
 * and UI of the map
 */
@SuppressWarnings("serial")
public class MapPanel extends JPanel {

	public CIV civ;
	public Point playerPos;
	private AuthorWindow aw;

	/**
	 * Constructor of MapPanel adds the appropriate action listeners
	 * 
	 * @param authorWindow
	 */
	public MapPanel(AuthorWindow authorWindow) {
		civ = authorWindow.civ;
		aw = authorWindow;

		// Anonymous class was used to access MapPanel fields
		MouseListener mousehandler = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				try {
					civ.mousePressed(e.getPoint(), e.isAltDown(), SwingUtilities.isLeftMouseButton(e), SwingUtilities.isRightMouseButton(e));
				} catch (Throwable error) {
					dialog(error.getMessage());
					error.printStackTrace();
				}
				aw.authorPanel.update();
				repaint();
			}
		};
		addMouseListener(mousehandler);
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
	 * Changes state of MapPanel to draw Outline
	 */
	public void paintRooms() {
		civ.stopDrawing();
		civ.stopPlacingPlayer();
		civ.outlining();
	}

	/**
	 * Changes state of MapPanel to add walls
	 */
	public void paintWalls() {
		civ.stopDrawing();
		civ.stopPlacingPlayer();
		civ.walling();
	}

	/**
	 * Changes state of MapPanel to add doors
	 */
	public void paintDoors() {
		civ.stopDrawing();
		civ.stopPlacingPlayer();
		civ.doorAdd();
	}
    public void paintLockDoors() {
        civ.stopDrawing();
        civ.stopPlacingPlayer();
        civ.lockDoorAdd();
    }
	public void paintKey() {
		civ.stopDrawing();
		civ.stopPlacingPlayer();
		civ.keyAdd();
	}
    public void dropKey() {
        civ.dropKey();
        repaint();
    }
    public void pickUpKey() {
        civ.pickUpKey();
        repaint();
    }

   /**
    * Changes state of MapPanel to add Archway
    */
   public void paintArchway() {
		civ.stopDrawing();
		civ.stopPlacingPlayer();
		civ.archwayAdd();
   }


	/**
	 * Resets state of MapPanel
	 */
	public void clear() {
		civ.clear();
		repaint();
	}
	
	public void save() {
		civ.save();
	}
	
	public void restore() {
		civ.restore();
		aw.authorPanel.update();
		repaint();
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

	public void stopPlacingPlayer() {
		civ.stopPlacingPlayer();
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

	public String getRoomName() {
		return civ.getRoomName();
	}

	public String getRoomDesc() {
		return civ.getRoomDesc();
	}

	public void stopDrawing() {
		civ.stopDrawing();
	}

	public void drawRoom(String str) {
		civ.drawRoom(str);
	}

	public void setSelectedRoom(String str) {
		civ.setSelectedRoom(str);
		if (str != null) {
			Rectangle rect = civ.getRoomBounds(str);
			if(rect!=null) {
				this.scrollRectToVisible(rect);
			}
		}
	}

   /**
    * Method to set whether the MapLayer is for the player mode
    * @param setting the value to give to player mode
    */
   public void setPlayerMode(boolean setting) {
	   civ.setPlayerMode(setting);
   }

   /**
    * Method to delete walls and passageways
    */
   public void delete() {
      civ.delete();
   }
}
