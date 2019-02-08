package pdc;

public class SelectRoom extends Room {

	public SelectRoom() {
		super();
		ROOMID = -1;
		idCount = 1;
	}
	@Override
	public String toString() {
		return "Select Room";
	}
}
