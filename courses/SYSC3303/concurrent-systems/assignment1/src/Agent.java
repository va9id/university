import static java.lang.Thread.sleep;

/**
 * Agent Thread Class.
 */
public class Agent implements Runnable{

    private int amountOfSandwiches;
    private Table table;

    /**
     * Constructor for Agent Thread.
     *
     * @param table Table the Agent Thread places ingredients on.
     */
    public Agent (Table table) {
        this.amountOfSandwiches = 0;
        this.table = table;
    }

    /**
     * Agent Thread adds two random ingredients to the Table and increments the
     * number of sandwiches that were made.
     */
    @Override
    public void run() {
        while (this.amountOfSandwiches < 20) {
            this.table.addTwoRandomIngredientsToTable();
            this.amountOfSandwiches += 1;
            System.out.println("Sandwich " + amountOfSandwiches + " was eaten");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
    }


}
