package uk.co.essarsoftware.par.engine.std;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.games.cards.CardArray;
import uk.co.essarsoftware.games.cards.OrderedCardArray;
import uk.co.essarsoftware.par.engine.Play;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 17:26
 * To change this template use File | Settings | File Templates.
 */
abstract class PlayImpl implements Play
{
    protected OrderedCardArray playCards;

    protected PlayImpl(Comparator<Card> comparator) {
        playCards = new OrderedCardArray(comparator);
    }

    boolean addCard(Card card) {
        // Validate arguments
        if(card == null) {
            return false;
        }
        if(card.isJoker() && !card.isBoundJoker()) {
            return false;
        }

        for(Card ac : getAllowableCards()) {
            if(ac.sameCard(card)) {
                return playCards.add(card);
            }
        }
        return false;
    }

    abstract boolean validate(Card[] cards);

    void init(Card[] cards) {
        if(validate(cards)) {
            for(Card c : cards) {
                playCards.add(c);
            }
        }
    }

    @Override
    public Card[] getCards() {
        return playCards.toCardArray().getCards();
    }

    @Override
    public boolean isInitialised() {
        return playCards.size() > 0;
    }

    @Override
    public boolean isPrial() {
        return false;
    }

    @Override
    public boolean isRun() {
        return false;
    }

    @Override
    public int size() {
        return playCards.size();
    }
}
