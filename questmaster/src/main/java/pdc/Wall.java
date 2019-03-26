package pdc;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * Representation of a wall as a Line2D and type of wall
 */
@SuppressWarnings("serial")
public class Wall implements Serializable{
   private Line2D lineRepresentation;
   private Type type;

   public Wall(Line2D lineRepresentation,Type type){
      this.lineRepresentation = lineRepresentation;
      this.type = type;
   }

   public Wall(Point2D pointA, Point2D pointB, Type type){
      this.lineRepresentation = new Line2D.Double(pointA,pointB);
      this.type = type;
   }
   public void setType(Type type)
   {
      this.type = type;
   }
   public double getX1(){
      return lineRepresentation.getX1();
   }

   public double getX2(){
      return lineRepresentation.getX2();
   }
   public double getY1(){
      return lineRepresentation.getY1();
   }
   public double getY2(){
      return lineRepresentation.getY2();
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
   public Type getType(){
      return type;
   }
}
