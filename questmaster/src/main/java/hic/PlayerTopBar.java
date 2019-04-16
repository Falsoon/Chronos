package hic;

import hic.MapPanel;
import pdc.CardinalDirection;
import pdc.Room;

import java.util.Set;

import javax.swing.*;


public class PlayerTopBar extends JComponent implements TopBar{
    private JPanel mainJPanel = TopBar.mainJPanel;
    private MapPanel mapPanel;

    public PlayerTopBar(MapPanel mapPanel){
       this.mapPanel = mapPanel;
       mainJPanel.setLayout(new BoxLayout(mainJPanel,BoxLayout.PAGE_AXIS));
       labelJPanel.add(panelLabel);
       mainJPanel.add(labelJPanel);

       northButton.addActionListener(e -> goNorthRoom());
       southButton.addActionListener(e -> goSouthRoom());
       eastButton.addActionListener(e -> goEastRoom());
       westButton.addActionListener(e -> goWestRoom());
       upButton.addActionListener(e -> goUpRoom());
       downButton.addActionListener(e -> goDownRoom());

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

    private void goNorthRoom() {
       mapPanel.teleportThroughNorthPortal();
       updateExits(mapPanel.getRoom(mapPanel.civ.map.getPlayer().getPosition()));
    }

    private void goSouthRoom() {
       mapPanel.teleportThroughSouthPortal();
       updateExits(mapPanel.getRoom(mapPanel.civ.map.getPlayer().getPosition()));
    }

    private void goEastRoom() {
       mapPanel.teleportThroughEastPortal();
       updateExits(mapPanel.getRoom(mapPanel.civ.map.getPlayer().getPosition()));
    }

    private void goWestRoom() {
       mapPanel.teleportThroughWestPortal();
       updateExits(mapPanel.getRoom(mapPanel.civ.map.getPlayer().getPosition()));
    }

    private void goUpRoom() {
       mapPanel.teleportThroughUpPortal();
       updateExits(mapPanel.getRoom(mapPanel.civ.map.getPlayer().getPosition()));
    }

    private void goDownRoom() {
       mapPanel.teleportThroughDownPortal();
       updateExits(mapPanel.getRoom(mapPanel.civ.map.getPlayer().getPosition()));
    }

    public void updateExits(Room currentRoom){
      Set<CardinalDirection> portalDirections = currentRoom.getPortals().keySet();
      resetButtons();
      for(CardinalDirection direction : portalDirections){
         setEnabled(direction);
      }
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

   public void resetButtons() {
      northButton.setEnabled(false);
      southButton.setEnabled(false);
      eastButton.setEnabled(false);
      westButton.setEnabled(false);
      upButton.setEnabled(false);
      downButton.setEnabled(false);
   }

}
