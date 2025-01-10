import javax.swing.*;

/**
 * Abstract class that represents properties that can be bought
 * @author Connor Marcus
 */
public abstract class OwnableProperty extends Property {
    private final int PRICE;
    private Player owner;
    private String currencySymbol;

    abstract int calculateCost();

    /**
     * Constructor for this class
     * @param name The name of the property
     * @param price The price of buying the property
     */
    public OwnableProperty(String name, int price) {
        super(name);
        this.currencySymbol = "$"; //default currency symbol
        this.PRICE = price;
    }

    /**
     * Gets the price of the property
     * @return The price of the property
     */
    public int getPrice() {
        return this.PRICE;
    }

    /**
     * Gets the owner of this property
     * @return The Player object that owns this property, or null if there is no owner
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Removes the owner from this property
     */
    public void removeOwner() {
        owner = null;
    }

    /**
     * Sets the currency symbol for this property
     * @param currencySymbol The currency symbol of the property
     */
    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    /**
     * Handles player that landed on the property (Player can buy if available or must pay money to owner).
     *
     * @param landedPlayer the Player object that landed on the property
     * @return A string describing what happened when the player landed on the property
     */
    @Override
    public String landed(Player landedPlayer) {
        if(owner != null && landedPlayer != owner) {
            int cost = calculateCost();
            cost = landedPlayer.payMoney(cost);
            owner.addMoney(cost);
            if (!landedPlayer.getIsAI()) {
                JOptionPane.showMessageDialog( null,"Player " + owner.getIdentifier() + " owns this property you must pay them " + currencySymbol + cost);
            }
            return "Player " + landedPlayer.getIdentifier() + " payed " + currencySymbol + cost + " to Player " + owner.getIdentifier() + ".";
        }
        else if (owner != null) { //landed player owns the property
            if (!landedPlayer.getIsAI()) JOptionPane.showMessageDialog(null, "You already own this property!");
            return "";
        }
        else {
            return buyHandler(landedPlayer);
        }
    }


    /**
     * Offers the Player to buy the property.
     *
     * @param landedPlayer Player object that landed on the property.
     * @return a string indicating whether the player bought the property or not.
     */
    private String buyHandler(Player landedPlayer) {
        String boughtString = "";
        if (landedPlayer.getMoney() >= this.PRICE) {
            String[] options = {"no", "yes"};
            if (!landedPlayer.getIsAI()) {
                int choice = JOptionPane.showOptionDialog(null, "Would you like to buy this property for " + currencySymbol + this.PRICE + "?",
                        "Buy Property",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);

                if (choice == 1) {
                    landedPlayer.purchaseProperty(this);
                    owner = landedPlayer;
                    boughtString = "Player " + landedPlayer.getIdentifier() + " has bought " + this.getName() + ".";
                }
            }
            else {
                landedPlayer.purchaseProperty(this);
                owner = landedPlayer;
                boughtString = "Player " + landedPlayer.getIdentifier() + " has bought " + this.getName() + ".";
            }
        }
        else {
            if (!landedPlayer.getIsAI()) {
                JOptionPane.showMessageDialog(null, "You do not have enough money to buy this property!");
            }
        }
        return boughtString;
    }
}
