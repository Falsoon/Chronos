package hic;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
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
	private JPanel disPanel = new JPanel();

   public StoryPanel(MapPanel mp) {
		mapPanel = mp;		
		createScene();
		this.add(jfxPanel, BorderLayout.CENTER);
		this.add(disPanel, BorderLayout.SOUTH);
	}
	private void createScene() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Text t = new Text("Begin exploring with WASD.");
                printDetails(mapPanel.getRoomName(), mapPanel.getRoomDesc(), t);
                VBox root = new VBox();
                root.getChildren().add(t);
                JPanel topBar = new PlayerTopBar().getMainJPanel();
                root.getChildren().add(topBar);
                root.setPrefSize(400, 400);
                t.setTextAlignment(TextAlignment.LEFT);
                t.setWrappingWidth(375);
                ScrollPane pane = new ScrollPane(root);
                pane.setFitToWidth(true);
                Scene scene = new Scene(pane);

                scene.addEventFilter(KeyEvent.KEY_PRESSED, event->{
                    switch (event.getCode()) {
                        case A:
                        case LEFT:
                            mapPanel.goLeft();
                            printDetails(mapPanel.getRoomName(), mapPanel.getRoomDesc(), t);
                            break;
                        case D:
                        case RIGHT:
                            mapPanel.goRight();
                            printDetails(mapPanel.getRoomName(), mapPanel.getRoomDesc(), t);
                            break;
                        case W:
                        case UP:
                            mapPanel.goUp();
                            printDetails(mapPanel.getRoomName(), mapPanel.getRoomDesc(), t);
                            break;
                        case S:
                        case DOWN:
                            mapPanel.goDown();
                            printDetails(mapPanel.getRoomName(), mapPanel.getRoomDesc(), t);
                            break;
                        default:
                            break;
                    }
                    event.consume();
                });
                jfxPanel.setScene(scene);
            }

            private void printDetails(String name, String desc, Text t) {
                t.setText(name + "\n\n" + desc);
            }
        });
    }
}
