package uk.co.essarsoftware.par.gui.panels;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.gui.components.CardComponent;
import uk.co.essarsoftware.par.gui.beans.CardBean;
import uk.co.essarsoftware.par.gui.beans.SelectableCardBean;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Swing panel holding an array of cards that can be selected.
 * @author Steve Roberts <steve.roberts@essarsoftware.co.uk>
 * @version 1.0
 */
class SelectableCardsPanel extends CardsPanel
{
    private ArrayList<SelectableCardBean> selectedCards;
    private int selectMode = SELECT_MODE_MULTIPLE;
    
    public static final int SELECT_MODE_SINGLE = 0x01;
    public static final int SELECT_MODE_MULTIPLE = 0x02;

    public SelectableCardsPanel() {
        super();
        selectedCards = new ArrayList<SelectableCardBean>();
    }

    private void updateSelectedCards() {
        if(selectMode == SELECT_MODE_SINGLE) {
            // De-select currently selected cards
            for(SelectableCardBean scb : selectedCards) {
                scb.setSelected(false);
            }
            refreshComponents();
        }
        // Clear selected card list
        selectedCards.clear();
        for(CardBean cb : cards) {
            if(cb instanceof SelectableCardBean) {
                SelectableCardBean scb = (SelectableCardBean) cb;
                if(scb.isSelected()) {
                    selectedCards.add(scb);
                }
            }
        }
    }

    @Override
    protected CardComponent createComponent() {
        CardComponent cc = new CardComponent(true, true);
        cc.addPropertyChangeListener("selected", new CardSelectedPropertyChangeListener());
        return cc;
    }

    @Override
    protected void updateBean(CardComponent cc, CardBean bean) {
        super.updateBean(cc, bean);
        if(bean instanceof SelectableCardBean) {
            SelectableCardBean scb = (SelectableCardBean) bean;
            scb.setSelected(cc.isSelected());
        }
    }

    @Override
    protected void updateComponent(CardComponent cc, CardBean bean) {
        super.updateComponent(cc, bean);
        if(bean instanceof SelectableCardBean) {
            SelectableCardBean scb = (SelectableCardBean) bean;
            cc.setSelected(scb.isSelected());
        } else {
            cc.setSelected(false);
        }
    }

    @Override
    SelectableCardBean createBean(Card card, boolean faceUp) {
        return createBean(card, faceUp, false);
    }

    SelectableCardBean createBean(Card card, boolean faceUp, boolean selected) {
        SelectableCardBean scb = new SelectableCardBean();
        scb.setCard(card);
        scb.setFaceUp(faceUp);
        scb.setSelected(selected);
        return scb;
    }

    List<SelectableCardBean> getSelectedCardBeans() {
        return selectedCards;
    }

    public void addCard(Card card, boolean faceUp, boolean selected) {
        cards.add(createBean(card, faceUp, selected));
        refreshComponents();
    }
    
    public void deselectAll() {
        for(CardBean cb : cards) {
            if(cb instanceof SelectableCardBean) {
                ((SelectableCardBean) cb).setSelected(false);
            }
        }
    }
    
    public int getSelectedCardCount() {
        return getSelectedCardBeans().size();
    }

    public Card[] getSelectedCards() {
        List<Card> sc = new ArrayList<Card>();
        for(SelectableCardBean scb : getSelectedCardBeans()) {
            sc.add(scb.getCard());
        }
        return sc.toArray(new Card[sc.size()]);
    }
    
    public int getSelectMode() {
        return selectMode;
    }

    public void selectAll() {
        for(CardBean cb : cards) {
            if(cb instanceof SelectableCardBean) {
                ((SelectableCardBean) cb).setSelected(true);
            }
        }
    }

    public void setSelectMode(int selectMode) {
        this.selectMode = selectMode;
    }

    private class CardSelectedPropertyChangeListener implements PropertyChangeListener
    {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if(evt.getSource() instanceof CardComponent) {
                CardComponent cc = (CardComponent) evt.getSource();
                updateBean(cc);
                updateSelectedCards();
            }

            // Pass event to panel listeners
            firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
        }
    }
}
