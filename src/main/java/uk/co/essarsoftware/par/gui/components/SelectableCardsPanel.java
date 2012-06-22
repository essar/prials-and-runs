package uk.co.essarsoftware.par.gui.components;

import uk.co.essarsoftware.games.cards.Card;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 18/06/12
 * Time: 10:59
 * To change this template use File | Settings | File Templates.
 */
public class SelectableCardsPanel extends CardsPanel
{
    private void swapCards(CardComponent cc, CardComponent cc2) {
        Card c = cc.getCard();
        Card c2 = cc2.getCard();

        cc2.setCard(c);
        cc2.setSelected(true);

        cc.setCard(c2);
        cc.setSelected(false);
    }

    @Override
    protected CardComponent createComponent() {
        // Create new selectable card component
        CardComponent cc = new CardComponent(true, true);
        // Add a property change listener
        cc.getCardComponentBean().addPropertyChangeListener("selected", new CardSelectedPropertyListener());
        return cc;
    }

    @Override
    protected SelectableCardsPanelBean getBean() {
        if(bean == null) {
            return new SelectableCardsPanelBean();
        }
        return (SelectableCardsPanelBean) bean;
    }

    @Override
    protected void removeComponent(CardComponent cc) {
        // Remove card from bean
        getBean().removeSelectedCard(cc.getCard());
        // Pass to superclass
        super.removeComponent(cc);
    }
    
    public void addSelectedCardsPropertyChangeListener(PropertyChangeListener l) {
        bean.addPropertyChangeListener("selectedCards", l);
    }

    public void deselectAll() {
        for(int i = 0; i < getComponentCount(); i ++) {
            if(getComponent(i) instanceof CardComponent) {
                CardComponent cc = (CardComponent) getComponent(i);
                cc.setSelected(false);
            }
        }
    }

    public int getSelectedCardCount() {
        return getBean().getSelectedCards().size();
    }

    public Card[] getSelectedCards() {
        return getBean().getSelectedCards().toArray(new Card[getBean().getSelectedCards().size()]);
    }

    public void moveSelectedDown() {
        for(int i = 1; i < getComponentCount(); i ++) {
            if(getComponent(i) instanceof CardComponent) {
                CardComponent cc = (CardComponent) getComponent(i);
                if(cc.isSelected()) {
                    CardComponent cc2 = (CardComponent) getComponent(i - 1);
                    swapCards(cc, cc2);
                }
            }
        }
    }
    
    public void moveSelectedUp() {
        for(Card c : getBean().getSelectedCards()) {
            int index = getBean().getCards().indexOf(c);
            if(index < 0) {
                for(int i = 0; i < getBean().getCards().size(); i ++) {
                    Card cc = getBean().getCards().get(i);
                    if(cc.sameCard(c)) {
                        index = i;
                        break;
                    }
                }
            }
            getBean().moveCard(index + 1, c);
        }
    }

    public void selectAll() {
        for(int i = 0; i < getComponentCount(); i ++) {
            if(getComponent(i) instanceof CardComponent) {
                CardComponent cc = (CardComponent) getComponent(i);
                cc.setSelected(true);
            }
        }
    }

    private class CardSelectedPropertyListener implements PropertyChangeListener
    {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            // Add or remove card from selected card map
            CardComponent src = ((CardComponentBean) evt.getSource()).getCardComponent();
            if((Boolean) evt.getNewValue()) {
                getBean().addSelectedCard(src.getCard());
            } else {
                getBean().removeSelectedCard(src.getCard());
            }
        }
    }
}
