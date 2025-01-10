import java.io.Serializable;
import java.util.Random;

/**
 * Dice object for monopoly.
 *
 * @author George Pantazopoulos
 */
public class Dice implements Serializable {
    private final int NUM_DICE;


    /**
     * Constructor for Dice.
     *
     * @param numDice int number of dice
     */
    public Dice(int numDice) {
        if (numDice <= 0) {
            throw new IllegalArgumentException("Number of dice must be > 0");
        }
        else {
            this.NUM_DICE = numDice;
        }
    }


    /**
     * Rolls all of the dice.
     *
     * @return int[] the roll of each dice
     */
    public int[] rollDice() {
        int[] dice = new int[NUM_DICE];
        Random r = new Random();
        for (int i = 0; i<this.NUM_DICE; i++) {
            int roll = r.nextInt(6) + 1;
            dice[i] = roll;
        }
        return dice;
    }
}
