package pdc;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.Iterator;
import pdc.Constants.*;

import static pdc.Constants.PLAYER_X_OFFSET;
import static pdc.Constants.PLAYER_Y_OFFSET;

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
	private ArrayList<Key> keysOwned;
	private String representation;
	private final double COLLISION_MARGIN = 13.01;
	private Key key;

	public Player(MapLayer mapLayer){
		placed = false;
		playing = false;
		placing = false;
		keysOwned = new ArrayList<>();
		this.mapLayer = mapLayer;

		representation = "\u00B6";
	}
	
	public void place(Point pos) {
		position = pos;
		position.setLocation(Math.round(position.x / GRIDDISTANCE) * GRIDDISTANCE + PLAYER_X_OFFSET,
				Math.round(position.y / GRIDDISTANCE) * GRIDDISTANCE - PLAYER_Y_OFFSET);
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
	public boolean hasKey(){return keysOwned.size()>0;}

	public Point getPosition() {
		return position;
	}

	public void lockDoor() {
	    if(keysOwned.size()>0) {
            ArrayList<Point> directions = new ArrayList<>();
            directions.add(new Point(position.x, position.y - GRIDDISTANCE));
            directions.add(new Point(position.x, position.y + GRIDDISTANCE));
            directions.add(new Point(position.x - GRIDDISTANCE, position.y));
            directions.add(new Point(position.x + GRIDDISTANCE, position.y));
            for (Point d : directions) {
                double closestCollision = Double.MAX_VALUE;
                double closestNonCollision = Double.MAX_VALUE;
                //TODO would like to not iterate through all walls. CurrentRoom currently does not store archways
                for (Wall w : mapLayer.wallList) {
                    // System.out.println("Wall WallType: " + w.getWallType());
                    double distance = w.getDistance(d);
                    if (w.getWallType() == WallType.LOCKED_DOOR) {
                        if (distance < closestCollision) {
                            closestCollision = distance;
                            if (closestCollision < closestNonCollision && closestCollision < COLLISION_MARGIN) {
                                w.setType(WallType.OPEN_LOCKED_DOOR);
                                mapLayer.dialogPlayer("Door","UNLOCKED!");
                                break;
                            }
                        }
                    } else if (w.getWallType() == WallType.OPEN_LOCKED_DOOR) {
                        if (distance < closestCollision) {
                            closestCollision = distance;
                            if (closestCollision < closestNonCollision && closestCollision < COLLISION_MARGIN) {
                                w.setType(WallType.LOCKED_DOOR);
                                mapLayer.dialogPlayer("Door","LOCKED!");
                                break;
                            }
                        }
                    }

                }
            }
        }
        else
        {
            mapLayer.dialogPlayer("Error","You do not have any keys.");
        }

    }

   private void positionDebug() {
		System.out.println("Player position: " + position);
      ArrayList<Room> rl = RoomList.getInstance().list;
      for (Room room : rl) {
			System.out.println("Room#" + room.ROOMID + "contains player: " + room.contains(position));
      }
   }

	public void goNorth() {
		if (playing && !collides(new Point(position.x, position.y - GRIDDISTANCE))) {
			position.move(position.x, position.y - GRIDDISTANCE);
         mapLayer.setPlayerPosition(position);
		}
		positionDebug();
	}

   public void goSouth() {
		if (playing && !collides(new Point(position.x, position.y + GRIDDISTANCE))) {
			position.move(position.x, position.y + GRIDDISTANCE);
         mapLayer.setPlayerPosition(position);
		}
      positionDebug();
   }

	public void goWest() {
		if (playing && !collides(new Point(position.x - GRIDDISTANCE, position.y))) {
			position.move(position.x - GRIDDISTANCE, position.y);
         mapLayer.setPlayerPosition(position);
		}
      positionDebug();
   }

	public void goEast() {
		if (playing && !collides(new Point(position.x + GRIDDISTANCE, position.y))) {
			position.move(position.x + GRIDDISTANCE, position.y);
			mapLayer.setPlayerPosition(position);
		}
      positionDebug();
   }

   public void pickUpKey() {
       boolean keyPresent = false;
       for(Key K :mapLayer.keyList)
       {
           if(Math.abs(K.getPosition().getX() - position.getX()) < 5 &&Math.abs(K.getPosition().getY() - position.getY())<5)
           {
               mapLayer.keyList.remove(K);
               keysOwned.add(K);
               mapLayer.dialogPlayer("Inventory", "Key Added.");
               keyPresent = true;
               break;
           }
       }
       if(!keyPresent)
       {
           mapLayer.dialogPlayer("Inventory", "No Key Here.");
       }

   }

   public void dropKey()
   {
        Point p  = new Point();
        p.setLocation(position.getX(), position.getY());
       if(keysOwned.size()>0) {
           mapLayer.placeKey(p);
           keysOwned.remove(0);
           mapLayer.dialogPlayer("Inventory", "Key Dropped.");
       }
       else
       {
           mapLayer.dialogPlayer("Inventory", "No Key to Drop.");
       }

	}

	public void teleportThroughNorthPortal(){
		Room room = RoomList.getInstance().getRoom(position);
		Wall portal = room.getPortals().get(CardinalDirection.NORTH);
		Point2D portalPoint = portal.getP1();
		if(portal.getP2().getX() < portalPoint.getX()){
			portalPoint = portal.getP2();
		}
      Point2D teleportationPoint;
		if(portal.getWallType().equals(WallType.ARCHWAY)||portal.getWallType().equals(WallType.OPEN_DOOR)||portal.getWallType().equals(WallType.OPEN_LOCKED_DOOR)){
		   teleportationPoint = new Point2D.Double(Math.round(portalPoint.getX()) + PLAYER_X_OFFSET, Math.round(portalPoint.getY()) + PLAYER_Y_OFFSET - GRIDDISTANCE);
      }else{
		   teleportationPoint = new Point2D.Double(Math.round(portalPoint.getX()) + PLAYER_X_OFFSET, Math.round(portalPoint.getY()) - PLAYER_Y_OFFSET + 2 * GRIDDISTANCE);
      }
		position.move((int)teleportationPoint.getX(),(int)teleportationPoint.getY());
      mapLayer.setPlayerPosition(position);
		positionDebug();
	}

	public void teleportThroughSouthPortal(){
      Room room = RoomList.getInstance().getRoom(position);
      Wall portal = room.getPortals().get(CardinalDirection.SOUTH);
      Point2D portalPoint = portal.getP1();
      if(portal.getP2().getX() < portalPoint.getX()){
         portalPoint = portal.getP2();
      }
      Point2D teleportationPoint;
      if(portal.getWallType().equals(WallType.ARCHWAY)||portal.getWallType().equals(WallType.OPEN_DOOR)||portal.getWallType().equals(WallType.OPEN_LOCKED_DOOR)){
         teleportationPoint = new Point2D.Double(Math.round(portalPoint.getX()) + PLAYER_X_OFFSET, Math.round(portalPoint.getY()) - PLAYER_Y_OFFSET + 2 * GRIDDISTANCE);
      }else{
         teleportationPoint = new Point2D.Double(Math.round(portalPoint.getX()) + PLAYER_X_OFFSET, Math.round(portalPoint.getY()) + PLAYER_Y_OFFSET - GRIDDISTANCE);
      }
      position.move((int)teleportationPoint.getX(),(int)teleportationPoint.getY());
      mapLayer.setPlayerPosition(position);
      positionDebug();
	}

	public void teleportThroughEastPortal(){
      Room room = RoomList.getInstance().getRoom(position);
      Wall portal = room.getPortals().get(CardinalDirection.EAST);
      Point2D portalPoint = portal.getP1();
      if(portal.getP2().getY() < portalPoint.getY()){
         portalPoint = portal.getP2();
      }
      Point2D teleportationPoint;
      if(portal.getWallType().equals(WallType.ARCHWAY)||portal.getWallType().equals(WallType.OPEN_DOOR)||portal.getWallType().equals(WallType.OPEN_LOCKED_DOOR)){
         teleportationPoint = new Point2D.Double(Math.round(portalPoint.getX()) + PLAYER_X_OFFSET + GRIDDISTANCE, Math.round(portalPoint.getY()) - PLAYER_Y_OFFSET + GRIDDISTANCE);
      }else{
         teleportationPoint = new Point2D.Double(Math.round(portalPoint.getX()) + PLAYER_X_OFFSET - 2 * GRIDDISTANCE, Math.round(portalPoint.getY()) - PLAYER_Y_OFFSET + GRIDDISTANCE);
      }
      position.move((int)teleportationPoint.getX(),(int)teleportationPoint.getY());
      mapLayer.setPlayerPosition(position);
      positionDebug();
	}

	public void teleportThroughWestPortal(){
      Room room = RoomList.getInstance().getRoom(position);
      Wall portal = room.getPortals().get(CardinalDirection.WEST);
      Point2D portalPoint = portal.getP1();
      if(portal.getP2().getY() < portalPoint.getY()){
         portalPoint = portal.getP2();
      }
      Point2D teleportationPoint;
      if(portal.getWallType().equals(WallType.ARCHWAY)||portal.getWallType().equals(WallType.OPEN_DOOR)||portal.getWallType().equals(WallType.OPEN_LOCKED_DOOR)){
         teleportationPoint = new Point2D.Double(Math.round(portalPoint.getX()) + PLAYER_X_OFFSET - 2 * GRIDDISTANCE, Math.round(portalPoint.getY()) - PLAYER_Y_OFFSET + GRIDDISTANCE);
      }else{
         teleportationPoint = new Point2D.Double(Math.round(portalPoint.getX()) + PLAYER_X_OFFSET + GRIDDISTANCE, Math.round(portalPoint.getY()) - PLAYER_Y_OFFSET + GRIDDISTANCE);
      }
      position.move((int)teleportationPoint.getX(),(int)teleportationPoint.getY());
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
			if (w.getWallType() == WallType.OPAQUE||w.getWallType() == WallType.LOCKED_DOOR) {
				if (distance < closestCollision) {
					closestCollision = distance;
				}
			}
			else if (w.getWallType() == WallType.CLOSED_DOOR) {
            if (distance < closestCollision) {
               closestCollision = distance;
               if (closestCollision < closestNonCollision && closestCollision < COLLISION_MARGIN) {
                  w.setType(WallType.OPEN_DOOR);
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

	public void checkStairs() {
		Stair warpTo = null;
		for (Stair s : mapLayer.stairList) {
			System.out.println("stairPos: " + s.getLocation().x + ", " + s.getLocation().y);
			if (s.getLocation().x == position.x && s.getLocation().y == position.y) {
				warpTo = s.linkedStair;
			}
		}
		if (warpTo != null) {
			position = new Point(warpTo.getLocation());
         mapLayer.setPlayerPosition(position);
		}
	}
}
