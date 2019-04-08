package pdc;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;

public class Stair implements Serializable {
    private Point location;
    private String representation;
    public Stair linkedStair = null;
    public boolean goingUp;
    private final int XOFFSET = 2;
	private final int YOFFSET = 4;

    Stair(Point p) {
        location = p;
        location.translate(XOFFSET, -YOFFSET);
    }

    public void linkWith(Stair s) {
        linkedStair = s;
        s.linkedStair = this;
    }

    public void isGoingUp(boolean b) {
        goingUp = b;
        if (b) {
            representation = "\u2636";
        } else {
            representation = "\u2633";
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (linkedStair == null) {
            g2d.setColor(Color.RED);
        } else {
            g2d.setColor(Color.BLACK);
        }
		g2d.drawString(representation, location.x, location.y);
    }

    public Point getLocation() {
        return location;
    }
}