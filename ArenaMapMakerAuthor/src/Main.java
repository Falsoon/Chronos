import java.awt.EventQueue;

public class Main {

	/**
	 * Launch the application.
	 */
	public Main(String[] args) {
		// TODO Auto-generated method stub
		AuthorWindow window = new AuthorWindow();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AuthorWindow.setInitialFrame(window);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
	}

}
