package pdc;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a graph theory graph, used for room detection
 * adapted from https://stackoverflow.com/a/14115627
 */
public class Graph {

   public ArrayList<Point2D[]> cycles;
   private Point2D[][] adjListArray;
   private int adjListArrayIndex;
   private HashMap<Point2D,Integer> vertexOrdering;
   private int vertexOrderingIndex;

   public Graph(int edges) {
      // define the size of array as
      // number of edges
      adjListArrayIndex = 0;
      vertexOrderingIndex = 0;
      cycles = new ArrayList<>();
      vertexOrdering = new HashMap<>();
      adjListArray = new Point2D[edges][2];
   }

   /**
    * Function to find all cycles in the graph
    */
   public void findCycles() {
      //System.out.println("here be the graph: " + adjListArray[0]);
      for (int i = 0; i < adjListArray.length; i++)
         for (int j = 0; j < adjListArray[i].length; j++)
         {
            findNewCycles(new Point2D[] {adjListArray[i][j]});
         }
   }

   /**
    * Method to add an edge to the graph
    * @param src the first vertex
    * @param dest the second vertext
    */
   public void addEdge(Point2D src, Point2D dest) {
      // Add an edge from src to dest.
      adjListArray[adjListArrayIndex][0] = src;
      adjListArray[adjListArrayIndex][1] = dest;
      adjListArrayIndex++;
      //index the vertices so they can be checked in smallest() later
      if(!vertexOrdering.containsKey(src)){
         vertexOrdering.put(src,vertexOrderingIndex);
         vertexOrderingIndex++;
      }
      if(!vertexOrdering.containsKey(dest)){
         vertexOrdering.put(dest,vertexOrderingIndex);
         vertexOrderingIndex++;
      }
   }

   /**
    * Method to find new cycles in the graph
    * @param path the current path in the cycle detection
    */
   private void findNewCycles(Point2D[] path) {
      Point2D n = path[0];
      Point2D x;
      Point2D[] sub = new Point2D[path.length + 1];

      for (Point2D[] point2DS : adjListArray) {
         for (int y = 0; y <= 1; y++) {
            if (point2DS[y].equals(n)) {
               //  edge refers to our current node
               x = point2DS[(y + 1) % 2];
               if (!visited(x, path)) {
                  //  neighbor node not on path yet
                  sub[0] = x;
                  System.arraycopy(path, 0, sub, 1, path.length);
                  //  explore extended path
                  findNewCycles(sub);
               } else if ((path.length > 2) && (x.equals(path[path.length - 1]))) {
                  //  cycle found
                  Point2D[] p = normalize(path);
                  Point2D[] inv = reverse(p);
                  if (isNew(p) && isNew(inv)) {
                     cycles.add(p);
                  }
               }
            }
         }
      }
   }

   /**
    * Method to determine if 2 arrays are the same
    * @param a the first array
    * @param b the second array
    * @return true if the arrays have the same contents
    */
   private boolean equals(Point2D[] a, Point2D[] b)
   {
      boolean ret = (a[0].equals(b[0])) && (a.length == b.length);

      for (int i = 1; ret && (i < a.length); i++)
      {
         if (!a[i].equals(b[i]))
         {
            ret = false;
         }
      }

      return ret;
   }

   /**
    * Reverses the order of vertices on the path
    * @param path the path of vertices to reverse
    * @return path with its elements in reversed order
    */
   private Point2D[] reverse(Point2D[] path)
   {
      Point2D[] p = new Point2D[path.length];

      for (int i = 0; i < path.length; i++)
      {
         p[i] = path[path.length - 1 - i];
      }

      return normalize(p);
   }

   /**
    * Rotates the cycle so that the vertices are in a standardized order
    * @param path the vertices of the path
    * @return the path in a standardized order
    */
   private Point2D[] normalize(Point2D[] path)
   {
      Point2D[] p = new Point2D[path.length];
      Point2D x = smallest(path);
      Point2D n;

      System.arraycopy(path, 0, p, 0, path.length);

      while (!p[0].equals(x))
      {
         n = p[0];
         System.arraycopy(p, 1, p, 0, p.length - 1);
         p[p.length - 1] = n;
      }

      return p;
   }

   /**
    * Determines if a path is unique or a permutation of an existing path
    * @param path the path to check
    * @return true if the path is unique
    */
   private boolean isNew(Point2D[] path)
   {
      boolean ret = true;

      for(Point2D[] p : cycles)
      {
         if (equals(p, path))
         {
            ret = false;
            break;
         }
      }

      return ret;
   }

   /**
    * Method to output the specified string
    * @param s the string to output
    */
   static void o(String s)
   {
      System.out.println(s);
   }

   /**
    * Method to determine the smallest point of the path
    * @param path the path to examine
    * @return the smallest point on the path
    */
   private Point2D smallest(Point2D[] path)
   {
      Point2D min = path[0];

      for (Point2D p : path)
      {
         if (vertexOrdering.get(p) < vertexOrdering.get(min))
         {
            min = p;
         }
      }

      return min;
   }

   /**
    * Method to determine if the specified point is contained in the specified path
    * @param n the point to check
    * @param path the path to check
    * @return true if the path contains the point n
    */
   private boolean visited(Point2D n, Point2D[] path) {
      boolean ret = false;

      for (Point2D p : path)
      {
         if (p.equals(n))
         {
            ret = true;
            break;
         }
      }

      return ret;
   }

}

