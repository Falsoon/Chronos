package hic;

import javax.swing.*;
import java.awt.*;

/**
 * Presenter class used to update playerWindow and player data.
 */
@SuppressWarnings("serial")
public class StoryPanel extends JPanel {
	private final JPanel jPanel = new JPanel();
	private MapPanel mapPanel;
	private JTextArea textArea;

   public StoryPanel(MapPanel mp) {
		mapPanel = mp;
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

      JPanel topBar = new PlayerTopBar().getMainJPanel();
      root.add(topBar,BorderLayout.PAGE_START);

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
}
