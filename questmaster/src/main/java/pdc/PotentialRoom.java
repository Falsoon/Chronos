package pdc;

import javafx.geometry.Point2D;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class PotentialRoom extends Room {
   public ArrayList<Line2D> walls;
   public ArrayList<Point> pointList;
   public int ROOMID;
   public static int idCount = 1;
   public GeneralPath path;

   public PotentialRoom(ArrayList<Line2D> walls) {
      this.walls = walls;
      ROOMID = idCount;
      idCount++;
      pointList = new ArrayList<>();
      makeList(walls);
      makePath();
   }

   private void makeList(ArrayList<Line2D> walls) {
      walls.forEach(wall->{
         pointList.add(new Point((int)wall.getX1(),(int)wall.getY1()));
         pointList.add(new Point((int)wall.getX2(),(int)wall.getY2()));
      });
   }

   private void makePath() {
      if (this.path == null) {
         this.path = new GeneralPath();
      }
      if (this.path.getCurrentPoint() == null) {
         this.path.moveTo(this.pointList.get(0).getX(), this.pointList.get(0).getY());
         for (int i = 1; i < this.pointList.size(); i++) {
            this.path.lineTo(this.pointList.get(i).getX(), this.pointList.get(i).getY());
         }
      } else {
         this.path.reset();
         this.path.moveTo(this.pointList.get(0).getX(), this.pointList.get(0).getY());
         for (int i = 1; i < this.pointList.size(); i++) {
            this.path.lineTo(this.pointList.get(i).getX(), this.pointList.get(i).getY());
         }
      }
   }

   private boolean pointBetween(Point p1, Point p2, Point p3) {
      return Point.distance(p2.getX(), p2.getY(), p1.getX(), p1.getY())
         + Point.distance(p1.getX(), p1.getY(), p3.getX(), p3.getY()) == Point.distance(p2.getX(), p2.getY(),
         p3.getX(), p3.getY());
   }

   public boolean contains(Point p) {
      if (this.path == null) {
         return false;
      }
      if (this.path.contains(p)) {
         return true;
      }
      return(onBoundary(p));
   }

   /**
    * Checks if this room contains the specified room
    * @param r the room to check is contained by this room
    * @return true if r is contained by this room
    */
   public boolean contains (Room r){
      return r.pointList.stream().allMatch(this::contains);
   }

   @Override
   public String toString() {
      if (this.path == null) {
         return "*Room#" + this.ROOMID + "-" + this.title;
      } else {
         return "Room#" + this.ROOMID + "-" + this.title;
      }
   }

   public boolean onBoundary(Point p) {
      boolean found = false;
      for (int i = 0; !found && i < this.pointList.size() - 1; i++) {
         found = this.pointBetween(p, this.pointList.get(i), this.pointList.get(i + 1));
      }
      return found;
   }
}
