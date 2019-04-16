package pdc;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.Iterator;

/*
 * encapsulates player's avatar data
 */
@SuppressWarnings("serial")
public class Player implements Serializable {
	private static final int GRIDDISTANCE = Constants.GRIDDISTANCE;
	private Point position;
	private boolean placed, playing, placing;
	private Room currentRoom;
	private MapLayer mapLayer;
	private String representation;
	private final int XOFFSET = 2;
	private final int YOFFSET = 4;
	private final double COLLISION_MARGIN = 13.01;
	
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
		rePlace();
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
	public void stopPlaying() {
		playing = false;
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

	
   private void positionDebug() {
		System.out.println("Player position: " + position);
      ArrayList<Room> rl = RoomList.getInstance().list;
      for (Room room : rl) {
			System.out.println("Room#" + room.ROOMID + "contains player: " + room.contains(position));
      }
   }
	
	public void goUp() {
		if (playing && !collides(new Point(position.x, position.y - GRIDDISTANCE))) {
			position.move(position.x, position.y - GRIDDISTANCE);
			checkStairs();
         mapLayer.setPlayerPosition(position);
		}
		positionDebug();
	}

   public void goDown() {
		if (playing && !collides(new Point(position.x, position.y + GRIDDISTANCE))) {
			position.move(position.x, position.y + GRIDDISTANCE);
			checkStairs();
         mapLayer.setPlayerPosition(position);
		}
      positionDebug();
   }

	public void goLeft() {
		if (playing && !collides(new Point(position.x - GRIDDISTANCE, position.y))) {
			position.move(position.x - GRIDDISTANCE, position.y);
			checkStairs();
         mapLayer.setPlayerPosition(position);
		}
      positionDebug();
   }

	public void goRight() {
		if (playing && !collides(new Point(position.x + GRIDDISTANCE, position.y))) {
			position.move(position.x + GRIDDISTANCE, position.y);
			checkStairs();
			mapLayer.setPlayerPosition(position);
		}
      positionDebug();
	}
	
	public void teleportThroughNorthPortal(){
		Room room = RoomList.getInstance().getRoom(position);
		Wall portal = room.getPortals().get(CardinalDirection.NORTH);
		Point2D point = portal.getP1();
		if(portal.getP2().getX() < point.getX()){
			point = portal.getP2();
		}
		position.move((int) Math.round(point.getX()) + XOFFSET, (int) Math.round(point.getY()) - YOFFSET - GRIDDISTANCE);
      mapLayer.setPlayerPosition(position);
		positionDebug();
	}

	public void teleportThroughSouthPortal(){
		Room room = RoomList.getInstance().getRoom(position);
		Wall portal = room.getPortals().get(CardinalDirection.SOUTH);
		Point2D point = portal.getP1();
		if(portal.getP2().getX() < point.getX()){
			point = portal.getP2();
		}
		position.move((int) Math.round(point.getX()) + XOFFSET, (int) Math.round(point.getY()) - YOFFSET + 2 * GRIDDISTANCE);
      mapLayer.setPlayerPosition(position);
		positionDebug();
	}

	public void teleportThroughEastPortal(){
		Room room = RoomList.getInstance().getRoom(position);
		Wall portal = room.getPortals().get(CardinalDirection.EAST);
		Point2D point = portal.getP1();
		if(portal.getP2().getY() > point.getY()){
			point = portal.getP2();
		}
		position.move((int) Math.round(point.getX()) + XOFFSET + GRIDDISTANCE, (int) Math.round(point.getY()) - YOFFSET);
      mapLayer.setPlayerPosition(position);
		positionDebug();
	}

	public void teleportThroughWestPortal(){
		Room room = RoomList.getInstance().getRoom(position);
		Wall portal = room.getPortals().get(CardinalDirection.WEST);
		Point2D point = portal.getP1();
		if(portal.getP2().getY() > point.getY()){
			point = portal.getP2();
		}
		position.move((int) Math.round(point.getX()) + XOFFSET - 2 * GRIDDISTANCE, (int) Math.round(point.getY()) - YOFFSET);
      mapLayer.setPlayerPosition(position);
		positionDebug();
	}

	public void teleportThroughUpPortal(){
      Room room = RoomList.getInstance().getRoom(position);
      Stair stair = room.getStairs().get(CardinalDirection.UP);
      Point point = stair.getLinkedStair().getLocation();
      position.move(point.x,point.y);
      mapLayer.setPlayerPosition(position);
      positionDebug();
	}

	public void teleportThroughDownPortal(){
      Room room = RoomList.getInstance().getRoom(position);
      Stair stair = room.getStairs().get(CardinalDirection.DOWN);
      Point point = stair.getLinkedStair().getLocation();
      position.move(point.x,point.y);
      mapLayer.setPlayerPosition(position);
      positionDebug();
	}
	
	/**
	 * Returns true if there is no collision at point p, false otherwise
	 * @param p the point to move to
	 */
	private boolean collides(Point p){
		double closestCollision = Double.MAX_VALUE;
		double closestNonCollision = Double.MAX_VALUE;
		for (Wall w : mapLayer.wallList) {
			// System.out.println("Wall WallType: " + w.getWallType());
			double distance = w.getDistance(p);
			if (w.getWallType() == WallType.OPAQUE) {
				if (distance < closestCollision) {
					closestCollision = distance;
				}
			}
			else if (w.getWallType() == WallType.CLOSEDDOOR) {
            if (distance < closestCollision) {
               closestCollision = distance;
               if (closestCollision < closestNonCollision && closestCollision < COLLISION_MARGIN) {
                  w.setType(WallType.OPENDOOR);
               }
            }
         }
         else if (distance < closestNonCollision) {
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
			return "~CANT GET ROOM NAME~";
		}
		return RoomList.getInstance().getRoom(position).title;
		// return currentRoom.title;
	}

	public String getRoomDesc() {
		if (RoomList.getInstance().getRoom(position) == null) {
			return "~CANT GET ROOM DESCRIPTION~";
		}
		return RoomList.getInstance().getRoom(position).desc;
		// return currentRoom.desc;
	}

	private void checkStairs() {
		Stair warpTo = null;
		for (Stair s : mapLayer.stairList) {
			System.out.println("stairPos: " + s.getLocation().x + ", " + s.getLocation().y);
			if (s.getLocation().x == position.x && s.getLocation().y == position.y) {
				warpTo = s.linkedStair;
			}
		}
		if (warpTo != null) {
			position = new Point(warpTo.getLocation());
		}
	}
}
