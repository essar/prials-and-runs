package uk.co.essarsoftware.par.gui.components;

import uk.co.essarsoftware.games.cards.Card;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 18/06/12
 * Time: 10:59
 * To change this template use File | Settings | File Templates.
 */
public class CardsPanel extends JPanel
{
    protected CardsPanelBean bean;

    public CardsPanel() {
        super(new OverlapLayoutManager(20, 0));
        bean = getBean();
        bean.addPropertyChangeListener("cards", new CardsPropertyChangeListener());
    }

    protected CardComponent addComponent() {
        // Create new empty component
        CardComponent cc = createComponent();
        // Add to end of panel
        add(cc);
        return cc;
    }

    protected CardComponent createComponent() {
        return new CardComponent();
    }

    protected CardsPanelBean getBean() {
        if(bean == null) {
            return new CardsPanelBean();
        }
        return bean;
    }

    protected void removeComponent(CardComponent cc) {
        // Remove card from bean
        bean.removeCard(cc.getCard());
        // Remove component from panel
        remove(cc);
    }

    protected List<Card> getCardList() {
        return bean.getCards();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        bean.addPropertyChangeListener(l);
        super.addPropertyChangeListener(l);
    }

    @Override
    public void addPropertyChangeListener(String name, PropertyChangeListener l) {
        bean.addPropertyChangeListener(name, l);
        super.addPropertyChangeListener(name, l);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
        bean.removePropertyChangeListener(l);
        super.removePropertyChangeListener(l);
    }

    @Override
    public void removePropertyChangeListener(String name, PropertyChangeListener l) {
        bean.removePropertyChangeListener(name, l);
        super.removePropertyChangeListener(name, l);
    }

    public int getCardCount() {
        return bean.getCards().size();
    }

    public void setCards(Card[] cards) {
        List<Card> cardsIn = Arrays.asList(cards);
        
        // Remove cards that are no longer in card array
        for(Card c : bean.getCards()) {
            if(! cardsIn.contains(c)) {
                bean.removeCard(c);
            }
        }
        
        // Add new cards in card array
        for(Card c : cardsIn) {
            if(! bean.getCards().contains(c)) {
                System.out.println("Adding card: " + c);
                bean.addCard(c);
            }
        }
    }

    public void sortBySuit() {
        bean.sortCards(new Comparator<Card>() {
            @Override
            public int compare(Card o1, Card o2) {
                if (o1.sameSuit(o2)) {
                    return o1.getValue().compareTo(o2.getValue());
                }
                return o1.getSuit().compareTo(o2.getSuit());
            }
        });
    }

    public void sortByValue() {
        bean.sortCards(new Comparator<Card>() {
            @Override
            public int compare(Card o1, Card o2) {
                if (o1.sameValue(o2)) {
                    return o1.getSuit().compareTo(o2.getSuit());
                }
                return o1.getValue().compareTo(o2.getValue());
            }
        });
    }

    private class CardsPropertyChangeListener implements PropertyChangeListener
    {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            System.out.println("Change to cards");
            List<Card> cards = bean.getCards();

            while(cards.size() > getComponentCount()) {
                // Add new component
                addComponent();
            }
            while(cards.size() < getComponentCount()) {
                // Remove last component
                removeComponent((CardComponent) getComponent(getComponentCount() - 1));
            }
            for(int i = 0; i < cards.size(); i ++) {
                Component c = getComponent(i);
                if(c instanceof CardComponent) {
                    ((CardComponent) c).setCard(cards.get(i));
                }
            }

            // Force re-layout of container
            validate();
        }
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