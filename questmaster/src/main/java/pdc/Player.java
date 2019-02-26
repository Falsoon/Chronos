package pdc;

import java.awt.*;
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
	private final double COLLISION_MARGIN = 10;
	
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
		Iterator<Room> itr = RoomList.getInstance().iterator();
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
		if (playing && !collides(new Point(position.x, position.y - GRIDDISTANCE))) {
			position.move(position.x, position.y - GRIDDISTANCE);
		}
	}

	public void goDown() {
		if (playing && !collides(new Point(position.x, position.y + GRIDDISTANCE))) {
			position.move(position.x, position.y + GRIDDISTANCE);
		}
	}

	public void goLeft() {
		if (playing && !collides(new Point(position.x - GRIDDISTANCE, position.y))) {
			position.move(position.x - GRIDDISTANCE, position.y);
		}
	}

	public void goRight() {
		if (playing && !collides(new Point(position.x + GRIDDISTANCE, position.y))) {
			position.move(position.x + GRIDDISTANCE, position.y);
		}
	}
	
	/**
	 * Returns true if there is no collision at point p, false otherwise
	 * @param p the point to move to
	 */
	private boolean collides(Point p){
		double closestCollision = Double.MAX_VALUE;
		double closestNonCollision = Double.MAX_VALUE;
		//TODO would like to not iterate through all walls. CurrentRoom currently does not store archways
		for (Wall w : mapLayer.wallList) {
			// System.out.println("Wall Type: " + w.getType());
			double distance = w.getDistance(p);
			if (w.getType() == Type.OPAQUE) {
				if (distance < closestCollision) {
					closestCollision = distance;
				}
			} else if (distance < closestNonCollision) {
				closestNonCollision = distance;
			}
		}
		System.out.println("Closest collision: " + closestCollision);
		System.out.println("Closest non-collision: " + closestNonCollision);
		return closestCollision < closestNonCollision && closestCollision < COLLISION_MARGIN;
	}

	public void rePlace() {
		Iterator<Room> itr = RoomList.getInstance().iterator();
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

	public String getRoomName() {
		if (RoomList.getInstance().getRoom(position) == null) {
			return "why are you null shithead";
		}
		return RoomList.getInstance().getRoom(position).title;
		// return currentRoom.title;
	}

	public String getRoomDesc() {
		if (RoomList.getInstance().getRoom(position) == null) {
			return "why are you null shithead";
		}
		return RoomList.getInstance().getRoom(position).desc;
		// return currentRoom.desc;
	}
}
