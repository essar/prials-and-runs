package uk.co.essarsoftware.par.engine;

import uk.co.essarsoftware.games.cards.Card;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 06/06/12
 * Time: 15:10
 * To change this template use File | Settings | File Templates.
 */
public class CardNotFoundException extends EngineException
{
    private Card card;
    private Player player;
    
    public CardNotFoundException() {
        super();
    }
    
    public CardNotFoundException(String message) {
        super(message);
    }
    
    public CardNotFoundException(String message, Card card, Player player) {
        super(message);
        this.card = card;
        this.player = player;
    }
}
