package uk.co.essarsoftware.games.cards;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 12:41
 * To change this template use File | Settings | File Templates.
 */
public class CardArray extends ArrayList<Card>
{
    public CardArray() {

    }

    Card findCard(Card card) {
        if(contains(card)) {
            // If exact card is present, look for that
            for(Card c : this) {
                if(c.equals(card)) {
                    return c;
                }
            }
        } else {
            // Look for cards with same suit and value
            for(Card c : this) {
                if(c.sameCard(card)) {
                    return c;
                }
            }
        }
        // Nothing found, return null
        return null;
    }

    @Override
    public boolean add(Card card) {
        if(card.packID == 0) {
            throw new IllegalArgumentException("Card must have pack ID");
        }
        return super.add(card);
    }

    @Override
    public void add(int index, Card card) {
        if(card.packID == 0) {
            throw new IllegalArgumentException("Card must have pack ID");
        }
        super.add(index, card);
    }

    @Override
    public boolean addAll(Collection<? extends Card> cards) {
        for(Card c : cards) {
            if(c.packID == 0) {
                throw new IllegalArgumentException("Card must have pack ID");
            }
        }
        return super.addAll(cards);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Card> cards) {
        for(Card c : cards) {
            if(c.packID == 0) {
                throw new IllegalArgumentException("Card must have pack ID");
            }
        }
        return super.addAll(index, cards);
    }

    @Override
    public synchronized Card set(int index, Card card) {
        if(card.packID == 0) {
            throw new IllegalArgumentException("Card must have pack ID");
        }
        return super.set(index, card);
    }

    public Card[] getCards() {
        return toArray(new Card[size()]);
    }
    
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("|");
        for(Card c : this) {
            buf.append(c);
            buf.append("|");
        }
        return new String(buf);
    }
}
