import java.awt.geom.GeneralPath;

public class Door {
	public GeneralPath path;
	public int orientation;
	public Boolean open;
	
	public Door(GeneralPath p) {
		path = p;
		orientation = 0;
		open = false;
	}
	
	public void open() {
		orientation += 90;
	}
	
	public void close() {
		orientation -= 90;
	}
}
