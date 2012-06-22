package uk.co.essarsoftware.par.gui.components;

import uk.co.essarsoftware.games.cards.Card;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Bean class for holding a CardComponent that can be selected.
 */
class CardComponentBean
{
    private boolean faceUp = true;
    private boolean selected = false;
    private Card card = null;
    private final CardComponent component;

    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public CardComponentBean(CardComponent component) {
        this.component = component;
    }

    public CardComponentBean(CardComponent component, Card card) {
        this(component);
        this.card = card;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    public void addPropertyChangeListener(String name, PropertyChangeListener l) {
        pcs.addPropertyChangeListener(name, l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    CardComponent getCardComponent() {
        return component;
    }

    public Card getCard() {
        return card;
    }

    public boolean isFaceUp() {
        return faceUp;
    }
    
    public boolean isSelected() {
        return selected;
    }
    
    public void setCard(Card card) {
        Card oldCard = this.card;
        this.card = card;
        pcs.firePropertyChange("card", oldCard, card);
    }

    public void setFaceUp(boolean faceUp) {
        boolean oldFaceUp = this.faceUp;
        this.faceUp = faceUp;
        pcs.firePropertyChange("faceUp", oldFaceUp, faceUp);
    }

    public void setSelected(boolean selected) {
        boolean oldSelected = this.selected;
        this.selected = selected;
        pcs.firePropertyChange("selected", oldSelected, selected);
    }
}
