package hic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Presenter class used to update playerWindow and player data.
 */
@SuppressWarnings("serial")
public class StoryPanel extends JPanel {
	private final JPanel jPanel = new JPanel(new BorderLayout());
	private MapPanel mapPanel;
	private JTextArea textArea;

   public StoryPanel(MapPanel mp) {
		mapPanel = mp;		
		initialize();
		this.add(jPanel, BorderLayout.CENTER);
      JPanel disPanel = new JPanel();
      this.add(disPanel, BorderLayout.SOUTH);
	}

	private void initialize() {
      textArea = new JTextArea("Begin exploring with WASD.");
      printDetails(mapPanel.getRoomName(), mapPanel.getRoomDesc(), textArea);
      JPanel root = new JPanel(new BorderLayout());
      ///TODO add top bar here
      PlayerTopBar topBar = new PlayerTopBar();
      textArea.setLineWrap(true);
      textArea.setEditable(false);
      textArea.setSize(400,400);
      root.add(textArea);
      root.setSize(400,400);
      JScrollPane pane = new JScrollPane(root);
      KeyListener keyListener = new KeyListener() {
         @Override
         public void keyTyped(KeyEvent e) {

         }

         @Override
         public void keyPressed(KeyEvent e) {
            handleKeyPressed(e);
         }

         @Override
         public void keyReleased(KeyEvent e) {

         }
      };
      pane.addKeyListener(keyListener);
      pane.setFocusable(true);
      jPanel.add(pane);

   }
   private void handleKeyPressed(KeyEvent e){
      switch (e.getKeyChar()){
         case 'a':
            mapPanel.goLeft();
            printDetails(mapPanel.getRoomName(), mapPanel.getRoomDesc(), textArea);
            break;
         case 'd':
            mapPanel.goRight();
            printDetails(mapPanel.getRoomName(), mapPanel.getRoomDesc(), textArea);
            break;
         case 'w':
            mapPanel.goUp();
            printDetails(mapPanel.getRoomName(), mapPanel.getRoomDesc(), textArea);
            break;
         case 's':
            mapPanel.goDown();
            printDetails(mapPanel.getRoomName(), mapPanel.getRoomDesc(), textArea);
            break;
         default:
            break;

      }
   }

   private void printDetails(String name, String desc, JTextArea textArea) {
      textArea.setText(name + "\n\n" + desc);
   }
}
