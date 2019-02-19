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


      for (Point2D[] cy : cycles) {
         String s = "" + cy[0];

         for (int i = 1; i < cy.length; i++)
         {
            s += "," + cy[i];
         }

         o(s);

      }
      System.out.println("The num of cycles is: "+cycles.size());

   }

   // Adds an edge to an undirected graph
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

   private void findNewCycles(Point2D[] path) {
      Point2D n = path[0];
      Point2D x;
      Point2D[] sub = new Point2D[path.length + 1];

      for (int i = 0; i < adjListArray.length; i++) {
         for (int y = 0; y <= 1; y++) {
            if (adjListArray[i][y].equals(n)){
            //  edge refers to our current node
               x = adjListArray[i][(y + 1) % 2];
               if (!visited(x, path)){
               //  neighbor node not on path yet
                  sub[0] = x;
                  System.arraycopy(path, 0, sub, 1, path.length);
                  //  explore extended path
                  findNewCycles(sub);
               } else if ((path.length > 2) && (x.equals(path[path.length - 1]))){
                  //  cycle found
                  Point2D[] p = normalize(path);
                  Point2D[] inv = invert(p);
                  if (isNew(p) && isNew(inv)) {
                     cycles.add(p);
                  }
               }
            }
         }
      }
   }

   //  check of both arrays have same lengths and contents
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

   //  create a path array with reversed order
   private Point2D[] invert(Point2D[] path)
   {
      Point2D[] p = new Point2D[path.length];

      for (int i = 0; i < path.length; i++)
      {
         p[i] = path[path.length - 1 - i];
      }

      return normalize(p);
   }

   //  rotate cycle path such that it begins with the smallest node
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

   //  compare path against known cycles
   //  return true, iff path is not a known cycle
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

   static void o(String s)
   {
      System.out.println(s);
   }

   //  return the Point of the array which is the smallest
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

   //  check if vertex n is contained in path
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

