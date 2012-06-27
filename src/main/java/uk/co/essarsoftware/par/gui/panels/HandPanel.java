package uk.co.essarsoftware.par.gui.panels;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.gui.components.CardComponent;
import uk.co.essarsoftware.par.gui.beans.CardBean;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;

/**
 * <p>Swing panel responsible for displaying a player's hand of cards.</p>
 * <p>Cards can be selected, sorted and moved into a custom order.</p>
 * @author Steve Roberts <steve.roberts@essarsoftware.co.uk>
 * @version 1.0
 */
public class HandPanel extends JPanel
{
    private JButton btnMoveCardsDown, btnMoveCardsUp, btnSortBySuit, btnSortByValue;
    private JLabel lblCardCount, lblSelectedCount;

    private CardComponent penaltyCard;
    private SelectableCardsPanel cards;

    public HandPanel() {
        setLayout(new BorderLayout());
        
        initComponents();
        drawComponents();
    }
    
    private void initComponents() {
        // Set up cards panel
        cards = new SelectableCardsPanel();
        cards.addMouseWheelListener(new HandPanelMouseWheelListener());
        cards.addPropertyChangeListener("selected", new SelectedCardsPropertyListener());

        // Set up penalty card
        penaltyCard = new CardComponent(false, false);
        penaltyCard.addMouseListener(new PenaltyCardMouseAdapter());

        // Set up buttons
        btnMoveCardsDown = new JButton(new MoveCardsDownAction());
        btnMoveCardsDown.setPreferredSize(new Dimension(80, 30));
        btnMoveCardsDown.getAction().setEnabled(getSelectedCardCount() > 0);
        
        btnMoveCardsUp = new JButton(new MoveCardsUpAction());
        btnMoveCardsUp.setPreferredSize(new Dimension(80, 30));
        btnMoveCardsUp.getAction().setEnabled(getSelectedCardCount() > 0);
        
        btnSortBySuit = new JButton(new SortCardsBySuitAction());
        btnSortBySuit.setPreferredSize(new Dimension(80, 30));
        
        btnSortByValue = new JButton(new SortCardsByValueAction());
        btnSortByValue.setPreferredSize(new Dimension(80, 30));

        // Set up status labels
        lblCardCount = new JLabel("0");
        lblCardCount.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), BorderFactory.createEmptyBorder(2, 10, 2, 10)));
        lblCardCount.setPreferredSize(new Dimension(30, 20));
        lblCardCount.setToolTipText("Card count");

        lblSelectedCount = new JLabel("0");
        lblSelectedCount.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), BorderFactory.createEmptyBorder(2, 10, 2, 10)));
        lblSelectedCount.setPreferredSize(new Dimension(30, 20));
        lblSelectedCount.setToolTipText("Selected card count");
    }
    
    private void drawComponents() {
        Box btns = Box.createHorizontalBox();
        btns.add(btnSortBySuit);
        btns.add(btnSortByValue);
        btns.add(btnMoveCardsDown);
        btns.add(btnMoveCardsUp);
        add(btns, BorderLayout.NORTH);
        
        Box status = Box.createHorizontalBox();
        status.add(lblCardCount);
        status.add(lblSelectedCount);
        add(status, BorderLayout.SOUTH);
        
        Container ctr = new Container();
        ctr.setLayout(new FlowLayout());
        ctr.add(cards);
        ctr.add(penaltyCard);
        
        add(ctr, BorderLayout.CENTER);
    }

    private void moveSelectedDown() {
        for(int i = 0; i < cards.getSelectedCardCount(); i ++) {
            CardBean cb = cards.getSelectedCardBeans().get(i);
            cards.moveDown(cb);
        }
    }
    
    private void moveSelectedUp() {
        for(int i = cards.getSelectedCardCount(); i > 0; i --) {
            CardBean cb = cards.getSelectedCardBeans().get(i - 1);
            cards.moveUp(cb);
        }
    }

    private void sortBySuit() {
        cards.sortBySuit();
    }

    private void sortByValue() {
        cards.sortByValue();
    }

    public int getSelectedCardCount() {
        return cards.getSelectedCardCount();
    }

    public Card[] getSelectedCards() {
        return cards.getSelectedCards();
    }
    
    public void setCards(Card[] cardsIn) {
        if(! SwingUtilities.isEventDispatchThread()) {
            throw new IllegalThreadStateException("Must call from event dispatch thread");
        }
        // Deselect any currently selected cards
        cards.deselectAll();

        // Pass through card array
        cards.setCards(cardsIn);

        // Update buttons
        btnMoveCardsDown.getAction().setEnabled(getSelectedCardCount() > 0);
        btnMoveCardsUp.getAction().setEnabled(getSelectedCardCount() > 0);

        // Update status component
        lblCardCount.setText(cards.getCardCount() + "");
    }


    /* **********************
     * ADAPTER / LISTENER CLASSES
     */

    private class HandPanelMouseWheelListener implements MouseWheelListener
    {
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if(e.getWheelRotation() > 0) {
                moveSelectedDown();
            }
            if(e.getWheelRotation() < 0) {
                moveSelectedUp();
            }
        }
    }

    private class PenaltyCardMouseAdapter extends MouseAdapter
    {
        @Override
        public void mousePressed(MouseEvent e) {
            if(e.getSource() instanceof CardComponent) {
                // Set penalty card face up
                CardComponent cc = (CardComponent) e.getSource();
                cc.setFaceUp(true);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(e.getSource() instanceof CardComponent) {
                // Set penalty card face down
                CardComponent cc = (CardComponent) e.getSource();
                cc.setFaceUp(false);
            }
        }
    }

    private class SelectedCardsPropertyListener implements PropertyChangeListener
    {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            // Update buttons
            btnMoveCardsDown.getAction().setEnabled(getSelectedCardCount() > 0);
            btnMoveCardsUp.getAction().setEnabled(getSelectedCardCount() > 0);

            // Update status component
            lblSelectedCount.setText(getSelectedCardCount() + "");
        }
    }
    
    /* **********************
     * ACTION CLASSES
     */

    private class MoveCardsDownAction extends AbstractAction
    {
        MoveCardsDownAction() {
            putValue(Action.NAME, "Move down");
            putValue(Action.SHORT_DESCRIPTION, "Move selected cards down");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            moveSelectedDown();
        }
    }

    private class MoveCardsUpAction extends AbstractAction
    {
        MoveCardsUpAction() {
            putValue(Action.NAME, "Move up");
            putValue(Action.SHORT_DESCRIPTION, "Move selected cards up");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            moveSelectedUp();
        }
    }

    private class SortCardsBySuitAction extends AbstractAction
    {
        SortCardsBySuitAction() {
            putValue(Action.NAME, "Sort by Suit");
            putValue(Action.SHORT_DESCRIPTION, "Sort hand cards by Suit");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            sortBySuit();
        }
    }

    private class SortCardsByValueAction extends AbstractAction
    {
        SortCardsByValueAction() {
            putValue(Action.NAME, "Sort by Value");
            putValue(Action.SHORT_DESCRIPTION, "Sort hand cards by Value");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            sortByValue();
        }
    }
    
    public static void main(String[] args) {
        final Card[] cards = new Card[] {
                Card.createCard(Card.Suit.CLUBS, Card.Value.ACE)
              , Card.createCard(Card.Suit.SPADES, Card.Value.SEVEN)
              , Card.createCard(Card.Suit.DIAMONDS, Card.Value.THREE)
              , Card.createCard(Card.Suit.HEARTS, Card.Value.KING)
              , Card.createCard(Card.Suit.CLUBS, Card.Value.TWO)
        };
        
        final HandPanel hp = new HandPanel();
        final JFrame fr = new JFrame("HandPanel Test");

        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    fr.setLayout(new BorderLayout());

                    hp.setCards(cards);

                    fr.add(hp, BorderLayout.CENTER);

                    fr.pack();
                    fr.setVisible(true);
                }
            });
        } catch(InvocationTargetException ite) {
            System.err.println(ite);
        } catch(InterruptedException ie) {
            System.err.println(ie);
        }
    }
}
