import javax.swing.*;

/**
 * Class that models what happens when you land on the Go To Jail space
 * @author Connor Marcus
 */
public class PropertyGoToJail extends Property {
    /**
     * Constructor of the class
     * @param name The name of this property
     */
    public PropertyGoToJail(String name) {
        super(name);
    }

    /**
     * Handles what happens when you land on the property
     * @param landedPlayer the Player object that landed on the property
     * @return A string describing what happened when the player landed on the property
     */
    @Override
    public String landed(Player landedPlayer) {
        landedPlayer.setPosition(10);
        landedPlayer.setJailed(true);
        if (!landedPlayer.getIsAI()) JOptionPane.showMessageDialog(null, "You are now in Jail!");
        return "Player " + landedPlayer.getIdentifier() + " is now in Jail!";
    }
}
