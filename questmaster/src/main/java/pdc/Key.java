package pdc;

import java.awt.*;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.Iterator;

/*
 * encapsulates player's avatar data
 */
@SuppressWarnings("serial")
public class Key implements Serializable {
	private static final int GRIDDISTANCE = Constants.GRIDDISTANCE;
	private Point position;
	private boolean placed, playing, placing;
	private Room currentRoom;
	private MapLayer mapLayer;
	private String representation;
	private final int XOFFSET = 2;
	private final int YOFFSET = 4;
	private final double COLLISION_MARGIN = 13.01;
	private boolean key;
	
	public Key(Point p, MapLayer mapLayer){
		position = p;
		this.mapLayer = mapLayer;
		representation = "\u266a";
	}

    public Point getPosition() {
        return position;
    }

    public void draw(Graphics g) {

            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.BLACK);
            g2d.drawString(representation, position.x, position.y);

	}
}
