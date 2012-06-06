package uk.co.essarsoftware.par.engine.events;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.engine.Player;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 06/06/12
 * Time: 16:02
 * To change this template use File | Settings | File Templates.
 */
public class DiscardEvent extends AbstractGameEvent
{
    private Card discard;
    
    public DiscardEvent(Player player, Card discard) {
        super(player);
        this.discard = discard;
    }
    
    public Card getDiscard() {
        return discard;
    }
}
