import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;

public class CIV {
	private Map map;

	public CIV() {
		map = new Map();
	}

	public void mousePressed(MouseEvent e) {
		Point p = e.getPoint();
		if (!e.isAltDown()) {
			p.setLocation(Math.round(((double) p.x) / Constants.GRIDDISTANCE) * Constants.GRIDDISTANCE,
					Math.round(((double) p.y) / Constants.GRIDDISTANCE) * Constants.GRIDDISTANCE);
		}
		if (map.isCreating()) {
			map.mousePressed(p);
		} else {
			Room room = map.getRoom(p);
			if (room != null) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							FormWindow window = new FormWindow(room);
							window.frame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}
	}

	public boolean undo() {
		return map.undo();
	}

	public void clear() {
		map = new Map();
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
		return map.playerPlaced;
	}

	public void goUp() {
		map.goUp();
	}

	public void goDown() {
		map.goDown();
	}

	public void goLeft() {
		map.goLeft();
	}

	public void goRight() {
		map.goRight();
	}

}
