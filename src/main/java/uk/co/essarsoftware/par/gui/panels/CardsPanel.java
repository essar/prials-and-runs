package uk.co.essarsoftware.par.gui.panels;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.gui.beans.CardBean;
import uk.co.essarsoftware.par.gui.components.CardComponent;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Swing panel holding an array of cards.
 * @author Steve Roberts <steve.roberts@essarsoftware.co.uk>
 * @version 1.0 (27-Jun-2012)
 */
class CardsPanel extends JPanel
{
    /** Mapping between card component and bean objects. */
    private HashMap<CardComponent, CardBean> cardMapping;

    /** Order-sensitive array of <tt>CardBean</tt> objects. */
    protected ArrayList<CardBean> cards;

    public CardsPanel() {
        super(new OverlapLayoutManager(20, 0));
        cards = new ArrayList<CardBean>();
        cardMapping = new HashMap<CardComponent, CardBean>();
    }
    
    protected CardComponent createComponent() {
        return new CardComponent();
    }

    protected void refreshComponents() {
        while(cards.size() > getComponentCount()) {
            add(createComponent());
        }
        while(cards.size() < getComponentCount()) {
            remove(getComponent(getComponentCount() - 1));
        }
        for(int i = 0; i < getComponentCount(); i ++) {
            updateComponent((CardComponent) getComponent(i), cards.get(i));
        }
    }

    protected void updateBean(CardComponent cc) {
        CardBean bean = cardMapping.get(cc);
        if(bean != null) {
            updateBean(cc, bean);
        }
    }
    
    protected void updateBean(CardComponent cc, CardBean bean) {
        bean.setCard(cc.getCard());
        bean.setFaceUp(cc.isFaceUp());
    }

    protected void updateComponent(CardComponent cc, CardBean bean) {
        cardMapping.put(cc, bean);
        cc.setCard(bean.getCard());
        cc.setFaceUp(bean.isFaceUp());
    }
    
    CardBean createBean(Card card, boolean faceUp) {
        CardBean cb = new CardBean();
        cb.setCard(card);
        cb.setFaceUp(faceUp);
        return cb;
    }
    
    void moveDown(CardBean cb) {
        int index = cards.indexOf(cb) - 1;
        if(index >= 0) {
            moveTo(index, cb);
        }
    }

    void moveTo(int index, CardBean cb) {
        if(cards.remove(cb)) {
            cards.add(index, cb);
            refreshComponents();
        }
    }

    void moveUp(CardBean cb) {
        int index = cards.indexOf(cb) + 1;
        if(index < cards.size()) {
            moveTo(index, cb);
        }
    }

    void removeCard(CardBean cb) {
        cards.remove(cb);
        refreshComponents();
    }

    void setCards(ArrayList<CardBean> cardsIn) {
        ArrayList<CardBean> newCards = new ArrayList<CardBean>();
        // Copy any cards from cards that are still present in cardsIn, in the same order
        for(CardBean cb : cards) {
            if(cardsIn.contains(cb)) {
                newCards.add(cb);
            }
        }
        // Add any cards from cardsIn that have not yet been added
        for(CardBean cb : cardsIn) {
            if(! newCards.contains(cb)) {
                newCards.add(cb);
            }
        }

        cards = newCards;
        refreshComponents();
    }

    public void addCard(Card card, boolean faceUp) {
        cards.add(createBean(card, faceUp));
        refreshComponents();
    }
    
    public int getCardCount() {
        return cards.size();
    }
    
    public Card[] getCards() {
        ArrayList<Card> cardList = new ArrayList<Card>();
        for(CardBean cb : cards) {
            cardList.add(cb.getCard());
        }
        return cardList.toArray(new Card[cardList.size()]);
    }

    public void moveTo(int from, int to) {
        CardBean cb = cards.remove(from);
        if(cb != null) {
            cards.add(to, cb);
            refreshComponents();
        }
    }
    
    public void removeCard(int index) {
        cards.remove(index);
        refreshComponents();
    }

    public void setCards(Card[] cardsIn) {
        // Convert array of cards to list of card beans
        ArrayList<CardBean> cbIn = new ArrayList<CardBean>();
        for(Card c : cardsIn) {
            cbIn.add(createBean(c, true));
        }
        setCards(cbIn);
    }

    public void sortBySuit() {
        Collections.sort(cards, new Comparator<CardBean>() {
            @Override
            public int compare(CardBean c1, CardBean c2) {
                if(c1 == null || c2 == null) {
                    return 1;
                }
                if(c1.equals(c2)) {
                    return 0;
                }
                return (c1.getCard() == null ? -1 : c1.getCard().compareTo(c2.getCard()));
            }
        });
        refreshComponents();
    }

    public void sortByValue() {
        Collections.sort(cards, new Comparator<CardBean>() {
            @Override
            public int compare(CardBean c1, CardBean c2) {
                if(c1 == null || c2 == null) {
                    return 1;
                }
                if(c1.equals(c2)) {
                    return 0;
                }
                return (c1.getCard() == null ? -1 : c1.getCard().compareTo(c2.getCard(), true));
            }
        });
        refreshComponents();
    }

    public static class OverlapLayoutManager implements LayoutManager
    {
        private int xSpacing, ySpacing;

        public OverlapLayoutManager(int xSpacing, int ySpacing) {
            this.xSpacing = xSpacing;
            this.ySpacing = ySpacing;
        }

        public void addLayoutComponent(String name, Component comp) {
            // Redraw all components
            layoutContainer(comp.getParent());
        }

        public void removeLayoutComponent(Component comp) {
            // Redraw all components
            layoutContainer(comp.getParent());
        }

        public Dimension preferredLayoutSize(Container parent) {
            Rectangle bounds = new Rectangle();
            int x = 0;
            int y = 0;

            for (Component c : parent.getComponents()) {
                // Get component preferred size
                Dimension prefSize = c.getPreferredSize();
                // Include component location in bounds
                bounds = bounds.union(new Rectangle(x, y, prefSize.width, prefSize.height));

                // Recalculate for next component
                x += xSpacing;
                y += ySpacing;
            }

            // Add parent insets and get width and heights
            int prefWidth = parent.getInsets().left
                    + (int) bounds.getWidth()
                    + parent.getInsets().right;
            int prefHeight = parent.getInsets().top
                    + (int) bounds.getHeight()
                    + parent.getInsets().bottom;
            return new Dimension(prefWidth, prefHeight);
        }

        public Dimension minimumLayoutSize(Container parent) {
            Rectangle bounds = new Rectangle();
            int x = 0;
            int y = 0;

            for (Component c : parent.getComponents()) {
                // Get component preferred size
                Dimension minSize = c.getMinimumSize();
                // Include component location in bounds
                bounds = bounds.union(new Rectangle(x, y, minSize.width, minSize.height));

                // Recalculate for next component
                x += xSpacing;
                y += ySpacing;
            }

            // Add parent insets and get width and heights
            int minWidth = parent.getInsets().left
                    + (int) bounds.getWidth()
                    + parent.getInsets().right;
            int minHeight = parent.getInsets().top
                    + (int) bounds.getHeight()
                    + parent.getInsets().bottom;
            return new Dimension(minWidth, minHeight);
        }

        public void layoutContainer(Container parent) {
            int x = parent.getInsets().left;
            int y = parent.getInsets().top;

            // TODO look at getComponentOrientation()

            for (Component c : parent.getComponents()) {
                // Draw component
                c.setLocation(x, y);
                c.setSize(c.getPreferredSize());

                // Recalculate for next component
                x += xSpacing;
                y += ySpacing;
            }
        }
    }
}
