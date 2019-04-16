package hic;

import pdc.Room;

import javax.swing.*;
import java.awt.*;

/**
 * Presenter class used to update playerWindow and player data.
 */
@SuppressWarnings("serial")
public class StoryPanel extends JPanel {
	private final JPanel jPanel;
	private MapPanel mapPanel;
	private JTextArea textArea;
	private TopBar topBar;

   public StoryPanel(MapPanel mp) {
      mapPanel = mp;
		jPanel = new JPanel();
		jPanel.setLayout(new BoxLayout(jPanel,BoxLayout.PAGE_AXIS));
		initialize();
		this.add(jPanel, BorderLayout.CENTER);
		JPanel disPanel = new JPanel();
		this.add(disPanel, BorderLayout.SOUTH);
	}

    private void initialize() {
      textArea = new JTextArea("Begin exploring with WASD.");
      printDetails(mapPanel.getRoomName(), mapPanel.getRoomDesc());

      JPanel root = new JPanel();
      root.setLayout(new BoxLayout(root,BoxLayout.PAGE_AXIS));

      topBar = new PlayerTopBar(mapPanel);
      JPanel topBarJPanel = topBar.getMainJPanel();
      root.add(topBarJPanel,BorderLayout.PAGE_START);

      textArea.setLineWrap(true);
      textArea.setEditable(false);
      textArea.setSize(400,400);
      textArea.setFocusable(false);
      root.add(textArea,BorderLayout.PAGE_END);

      root.setSize(400,400);


      JScrollPane pane = new JScrollPane(root);

      jPanel.add(pane);

    }

    public void printDetails(String name, String desc) {
        textArea.setText(name + "\n\n" + desc);
    }

    public void updateExits(Room currentRoom){
     topBar.updateExits(currentRoom);
    }
}
