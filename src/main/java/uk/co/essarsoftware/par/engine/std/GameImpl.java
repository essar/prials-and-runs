package uk.co.essarsoftware.par.engine.std;

import uk.co.essarsoftware.par.engine.Game;
import uk.co.essarsoftware.par.engine.Player;
import uk.co.essarsoftware.par.engine.Round;
import uk.co.essarsoftware.par.engine.Table;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 20:17
 * To change this template use File | Settings | File Templates.
 */
class GameImpl implements Game
{
    private int turn;
    private PlayerImpl currentPlayer, dealer;
    private PlayerImpl[] players;
    private Round currentRound;
    private TableImpl table;

    boolean isCurrentPlayer(PlayerImpl player) {
        return (player.equals(currentPlayer));
    }

    boolean isBuyAllowed() {
        if(turn == 1) {
            return false;
        }
        if(currentRound == Round.PP) {
            return false;
        }
        return true;
    }


    public PlayerImpl getCurrentPlayer() {
        return currentPlayer;
    }

    public Round getCurrentRound() {
        return currentRound;
    }

    public int getTurn() {
        return turn;
    }

    public PlayerImpl[] getPlayers() {
        return players;
    }

    public TableImpl getTable() {
        return table;
    }
}
