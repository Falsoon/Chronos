package hic;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.net.URL;

import javax.sound.sampled.Control;
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
import javafx.scene.*;
import javafx.scene.text.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Label;

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
            	// URL url = this.getClass().getResource("/Release/play.html");
            	// engine.load(url.toString());
            }
        });	
	}
	private void createScene() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                Text t = new Text(mapPanel.getRoomName() + "\n\n" + mapPanel.getRoomDesc());
                TextFlow tf = new TextFlow(t);
                VBox root = new VBox();
                root.getChildren().add(tf);
                //VBox.setVgrow(t, Priority.ALWAYS);
                root.setPrefSize(400, 400);
                // Region root = new Region();
                //t.setX(400); 
                //t.setY(400);
                t.setTextAlignment(TextAlignment.LEFT);
                //t.setWrappingWidth(400);
                
                Scene scene = new Scene(root);
                scene.setOnKeyPressed(event->{
                    switch (event.getCode()) {
                        case LEFT:
                            mapPanel.goLeft();
                            printDetails(mapPanel.getRoomName(), mapPanel.getRoomDesc(), t);
                            break;
                        case RIGHT:
                            mapPanel.goRight();
                            printDetails(mapPanel.getRoomName(), mapPanel.getRoomDesc(), t);
                            break;
                        case UP:
                            mapPanel.goUp();
                            printDetails(mapPanel.getRoomName(), mapPanel.getRoomDesc(), t);
                            break;
                        case DOWN:
                            mapPanel.goDown();
                            printDetails(mapPanel.getRoomName(), mapPanel.getRoomDesc(), t);
                            break;
                        default:
                            break;
                    }
                });
                jfxPanel.setScene(scene);
            }

            private void printDetails(String name, String desc, Text t) {
                t.setText(name + "\n\n" + desc);
                System.out.println("please just work this time: " + name + desc + " | " + t.getText());
                repaint();
            }
        });
    }
}
