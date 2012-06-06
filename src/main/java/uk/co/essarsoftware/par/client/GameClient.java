package uk.co.essarsoftware.par.client;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.engine.Game;
import uk.co.essarsoftware.par.engine.Play;
import uk.co.essarsoftware.par.engine.Player;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 06/06/12
 * Time: 13:29
 * To change this template use File | Settings | File Templates.
 */
public interface GameClient extends Game, Player
{
    /* ** GAME METHODS **/
    public Card approveBuy(Player buyer);
    
    public boolean buy(Player player, Card card);

    public void discard(Card card);

    public void pegCard(Play play, Card card);

    public Card pickupDiscard();
    
    public Card pickupDraw();

    public void playCards(Card[] cards);

    public Card rejectBuy(Player buyer);

    public void resetPlays();

    /* ** PLAYER METHODS ** */
    public Card[] getHand();

    public Play[] getPlays();

    public Card getPenaltyCard();

    public Player getPlayer();
}
