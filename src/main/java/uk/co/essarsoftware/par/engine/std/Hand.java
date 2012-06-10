package uk.co.essarsoftware.par.engine.std;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.games.cards.CardArray;
import uk.co.essarsoftware.par.engine.CardNotFoundException;
import uk.co.essarsoftware.par.engine.DuplicateCardException;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 13:07
 * To change this template use File | Settings | File Templates.
 */
class Hand
{
    private CardArray cards;

    boolean contains(Card card) {
        return cards.contains(card);
    }

    void setCards(CardArray cards) {
        cards.clear();
        cards.addAll(cards);
    }

    public void discard(Card card) throws CardNotFoundException {
        if(card == null) {
            throw new IllegalArgumentException("Cannot discard null");
        }
        if(! cards.contains(card)) {
            throw new CardNotFoundException("Card not found in hand", card, null);
        }
        cards.remove(card);
    }

    public void pickup(Card card) throws DuplicateCardException {
        if(card == null) {
            throw new IllegalArgumentException("Cannot pickup null");
        }
        if(cards.contains(card)) {
            throw new DuplicateCardException("Card already in hand", card, null);
        }
        cards.add(card);
    }
    
    public Card[] getCards() {
        return cards.getCards();
    }

    public int size() {
        return cards.size();
    }
}
