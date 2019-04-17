package pdc;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Optional;

/**
 * Helper class containing geometry libraries useful when placing walls
 */
class Geometry {
   static Point point2DToPoint(Point2D p2d){
      return new Point((int)p2d.getX(),(int)p2d.getY());
   }

   /**
    * Determines where the specified Line2Ds intersect
    * Adapted from https://www.geeksforgeeks.org/program-for-point-of-intersection-of-two-lines/
    * and https://www.baeldung.com/java-intersection-of-two-lines
    * @param lineA the first line
    * @param lineB the second line
    * @return the intersection as a point if the lines intersect, Optional.Empty() if not
    */
   static Optional<Point> intersectionPoint(Line2D lineA, Line2D lineB) {
      // Line AB represented as a1x + b1y = c1
      double a1 = lineA.getY2() - lineA.getY1();
      double b1 = lineA.getX1() - lineA.getX2();
      double c1 = a1*(lineA.getX1()) + b1*(lineA.getY1());

      // Line CD represented as a2x + b2y = c2
      double a2 = lineB.getY2() - lineB.getY1();
      double b2 = lineB.getX1() - lineB.getX2();
      double c2 = a2*(lineB.getX1())+ b2*(lineB.getY1());

      double determinant = a1*b2 - a2*b1;

      if (determinant == 0)
      {
         // The lines are parallel
         return Optional.empty();
      }
      else
      {
         double x = (b2*c1 - b1*c2)/determinant;
         double y = (a1*c2 - a2*c1)/determinant;
         return Optional.of(new Point((int)x, (int)y));
      }
   }

   /**
    * Determines the shared endpoint of two Line2Ds, if it exists
    * @param lineA the first Line2D
    * @param lineB the second Line2D
    * @return Returns the shared endpoint of two Line2Ds if it exists, otherwise Optional.empty()
    */
   static Optional<Point> lineSegmentsShareSingleEndpoint(Line2D lineA, Line2D lineB) {
      Point candidateP1 = point2DToPoint(lineA.getP1());
      Point candidateP2 = point2DToPoint(lineA.getP2());
      Point listP1 = point2DToPoint(lineB.getP1());
      Point listP2 = point2DToPoint(lineB.getP2());
      if(candidateP1.equals(listP1) || candidateP1.equals(listP2)){
         return Optional.of(candidateP1);
      }else if(candidateP2.equals(listP1)|| candidateP2.equals(listP2)){
         return Optional.of(candidateP2);
      }
      return Optional.empty();
   }

   /**
    * Determines if the two line segments intersect and are collinear
    * @param lineA the first line
    * @param lineB the second line
    * @return true if the two line segments intersect and are collinear
    */
   static boolean lineSegmentsIntersectAndCollinear(Line2D lineA, Line2D lineB) {
      if(lineA.getX2()==lineA.getX1() && lineB.getX2() == lineB.getX1()) {
         return xMatch(lineA, lineB) && lineA.intersectsLine(lineB);
      }
      else if(lineA.getY2()==lineA.getY1() && lineB.getY2() == lineB.getY1()) {
         return yMatch(lineA, lineB) && lineA.intersectsLine(lineB);
      }
      return false;
   }

   /**
    * Helper method to determine if 2 lines are the same
    * @param lineA the first line
    * @param lineB the second line
    * @return true if lineA and lineB have the same coordinates in either order
    */
   static boolean lineSegmentsMatch(Line2D lineA, Line2D lineB){
      return xMatch(lineA,lineB)&&yMatch(lineA,lineB);
   }

   /**
    * Helper method to determine if the x coordinates of the 2 lines are the same
    * @param lineA the first line
    * @param lineB the second line
    * @return true of lineA and lineB have the same x coordinate in either order
    */
   private static boolean xMatch(Line2D lineA, Line2D lineB){
      return (lineA.getX1()==lineB.getX1()
         ||lineA.getX1()==lineB.getX2())
         &&
         (lineA.getX2()==lineB.getX1()
            ||lineA.getX2()==lineB.getX2());
   }

   /**
    * Helper method to determine if the y coordinates of the 2 lines are the same
    * @param lineA the first line
    * @param lineB the second line
    * @return true of lineA and lineB have the same y coordinate in either order
    */
   private static boolean yMatch(Line2D lineA, Line2D lineB){
      return (lineA.getY1()==lineB.getY1()
         ||lineA.getY1()==lineB.getY2())
         &&
         (lineA.getY2()==lineB.getY1()
            ||lineA.getY2()==lineB.getY2());
   }

   /**
    * Determines if the Point p lies on the line
    * @param p the point
    * @param line the line
    * @return true if the point lies on the line
    */
   public static boolean pointLiesOnLine(Point2D p, Line2D line){
      return (line.getP1().distance(p) + line.getP2().distance(p) == line.getP1().distance(line.getP2()));
   }

}
