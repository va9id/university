import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * The model part of the MVC; handles the game logic.
 * @author Connor Marcus
 */
public class MonopolyModel implements Serializable {
    private MonopolyBoard board;
    private List<Player> playerList;
    private Dice dice;
    private int turn;
    private List<MonopolyObserver> observers;
    private boolean foundWinner;
    private final int MAX_CONSECUTIVE_DOUBLES = 3;
    static final int MAX_JAIL_TURNS = 3;
    static final int JAIL_PRICE = 50;
    static final int GO_MONEY = 200;

    /**
     * Constructor of the class; initializes the class attributes.
     * @param numPlayers The number of players playing the Monopoly game
     */
    public MonopolyModel(String type, int numPlayers) {
        this.board = new MonopolyBoard();
        this.dice = new Dice(2);
        this.turn = 0;
        this.foundWinner = false;
        this.observers = new ArrayList<>();
        this.playerList = new ArrayList<>();
        this.playerList.add(new Player(String.valueOf(1), String.format("images/player%s.png", 1), false));
        for (int i=1; i<numPlayers; i++) {
            this.playerList.add(new Player(String.valueOf(i+1), String.format("images/player%s.png", i+1), type.equals(MonopolyView.AI_STRING)));
        }
    }

    /**
     * Gets the MonopolyBoard object used in the game.
     * @return The MonopolyBoard object used in the game
     */
    public MonopolyBoard getBoard() {
        return this.board;
    }

    /**
     * Gets the value of the turn variable used to keep
     * of the current turn.
     * @return the current value of the turn variable
     */
    public int getTurn(){
        return this.turn;
    }


    /**
     * Adds a new MonopolyObserver to the list of observers.
     * @param observer The new MonopolyObserver
     */
    public void addMonopolyObserver(MonopolyObserver observer) {
        if (observer != null && !this.observers.contains(observer)) {
            this.observers.add(observer);
        }
        else {
            throw new IllegalArgumentException("MonopolyObserver cannot be null!");
        }
    }

    /**
     * Takes the turn of the turn player.
     */
    public void takeTurn() {
        Player turnPlayer = this.playerList.get(this.turn);
        boolean rollAgain = !turnPlayer.isJailed(); //only roll again with doubles if not previously in jail
        jailCheck(turnPlayer);
        int[] roll = dice.rollDice();
        boolean isDouble = roll[0] == roll[1];

        if (isDouble) {
            turnPlayer.incrementNumConsecutiveDoubles();
            turnPlayer.setJailed(false);
            turnPlayer.resetTimeInJail();
        }
        else turnPlayer.resetNumConsecutiveDoubles();

        if (turnPlayer.getNumConsecutiveDoubles() == MAX_CONSECUTIVE_DOUBLES) {
            turnPlayer.setJailed(true);
            turnPlayer.setPosition(board.getJailIndex());
            turnPlayer.resetNumConsecutiveDoubles();
            for (MonopolyObserver o : this.observers) {
                o.handleThreeDoubles(turnPlayer, roll);
            }

        }

        else if (turnPlayer.isJailed()) {
            for (MonopolyObserver o : this.observers) {
                o.handleStuckInJail(turnPlayer, roll);
            }
        }
        else {
            int rollSum = IntStream.of(roll).sum();
            if ((turnPlayer.getPosition() + rollSum) > (this.board.getNumProperties()-1)) {
                turnPlayer.setPassingGo(true);
                turnPlayer.addMoney(GO_MONEY);
                for (MonopolyObserver o : this.observers) {
                    o.handlePlayerUpdate(this.playerList);
                }
            }
            int newPosition = (turnPlayer.getPosition() + rollSum) % this.board.getNumProperties();
            turnPlayer.setPosition(newPosition);
            Property propertyLandedOn = this.board.getProperty(newPosition);
            for (MonopolyObserver o : this.observers) {
                o.handleTakeTurn(turnPlayer, roll, propertyLandedOn);
                o.handlePlayerUpdate(this.playerList);
            }
            turnPlayer.setPassingGo(false);
            if (turnPlayer.getIsBankrupt()) bankrupt(turnPlayer); // Checks if player is bankrupt after paying rent
            else if (isDouble && !turnPlayer.isJailed() && rollAgain) {
                for (MonopolyObserver o : this.observers) {
                    o.handleRolledDoubles(turnPlayer);
                }
            }
            else if (isDouble) turnPlayer.resetNumConsecutiveDoubles(); //reset counter while in jail
        }
    }

    /**
     * Helper method to check if player is still in jail at the start of their turn
     * @param turnPlayer The player whose turn it is
     */
    private void jailCheck(Player turnPlayer) {
        if (turnPlayer.isJailed()) {
            turnPlayer.incrementTimeInJail();
            if (turnPlayer.getTimeInJail() == MAX_JAIL_TURNS) {
                turnPlayer.payMoney(JAIL_PRICE);
                turnPlayer.setJailed(false);
                turnPlayer.resetTimeInJail();
                for (MonopolyObserver o : this.observers) {
                    o.handleThreeTurnsInJail(turnPlayer);
                    o.handlePlayerUpdate(this.playerList);
                }
                if (turnPlayer.getMoney() < 0) {
                    turnPlayer.setBankrupt(true);
                    bankrupt(turnPlayer);
                }
            }
            else {
                for (MonopolyObserver o : this.observers) {
                    o.handleJailChoice(turnPlayer);
                    o.handlePlayerUpdate(this.playerList);
                }
                if (!turnPlayer.isJailed())  {
                    turnPlayer.payMoney(JAIL_PRICE);
                    turnPlayer.resetTimeInJail();
                }
            }
        }
    }

    /**
     * Passes Player's turn.
     */
    public void passTurn() {
        Player turnPlayer = this.playerList.get(this.turn);
        this.turn = (this.turn + 1) % this.playerList.size();
        for (MonopolyObserver o : this.observers) {
            o.handlePassTurn(turnPlayer);
        }
        Player nextPlayer = this.playerList.get(this.turn);
        if (nextPlayer.getIsAI()) {
            for (MonopolyObserver o : this.observers) {
                o.handleAITurn(nextPlayer);
            }
        }
    }

    /**
     * Removes bankrupted Player from game and checks if there is a winner (One player remaining).
     * @param p Player that has gone Bankrupt
     */
    public void bankrupt(Player p) {
        for (MonopolyObserver o : this.observers) {
            o.handleBankrupt(p);
        }
        for (OwnableProperty property : p.getProperties()) {
            property.removeOwner();
            if (property instanceof PropertyStreet) ((PropertyStreet) property).setNumHouses(0);
        }
        this.playerList.remove(p);
        if (this.turn==this.playerList.size()) turn--; //if last player in list went bankrupt

        if (this.playerList.size() == 1) {
            Player winner = this.playerList.get(0);
            this.foundWinner = true;
            for (MonopolyObserver o : this.observers) {
                o.handleWinner(winner);
            }
        }
        else if (this.playerList.get(turn).getIsAI()) {
            for (MonopolyObserver o : this.observers) {
                o.handleAITurn(this.playerList.get(turn));
            }
        }
    }

    /**
     * Gets whether a winner has been found in the game
     * @return true if a winner has been found, and false otherwise
     */
    public boolean getFoundWinner() {
        return this.foundWinner;
    }

    /**
     * Getter for the list of players.
     * @return The list of Player objects playing the game
     */
    public List<Player> getPlayerList() {
        return this.playerList;
    }

    /**
     * Buys a house/hotel on a street property
     * @param property The property to buy a house/hotel on
     */
    public void buyHouse(PropertyStreet property) {
        Player turnPlayer = this.playerList.get(turn);
        boolean buySuccessful = false;
        if (turnPlayer.getMoney() >= property.getHouseCost()) {
            turnPlayer.payMoney(property.getHouseCost());
            if (property.getNumHouses() < 5) property.setNumHouses(property.getNumHouses() + 1);
            else throw new IllegalArgumentException("Cannot buy a house/hotel on a property that already has a hotel!");
            buySuccessful = true;
        }
        for (MonopolyObserver o : this.observers) {
            o.handleHouseBought(property, turnPlayer, buySuccessful);
            o.handlePlayerUpdate(this.playerList);
        }
    }
}
