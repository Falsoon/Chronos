package hic;

import civ.CIV;
import pdc.Room;
import pdc.RoomList;

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
	// private AuthorWindow aw;
	public AuthorWindow aw;

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
		final JDialog d = jop.createDialog( null, "Error");
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
		setSelectedRoom(null);
		repaint();
		civ.outlining();		
	}

	/**
	 * Changes state of MapPanel to add walls
	 */
	public void paintWalls() {
		setSelectedRoom(null);
		repaint();
		civ.walling();
	}

	/**
	 * Changes state of MapPanel to add doors
	 */
	public void paintDoors() {
		setSelectedRoom(null);
		repaint();
		civ.doorAdd();
	}
    public void paintLockedDoors() {
		setSelectedRoom(null);
		repaint();
        civ.lockedDoorAdd();
    }
	public void paintKey() {
		setSelectedRoom(null);
		repaint();
		civ.keyAdd();
	}

   public void moveStair() {
      civ.map.getPlayer().checkStairs();
      repaint();
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
		setSelectedRoom(null);
		repaint();
		civ.archwayAdd();
   }

      /**
    * Changes state of MapPanel to add stairs
    */
	public void paintStairs() {
		setSelectedRoom(null);
		repaint();
		civ.stairsAdd();
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
		//setSelectedRoom(null);
		//repaint();
		civ.placeStart();
	}

	public void stopPlacingPlayer() {
		civ.stopPlacingPlayer();
	}

	public boolean placedPlayer() {
		return civ.placedPlayer();
	}

    public void lockDoor()
    {
        civ.lockDoor();
        repaint();
    }
	public void goUp() {
		civ.goNorth();
		repaint();
	}

	public void goDown() {
		civ.goSouth();
		repaint();
	}

	public void goLeft() {
		civ.goWest();
		repaint();
	}

	public void goRight() {
		civ.goEast();
		repaint();
	}

	public void teleportThroughNorthPortal(){
		civ.teleportThroughNorthPortal();
		repaint();
	}

	public void teleportThroughSouthPortal(){
		civ.teleportThroughSouthPortal();
		repaint();
	}

	public void teleportThroughEastPortal(){
		civ.teleportThroughEastPortal();
		repaint();
	}

	public void teleportThroughWestPortal(){
		civ.teleportThroughWestPortal();
		repaint();
	}

	public void teleportThroughUpPortal(){
		civ.teleportThroughUpPortal();
		repaint();
	}

	public void teleportThroughDownPortal(){
		civ.teleportThroughDownPortal();
		repaint();
	}

	public String getRoomName() {
		return civ.getRoomName();
	}

	public String getRoomDesc() {
		return civ.getRoomDesc();
	}

	public Room getRoom(Point p){
      return RoomList.getInstance().getRoom(p);
   }

	public void stopDrawing() {
		//setSelectedRoom(null);
		//repaint();
		civ.stopDrawing();
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
			setSelectedRoom(null);
			repaint();
      civ.delete();
   }
}
