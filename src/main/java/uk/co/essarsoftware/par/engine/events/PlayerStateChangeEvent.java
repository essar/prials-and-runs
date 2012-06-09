package uk.co.essarsoftware.par.engine.events;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.engine.Player;
import uk.co.essarsoftware.par.engine.PlayerState;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 06/06/12
 * Time: 16:05
 * To change this template use File | Settings | File Templates.
 */
public class PlayerStateChangeEvent extends AbstractGameEvent
{
    private PlayerState newState, oldState;

    public PlayerStateChangeEvent(Player player, PlayerState oldState, PlayerState newState) {
        super(player);
        this.oldState = oldState;
        this.newState = newState;
    }

    public PlayerState getNewState() {
        return newState;
    }

    public PlayerState getOldState() {
        return oldState;
    }
}
