import java.io.Serializable;

/**
 * The Property object for monopoly.
 *
 * @author Vahid
 */
public abstract class Property implements Serializable {
    private String name;

    /**
     * Abstract method to handler player landing on property.
     *
     * @param landedPlayer the Player object that landed on the property
     * @return A string describing what happened when the player landed on the
     *         property
     */
    public abstract String landed(Player landedPlayer);

    /**
     * Constructor for Property.
     *
     * @param name String name of Property
     */
    public Property(String name) {
        this.name = name;
    }

    /**
     * Get's name of Property.
     *
     * @return String name of Property
     */
    public String getName() {
        return name;
    }

    /**
     * Sets a property's name
     * 
     * @param name The name to be set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * String representation of Property object.
     *
     * @return String representation of Property
     */
    @Override
    public String toString() {
        return String.format("%s", this.name);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Property && ((Property) o).name.equals(this.name));
    }

}
