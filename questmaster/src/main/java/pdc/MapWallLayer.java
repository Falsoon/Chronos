package pdc;

import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;

import static pdc.Constants.GRIDDISTANCE;


/*
 * handles the logic behind drawingTransparent, coping and undoing an opaque wall
 */
@SuppressWarnings("serial")
public class MapWallLayer extends MapLayer {

   private boolean playerMode;

   private void fillRoomsFromPoint(Graphics g, Point startLoc, Color c) {
      Graphics2D g2d = (Graphics2D) g;
      Stroke defaultStroke = g2d.getStroke();
      ArrayList<Room> accessibleRooms = RoomList.getInstance().getAccessibleRooms(startLoc);
      //black out inaccessible rooms
      ArrayList<Room> inaccessibleRooms = new ArrayList<>();
      g2d.setColor(c);
      RoomList.getInstance().list.forEach(room -> {
         if (!accessibleRooms.contains(room)) {
            inaccessibleRooms.add(room);
            g2d.fill(room.path);
         }
      });
      //re-color any internal accessible rooms of inaccessible rooms
      inaccessibleRooms.forEach(ir-> accessibleRooms.forEach(room->{
         if(!ir.equals(room)&&ir.contains(room)){
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fill(room.path);
            g2d.setStroke(defaultStroke);
            g2d.setColor(Color.white);
            //get the bounding box of the room's path to reduce the number of points to check
            Rectangle roomBoundingBox = room.path.getBounds();
            for (int i = roomBoundingBox.x; i <= roomBoundingBox.width+roomBoundingBox.x; i+=GRIDDISTANCE) {
               for (int j = roomBoundingBox.y; j <= roomBoundingBox.height+roomBoundingBox.y; j+=GRIDDISTANCE) {
                  Point p = new Point(i,j);
                  if(room.path.contains(p)){
                     g2d.drawLine(i, j, i, j);
                  }
               }
            }
            g2d.setColor(Color.BLACK);
            room.walls.forEach(wall-> drawWall(g2d,wall));
         }
      }));
      //then re-color any inaccessible rooms within current room
      ArrayList<Room> internalRooms = RoomList.getInstance().getPlayerCurrentRoom(playerPosition).getContainedRooms();
      g2d.setColor(Color.BLACK);
      internalRooms.forEach(room->{
         if(inaccessibleRooms.contains(room)){
            g2d.fill(room.path);
         }
      });
   }

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		wallList.forEach(wall-> drawWall(g2d,wall));
      //black out inaccessible rooms to player
      if (RoomList.getInstance().list.size() != 0 && playerPosition != null) {
         if (playerMode) {
            fillRoomsFromPoint(g, playerPosition, Color.black);
         } else {
            fillRoomsFromPoint(g, playerPosition, new Color(0, 0, 0, 50));
            for (Stair s : stairList) {
               fillRoomsFromPoint(g, s.getLocation(), new Color(0, 0, 0, 50));
            }
         }
      }

		if (selectedRoom != null) {
			g2d.setColor(Color.RED);
			setDrawMode(g2d,WallType.OPAQUE);
			selectedRoom.walls.forEach(wall -> g2d.draw(wall.getLineRepresentation()));
      }
      
      for (Stair s : stairList) {
         s.draw(g);
      }
	}

   /**
    * Sets the draw mode based on the type of wall to draw
    * @param g2d the Graphics2D object to modify
    * @param type the type of the wall
    */
   private void setDrawMode(Graphics2D g2d,WallType type) {
	   if(type.equals(WallType.OPAQUE)){
         g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
      }
      else if(type.equals(WallType.ARCHWAY)) {
         g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         Stroke arch = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{3}, 0);
         g2d.setStroke(arch);
      }
      else if(type.equals(WallType.CLOSED_DOOR)) {
         g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         Stroke arch = new BasicStroke(10, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
         g2d.setStroke(arch);
      }
      //UPDATE COLOR
       else if(type.equals(WallType.LOCKED_DOOR)) {
           g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
           Stroke arch = new BasicStroke(10, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);

           g2d.setStroke(arch);
           g2d.setPaint(Color.red);
       }
       //UPDATE
       else if(type.equals(WallType.OPEN_LOCKED_DOOR)) {
           g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
           Stroke arch = new BasicStroke(10, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{3}, 0);
           g2d.setStroke(arch);
           g2d.setPaint(Color.blue);
       }
      else if(type.equals(WallType.OPEN_DOOR)) {
         g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         Stroke arch = new BasicStroke(10, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{3}, 0);
         g2d.setStroke(arch);
      }
      else if(type.equals(WallType.TRANSPARENT)){
         g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
         g2d.setStroke(dashed);
      }
   }

   private void drawWall(Graphics2D g2d, Wall wall){
       WallType wallType = wall.getWallType();
      //only draw transparent walls in author mode
      if(!playerMode||!wallType.equals(WallType.TRANSPARENT)) {
         setDrawMode(g2d, wallType);
         g2d.draw(wall.getLineRepresentation());
      }
      g2d.setPaint(Color.black);
   }

   @SuppressWarnings("unchecked")
   @Override
	public MapLayer copy() {
		MapLayer copy = new MapWallLayer();
		copy.start = start;
		/*
		if (copy.pathList == null) {
			copy.pathList = new ArrayList<GeneralPath>();
		}
		if (pathList == null) {
			pathList = new ArrayList<GeneralPath>();
		}
		for (int i = 0; i < pathList.size(); i++) {
			copy.pathList.add((GeneralPath) pathList.get(i).clone());
		}
		*/
		if(!pointList.isEmpty()) {
			copy.pointList = (ArrayList<Point>) pointList.clone();
			copy.wallList =(ArrayList<Wall>) wallList.clone();
		}
			
		return copy;
	}
    /*
	@Override
	public void undo() {
	   //TODO need to fix this now that we've changed how rooms are created

	} */

   @Override
   public void setPlayerMode(boolean setting) {
      playerMode = setting;
   }

   @Override
   public void setPlayerPosition(Point position) {
      this.playerPosition = position;
   }

   @Override
   public void storeState(Hashtable<Object, Object> state) {

   }

   @Override
   public void restoreState(Hashtable<?, ?> state) {

   }
}
