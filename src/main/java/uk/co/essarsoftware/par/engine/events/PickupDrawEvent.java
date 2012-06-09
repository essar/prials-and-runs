package uk.co.essarsoftware.par.engine.events;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.engine.Player;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 06/06/12
 * Time: 16:05
 * To change this template use File | Settings | File Templates.
 */
public class PickupDrawEvent extends AbstractGameEvent
{
    public PickupDrawEvent(Player player) {
        super(player);
    }
}
