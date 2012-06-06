package uk.co.essarsoftware.par.engine.events;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.engine.Play;
import uk.co.essarsoftware.par.engine.Player;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 06/06/12
 * Time: 16:02
 * To change this template use File | Settings | File Templates.
 */
public class PegCardEvent extends AbstractGameEvent
{
    private Card card;
    private Play play;

    public PegCardEvent(Player player, Play play, Card card) {
        super(player);
        this.play = play;
        this.card = card;
    }
    
    public Card getCard() {
        return card;
    }
    
    public Play getPlay() {
        return play;
    }
}
