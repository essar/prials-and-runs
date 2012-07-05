package uk.co.essarsoftware.par.engine.events;

import uk.co.essarsoftware.par.engine.Player;
import uk.co.essarsoftware.par.engine.Round;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 06/06/12
 * Time: 16:05
 * To change this template use File | Settings | File Templates.
 */
public class RoundEndedEvent extends AbstractGameEvent
{
    private Round currentRound;

    public RoundEndedEvent(Player player, Round currentRound) {
        super(player);
        this.currentRound = currentRound;
    }

    public Round getCurrentRound() {
        return currentRound;
    }
}
