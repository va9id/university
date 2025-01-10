import javax.swing.*;

/**
 * Class that corresponds to the Tax property in a Monopoly game (income tax and luxury tax)
 */
public class PropertyTax extends Property {
    private final int TAX_PRICE;
    private String currencySymbol;

    /**
     * Constructor of the class
     * @param name The name of the property
     * @param taxPrice The tax price of landing on the property
     */
    public PropertyTax(String name, int taxPrice) {
        super(name);
        this.currencySymbol = "$"; //default currency symbol
        this.TAX_PRICE = taxPrice;
    }

    /**
     * Sets the currency symbol for this property
     * @param currencySymbol The currency symbol of the property
     */
    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    /**
     * Handles what happens when you land on the property
     * @param landedPlayer the Player object that landed on the property
     * @return A string describing what happened when the player landed on the property
     */
    @Override
    public String landed(Player landedPlayer) {
        int cost = landedPlayer.payMoney(TAX_PRICE);
        if (!landedPlayer.getIsAI()) JOptionPane.showMessageDialog(null, "You must pay " + currencySymbol + cost + " in tax!");
        return "Player " + landedPlayer.getIdentifier() + " payed " + currencySymbol + cost + " in tax!";
    }
}
