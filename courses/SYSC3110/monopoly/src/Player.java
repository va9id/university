import java.io.Serializable;
import java.util.*;

/**
 * The Player object for monopoly
 *
 * @author Noah Hammoud
 */
public class Player implements Serializable {
    private final String IDENTIFIER;
    private int money;
    private int position;
    private Set<OwnableProperty> properties;
    private boolean isBankrupt;
    private boolean isJailed;
    private boolean passedGo;
    private int timeInJail;
    private int numRailroads;
    private int numUtilities;
    private int numConsecutiveDoubles;
    private final boolean isAI;
    private final String PLAYER_IMAGE_FILE;


    /**
     * The constructor for Player.
     *
     * @param identifier String identifier of the Player
     * @param playerImageFile String containing the file path to the player image
     */
    public Player(String identifier, String playerImageFile, boolean isAI) {
        this.IDENTIFIER = identifier;
        this.money = 1500;
        this.position = 0;
        this.numRailroads = 0;
        this.timeInJail = 0;
        this.properties = new HashSet<>();
        this.isBankrupt = false;
        this.isJailed = false;
        this.passedGo = false;
        this.numConsecutiveDoubles = 0;
        this.isAI = isAI;
        this.PLAYER_IMAGE_FILE = playerImageFile;
    }

    /**
     * Gets the file path to the player image.
     * @return the PLAYER_IMAGE_FILE String
     */
    public String getPlayerImageFile() {
        return this.PLAYER_IMAGE_FILE;
    }


    /**
     * Gets the identifier of the Player.
     *
     * @return the String identifier
     */
    public String getIdentifier() {
        return this.IDENTIFIER;
    }


    /**
     * Gets the Players money.
     *
     * @return the int amount
     */
    public int getMoney() {
        return this.money;
    }


    /**
     * Gets the Players current position.
     *
     * @return the int position
     */
    public int getPosition() {
        return this.position;
    }


    /**
     * Gets isBankrupt.
     *
     * @return boolean, isBankrupt value
     */
    public boolean getIsBankrupt() {
        return this.isBankrupt;
    }

    /**
     * Gets isAI
     *
     * @return boolean, isAI flag.
     */
    public boolean getIsAI() {
        return this.isAI;
    }

    /**
     * Sets Players Position.
     *
     * @param pos int position of Player
     */
    public void setPosition(int pos) {
        this.position = pos;
    }

    public void setBankrupt(boolean b) {this.isBankrupt = b;}

    /**
     * Adds money to Player.
     *
     * @param money int amount of money being added
     */
    public void addMoney(int money) {
        this.money += money;
    }


    /**
     * Removes money from Player.
     *
     * @param money int amount of money being removed
     */
    private void removeMoney(int money) {
            this.money -= money;
    }


    /**
     * Checks if player can pay amount of money.
     *
     * @param money int amount of money to pay
     * @return boolean, true if player can pay
     */
    private boolean canPay(int money) {
        return this.money - money >= 0;
    }


    /**
     * purchases property.
     *
     * @param p the Property object that the Player wants to purchase
     */
    public void purchaseProperty(OwnableProperty p) {
        int cost = p.getPrice();
        if (this.canPay(cost)) {
            this.removeMoney(cost);
            this.properties.add(p);
            if (p instanceof PropertyRailroad) numRailroads++;
            else if (p instanceof PropertyUtility) numUtilities++;
        }
    }


    /**
     * Pays rent.
     *
     * @param cost int amount of money to pay
     * @return int amount of money actual payed. (If player does not have enough pay all of money)
     */
    public int payMoney(int cost) {
        if (canPay(cost)) {
            this.removeMoney(cost);
            return cost;
        }
        else {
            int remainingMoney = this.money;
            this.removeMoney(this.money);
            this.isBankrupt = true;
            return remainingMoney;
        }
    }

    /**
     * Gets a set of all the colours that player owns a full group of.
     *
     * @param board the Monopoly board that is being used
     * @return a set of all the player's property groups
     */
    public Set<String> getPropertyGroups(MonopolyBoard board) {
        Set<String> propertyGroups = new HashSet<>();
        for (OwnableProperty p : this.properties) {
            if (p instanceof PropertyStreet && !propertyGroups.contains(((PropertyStreet) p).getColour())) {
                String colour = ((PropertyStreet) p).getColour();
                propertyGroups.add(colour);
                for (PropertyStreet p2 : board.getPropertyGroup(colour)) {
                    if (!this.properties.contains(p2)) {
                        propertyGroups.remove(colour);
                        break;
                    }
                }
            }
        }
        return propertyGroups;
    }


    /**
     * Gets a string of all the properties that the player owns.
     *
     * @return a String containing the players properties separated by a comma
     */
    public String getPropertyString() {
        StringBuilder propertyString = new StringBuilder();
        for (OwnableProperty p : this.properties) {
            propertyString.append(p).append(", ");
        }
        return propertyString.toString().replaceAll(", $", ""); //removes trailing comma
    }

    /**
     * Getter for the properties attribute.
     * @return The set of all properties that the player owns.
     */
    public Set<OwnableProperty> getProperties() {
        return this.properties;
    }

    /**
     * Gets the number of Railroad properties the player owns
     * @return the number of Railroad properties the player owns
     */
    public int getNumRailroads() {
        return numRailroads;
    }

    /**
     * Gets the number of Utility properties the player owns
     * @return the number of Utility properties the player owns
     */
    public int getNumUtilities() {
        return numUtilities;
    }

    /**
     * Gets whether the player is currently in jail or not
     * @return true if the player is in jail, and false otherwise
     */
    public boolean isJailed() {
        return isJailed;
    }

    /**
     * Sets whether the player is in jail
     * @param jailed true if the player is in jail, and false otherwise
     */
    public void setJailed(boolean jailed) {
        this.isJailed = jailed;
    }

    /**
     * Gets the number of turns the players has spent in jail
     * @return The number of turns the players has spent in jail
     */
    public int getTimeInJail() {
        return timeInJail;
    }

    /**
     * Increments the number of turns the player has spent in jail by 1.
     */
    public void incrementTimeInJail() {
        timeInJail++;
    }

    /**
     * Resets the number of turns the player has spent in jail
     */
    public void resetTimeInJail() {
        timeInJail = 0;
    }

    /**
     * Gets whether the player is currently passing go or not
     * @return true if the player is passing go, and false otherwise
     */
    public boolean isPassingGo() {
        return passedGo;
    }

    /**
     * Sets whether the player is passing go
     * @param passingGo true if the player is in jail, and false otherwise
     */
    public void setPassingGo(boolean passingGo) {
        this.passedGo = passingGo;
    }

    /**
     * @return the number of consecutive doubles that the player rolled
     */
    public int getNumConsecutiveDoubles() {
        return this.numConsecutiveDoubles;
    }

    /**
     * Increments the player's double counter
     */
    public void incrementNumConsecutiveDoubles() {
        this.numConsecutiveDoubles++;
    }

    /**
     * Resets the player's double counter to zero
     */
    public void resetNumConsecutiveDoubles() {
        this.numConsecutiveDoubles = 0;
    }

    /**
     * Overrides the equals method from the Object class.
     * @param o The object that is being compared
     * @return true if the object is a player and the identifier of the player is the same, and false otherwise
     */
    @Override
    public boolean equals(Object o) {
        return (o instanceof Player) && (((Player) o).IDENTIFIER.equals(this.IDENTIFIER));
    }
}
