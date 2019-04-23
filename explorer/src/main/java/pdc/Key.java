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
	private Point position;
	private String representation;

	
	public Key(Point p){
		position = p;
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
