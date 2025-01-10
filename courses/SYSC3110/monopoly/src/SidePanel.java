import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * SidePanel class used to display Players info and buttons.
 *
 * @author Noah Hammoud
 */
public class SidePanel extends JPanel {
    private final int PANEL_WIDTH = 350;
    private final int PANEL_HEIGHT = 693;
    private final int INFO_AREA_HEIGHT = 620;
    private Map<String, JLabel> playerInfoMap;
    private MonopolyController controller;
    private String currencySymbol;


    /**
     * Constructor for SidePanel class.
     *
     * @param model The MonopolyModel that corresponds with the object.
     */
    public SidePanel(MonopolyModel model, String currency) {
        this.currencySymbol = currency;
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(new Color(211, 236, 211));
        this.playerInfoMap = new HashMap<>();
        this.addPlayersInfo(model);
        this.addButtons(model);
    }

    /**
     * Adds the players info Jlabels to the JPanel.
     */
    private void addPlayersInfo(MonopolyModel model) {
        int numberOfPlayer = model.getPlayerList().size();
        for (Player p: model.getPlayerList()) {
            String text = "<html><br>Money: " + currencySymbol + p.getMoney() + "<br>Properties: " +  p.getPropertyString() + "</html>";
            JLabel info = new JLabel(text);
            info.setName(p.getIdentifier());

            info.setFont(new Font("Aharoni", Font.BOLD, 12 ));
            info.setPreferredSize(new Dimension(PANEL_WIDTH - 10, (INFO_AREA_HEIGHT / numberOfPlayer)));

            Border line = BorderFactory.createLineBorder(Color.black);
            String titleString = "Player " + p.getIdentifier();
            if (p.getIsAI()) titleString += " (AI)";
            TitledBorder title = BorderFactory.createTitledBorder(line, titleString);
            title.setTitleFont(new Font("Aharoni", Font.PLAIN, 20));
            title.setTitleJustification(TitledBorder.CENTER);

            info.setVerticalAlignment(JLabel.NORTH);
            info.setBorder(title);
            this.playerInfoMap.put(p.getIdentifier(), info);
            this.add(info);
        }
    }

    /**
     * Updates a players information label.
     * @param player The player whose label is changing.
     */
    public void updatePlayerInfo(Player player) {
        JLabel playerLabel = this.playerInfoMap.get(player.getIdentifier());
        if (playerLabel == null) {
            throw new IllegalArgumentException("The Player object that was passed does not correspond with a player info label!");
        }
        else {
            playerLabel.setText("<html><br>Money: " + currencySymbol + player.getMoney() + "<br>Properties: " +  player.getPropertyString() + "</html>");
        }
    }

    /**
     * Removes a player's info from the SidePanel.
     * @param player The player whose label is being removed
     */
    public void removePlayerInfo(Player player) {
        JLabel playerLabel = this.playerInfoMap.remove(player.getIdentifier());
        if (playerLabel == null) {
            throw new IllegalArgumentException("The Player object that was passed does not correspond with a player info label!");
        }
        else {
            this.remove(playerLabel);
            this.revalidate();
            for (JLabel label : this.playerInfoMap.values()) {
                label.setPreferredSize(new Dimension(PANEL_WIDTH - 10, (INFO_AREA_HEIGHT / this.playerInfoMap.size())));
            }
        }
    }

    /**
     * Adds the buttons to the JPanel.
     */
    private void addButtons(MonopolyModel model) {
        Font buttonFont = new Font("arial",Font.BOLD, 25);
        Dimension buttonSize = new Dimension(100, 40);
        controller = new MonopolyController(model);

        JButton roll = new JButton(MonopolyView.ROLL);
        roll.setFont(buttonFont);
        roll.setFocusPainted(false);
        roll.setPreferredSize(buttonSize);
        roll.addActionListener(controller);

        JButton pass = new JButton(MonopolyView.PASS);
        pass.setFont(buttonFont);
        pass.setFocusPainted(false);
        pass.setPreferredSize(buttonSize);
        pass.setEnabled(false);
        pass.addActionListener(controller);

        JButton buy = new JButton(MonopolyView.BUY);
        buy.setFont(buttonFont);
        buy.setFocusPainted(false);
        buy.setPreferredSize(buttonSize);
        buy.addActionListener(controller);

        this.add(roll);
        this.add(pass);
        this.add(buy);
    }

    /**
     * Gets the button with the corresponding text
     * @param buttonText The text of the button
     * @return The corresponding button
     */
    private JButton getButton(String buttonText) {
        for (Component c : this.getComponents()) {
            if (c instanceof JButton && ((JButton) c).getText().equals(buttonText)) {
                return (JButton) c;
            }
        }
        return null;
    }

    /**
     * Enables one of the SidePanel's buttons.
     * @param buttonText The text of the button to enable/disable
     */
    public void enableButton(String buttonText, boolean enabled) {
        JButton button = this.getButton(buttonText);
        button.setEnabled(enabled);
    }

    /**
     * Clicks one of the SidePanel's buttons.
     * @param buttonText The text of the button to click
     */
    public void clickButton(String buttonText) {
        JButton button = this.getButton(buttonText);
        button.doClick();
    }

}
