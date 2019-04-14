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
import javafx.scene.Scene;
import javafx.scene.*;
import javafx.scene.text.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Label;
import javafx.event.*;
import javafx.scene.input.*;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

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
                            mapPanel.dropKey();
                            break;
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
                        case K:
                            System.out.println("K");
                            mapPanel.pickUpKey();
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
