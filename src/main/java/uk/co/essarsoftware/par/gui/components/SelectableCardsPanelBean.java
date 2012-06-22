package uk.co.essarsoftware.par.gui.components;

import uk.co.essarsoftware.games.cards.Card;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 21/06/12
 * Time: 11:46
 * To change this template use File | Settings | File Templates.
 */
class SelectableCardsPanelBean extends CardsPanelBean
{
    private final ArrayList<Card> selectedCards = new ArrayList<Card>();

    public void addSelectedCard(Card value) {
        ArrayList<Card> oldSelectedCards = new ArrayList<Card>(selectedCards);
        selectedCards.add(value);
        pcs.firePropertyChange("selectedCards", oldSelectedCards, selectedCards);
    }

    public List<Card> getSelectedCards() {
        return selectedCards;
    }

    public void removeSelectedCard(Card value) {
        ArrayList<Card> oldSelectedCards = new ArrayList<Card>(selectedCards);
        // Remove card from array
        if(! selectedCards.remove(value)) {
            for(int i = 0; i < selectedCards.size(); i ++) {
                Card c = selectedCards.get(i);
                if(c.sameCard(value)) {
                    selectedCards.remove(i);
                    break;
                }
            }
        }
        pcs.firePropertyChange("selectedCards", oldSelectedCards, selectedCards);
    }
}
