package pdc;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * Representation of a wall as a Line2D and wallType of wall
 */
@SuppressWarnings("serial")
public class Wall implements Serializable{
   private Line2D lineRepresentation;
   private WallType wallType;

   public Wall(Line2D lineRepresentation, WallType wallType){
      this.lineRepresentation = lineRepresentation;
      this.wallType = wallType;
   }

   public Wall(Point2D pointA, Point2D pointB, WallType wallType){
      this.lineRepresentation = new Line2D.Double(pointA,pointB);
      this.wallType = wallType;
   }

   public void setType(WallType type)
   {
      this.wallType = type;
   }

   public Point2D getP1(){
      return lineRepresentation.getP1();
   }
   public Point2D getP2(){
      return lineRepresentation.getP2();
   }
   public double getDistance(Point2D p){
      return lineRepresentation.ptSegDist(p);
   }
   public Line2D getLineRepresentation(){
      return lineRepresentation;
   }
   public WallType getWallType(){
      return wallType;
   }
   public void setWallType(WallType newType) {this.wallType = newType;}

   /**
    * Determines if this is longer than the specified Wall. Used for collinearity
    * @param wall the wall to check against
    * @return true if this is longer than wall
    */
   public boolean isLongerThan(Wall wall){
      double thisXDelta = Math.pow(this.getLineRepresentation().getX2()-this.getLineRepresentation().getX1(),2);
      double thisYDelta = Math.pow(this.getLineRepresentation().getY2()-this.getLineRepresentation().getY1(),2);
      double thisLength = Math.sqrt(thisXDelta+thisYDelta);

      double wallXDelta = Math.pow(wall.getLineRepresentation().getX2()-wall.getLineRepresentation().getX1(),2);
      double wallYDelta = Math.pow(wall.getLineRepresentation().getY2()-wall.getLineRepresentation().getY1(),2);
      double wallLength = Math.sqrt(wallXDelta+wallYDelta);

      return thisLength > wallLength;
   }

   /**
    * Determines if p is an endpoint of this
    * @param p the point to check
    * @return true if p is an endpoint of this
    */
   public boolean hasEndpoint(Point p) {
      return this.getP1().equals(p)||this.getP2().equals(p);
   }

   public boolean isTraversable(){
      return isPortal()||wallType.equals(WallType.TRANSPARENT);
   }

   public boolean isPortal(){
      return wallType.equals(WallType.ARCHWAY)||wallType.equals(WallType.CLOSEDDOOR)||wallType.equals(WallType.OPENDOOR);
   }
}
