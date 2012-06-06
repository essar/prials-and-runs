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
public class PlayCardsEvent extends AbstractGameEvent
{
    private Play[] plays;

    public PlayCardsEvent(Player player, Play[] plays) {
        super(player);
        this.plays = plays;
    }
    
    public Play[] getPlays() {
        return plays;
    }
}
