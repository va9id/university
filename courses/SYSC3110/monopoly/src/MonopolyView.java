import javax.swing.*;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * MonopolyView class which handles the GUI.
 * @author Connor Marcus
 */
public class MonopolyView extends JFrame implements MonopolyObserver, Serializable {
    private BoardPanel boardPanel;
    private SidePanel sidePanel;
    private GameLogPanel gameLogPanel;
    private MonopolyModel model;
    private HashMap<String, Object> customization;
    private String currencySymbol;
    static final String MULTIPLAYER_STRING = "Multiplayer";
    static final String AI_STRING = "AI";
    static final String NEW_GAME = "New Game";
    static final String IMPORT_GAME = "Import Game";
    static final String ROLL = "Roll";
    static final String PASS = "Pass";
    static final String BUY = "Buy";

    /**
     * Constructor for MonopolyView class.
     * @param title String title of JFrame
     */
    public MonopolyView(String title) {
        super(title);
        this.currencySymbol = "$";
        this.importGamePrompt();
        JPanel mainPanel = new JPanel(); //Container panel with flow layout
        mainPanel.add(boardPanel);
        mainPanel.add(sidePanel);
        this.add(this.gameLogPanel, BorderLayout.NORTH);
        this.add(mainPanel, BorderLayout.CENTER);
        this.addSaveFeature();
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
    }

    /**
     * Prompts the user if they would like to import a saved game
     */
    private void importGamePrompt() {
        String[] options = {NEW_GAME, IMPORT_GAME};
        int choice = JOptionPane.showOptionDialog(null, "Welcome to SYSC 3110 Monopoly",
                "Monopoly",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, 0);
        if (choice == -1) {
            System.exit(0);
        }
        else if (choice == 0) {
            String opponent = this.opponentTypePrompt();
            int numberOfPlayers = this.gamePrompt(opponent);
            this.model = new MonopolyModel(opponent, numberOfPlayers);
            this.model.addMonopolyObserver(this);
            this.boardPanel = new BoardPanel(this.model.getBoard(), this.model.getPlayerList());
            if (this.customizationPrompt()) {
                List<Integer> changed = this.model.getBoard().updatePropertyNames((HashMap) customization.get("propertyNames"));
                if (!customization.get("currency").equals("")) {
                    this.currencySymbol = (String) customization.get("currency");
                }
                for (int i=0; i<this.model.getBoard().getNumProperties(); i++) {
                    Property p = this.model.getBoard().getProperty(i);
                    if (changed.contains(i)) this.boardPanel.updatePropertyName(i);
                    if (p instanceof OwnableProperty) ((OwnableProperty) p).setCurrencySymbol(this.currencySymbol);
                    else if (p instanceof PropertyTax) ((PropertyTax) p).setCurrencySymbol(this.currencySymbol);
                }
            }
            this.sidePanel = new SidePanel(this.model, this.currencySymbol);
            this.gameLogPanel = new GameLogPanel();
        }
        else {
            FileSelector selector = new FileSelector(FileSelector.OPEN, new FileNameExtensionFilter(".ser files","ser"));
            try {
                EventQueue.invokeAndWait(selector);
            }
            catch (InvocationTargetException | InterruptedException e) {
                JOptionPane.showMessageDialog(null, "Could not open file selector!");
            }
            if (selector.fileSelected()) {
                importGame(selector.getAbsoluteFilePath());
            }
            else {
                this.importGamePrompt();
            }
        }
    }

    /**
     * Adds the save game feature. Prompts user to select if they would like to
     * save their game. If so user must enter a filename.
     */
    private void addSaveFeature() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                int answer = JOptionPane.showConfirmDialog(null, "Would you like to save your game?", "Save Game", JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.YES_OPTION) {
                    try {
                        FileSelector selector = new FileSelector(FileSelector.SAVE, new FileNameExtensionFilter(".ser files","ser"));
                        selector.run();
                        if (selector.fileSelected()) {
                            String filename = selector.getAbsoluteFilePath();
                            if (!filename.endsWith(".ser")) filename = filename.concat(".ser");
                            FileOutputStream fileOutputStream = new FileOutputStream(filename);
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                            objectOutputStream.writeObject(model);
                            objectOutputStream.writeObject(boardPanel);
                            objectOutputStream.writeObject(sidePanel);
                            objectOutputStream.writeObject(gameLogPanel);
                            objectOutputStream.writeObject(currencySymbol);
                            objectOutputStream.close();
                            fileOutputStream.close();
                        }
                    }
                    catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "Error saving file!");
                    }
                }
                System.exit(0);
            }
        });
    }

    /**
     * Allows users to import previous games stored in the savedGames directory.
     */
    private void importGame(String fileName) {
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            this.model = (MonopolyModel) objectInputStream.readObject();
            this.boardPanel = (BoardPanel) objectInputStream.readObject();
            this.sidePanel = (SidePanel) objectInputStream.readObject();
            this.gameLogPanel = (GameLogPanel) objectInputStream.readObject();
            this.currencySymbol = (String) objectInputStream.readObject();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid file!");
            this.importGamePrompt();
        }
    }

    /**
     * Option Dialog that Appears at beginning of the game.
     * @return The string opponent type selected by the user.
     */
    private String opponentTypePrompt() {
        String[] options = {MULTIPLAYER_STRING, AI_STRING};
        int choice = JOptionPane.showOptionDialog(null, "Select type of opponents:",
                "Game type",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, 0);
        if (choice == -1) {
            System.exit(0);
        }
        return options[choice];
    }

    /**
     * Option Dialog that Appears if the user selected a new game
     * @param gameType The type of game that is being player
     * @return The integer corresponding to the user's choice
     */
    private int gamePrompt(String gameType) {
        String[] options;
        String message;
        if (gameType.equals(MULTIPLAYER_STRING)) {
            options = new String[]{"4", "3", "2"};
            message = "How many players would like to play:";
        }
        else {
            options = new String[]{"3", "2", "1"};
            message = "How many AI players would you like to play against:";
        }
        int choice = JOptionPane.showOptionDialog(null, message,
                "Choose Players",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, 0);
        if (choice == -1) {
            System.exit(0);
        }
        if (gameType.equals(AI_STRING)) return Integer.parseInt(options[choice])+1;
        return Integer.parseInt(options[choice]);
    }

    /**
     * Displays the customization prompt
     * @return true if customization is selected and false otherwise
     */
    private boolean customizationPrompt() {
        int choice = JOptionPane.showOptionDialog(null, "Would you like to import customization options?",
                "Customization",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"no", "yes"}, 0);
        if (choice == -1) {
            System.exit(0);
        }
        else if (choice == 1) {
            FileSelector selector = new FileSelector(FileSelector.OPEN, new FileNameExtensionFilter(".json files","json"));
            try {
                EventQueue.invokeAndWait(selector);
            }
            catch (InterruptedException | InvocationTargetException e) {
                JOptionPane.showMessageDialog(null, "Could not open file selector!");
            }
            if (selector.fileSelected()) {
                try {
                    InputStream in = new FileInputStream(selector.getAbsoluteFilePath());
                    customization = new ObjectMapper().readValue(in, HashMap.class);
                    if (customization.get("propertyNames") == null || customization.get("currency") == null || !(customization.get("propertyNames") instanceof LinkedHashMap) || !(customization.get("currency") instanceof String)) {
                        throw new RuntimeException("JSON file does not match the expected format");
                    }
                    return true;
                }
                catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Invalid file selected (doesn't match expected JSON format)!");
                    this.customizationPrompt();
                }
            }
            else {
                this.customizationPrompt();
            }
        }
        return false;
    }

    /**
     * Handles what happens when a player takes their turn.
     * @param player The Player who took their turn
     * @param roll The list of the players roll
     * @param propertyLandedOn The property that the player landed on
     */
    @Override
    public void handleTakeTurn(Player player, int[] roll, Property propertyLandedOn) {
        int rollSum = IntStream.of(roll).sum();
        this.boardPanel.updatePlayerLabelPosition(player);
        this.boardPanel.updateDice(roll[0], roll[1]);
        String gameLogString = "Player " + player.getIdentifier() + " has rolled a " + rollSum + ". They are now on " + propertyLandedOn.getName() + ".";
        if (player.isPassingGo()) {
            gameLogString += " Player " + player.getIdentifier() + " has passed GO to collect " + this.currencySymbol + MonopolyModel.GO_MONEY + ". ";
        }

        if (propertyLandedOn instanceof PropertyUtility) ((PropertyUtility) propertyLandedOn).setDiceRoll(rollSum);
        gameLogString += " " + propertyLandedOn.landed(player);
        this.gameLogPanel.updateGameLog(gameLogString);
        if (propertyLandedOn.getName().equals("Go To Jail")) this.boardPanel.updatePlayerLabelPosition(player);

        this.sidePanel.enableButton(PASS, true);
    }

    /**
     * Handles what happens when a player passes their turn.
     * @param player The Player who passed their turn.
     */
    @Override
    public void handlePassTurn(Player player) {
        this.gameLogPanel.updateGameLog("Player " + player.getIdentifier() + " has finished their turn.");
        this.sidePanel.enableButton(ROLL, true);
        this.sidePanel.enableButton(BUY, true);
    }

    /**
     * Handles what happens when a player becomes bankrupt.
     * @param player The Player who has gone bankrupt
     */
    @Override
    public void handleBankrupt(Player player) {
        this.gameLogPanel.updateGameLog("Player " + player.getIdentifier() + " has gone bankrupt! They are out of the game.");
        this.boardPanel.removePlayerLabel(player);
        this.sidePanel.removePlayerInfo(player);
        this.sidePanel.enableButton(ROLL, true);
        this.sidePanel.enableButton(PASS, false);
    }

    /**
     * Handles what happens when a player wins.
     * @param player The Player who has won
     */
    @Override
    public void handleWinner(Player player) {
        JOptionPane.showMessageDialog(null, "Player " + player.getIdentifier() + " has won the game!");
        System.exit(0);
    }

    /**
     * Handles updates related to the player's information
     * @param playerList The list of players in the game
     */
    @Override
    public void handlePlayerUpdate(List<Player> playerList) {
        playerList.forEach((player) -> this.sidePanel.updatePlayerInfo(player));
    }

    /**
     * Handles the start of a jailed player's turn
     * @param player The player who is in jail
     */
    @Override
    public void handleJailChoice(Player player) {
        String jailString = "";
        if (player.getMoney() >= MonopolyModel.JAIL_PRICE) {
            String[] options = {"no", "yes"};
            int choice = -1;
            if (!player.getIsAI()) {
                choice=JOptionPane.showOptionDialog(null, "Would you like to pay " + this.currencySymbol + MonopolyModel.JAIL_PRICE + "to get out of jail this turn?", "Player " + player.getIdentifier(),
                            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
                }
            else if (player.getTimeInJail()==MonopolyModel.MAX_JAIL_TURNS-1) choice=1;

            if (choice == 1) {
                player.setJailed(false);
                jailString = "Player " + player.getIdentifier() + " has spent " + this.currencySymbol + MonopolyModel.JAIL_PRICE +  "to get out of jail!";
            }
            else {
                jailString = "Player " + player.getIdentifier() + " has not paid the fine to get out of jail!";
            }
        }
        else {
            jailString = "Player " + player.getIdentifier() + " cannot currently afford to pay the fine of " + this.currencySymbol + MonopolyModel.JAIL_PRICE + ", they must roll doubles!";
        }

        this.gameLogPanel.updateGameLog(jailString);
    }

    /**
     * Handles what happens when a player spends three turns in jail.
     * @param player The player who is in jail
     */
    @Override
    public void handleThreeTurnsInJail(Player player) {
        this.gameLogPanel.updateGameLog("Player " + player.getIdentifier() + " has spent three turns in jail and paid the " + this.currencySymbol + MonopolyModel.JAIL_PRICE + "fine!");
    }

    /**
     * Handles what happens when a player fails to get out of jail
     * @param player The player that is stuck in jail
     */
    @Override
    public void handleStuckInJail(Player player, int[] roll) {
        this.boardPanel.updateDice(roll[0], roll[1]);
        this.sidePanel.enableButton(PASS, true);
        this.gameLogPanel.updateGameLog("Player " + player.getIdentifier() + " did not roll doubles, they are stuck in jail!");
    }

    /**
     * Handles AI players turn, Rolls and Passes turn.
     * @param player The AI player.
     */
    @Override
    public void handleAITurn(Player player) {
        if (!this.model.getFoundWinner()) {
            this.sidePanel.enableButton(BUY, false); //disable button so cannot be clicked by user
            this.sidePanel.enableButton(ROLL, false);
            Set<String> sets = player.getPropertyGroups(this.model.getBoard());
            String[] propertyGroups = sets.toArray(new String[sets.size()]);
            if (propertyGroups.length > 0 && player.getMoney() > 400) {
                Random r = new Random();
                String colourGroup = propertyGroups[r.nextInt(propertyGroups.length)];
                int colourGroupSize = this.model.getBoard().getPropertyGroup(colourGroup).size();
                PropertyStreet houseProperty = this.model.getBoard().getPropertyGroup(colourGroup).get(r.nextInt(colourGroupSize));
                if (houseProperty.getNumHouses() < 5) this.model.buyHouse(houseProperty);
            }
            Timer rollTimer = new Timer(2000, (e) -> {
                this.sidePanel.enableButton(ROLL, true);
                this.sidePanel.clickButton(ROLL);
                this.sidePanel.enableButton(PASS, false);
                Timer passTimer = new Timer(3000, (e2) -> {
                    if ((!(player.getNumConsecutiveDoubles() > 0) || (player.isJailed())) && !player.getIsBankrupt()) {
                        this.sidePanel.enableButton(PASS, true);
                        this.sidePanel.clickButton(PASS);
                    }
                    else if (!player.getIsBankrupt()) handleAITurn(player); //AI rolled doubles
                });
                passTimer.setRepeats(false);
                passTimer.start();
            });
            rollTimer.setRepeats(false);
            rollTimer.start();
        }
    }

    /**
     * Handles when a player rolls doubles on their turn.
     * @param player the player who rolled doubles.
     */
    @Override
    public void handleRolledDoubles(Player player) {
            this.gameLogPanel.updateGameLog("Player " + player.getIdentifier() + " rolled doubles and gets another turn!");
            this.sidePanel.enableButton(PASS, false);
            if (!(player.getIsAI())) {
                this.sidePanel.enableButton(ROLL, true);
            }
    }

    /**
     * Handles what happens when a player rolls three doubles in a row at the start of their turn
     * @param player The player who rolled three doubles
     * @param roll The player's roll
     */
    @Override
    public void handleThreeDoubles(Player player, int[] roll) {
        this.gameLogPanel.updateGameLog("Player " + player.getIdentifier() + " rolled doubles three times in a row and is going to jail!");
        this.boardPanel.updateDice(roll[0], roll[1]);
        this.boardPanel.updatePlayerLabelPosition(player);
        this.sidePanel.enableButton(PASS, true);
    }

    /**
     * Handles what happens to the GUI when a house/hotel is bought on a property
     * @param property The property where a house/hotel was bought on
     * @param turnPlayer The player whose turn it is
     * @param buySuccessful A boolean representing whether the player was able to successful buy the house/hotel
     */
    @Override
    public void handleHouseBought(PropertyStreet property, Player turnPlayer, boolean buySuccessful) {
        if (buySuccessful) {
            this.boardPanel.addHouse(property);
            String houseOrHotel = property.getNumHouses()==5 ? "hotel" : "house";
            this.gameLogPanel.updateGameLog("Player " + turnPlayer.getIdentifier() + " has purchased a " + houseOrHotel + " on " + property.getName() + ".");
        }
        else if (!turnPlayer.getIsAI()) JOptionPane.showMessageDialog(null, "Player " + turnPlayer.getIdentifier() + " is unable to purchase this house/hotel!");
    }

    /**
     * Main method to run game.
     * @param args String[] input for main method
     */
    public static void main(String[] args) {
        MonopolyView mv = new MonopolyView("Monopoly");
    }
}
