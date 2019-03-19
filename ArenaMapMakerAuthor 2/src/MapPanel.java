import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import javax.swing.JPanel;

public class MapPanel extends JPanel implements MouseListener{
	
	private boolean paintRooms;

	public MapPanel() {
		this.setBackground(Color.black);
		addMouseListener(this);
	}

	public void paintRooms() {
		paintRooms = true;
	}
	
	public void clear() {
		paintRooms = false;
	}

	public void paintComponent(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, 600, 600);
		if(paintRooms) {
			g.setColor(Color.white);
			g.drawString("....................", 100, 100);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
