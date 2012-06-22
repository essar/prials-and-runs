package uk.co.essarsoftware.par.gui.panels;

import uk.co.essarsoftware.games.cards.Card;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 18/06/12
 * Time: 09:05
 * To change this template use File | Settings | File Templates.
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
        cards = new SelectableCardsPanel() {
            @Override
            protected CardComponent createComponent() {
                CardComponent cc = new CardComponent(true, true);
                cc.addActionListener(new HandCardComponentActionListener());
                return cc;
            }
        };
        cards.addMouseWheelListener(new HandPanelMouseAdapter());

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

    private class HandCardComponentActionListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() instanceof CardComponent) {
                CardComponent cc = (CardComponent) e.getSource();
                cc.setSelected(! cc.isSelected());

                // Update buttons
                btnMoveCardsDown.getAction().setEnabled(getSelectedCardCount() > 0);
                btnMoveCardsUp.getAction().setEnabled(getSelectedCardCount() > 0);

                // Update status component
                lblSelectedCount.setText(getSelectedCardCount() + "");
            }
        }
    }

    private class PenaltyCardMouseAdapter extends MouseAdapter
    {
        @Override
         public void mousePressed(MouseEvent e) {
            if(e.getSource() instanceof CardComponent) {
                CardComponent cc = (CardComponent) e.getSource();
                cc.setFaceUp(true);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(e.getSource() instanceof CardComponent) {
                CardComponent cc = (CardComponent) e.getSource();
                cc.setFaceUp(false);
            }
        }
    }
    
    private class HandPanelMouseAdapter implements MouseWheelListener
    {
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if(e.getWheelRotation() > 0) {
                cards.moveSelectedDown();
            }
            if(e.getWheelRotation() < 0) {
                cards.moveSelectedUp();
            }
        }
    }

    private class MoveCardsDownAction extends AbstractAction
    {
        MoveCardsDownAction() {
            putValue(Action.NAME, "Move down");
            putValue(Action.SHORT_DESCRIPTION, "Move selected cards down");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            cards.moveSelectedDown();
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
            cards.moveSelectedUp();
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
            cards.sortBySuit();
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
            cards.sortByValue();
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
