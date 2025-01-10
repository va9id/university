/**
 * Railroad property class
 * @author Connor Marcus
 */
public class PropertyRailroad extends OwnableProperty {
    private final double RENT_RATE = 0.125;

    /**
     * Constructor for the class
     * @param name The name of the property
     */
    public PropertyRailroad(String name) {
        super(name, 200);
    }

    /**
     * Calculates the cost of landing on this property
     * @return The price of this property times the rent rate times a multiple of two (corresponding to how many railroads are owned)
     */
    @Override
    public int calculateCost() {
        return (int)(getPrice() * RENT_RATE * Math.pow(2, getOwner().getNumRailroads()-1));
    }
}

