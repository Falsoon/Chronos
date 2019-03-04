package hic;

import javax.swing.*;
import java.awt.*;

public interface TopBar {
   JPanel mainJPanel = new JPanel(new BorderLayout());
   JPanel labelJPanel = new JPanel(new BorderLayout());
   JPanel buttonsPanel = new JPanel(new BorderLayout());
   JButton northButton = new JButton("North");
   JButton southButton = new JButton("South");
   JButton eastButton = new JButton("East");
   JButton westButton = new JButton("West");
   JButton upButton = new JButton("Up");
   JButton downButton = new JButton("Down");
   JLabel panelLabel = new JLabel("Exits");
}
