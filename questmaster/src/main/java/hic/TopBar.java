package hic;

import javax.swing.*;
import java.awt.*;

public interface TopBar{
    JPanel mainJPanel = new JPanel();
    JPanel labelJPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JPanel cardinalDirectionButtonsPanel = new JPanel(new FlowLayout());
    JPanel verticalButtonsPanel = new JPanel(new FlowLayout());
    JPanel buttonsPanel = new JPanel();
    JButton northButton = new JButton("North");
    JButton southButton = new JButton("South");
    JButton eastButton = new JButton("East");
    JButton westButton = new JButton("West");
    JButton upButton = new JButton("Up");
    JButton downButton = new JButton("Down");
    JLabel panelLabel = new JLabel("Exits");
    JPanel getMainJPanel();
}
