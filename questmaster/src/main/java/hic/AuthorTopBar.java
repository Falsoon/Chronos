package hic;

import pdc.CardinalDirection;

import javax.swing.*;

public class AuthorTopBar extends JComponent implements TopBar {
   private JPanel mainJPanel = TopBar.mainJPanel;

   public AuthorTopBar(){
      mainJPanel.setLayout(new BoxLayout(mainJPanel,BoxLayout.PAGE_AXIS));
      labelJPanel.add(panelLabel);
      mainJPanel.add(labelJPanel);

      northButton.setEnabled(false);
      southButton.setEnabled(false);
      eastButton.setEnabled(false);
      westButton.setEnabled(false);
      upButton.setEnabled(false);
      downButton.setEnabled(false);

      cardinalDirectionButtonsPanel.add(TopBar.northButton);
      cardinalDirectionButtonsPanel.add(TopBar.southButton);
      cardinalDirectionButtonsPanel.add(TopBar.eastButton);
      cardinalDirectionButtonsPanel.add(TopBar.westButton);
      verticalButtonsPanel.add(TopBar.upButton);
      verticalButtonsPanel.add(TopBar.downButton);

      buttonsPanel.setLayout(new BoxLayout(buttonsPanel,BoxLayout.PAGE_AXIS));
      buttonsPanel.add(cardinalDirectionButtonsPanel);
      buttonsPanel.add(verticalButtonsPanel);

      mainJPanel.add(buttonsPanel);
   }

   public JPanel getMainJPanel(){
      return mainJPanel;
   }

   public void setEnabled(CardinalDirection direction){
      if(direction.equals(CardinalDirection.NORTH)){
         northButton.setEnabled(true);
      }else if(direction.equals(CardinalDirection.SOUTH)){
         southButton.setEnabled(true);
      } else if(direction.equals(CardinalDirection.EAST)){
         eastButton.setEnabled(true);
      } else if(direction.equals(CardinalDirection.WEST)){
         westButton.setEnabled(true);
      } else if(direction.equals(CardinalDirection.UP)){
         upButton.setEnabled(true);
      }else if(direction.equals(CardinalDirection.DOWN)){
         downButton.setEnabled(true);
      }
   }

   public void setRoomIdForButton(CardinalDirection direction, int id){
      JButton buttonToUpdate = null;
      String directionText = "";
      switch (direction){
         case NORTH:
             buttonToUpdate = northButton;
             directionText = "North";
             break;
         case SOUTH:
            buttonToUpdate = southButton;
            directionText = "South";
            break;
         case EAST:
            buttonToUpdate = eastButton;
            directionText = "East";
            break;
         case WEST:
            buttonToUpdate = westButton;
            directionText = "West";
            break;
         case UP:
            buttonToUpdate = upButton;
            directionText = "Up";
            break;
         case DOWN:
            buttonToUpdate = downButton;
            directionText = "Down";
            break;
      }
      buttonToUpdate.setText("<html>"+directionText+"<br />\nRoom #"+id+"</html>");
      buttonToUpdate.setEnabled(true);
   }

   public void resetButtons() {
      northButton.setEnabled(false);
      southButton.setEnabled(false);
      eastButton.setEnabled(false);
      westButton.setEnabled(false);
      upButton.setEnabled(false);
      downButton.setEnabled(false);

      northButton.setText("North");
      southButton.setText("South");
      eastButton.setText("East");
      westButton.setText("West");
      upButton.setText("Up");
      downButton.setText("Down");

   }
}
