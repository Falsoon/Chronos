import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

public class CIV {
	private Map map;

	public CIV() {
		map = new Map();
	}

	public void mousePressed(MouseEvent e) throws Throwable {
		Point p = e.getPoint();
		if (!e.isAltDown()) {
			p.setLocation(Math.round(((double) p.x) / Constants.GRIDDISTANCE) * Constants.GRIDDISTANCE,
					Math.round(((double) p.y) / Constants.GRIDDISTANCE) * Constants.GRIDDISTANCE);
		}
		if (SwingUtilities.isLeftMouseButton(e)) {
			if (map.isCreating()) {
				map.mousePressed(p);
			} else {
				if (!map.getPlayer().isPlaying()) {
					Room room = map.getRoom(p);
					if (room != null) {
						EventQueue.invokeLater(new Runnable() {
							public void run() {
								try {
									FormWindow window = new FormWindow(room, true);
									window.frame.setVisible(true);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					}
				}
			}
		}
	}

	public boolean undo() {
		return map.undo();
	}

	public void clear() {
		map = new Map();
		RoomList.reset();
	}

	public void draw(Graphics g) {
		map.draw(g);
	}

	public void outlining() {
		map.outlining();
	}

	public void walling() {
		map.walling();
	}

	public void startGame() {
		map.startGame();
	}

	public void placeStart() {
		map.placePlayerStart();
	}

	public boolean placedPlayer() {
		return map.getPlayer().isPlaced();
	}

	public void goUp() {
		map.getPlayer().goUp();
	}

	public void goDown() {
		map.getPlayer().goDown();
	}

	public void goLeft() {
		map.getPlayer().goLeft();
	}

	public void goRight() {
		map.getPlayer().goRight();
	}

	public Room getRoom() {
		return map.getRoom(map.getPlayer().getPosition());
	}

	public void stopDrawing() {
		map.stopDrawing();
	}

	public void dooring() {
		map.dooring();
	}

	public void drawRoom(Room r) {
		map.drawRoom(r);
	}

	public void setSelectedRoom(Room r) {
		map.setSelectedRoom(r);
	}

}
