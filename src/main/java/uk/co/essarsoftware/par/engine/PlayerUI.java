package uk.co.essarsoftware.par.engine;

import uk.co.essarsoftware.games.cards.Card;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 06/06/12
 * Time: 13:34
 * To change this template use File | Settings | File Templates.
 */
public interface PlayerUI
{

    public void buyApproved(Player player, Player buyer, Card card);
    
    public void buyRejected(Player player, Player buyer, Card card);

    public void buyRequest(Player player, Player buyer, Card card);

    public void cardDiscarded(Player player, Card card);

    public void cardPegged(Player player, Play play, Card card);

    public void cardsPlayed(Player player, Play play);
    
    public void discardPickup(Player player, Card card);
    
    public void drawPickup(Player player);


    public void playerStateChange(PlayerState oldState, PlayerState newState);

    public void playerOut(Player player);

    public void roundEnded(Round round);

    public void roundStarted(Round round);
}
