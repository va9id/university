import java.util.ArrayList;
import java.util.Random;

/**
 * Table Class.
 */
public class Table {

    //Constant for all ingredients of a sandwich.
    private final String[] ALL_INGREDIENTS = new String[] {"Peanut Butter", "Jam", "Bread"};
    private boolean isTableFull;
    private final ArrayList<String> ingredientsOnTable;

    /**
     * Constructor for Table.
     */
    public Table(){
        this.isTableFull = false;
        this.ingredientsOnTable = new ArrayList<>();
    }

    /**
     * Get a list of strings of all ingredients needed for a sandwich.
     * @return ALL_INGREDIENTS
     */
    public String[] getALL_INGREDIENTS() {
        return ALL_INGREDIENTS;
    }

    /**
     * Adds two random ingredients to the table and notifies all Chef Threads
     * that a sandwich can be made.
     */
    public synchronized void addTwoRandomIngredientsToTable() {
        while(this.isTableFull){
            try {
                wait();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        //Select two random ingredients
        Random random = new Random();
        int indexNotToPick = random.nextInt(3);
        for( int i = 0; i < 3; i++){
            if (i != indexNotToPick) {
                ingredientsOnTable.add(ALL_INGREDIENTS[i]);
            }
        }
        isTableFull = true;
        notifyAll();
    }

    /**
     * Removes existing ingredients from the table and gets the other ingredients
     * the Chef Thread needs to make a sandwich.
     *
     * @param remainingIngredients  Two remaining ingredients needed to make a sandwich
     */
    public synchronized void getOtherIngredients(ArrayList<String> remainingIngredients) {
        while(!this.isTableFull || !ingredientsOnTable.containsAll(remainingIngredients)){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ingredientsOnTable.removeAll(remainingIngredients);
        isTableFull = false;
        notifyAll();
    }
}
