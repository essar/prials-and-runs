package uk.co.essarsoftware.par.gui.panels;

import uk.co.essarsoftware.games.cards.Card;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 18/06/12
 * Time: 10:59
 * To change this template use File | Settings | File Templates.
 */
abstract class SelectableCardsPanel extends CardsPanel
{
    private void swapCards(CardComponent cc, CardComponent cc2) {
        Card c = cc.getCard();
        Card c2 = cc2.getCard();

        cc2.setCard(c);
        cc2.setSelected(true);

        cc.setCard(c2);
        cc.setSelected(false);
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
        int ct = 0;
        for(int i = 0; i < getComponentCount(); i ++) {
            if(getComponent(i) instanceof CardComponent) {
                CardComponent cc = (CardComponent) getComponent(i);
                if(cc.isSelected()) {
                    ct ++;
                }
            }
        }
        return ct;
    }

    public Card[] getSelectedCards() {
        ArrayList<Card> selected = new ArrayList<Card>();
        for(int i = 0; i < getComponentCount(); i ++) {
            if(getComponent(i) instanceof CardComponent) {
                CardComponent cc = (CardComponent) getComponent(i);
                if(cc.isSelected()) {
                    selected.add(cc.getCard());
                }
            }
        }
        return selected.toArray(new Card[selected.size()]);
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
        for(int i = getComponentCount() - 2; i >= 0; i --) {
            if(getComponent(i) instanceof CardComponent) {
                CardComponent cc = (CardComponent) getComponent(i);
                if(cc.isSelected()) {
                    CardComponent cc2 = (CardComponent) getComponent(i + 1);
                    swapCards(cc, cc2);
                }
            }
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
}
