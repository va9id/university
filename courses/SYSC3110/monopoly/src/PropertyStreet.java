/**
 * Normal Properties (houses) class
 *
 * @author Vahid
 * 
 */
public class PropertyStreet extends OwnableProperty {
    private final String COLOUR;
    private final double RENT_RATE = 0.1;
    private final int HOUSE_COST;
    private int numHouses;

    /**
     * Constructor for PropertyStreet object.
     *
     * @param colour String colour of PropertyStreet
     * @param name   String name of PropertyStreet
     * @param price  int price of PropertyStreet
     */
    public PropertyStreet(String colour, String name, int price, int houseCost) {
        super(name, price);
        this.COLOUR = colour;
        this.HOUSE_COST = houseCost;
        this.numHouses = 0;
    }

    /**
     * Calculates the cost of landing on this property
     * 
     * @return The price of the property times the rent rate.
     */
    @Override
    public int calculateCost() {
        return (int) (getPrice() * RENT_RATE * (Math.pow(3, numHouses)));
    }

    /**
     * Gets PropertyStreets colour.
     *
     * @return String colour of property
     */
    public String getColour() {
        return this.COLOUR;
    }

    /**
     * Gets the cost of building a house on this property
     * 
     * @return An integer corresponding to the house price
     */
    public int getHouseCost() {
        return this.HOUSE_COST;
    }

    /**
     * Gets the number of houses currently on this property
     * 
     * @return The number of houses currently on this property
     */
    public int getNumHouses() {
        return this.numHouses;
    }

    /**
     * Sets the number of houses currently on this property
     * 
     * @param numHouses The number of houses to set on this property
     */
    public void setNumHouses(int numHouses) {
        this.numHouses = numHouses;
    }

    /**
     * String representation of PropertyStreet object.
     *
     * @return String representation of PropertyStreet.
     */
    @Override
    public String toString() {
        return String.format("%s (%s)", super.getName(), this.COLOUR);
    }
}
