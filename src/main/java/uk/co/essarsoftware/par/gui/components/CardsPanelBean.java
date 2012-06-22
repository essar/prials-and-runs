package uk.co.essarsoftware.par.gui.components;

import uk.co.essarsoftware.games.cards.Card;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 21/06/12
 * Time: 12:28
 * To change this template use File | Settings | File Templates.
 */
class CardsPanelBean
{
    // Internal array to store ordered list of cards
    private final ArrayList<Card> cards = new ArrayList<Card>();

    protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    public void addPropertyChangeListener(String name, PropertyChangeListener l) {
        pcs.addPropertyChangeListener(name, l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    public void removePropertyChangeListener(String name, PropertyChangeListener l) {
        pcs.removePropertyChangeListener(name, l);
    }

    public void addCard(Card value) {
        ArrayList<Card> oldCards = new ArrayList<Card>(cards);
        // Add card to end of array
        cards.add(value);
        pcs.firePropertyChange("cards", oldCards, cards);
    }

    public List<Card> getCards() {
        return cards;
    }

    public void moveCard(int index, Card value) {
        ArrayList<Card> oldCards = new ArrayList<Card>(cards);
        index = Math.max(0, Math.min(cards.size() - 1, index));
        // Remove card and re-insert
        if(cards.remove(value)) {
            cards.add(index, value);
        } else {
            for(int i = 0; i < cards.size(); i ++) {
                Card c = cards.get(i);
                if(c.sameCard(value)) {
                    c = cards.remove(i);
                    cards.add(index, c);
                    break;
                }
            }
        }
        pcs.firePropertyChange("cards", oldCards, cards);
    }

    public void removeCard(Card value) {
        ArrayList<Card> oldCards = new ArrayList<Card>(cards);
        // Remove card from array
        if(! cards.remove(value)) {
            for(int i = 0; i < cards.size(); i ++) {
                Card c = cards.get(i);
                if(c.sameCard(value)) {
                    cards.remove(i);
                    break;
                }
            }
        }
        pcs.firePropertyChange("cards", oldCards, cards);
    }
    
    public void sortCards(Comparator<Card> comparator) {
        ArrayList<Card> oldCards = new ArrayList<Card>(cards);
        // Sort cards according to comparator parameter
        Collections.sort(cards, comparator);
        pcs.firePropertyChange("cards", oldCards, cards);
    }
}
