package uk.co.essarsoftware.par.client;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.engine.Game;
import uk.co.essarsoftware.par.engine.Play;
import uk.co.essarsoftware.par.engine.Player;

/**
 * Interface defining methods available to game clients.
 * @author Steve Roberts <steve.roberts@essarsoftware.co.uk>
 * @version 1.0 (07-Jun-12)
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
