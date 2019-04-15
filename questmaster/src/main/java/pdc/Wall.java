package pdc;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

import static pdc.Geometry.lineSegmentsMatch;
import static pdc.Geometry.lineSegmentsShareSingleEndpoint;

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
   public Double getX1() { return lineRepresentation.getX1();}
   public Double getX2() { return lineRepresentation.getX2();}
   public Double getY1() { return lineRepresentation.getY1();}
   public Double getY2() { return lineRepresentation.getY2();}
   public double getDistance(Point2D p){
      return lineRepresentation.ptSegDist(p);
   }
   public WallType getWallType(){
      return wallType;
   }
   public Line2D getLineRepresentation(){ return lineRepresentation; }
   public void setWallType(WallType newType) {this.wallType = newType;}
   public boolean containsWall(Wall wall){
      return containsPoint(wall.getP1())&&containsPoint(wall.getP2());
   }
   public boolean containsPoint(Point2D p){
      return Geometry.pointLiesOnLine(p,lineRepresentation);
   }
   public boolean containsAll(ArrayList<Wall> walls){
      boolean containsAll = true;
      for(Wall wall : walls){
         if(!containsWall(wall)){
            containsAll = false;
            break;
         }
      }
      return containsAll;
   }
   public boolean intersects(Wall wall){
      return this.lineRepresentation.intersectsLine(wall.lineRepresentation);
   }

   public Optional<Point> getIntersectionPoint(Wall otherWall){
      return Geometry.intersectionPoint(this.lineRepresentation,otherWall.lineRepresentation);
   }

   public Optional<Point> getSharedEndpoint(Wall otherWall){
      return lineSegmentsShareSingleEndpoint(this.lineRepresentation,otherWall.lineRepresentation);
   }

   public boolean valueEquals(Wall otherWall){
      return this.wallType.equals(otherWall.wallType) && lineSegmentsMatch(this.lineRepresentation,otherWall.lineRepresentation);
   }

   public boolean representationMatchesLine(Line2D line){
      return lineSegmentsMatch(lineRepresentation,line);
   }

   public boolean intersectsAndIsCollinearWith(Wall otherWall){
      return Geometry.lineSegmentsIntersectAndCollinear(this.lineRepresentation,otherWall.lineRepresentation);
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
