package uk.co.essarsoftware.par.engine.std;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.games.cards.CardArray;
import uk.co.essarsoftware.par.engine.Play;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 17:26
 * To change this template use File | Settings | File Templates.
 */
class PlayImpl implements Play
{
    private CardArray cards;

    boolean validate(Card[] cards) {
        return false;
    }

    void init(Card[] cards) {

    }

    public Card[] getAllowableCards() {
        return new Card[0];
    }

    public Card[] getCards() {
        return cards.getCards();
    }

    @Override
    public boolean isInitialised() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isPrial() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isRun() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int size() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
