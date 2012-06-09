package uk.co.essarsoftware.par.engine.events;

import uk.co.essarsoftware.par.engine.Player;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 06/06/12
 * Time: 16:02
 * To change this template use File | Settings | File Templates.
 */
abstract class AbstractGameEvent implements GameEvent
{
    protected Player player;
    
    protected AbstractGameEvent(Player player) {
        this.player = player;
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}
