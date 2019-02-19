package pdc;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.util.Iterator;

/*
 * encapsulates player's avatar data
 */
public class Player {
	private static final int GRIDDISTANCE = Constants.GRIDDISTANCE;
	private Point position;
	private boolean placed, playing, placing;
	private Room currentRoom;
	private MapLayer mapLayer;
	private String representation;
	private final int XOFFSET = 2;
	private final int YOFFSET = 4;
	
	public Player(MapLayer mapLayer){
		placed = false;
		playing = false;
		placing = false;
		this.mapLayer = mapLayer;
		representation = "\u00B6";
	}
	
	public void place(Point pos) {
		position = pos;
		position.setLocation(Math.round(position.x / GRIDDISTANCE) * GRIDDISTANCE + XOFFSET,
				Math.round(position.y / GRIDDISTANCE) * GRIDDISTANCE - YOFFSET);
		placed = true;
		placing = false;
		Iterator<Room> itr = RoomList.iterator();
		while (itr.hasNext()) {
			Room curr = itr.next();
			if (curr.contains(position)) {
				currentRoom = curr;
			}
		}
	}
	
	public String getRepresentation() {
		return representation;
	}
		
	public boolean isPlaced() {
		return placed;
	}
	public boolean isPlaying() {
		return playing;
	}
	public boolean isPlacing() {
		return placing;
	}
	public void startPlaying() {
		playing = true;
	}
	public void startPlacing() {
		placing = true;
	}
	public void stopPlacing() {
		placing = false;
	}
	
	public Point getPosition() {
		return position;
	}
	
	public void goUp() {
		if (playing) {
			position.move(position.x, position.y - GRIDDISTANCE);
			if (collides()) {
				position.move(position.x, position.y + GRIDDISTANCE);
			}
		}
	}

	public void goDown() {
		if (playing) {
			position.move(position.x, position.y + GRIDDISTANCE);
			if (collides()) {
				position.move(position.x, position.y - GRIDDISTANCE);
			}
		}
	}

	public void goLeft() {
		if (playing) {
			position.move(position.x - GRIDDISTANCE, position.y);
			if (collides()) {
				position.move(position.x + GRIDDISTANCE, position.y);
			}
		}
	}

	public void goRight() {
		if (playing) {
			position.move(position.x + GRIDDISTANCE, position.y);
			if (collides()) {
				position.move(position.x - GRIDDISTANCE, position.y);
			}
		}
	}
	
	private boolean collides() {
		boolean outside = true;
		Iterator<Room> itr = RoomList.iterator();
		while (itr.hasNext() && outside) {
			Room room = itr.next();
			if (room.equals(currentRoom) &&
					room.contains(new Point(position.x, position.y + YOFFSET - 2)) &&
						room.contains(new Point(position.x + GRIDDISTANCE - XOFFSET, position.y - GRIDDISTANCE - YOFFSET + 2)) &&
							room.contains(new Point(position.x + GRIDDISTANCE - XOFFSET, position.y + YOFFSET - 2)) &&
								room.contains(new Point(position.x, position.y - GRIDDISTANCE - YOFFSET + 2)))
									outside = false;
		}
		return outside;
	}

	public void rePlace() {
		Iterator<Room> itr = RoomList.iterator();
		while (itr.hasNext()) {
			Room curr = itr.next();
			if (curr.contains(position)) {
				currentRoom = curr;
			}
		}
	}
	
	public void draw(Graphics g) {
		if (placed) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(Color.BLACK);
			g2d.drawString(representation, position.x, position.y);
			placing = false;
		}
	}
}
