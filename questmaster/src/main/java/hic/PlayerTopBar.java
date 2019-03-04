package hic;

public class PlayerTopBar implements TopBar {
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
