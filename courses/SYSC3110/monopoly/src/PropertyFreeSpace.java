import javax.swing.*;

/**
 * Free space/Starting Property Object.
 *
 * @author Vahid
 */
public class PropertyFreeSpace extends Property {

    /**
     * Constructor for PropertyFreeSpace object.
     *
     * @param name String name for PropertyFreeSpace object.
     */
    public PropertyFreeSpace(String name) {
        super(name);
    }

    /**
     * Outputs free space message.
     *
     * @param landedPlayer the Player object that landed on the property.
     * @return A string describing what happened when the player landed on the
     *         property
     */
    @Override
    public String landed(Player landedPlayer) {
        if (!landedPlayer.getIsAI())
            JOptionPane.showMessageDialog(null, "This is a free space!");
        return "";
    }
}
