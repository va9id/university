/**
 * Class that corresponds to the Utility properties
 * @author Connor Marcus
 */
public class PropertyUtility extends OwnableProperty {
    private int diceRoll;

    /**
     * Constructor for this class
     * @param name The name of the property
     */
    public PropertyUtility(String name) {
        super(name, 150);
    }

    /**
     * Calculates the cost of landing on this property
     * @return The dice roll times 10 if two utilities are owned, and the dice roll times 4 otherwise
     */
    @Override
    public int calculateCost() {
        if (diceRoll == 0) {
            throw new RuntimeException("Dice roll 0, must call setDiceRoll() before calling this method!");
        }
        return getOwner().getNumUtilities()==2 ? (10*diceRoll) : (4*diceRoll);
    }

    /**
     * Sets the dice roll when a player lands on this property
     * @param diceRoll The dice roll when a player lands on this property
     */
    public void setDiceRoll(int diceRoll) {
        this.diceRoll = diceRoll;
    }
}
