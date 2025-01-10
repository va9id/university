import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * The GameLogPanel class used to display text to the users.
 * @author Noah Hammoud
 */
public class GameLogPanel extends JPanel implements Serializable {
    private final int PANEL_WIDTH = 1043;
    private final int PANEL_HEIGHT = 80;
    private JTextArea gameLog;
    private ArrayList<String> listOfStrings;

    /**
     * Constructor for GameLogPanel class.
     */
    public GameLogPanel() {
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setLayout(new GridBagLayout());
        this.setBackground(new Color(211, 236, 211));
        gameLog = new JTextArea(4, 80);
        String welcomeText = "Welcome to Monopoly!  Please roll to start the game";
        listOfStrings = new ArrayList<>();
        listOfStrings.add(welcomeText);
        gameLog.setText(welcomeText);
        gameLog.setOpaque(true);
        gameLog.setBackground(Color.WHITE);
        gameLog.setEditable(false);
        JScrollPane sp = new JScrollPane(gameLog);
        this.add(sp);

    }

    public int getPANEL_WIDTH() {
        return PANEL_WIDTH;
    }

    /**
     * Updates the GameLog Jlabel text.
     *
     * @param text the string that will be displayed
     */
    public void updateGameLog(String text) {
        if (this.listOfStrings.size() == 10) {
            this.listOfStrings.remove(0);
        }
        this.listOfStrings.add(text);
        gameLog.setText(String.join("\n", this.listOfStrings));
    }

}
