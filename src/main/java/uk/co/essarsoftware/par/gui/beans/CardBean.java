package uk.co.essarsoftware.par.gui.beans;

import uk.co.essarsoftware.games.cards.Card;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 26/06/12
 * Time: 11:31
 * To change this template use File | Settings | File Templates.
 */
public class CardBean
{
    private Card card = null;
    private boolean faceUp = true;
    
    public Card getCard() {
        return card;
    }
    
    public boolean isFaceUp() {
        return faceUp;
    }
    
    public void setCard(Card card) {
        this.card = card;
    }

    public void setFaceUp(boolean faceUp) {
        this.faceUp = faceUp;
    }
}
