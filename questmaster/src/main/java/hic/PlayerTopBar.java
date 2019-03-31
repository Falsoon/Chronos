package hic;

import javax.swing.*;

public class PlayerTopBar extends JComponent implements TopBar{
    private JPanel mainJPanel = TopBar.mainJPanel;

    public PlayerTopBar(){
        mainJPanel.setLayout(new BoxLayout(mainJPanel,BoxLayout.PAGE_AXIS));
        labelJPanel.add(panelLabel);
        mainJPanel.add(labelJPanel);

        northButton.addActionListener(e -> goNorthRoom());
        southButton.addActionListener(e -> goSouthRoom());
        eastButton.addActionListener(e -> goEastRoom());
        westButton.addActionListener(e -> goWestRoom());
        upButton.addActionListener(e -> goUpRoom());
        downButton.addActionListener(e -> goDownRoom());

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
