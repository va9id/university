import java.util.List;

/**
 * Interface that all the observers of the MonopolyModel must implement.
 */
public interface MonopolyObserver {
    void handleTakeTurn(Player player, int[] roll, Property propertyLandedOn);
    void handlePassTurn(Player player);
    void handleBankrupt(Player player);
    void handleWinner(Player player);
    void handlePlayerUpdate(List<Player> playerList);
    void handleJailChoice(Player player);
    void handleThreeTurnsInJail(Player player);
    void handleStuckInJail(Player player, int[] roll);
    void handleRolledDoubles(Player player);
    void handleThreeDoubles(Player player, int[] roll);
    void handleAITurn(Player player);
    void handleHouseBought(PropertyStreet property, Player turnPlayer, boolean buySuccessful);
}
