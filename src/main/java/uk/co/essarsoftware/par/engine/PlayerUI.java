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

    public void buyApproved(Player player, Player buyer, Card card, boolean thisPlayer);
    
    public void buyRejected(Player player, Player buyer, Card card, boolean thisPlayer);

    public void buyRequest(Player player, Player buyer, Card card, boolean thisPlayer);

    public void cardDiscarded(Player player, Card card, boolean thisPlayer);

    public void cardPegged(Player player, Play play, Card card, boolean thisPlayer);

    public void cardsPlayed(Player player, Play[] play, boolean thisPlayer);
    
    public void discardPickup(Player player, Card card, boolean thisPlayer);
    
    public void drawPickup(Player player, boolean thisPlayer);

    public void playerStateChange(Player player, PlayerState oldState, PlayerState newState, boolean thisPlayer);

    public void playerOut(Player player, boolean thisPlayer);


    public void roundEnded(Round round);

    public void roundStarted(Round round);


    public void handleException(EngineException ee);
}
