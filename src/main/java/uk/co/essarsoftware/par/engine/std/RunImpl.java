package uk.co.essarsoftware.par.engine.std;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.engine.Run;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 17:26
 * To change this template use File | Settings | File Templates.
 */
class RunImpl extends PlayImpl implements Run
{
    private Card.Suit runSuit;
    private Card.Value hiVal, loVal;

    public RunImpl() {
        super(new Comparator<Card>() {
            @Override
            public int compare(Card c1, Card c2) {
                if(c1 == null) {
                    return -1;
                }
                if(c2 == null) {
                    return 1;
                }
                return c1.compareTo(c2);
            }
        });
    }

    private static Card.Value nextValue(Card.Value v, boolean higher) {
        switch(v) {
            case ACE: return (higher ? Card.Value.TWO : Card.Value.ACE);
            case TWO: return (higher ? Card.Value.THREE : Card.Value.ACE);
            case THREE: return (higher ? Card.Value.FOUR : Card.Value.TWO);
            case FOUR: return (higher ? Card.Value.FIVE : Card.Value.THREE);
            case FIVE: return (higher ? Card.Value.SIX : Card.Value.FOUR);
            case SIX: return (higher ? Card.Value.SEVEN : Card.Value.FIVE);
            case SEVEN: return (higher ? Card.Value.EIGHT : Card.Value.SIX);
            case EIGHT: return (higher ? Card.Value.NINE : Card.Value.SEVEN);
            case NINE: return (higher ? Card.Value.TEN : Card.Value.EIGHT);
            case TEN: return (higher ? Card.Value.JACK : Card.Value.NINE);
            case JACK: return (higher ? Card.Value.QUEEN : Card.Value.TEN);
            case QUEEN: return (higher ? Card.Value.KING : Card.Value.JACK);
            case KING: return (higher ? Card.Value.KING : Card.Value.QUEEN);
            default: return null;
        }
    }

    @Override
    protected boolean addCard(Card card) {
        if(super.addCard(card)) {
            runSuit = card.getSuit();
            if(loVal == null || loVal.ordinal() > card.getValue().ordinal()) {
                loVal = card.getValue();
            }
            if(hiVal == null || hiVal.ordinal() < card.getValue().ordinal()) {
                hiVal = card.getValue();
            }
            return true;
        }
        return false;
    }

    @Override
    protected void cloneFrom(PlayImpl play) {
        RunImpl run = (RunImpl) play;
        super.cloneFrom(run);
        hiVal = run.hiVal;
        loVal = run.loVal;
        runSuit = run.runSuit;
    }

    @Override
    protected void resetPlay() {
        super.resetPlay();
        loVal = null;
        hiVal = null;
        runSuit = null;
    }

    @Override
    public Card[] getAllowableCards() {
        // Uninitialised prial, return null
        if(! isInitialised()) {
            return null;
        }
        // If prial value is not set or prial size is zero then return empty array - all cards allowed
        if(runSuit == null || size() == 0) {
            return new Card[0];
        }
        // Return an array of cards with run suit and one different either end
        ArrayList<Card> allowableCards = new ArrayList<Card>();
        if(loVal != Card.Value.ACE) {
            allowableCards.add(Card.createCard(runSuit, nextValue(loVal, false)));
        }
        if(hiVal != Card.Value.KING) {
            allowableCards.add(Card.createCard(runSuit, nextValue(hiVal, true)));
        }
        return allowableCards.toArray(new Card[allowableCards.size()]);
    }

    @Override
    public boolean isRun() {
        return true;
    }
}
