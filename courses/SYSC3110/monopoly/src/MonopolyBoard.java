import java.io.Serializable;
import java.util.*;

/**
 * Class to represent a class Monopoly Board with all the colour groups.
 *
 * @author Connor Marcus
 * @author George Pantazopoulos
 */
public class MonopolyBoard implements Serializable {
    private List<Property> properties;
    private int jailIndex;

    /**
     * Enum for each different PropertyGroup.
     */
    private enum PropertyGroups {
        PURPLE_PROPERTIES(Arrays.asList(new PropertyStreet("purple", "Mediterranean Avenue", 60, 50), new PropertyStreet("purple", "Baltic Avenue", 60, 50))),
        LIGHT_BLUE_PROPERTIES(Arrays.asList(new PropertyStreet("light blue", "Oriental Avenue", 100, 50), new PropertyStreet("light blue", "Vermont Avenue", 100, 50), new PropertyStreet("light blue", "Connecticut Avenue", 120, 50))),
        PINK_PROPERTIES(Arrays.asList(new PropertyStreet("pink", "St. Charles Place", 140, 100), new PropertyStreet("pink", "States Avenue", 140, 100), new PropertyStreet("pink", "Virginia Avenue", 160, 100))),
        ORANGE_PROPERTIES(Arrays.asList(new PropertyStreet("orange", "St. James Place", 180, 100), new PropertyStreet("orange", "Tennessee Avenue", 180, 100), new PropertyStreet("orange", "New York Avenue", 200, 100))),
        RED_PROPERTIES(Arrays.asList(new PropertyStreet("red", "Kentucky Avenue", 220, 150), new PropertyStreet("red", "Indiana Avenue", 220, 150), new PropertyStreet("red", "Illinois Avenue", 240, 150))),
        YELLOW_PROPERTIES(Arrays.asList(new PropertyStreet("yellow", "Atlantic Avenue", 260, 150), new PropertyStreet("yellow", "Ventnor Avenue", 260, 150), new PropertyStreet("yellow", "Marvin Gardens", 280, 150))),
        GREEN_PROPERTIES(Arrays.asList(new PropertyStreet("green", "Pacific Avenue", 300, 200), new PropertyStreet("green", "North Carolina Avenue", 300, 200), new PropertyStreet("green", "Pennsylvania Avenue", 320, 200))),
        DARK_BLUE_PROPERTIES(Arrays.asList(new PropertyStreet("dark blue", "Park Place", 350, 200), new PropertyStreet("dark blue", "Boardwalk", 400, 200)));

        private List<PropertyStreet> propertySet;


        /**
         * Constructor for PropertyGroups.
         *
         * @param propertySet
         */
        PropertyGroups(List<PropertyStreet> propertySet) {
            this.propertySet = propertySet;
        }
    }


    /**
     * Constructor for MonopolyBoard.
     */
    public MonopolyBoard() {
        this.properties = new ArrayList<>();
        addProperties();
    }

    /**
     * Helper method to add the properties in the correct order to the board; avoids too much code in the constructor
     */
    private void addProperties() {
        this.properties.add(new PropertyFreeSpace("GO"));
        this.properties.add(PropertyGroups.PURPLE_PROPERTIES.propertySet.get(0)); // Mediterranean Avenue
        this.properties.add(new PropertyFreeSpace("Free Space"));
        this.properties.add(PropertyGroups.PURPLE_PROPERTIES.propertySet.get(1)); // Baltic Avenue
        this.properties.add(new PropertyTax("Income Tax", 200));
        this.properties.add(new PropertyRailroad("Reading Railroad"));
        this.properties.add(PropertyGroups.LIGHT_BLUE_PROPERTIES.propertySet.get(0)); //Oriental Avenue
        this.properties.add(new PropertyFreeSpace("Free Space"));
        this.properties.addAll(PropertyGroups.LIGHT_BLUE_PROPERTIES.propertySet.subList(1, 3));
        this.properties.add(new PropertyFreeSpace("Jail"));
        this.properties.add(PropertyGroups.PINK_PROPERTIES.propertySet.get(0)); //St. Charles Place
        this.properties.add(new PropertyUtility("Electric Company"));
        this.properties.addAll(PropertyGroups.PINK_PROPERTIES.propertySet.subList(1,3));
        this.properties.add(new PropertyRailroad("Pennsylvania Railroad"));
        this.properties.add(PropertyGroups.ORANGE_PROPERTIES.propertySet.get(0)); //St. James Place
        this.properties.add(new PropertyFreeSpace("Free Space"));
        this.properties.addAll(PropertyGroups.ORANGE_PROPERTIES.propertySet.subList(1,3));
        this.properties.add(new PropertyFreeSpace("Free Parking"));
        this.properties.add(PropertyGroups.RED_PROPERTIES.propertySet.get(0)); //Kentucky Avenue
        this.properties.add(new PropertyFreeSpace("Free Space"));
        this.properties.addAll(PropertyGroups.RED_PROPERTIES.propertySet.subList(1,3));
        this.properties.add(new PropertyRailroad("B&O Railroad"));
        this.properties.addAll(PropertyGroups.YELLOW_PROPERTIES.propertySet.subList(0,2));
        this.properties.add(new PropertyUtility("Water Works"));
        this.properties.add(PropertyGroups.YELLOW_PROPERTIES.propertySet.get(2)); //Marvin Gardens
        this.properties.add(new PropertyGoToJail("Go To Jail"));
        this.properties.addAll(PropertyGroups.GREEN_PROPERTIES.propertySet.subList(0,2));
        this.properties.add(new PropertyFreeSpace("Free Space"));
        this.properties.add(PropertyGroups.GREEN_PROPERTIES.propertySet.get(2)); //Pennsylvania Avenue
        this.properties.add(new PropertyRailroad("Short Line"));
        this.properties.add(new PropertyFreeSpace("Free Space"));
        this.properties.add(PropertyGroups.DARK_BLUE_PROPERTIES.propertySet.get(0)); //Park Place
        this.properties.add(new PropertyTax("Luxury Tax", 100));
        this.properties.add(PropertyGroups.DARK_BLUE_PROPERTIES.propertySet.get(1)); //BoardWalk
        this.jailIndex = 10;
    }


    /**
     * Gets List of PropertyGroups based on colour.
     *
     * @param colour String colour of PropertyGroup
     * @return List<PropertyStreet> list of PropertyStreets in group
     */
    public List<PropertyStreet> getPropertyGroup(String colour) {
        return PropertyGroups.valueOf(colour.replaceAll("\\s+","_").toUpperCase() + "_PROPERTIES").propertySet;
    }


    /**
     * Gets Property at index of List.
     *
     * @param index int index of Property
     * @return Property at index
     */
    public Property getProperty(int index) {
        return this.properties.get(index);
    }

    /**
     * @param name The name of the property to get
     * @return The property object with the given name, or null otherwise
     */
    private Property getProperty(String name) {
        for (Property p : this.properties) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }


    /**
     * Gets the number of Properties.
     *
     * @return int number of Properties
     */
    public int getNumProperties() {
        return this.properties.size();
    }

    /**
     * Gets the index of a given property
     * @param property The property to get the index of
     * @return The index of the property in the monopoly board list
     */
    public int getPropertyIndex(Property property) {
        int count = 0;
        for (Property p : this.properties) {
            if (p.equals(property)) return count;
            count++;
        }
        return -1; //invalid property
    }

    /**
     * @return the index that is the jail space on the Monopoly board
     */
    public int getJailIndex() {
        return this.jailIndex;
    }

    /**
     * Updates the names of the properties on the Monopoly board
     * @param propertyNames A map where the keys are the original property names and the values are the new property names
     * @return A List of the indices of the properties that were changed
     */
    public List<Integer> updatePropertyNames(HashMap<String, String> propertyNames) {
        List<Integer> changed = new ArrayList<>();
        for (String originalName : propertyNames.keySet()) {
            String newName = propertyNames.get(originalName);
            if (newName != null && !newName.equals("")) {
                Property p = getProperty(originalName);
                if (p != null) {
                    p.setName(newName);
                    changed.add(getPropertyIndex(p));
                }
            }
        }
        return changed;
    }

}
