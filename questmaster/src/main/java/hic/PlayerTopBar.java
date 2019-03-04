package hic;

import javax.swing.*;

public class PlayerTopBar implements TopBar {
   private JPanel mainJPanel = TopBar.mainJPanel;
   private void initialize(){
      northButton.addActionListener(e -> goNorthRoom());
      southButton.addActionListener(e -> goSouthRoom());
      eastButton.addActionListener(e -> goEastRoom());
      westButton.addActionListener(e -> goWestRoom());
      upButton.addActionListener(e -> goUpRoom());
      downButton.addActionListener(e -> goDownRoom());
      labelJPanel.add(panelLabel);
      mainJPanel.add(labelJPanel);

      buttonsPanel.add(TopBar.northButton);
      buttonsPanel.add(TopBar.northButton);
      buttonsPanel.add(TopBar.northButton);
      buttonsPanel.add(TopBar.northButton);
      buttonsPanel.add(TopBar.northButton);
      buttonsPanel.add(TopBar.northButton);

      mainJPanel.add(buttonsPanel);
   }

   public JPanel getMainJPanel(){
      return mainJPanel;
   }

   private void goNorthRoom() {
   }

   private void goSouthRoom() {
   }

   private void goEastRoom() {
   }

   private void goWestRoom() {
   }

   private void goUpRoom() {
   }

   private void goDownRoom() {
   }

}
