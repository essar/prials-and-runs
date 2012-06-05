package uk.co.essarsoftware.par.engine;

import uk.co.essarsoftware.games.cards.Card;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 12:32
 * To change this template use File | Settings | File Templates.
 */
public interface Table
{
    public Card getDiscard();

    public Play[] getPlays();

    public Play[] getPlays(Player player);
}
