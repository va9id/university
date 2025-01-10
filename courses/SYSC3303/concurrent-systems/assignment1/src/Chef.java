import java.util.ArrayList;
import static java.lang.Thread.sleep;

/**
 * Chef Thread Class.
 */
public class Chef implements Runnable {

    private String ingredient;
    private Table table;
    //Other ingredients the Thread needs to make a sandwich.
    private ArrayList<String> otherIngredients;

    /**
     * Constructor for Chef Thread.
     *
     * @param ingredient    ingredient the Thread represents
     * @param table Table the Thread gets its remaining ingredients from
     */
    public Chef (String ingredient, Table table){
        this.ingredient = ingredient;
        this.table = table;
        this.otherIngredients = new ArrayList<>();
        for (String s: table.getALL_INGREDIENTS()) {
            if (!this.ingredient.equals(s)) {
                otherIngredients.add(s);
            }
        }
    }

    /**
     * Chef Thread gets remaining ingredients needed to make a sandwich and eats them.
     */
    @Override
    public void run() {
        while(true) {
            this.table.getOtherIngredients(this.otherIngredients);
            System.out.println(this.ingredient + " made and ate the sandwich");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
