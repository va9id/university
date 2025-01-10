/**
 * Main class that instantiates all threads and runs the program.
 */
public class Main {
    public static void main(String[] args) {
        Table table = new Table();
        Thread agent = new Thread(new Agent(table), "agentThread");
        Thread bread = new Thread(new Chef("Bread", table), "breadThread");
        Thread jam = new Thread(new Chef("Jam", table), "jamThread");
        Thread peanutButter = new Thread(new Chef("Peanut Butter", table), "peanutButterThread");

        agent.start();
        bread.start();
        jam.start();
        peanutButter.start();
    }
}
