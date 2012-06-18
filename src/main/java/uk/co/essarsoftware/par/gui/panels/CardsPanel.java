package uk.co.essarsoftware.par.gui.panels;

import uk.co.essarsoftware.games.cards.Card;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 18/06/12
 * Time: 10:59
 * To change this template use File | Settings | File Templates.
 */
abstract class CardsPanel extends JPanel
{
    public CardsPanel() {
        super(new OverlapLayoutManager(20, 0));
    }
    
    protected abstract CardComponent createComponent();

    protected ArrayList<Card> getCardList() {
        ArrayList<Card> cardList = new ArrayList<Card>();
        for(int i = 0; i < getComponentCount(); i ++) {
            Component c = getComponent(i);
            if(c instanceof CardComponent) {
                cardList.add(((CardComponent) c).getCard());
            }
        }
        return cardList;
    }

    public int getCardCount() {
        return getComponentCount();
    }
    
    public void setCards(Card[] cards) {
        while(cards.length > getComponentCount()) {
            // Add a new component to the end of the panel
            add(createComponent());
        }
        while(cards.length < getComponentCount()) {
            // Remove last component
            remove(getComponent(getComponentCount() - 1));
        }

        for(int i = 0; i < cards.length; i ++) {
            Component c = getComponent(i);
            if(c instanceof CardComponent) {
                ((CardComponent) c).setCard(cards[i]);
            }
        }

        validate();
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

    public void sortBySuit() {
        ArrayList<Card> cardList = getCardList();
        Collections.sort(cardList, new Comparator<Card>() {
            @Override
            public int compare(Card o1, Card o2) {
                if(o1.sameSuit(o2)) {
                    return o1.getValue().compareTo(o2.getValue());
                }
                return o1.getSuit().compareTo(o2.getSuit());
            }
        });

        setCards(cardList.toArray(new Card[cardList.size()]));
    }

    public void sortByValue() {
        ArrayList<Card> cardList = getCardList();
        Collections.sort(cardList, new Comparator<Card>() {
            @Override
            public int compare(Card o1, Card o2) {
                if(o1.sameValue(o2)) {
                    return o1.getSuit().compareTo(o2.getSuit());
                }
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        setCards(cardList.toArray(new Card[cardList.size()]));
    }
}
