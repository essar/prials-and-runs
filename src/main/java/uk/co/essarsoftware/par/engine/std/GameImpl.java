package uk.co.essarsoftware.par.engine.std;

import org.apache.log4j.Logger;
import uk.co.essarsoftware.par.engine.*;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 20:17
 * To change this template use File | Settings | File Templates.
 */
class GameImpl implements Game
{
    private static Logger log = Logger.getLogger(Game.class);
    
    private int turn;
    private PlayerImpl currentPlayer, dealer;
    private Round currentRound;

    private final GamePlayerList players;
    private final TableImpl table;

    public GameImpl() {
        players = new GamePlayerList();
        currentRound = Round.START;
        log.debug(String.format("Current round set to %s", currentRound));

        table = new TableImpl();
        log.debug(String.format("Table created"));
    }
    
    private PlayerImpl getNextPlayer(PlayerImpl pl) {
        return players.nextPlayer(pl);
    }
    
    private Round getNextRound() {
        switch(currentRound) {
            case START: return Round.PP;
            case PP: return Round.PR;
            case PR: return Round.RR;
            case RR: return Round.PPR;
            case PPR: return Round.PRR;
            case PRR: return Round.PPP;
            case PPP: return Round.RRR;
            case RRR: return Round.END;
            case END: return Round.END;
            default: throw new IllegalStateException("Unable to determine next round");
        }
    }

    PlayerImpl createPlayer(String playerName) {
        PlayerImpl player = new PlayerImpl(playerName);
        players.add(player);
        table.createSeat(player);
        log.debug(String.format("Player %s created", player));

        return player;
    }
    
    PlayerImpl lookupPlayer(Player player) {
        return players.lookupPlayer(player);
    }
    
    void removePlayer(PlayerImpl player) {
        if(currentRound != Round.START) {
            log.warn(String.format("Attempting to remove player whilst game is in round %s", currentRound));
        } else {
            players.remove(player);
            table.removeSeat(player);
            log.debug(String.format("Player %s removed", player));
        }
    }

    /**
     * Checks whether the supplied player is the current player.
     * @param player the <tt>PlayerImpl</tt> to check.
     * @return <tt>true</tt> if <tt>player</tt> is the current player, false otherwise.
     */
    boolean isCurrentPlayer(PlayerImpl player) {
        return (player != null && player.equals(currentPlayer));
    }

    /**
     * Move to the next player.  No action is taken if game is not yet in progress.
     * @return the new current player.
     */
    PlayerImpl nextPlayer() {
        if(currentPlayer == null) {
            log.warn(String.format("Attempting to move to next player before game initialised"));
        } else if(currentPlayer.getPlayerState() != PlayerState.END_TURN) {
            log.warn(String.format("Attempting to move to next player when %s is in %s state", currentPlayer, currentPlayer.getPlayerState()));
        } else {
            // Increment turn counter once dealer has had their turn
            if(currentPlayer.equals(dealer)) {
                turn ++;
                log.debug(String.format("Turn set to %d", turn));
            }

            // Move to next player
            currentPlayer = getNextPlayer(currentPlayer);
            log.debug(String.format("Current player set to %s", currentPlayer));

            // Run assertions
            assert(currentPlayer != null);
            assert(turn > 0);
        }

        return currentPlayer;
    }

    /**
     * Move to the next round.
     * @return the new current round.
     */
    Round nextRound() {
        if(players.size() < 2) {
            log.warn(String.format("Attempting to start game with %d players", players.size()));
        } else {
            if(dealer == null) {
                // Pick a random player to be first dealer
                dealer = players.getRandomPlayer();
            } else {
                // Move dealer marker to next player
                dealer = getNextPlayer(dealer);
            }
            log.debug(String.format("Dealer set to %s", dealer));
    
            // Move to next round
            currentRound = getNextRound();
            log.debug(String.format("Current round set to %s", currentRound));
    
            // Reset turn counter
            turn = 1;
            log.debug(String.format("Turn set to %d", turn));
    
            // Move to player after dealer
            currentPlayer = getNextPlayer(dealer);
            log.debug(String.format("Current player set to %s", currentPlayer));
    
            // Run assertions
            assert(currentRound != Round.START);
            assert(currentPlayer != null);
            assert(dealer != null);
            assert(turn > 0);
        }

        return currentRound;
    }

    public PlayerImpl getCurrentPlayer() {
        return currentPlayer;
    }

    public Round getCurrentRound() {
        return currentRound;
    }
    
    public PlayerImpl getDealer() {
        return dealer;
    }

    public int getTurn() {
        return turn;
    }

    public PlayerImpl[] getPlayers() {
        return players.getPlayers();
    }

    public TableImpl getTable() {
        return table;
    }

    public boolean isBuyAllowed() {
        return !(turn == 1 || currentRound == Round.PP);
    }
}
