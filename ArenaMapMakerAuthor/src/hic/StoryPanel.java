package hic;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.web.WebEngine;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.web.WebView;

/**
 * Presenter class used to update playerWindow and player data.
 */
@SuppressWarnings("serial")
public class StoryPanel extends JPanel {
	private final JFXPanel jfxPanel = new JFXPanel();
    private WebEngine engine;
	private MapPanel mapPanel;
	private JPanel disPanel = new JPanel();
	
	public StoryPanel(MapPanel mp) {
		mapPanel = mp;		
		createScene();
		this.add(jfxPanel, BorderLayout.CENTER);
		this.add(disPanel, BorderLayout.SOUTH);
		
		Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	URL url = this.getClass().getResource("/Release/play.html");
            	engine.load(url.toString());
            }
        });	
	}
	private void createScene() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                WebView view = new WebView();
                engine = view.getEngine();   
                Scene scene = new Scene(view);
                scene.setOnKeyPressed(event->{
                    switch (event.getCode()) {
                    case LEFT:
                    	mapPanel.goLeft();
                    	break;
                    case RIGHT:
                    	mapPanel.goRight();
                    	break;
                    case UP:
                    	mapPanel.goUp();
                    	break;
                    case DOWN:
                    	mapPanel.goDown();
                    	break;
					default:
						break;
                    }
                });
                jfxPanel.setScene(scene);
            }
        });
    }
}
