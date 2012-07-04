package uk.co.essarsoftware.par.gui.panels;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.engine.Play;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 19/06/12
 * Time: 15:33
 * To change this template use File | Settings | File Templates.
 */
class PlaysPanel extends JPanel
{
    private CardsPanel[] cards;

    public PlaysPanel(int sz) {
        cards = new CardsPanel[sz];
        initComponents();
        drawComponents();
    }

    private void initComponents() {
        for(int i = 0; i < cards.length; i ++) {
            cards[i] = new CardsPanel();
            cards[i].setCards(new Card[3]);
        }
    }
    
    private void drawComponents() {
        // Add panels
        for(CardsPanel cp : cards) {
            add(cp);
        }
    }

    public void reset(int sz) {
        removeAll();
        cards = new CardsPanel[sz];
        initComponents();
        drawComponents();
    }

    public void setPlays(Play[] ps) {
        for(int i = 0; i < ps.length && i < cards.length; i ++) {
            cards[i].setCards(ps[i].getCards());
        }
    }
}
