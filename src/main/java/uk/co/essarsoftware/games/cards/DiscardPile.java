package uk.co.essarsoftware.games.cards;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 15:36
 * To change this template use File | Settings | File Templates.
 */
public class DiscardPile extends Pile
{
    public void discard(Card card) {
        cards.push(card);
    }

    public void resetTo(Pile pile) {
        pile.cards.addAll(cards);
        cards.clear();
    }

    public Card getDiscard() {
        return cards.peek();
    }
}
