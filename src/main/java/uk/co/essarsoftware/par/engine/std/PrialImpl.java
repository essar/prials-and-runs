package uk.co.essarsoftware.par.engine.std;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.engine.Prial;

import java.util.Comparator;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 17:26
 * To change this template use File | Settings | File Templates.
 */
class PrialImpl extends PlayImpl implements Prial
{
    public PrialImpl() {
        super(new Comparator<Card>() {
            @Override
            public int compare(Card o1, Card o2) {
                return 0;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }

    @Override
    boolean addCard(Card card) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Card[] getAllowableCards() {
        return new Card[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    boolean validate(Card[] cards) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
