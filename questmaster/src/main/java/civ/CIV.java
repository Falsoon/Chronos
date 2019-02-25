package civ;

import hic.FormWindow;
import pdc.*;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * This class is used as the civ/presenter class for map
 */
public class CIV {
	public Map map;
	public FormCiv formCiv;

	public CIV() {
		map = new Map();
		formCiv = new FormCiv();
	}

	public void mousePressed(Point point,boolean isAltDown, boolean isLeftButton){
	   System.out.println(RoomList.getInstance().list.size());
		if (!isAltDown) {
			point.setLocation(Math.round(((double) point.x) / Constants.GRIDDISTANCE) * Constants.GRIDDISTANCE,
					Math.round(((double) point.y) / Constants.GRIDDISTANCE) * Constants.GRIDDISTANCE);
		}
		if (isLeftButton) {
			if (map.isCreating()) {
				map.mousePressed(point);
			} else {
				if (!map.getPlayer().isPlaying()) {
					Room room = RoomList.getInstance().getRoom(point);
					if (room != null) {
						EventQueue.invokeLater(new Runnable() {
							public void run() {
								try {
									formCiv.setRoomReference(room.toString());
									FormWindow window = new FormWindow(formCiv, true);
									window.frame.setVisible(true);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					}
				}
			}
		}else {
		   System.out.println("Mouse Position: " + point);
			map.stopDrawing();
		}
	}

	public boolean undo() {
		return map.undo();
	}

	public boolean clear() {
		map = new Map();
		RoomList.getInstance().reset();
		return true;
	}

	public void draw(Graphics g) {
		map.draw(g);
	}

	public void outlining() {
		map.outlining();
	}

	public void walling() {
		map.walling();
	}

	public void startGame() {
		map.startGame();
	}

	public void placeStart() {
		map.placePlayerStart();
	}

	public boolean placedPlayer() {
		return map.getPlayer().isPlaced();
	}

	public void goUp() {
		map.getPlayer().goUp();
	}

	public void goDown() {
		map.getPlayer().goDown();
	}

	public void goLeft() {
		map.getPlayer().goLeft();
	}

	public void goRight() {
		map.getPlayer().goRight();
	}

	public String getRoomName() {
		return map.getPlayer().getRoomName();
	}

	public String getRoomDesc() {
		return map.getPlayer().getRoomDesc();
	}

	public void stopDrawing() {
		map.stopDrawing();
	}

	public void dooring() {
		map.dooring();
	}

	public void archwayAdd() { map.archwayAdd();}
	//look into creating door pointList
	public int numOfDoors() {
		return map.numOfDoors();
	}

	public void drawRoom(String str) {
		map.drawRoom(str);
	}

	public void setSelectedRoom(String str) {
		map.setSelectedRoom(str);
	}

	public ArrayList<String> getRoomList() {
		ArrayList<String> rList = new ArrayList<String>();
		for (int i = 0; i < RoomList.getInstance().list.size(); i++) {
			rList.add(RoomList.getInstance().list.get(i).toString());
		}
		return rList;
	}

	public Rectangle getRoomBounds(String str) {
		Room r = RoomList.getInstance().getRoomByStr(str);
		if (r == null || r.path == null) {
			return null;
		} else {
			return r.path.getBounds();
		}
	}
	public boolean pathIntersection(Point p1, Point p2, Point pA, Point pB) {
		boolean ans = false;
		//found from page 1018 and 1017 in Intro to Algorithms book ed 3
		int d1 = direction(pA, pB, p1);
		int d2 = direction(pA, pB, p2);
		int d3 = direction(p1, p2, pA);
		int d4 = direction(p1, p2, pB);
		
		if( ((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0))  && ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0))) {
			ans = true;
		}
		else if( d1 == 0 && onSegment(pA, pB, p1) ) {
			ans = true;
		}
		else if( d2 == 0 && onSegment(pA, pB, p2) ) {
			ans = true;
		}
		else if( d3 == 0 && onSegment(p1, p2, pA) ) {
			ans = true;
		}
		else if( d4 == 0 && onSegment(p1, p2, pB) ) {
			ans = true;
		}
		
		return ans;
	}
	
	public boolean onSegment(Point pi, Point pj, Point pk) {
		boolean ans = false;
		
		if( Math.min(pi.x, pj.x) <= pk.x && Math.max(pi.x, pj.x) >= pk.x ) {
			if( Math.min(pi.y, pj.y) <= pk.y && Math.max(pi.y, pj.y) >= pk.y ) {
				ans = true;
			}
		}
		
		return ans;
	}
	
	public int direction(Point p0, Point p1, Point p2) {
		
		return (p1.x - p0.x)*(p2.y - p0.y) - (p2.x - p0.x)*(p1.y - p0.y);
	}
	/*

	NOTE: deliberately not deleted to be left as an output hook

	public void outputStory() {
		File out = new File("INFORM_Source/output.ni");
		PrintStream output = null;
		try {
			output = new PrintStream(out);
			System.setOut(output);
			StoryBuilder.build(map);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			output.close();
		}
	}
	*/

	public void showDialog(){

   }
}
