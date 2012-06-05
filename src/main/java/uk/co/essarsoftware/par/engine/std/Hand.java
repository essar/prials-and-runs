package uk.co.essarsoftware.par.engine.std;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.games.cards.CardArray;
import uk.co.essarsoftware.games.cards.SortableCardArray;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 13:07
 * To change this template use File | Settings | File Templates.
 */
class Hand
{
    private SortableCardArray cards;


    boolean contains(Card card) {
        return cards.contains(card);
    }

    void setCards(CardArray cards) {
        cards.clear();
        cards.addAll(cards);
    }

    public void discard(Card card) {
        if(card == null) {
            throw new IllegalArgumentException("Cannot discard null");
        }
        if(! cards.contains(card)) {
            // TODO Change to CardNotFoundException
            throw new IllegalArgumentException("Card not found in hand");
        }
        cards.remove(card);
    }

    public void pickup(Card card) {
        if(card == null) {
            throw new IllegalArgumentException("Cannot pickup null");
        }
        if(cards.contains(card)) {
            // TODO Change to sensible exception
            throw new IllegalArgumentException("Card already in hand");
        }
        cards.add(card);
    }

    public void moveCardDown(Card card) {
        cards.moveCardDown(card);
    }

    public void moveCardUp(Card card) {
        cards.moveCardUp(card);
    }

    public int size() {
        return cards.size();
    }

    public void sortBySuit() {
        cards.sortBySuit();
    }

    public void sortByValue() {
        cards.sortByValue();
    }
}
