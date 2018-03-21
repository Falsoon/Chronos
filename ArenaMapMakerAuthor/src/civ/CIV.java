package civ;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import pdc.*;
import hic.*;

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
		if (!isAltDown) {
			point.setLocation(Math.round(((double) point.x) / Constants.GRIDDISTANCE) * Constants.GRIDDISTANCE,
					Math.round(((double) point.y) / Constants.GRIDDISTANCE) * Constants.GRIDDISTANCE);
		}
		if (isLeftButton) {
			if (map.isCreating()) {
				map.mousePressed(point);
			} else {
				if (!map.getPlayer().isPlaying()) {
					Room room = RoomList.getRoom(point);
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
			map.stopDrawing();
		}
	}

	public boolean undo() {
		return map.undo();
	}

	public void clear() {
		map = new Map();
		RoomList.reset();
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

	public void stopDrawing() {
		map.stopDrawing();
	}

	public void dooring() {
		map.dooring();
	}

	public void drawRoom(String str) {
		map.drawRoom(str);
	}

	public void setSelectedRoom(String str) {
		map.setSelectedRoom(str);
	}

	public ArrayList<String> getRoomList() {
		ArrayList<String> rList = new ArrayList<String>();
		for (int i = 0; i < RoomList.list.size(); i++) {
			rList.add(RoomList.list.get(i).toString());
		}
		return rList;
	}

	public Rectangle getRoomBounds(String str) {
		Room r = RoomList.getRoomByStr(str);
		if (r == null || r.path == null) {
			return null;
		} else {
			return r.path.getBounds();
		}
	}

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
}
