package hic;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import javax.swing.*;
import java.awt.*;

/**
 * Presenter class used to update playerWindow and player data.
 */
@SuppressWarnings("serial")
public class StoryPanel extends JPanel {
	private final JFXPanel jfxPanel = new JFXPanel();
	private MapPanel mapPanel;

   public StoryPanel(MapPanel mp) {
		mapPanel = mp;		
		createScene();
		this.add(jfxPanel, BorderLayout.CENTER);
      JPanel disPanel = new JPanel();
      this.add(disPanel, BorderLayout.SOUTH);
	}

	private void createScene() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
               Text t = new Text("Begin exploring with WASD.");
               printDetails(mapPanel.getRoomName(), mapPanel.getRoomDesc(), t);
               VBox root = new VBox();
               ///TODO add top bar here
               PlayerTopBar topBar = new PlayerTopBar();
               root.getChildren().add(t);
               root.setPrefSize(400, 400);
               t.setTextAlignment(TextAlignment.LEFT);
               t.setWrappingWidth(375);
               ScrollPane pane = new ScrollPane(root);
               pane.setFitToWidth(true);
               Scene scene = new Scene(pane);
               scene.setOnKeyPressed(event->{
                   switch (event.getCode()) {
                       case A:
                           mapPanel.goLeft();
                           printDetails(mapPanel.getRoomName(), mapPanel.getRoomDesc(), t);
                           break;
                       case D:
                           mapPanel.goRight();
                           printDetails(mapPanel.getRoomName(), mapPanel.getRoomDesc(), t);
                           break;
                       case W:
                           mapPanel.goUp();
                           printDetails(mapPanel.getRoomName(), mapPanel.getRoomDesc(), t);
                           break;
                       case S:
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
            }
        });
    }
}
