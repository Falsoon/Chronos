package pdc;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;

import static pdc.Constants.PLAYER_X_OFFSET;
import static pdc.Constants.PLAYER_Y_OFFSET;

public class Stair implements Serializable {
    private Point location;
    private String representation;
    public Stair linkedStair = null;
    private CardinalDirection direction;
    private static final long serialVersionUID = 1L;

    Stair(Point p) {
        location = p;
        location.translate(PLAYER_X_OFFSET, -PLAYER_Y_OFFSET);
    }

    public void linkWith(Stair s) {
        linkedStair = s;
        s.linkedStair = this;
    }

    public void setDirection(CardinalDirection direction) {
        this.direction = direction;
        switch (direction){
           case UP:
              representation = "\u2636";
              break;
           case DOWN:
              representation = "\u2633";
              break;
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

    public Stair getLinkedStair(){
       return linkedStair;
    }

    public CardinalDirection getDirection(){
       return direction;
    }
}
