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
public class PickupDiscardEvent extends AbstractGameEvent
{
    private Card pickup;

    public PickupDiscardEvent(Player player) {
        this(player, null);
    }

    public PickupDiscardEvent(Player player, Card pickup) {
        super(player);
        this.pickup = pickup;
    }
    
    public Card getPickup() {
        return pickup;
    }
}
