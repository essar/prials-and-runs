package uk.co.essarsoftware.par.gui.debugtool;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.client.GameClient;
import uk.co.essarsoftware.par.engine.Play;
import uk.co.essarsoftware.par.engine.Player;
import uk.co.essarsoftware.par.gui.panels.CommandPanel;
import uk.co.essarsoftware.par.gui.panels.HandPanel;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 28/06/12
 * Time: 18:54
 * To change this template use File | Settings | File Templates.
 */
public class PlayerFrame extends JInternalFrame implements CommandPanel.ClientActionFactory
{
    // Player client
    private GameClient client;
    private Player buyer;

    CommandPanel pnlCmd;
    HandPanel pnlHand;
    JLabel lblPlayerName, lblPlayerState, lblRound;

    public PlayerFrame(GameClient client) {
        super(client.getPlayerName(), true);
        this.client = client;

        initComponents();
        drawComponents();

        pack();
    }

    private void initComponents() {
        pnlHand = new HandPanel();
        pnlHand.addPropertyChangeListener("selected", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                // Update buttons when a card is selected
                refreshButtons();
            }
        });

        pnlCmd = new CommandPanel(this);

        lblPlayerName = new JLabel(client.getPlayerName());
        lblPlayerName.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), BorderFactory.createEmptyBorder(2, 10, 2, 10)));
        lblPlayerName.setPreferredSize(new Dimension(150, 20));

        lblPlayerState = new JLabel(client.getPlayerState().name());
        lblPlayerState.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), BorderFactory.createEmptyBorder(2, 10, 2, 10)));
        lblPlayerState.setPreferredSize(new Dimension(100, 20));

        lblRound = new JLabel(client.getCurrentRound().name());
        lblRound.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), BorderFactory.createEmptyBorder(2, 10, 2, 10)));
        lblRound.setPreferredSize(new Dimension(50, 20));
    }

    private void drawComponents() {
        // Configure content
        Container content = new Container();
        content.setLayout(new BorderLayout());

        // Create status bar
        Box status = Box.createHorizontalBox();
        status.add(lblPlayerName);
        status.add(lblPlayerState);
        status.add(lblRound);
        status.add(Box.createHorizontalGlue());
        content.add(status, BorderLayout.SOUTH);

        // Add command panel
        content.add(pnlCmd, BorderLayout.EAST);

        // Add hand panel
        content.add(pnlHand, BorderLayout.CENTER);

        setContentPane(content);
    }

    void refreshButtons() {
        pnlCmd.refreshButtons(client, pnlHand.getSelectedCardCount());
    }

    @Override
    public AbstractAction getClientAction(String actionClass) {
        Class<PlayerFrame> thisClz = PlayerFrame.class;

        try {
            for(Class<?> innerClz : thisClz.getDeclaredClasses()) {
                if(innerClz.getSimpleName().equals(actionClass)) {
                    // Cast to AbstractAction
                    Class<? extends AbstractAction> actionClz = innerClz.asSubclass(AbstractAction.class);
                    // Lookup default constructor
                    return actionClz.getDeclaredConstructor(new Class[] { thisClz }).newInstance(this);
                }
            }
            System.out.println(actionClass + " not found");
        } catch(ClassCastException cce) {
            System.err.println(cce);
        } catch(IllegalAccessException iae) {
            System.err.println(iae);
        } catch(InstantiationException ie) {
            System.err.println(ie);
        } catch(InvocationTargetException ite) {
            System.err.println(ite);
        } catch(NoSuchMethodException nsme) {
            System.err.println(nsme);
        }
        return null;
    }

    /* ***********************
     * CLIENT ACTION CLASSES
     */
    public class ApproveBuyAction extends AbstractAction
    {
        ApproveBuyAction() {
            putValue(Action.NAME, "Accept Buy");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
            putValue(Action.SHORT_DESCRIPTION, "Accept current buy request");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(buyer != null) {
                client.approveBuy(buyer);
                // Refresh hand
                pnlHand.setCards(client.getHand());
            }
            buyer = null;
        }
    }

    public class BuyAction extends AbstractAction
    {
        BuyAction() {
            putValue(Action.NAME, "Buy");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_B);
            putValue(Action.SHORT_DESCRIPTION, "Buy the top discarded card");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Player currentPlayer = client.getCurrentPlayer();
            Card discard = client.getTable().getDiscard();
            client.buy(currentPlayer, discard);
        }
    }

    public class DiscardAction extends AbstractAction
    {
        DiscardAction() {
            putValue(Action.NAME, "Discard");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);
            putValue(Action.SHORT_DESCRIPTION, "Discard the current selected card");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Card discard = pnlHand.getSelectedCards()[0];
            client.discard(discard);
            // Refresh hand
            pnlHand.setCards(client.getHand());
        }
    }


    public class PegCardAction extends AbstractAction
    {
        PegCardAction() {
            putValue(Action.NAME, "Peg Card");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);
            putValue(Action.SHORT_DESCRIPTION, "Peg the current selected card");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Card card = pnlHand.getSelectedCards()[0];

            if(card.isJoker()) {
                // Urgh! need to resolve joker
                // TODO Resolve joker

                ArrayList<Card> allowableCards = new ArrayList<Card>();
                for(Play p : client.getPlays()) {
                    // Look through allowable cards for our card
                    for(Card c : p.getAllowableCards()) {
                        allowableCards.add(c);
                    }
                }
            }
            ArrayList<Play> peggablePlays = new ArrayList<Play>();
            for(Play p : client.getTable().getPlays()) {
                if(p != null && p.isInitialised()) {
                    // Look through allowable cards for our card
                    for(Card c : p.getAllowableCards()) {
                        if(c.sameCard(card)) {
                            peggablePlays.add(p);
                        }
                    }
                }
            }

            if(peggablePlays.size() == 0) {
                // No plays available for card to be pegged to
                System.out.println("No peggable plays found");
            } else {
                Play play;
                if(peggablePlays.size() == 1) {
                    // Peg to the play
                    play = peggablePlays.get(0);
                } else {
                    // Ask player which play to use
                    play = peggablePlays.get(0);
                }

                client.pegCard(play, card);
                // Refresh hand
                pnlHand.setCards(client.getHand());
            }
        }
    }

    public class PickupDiscardAction extends AbstractAction
    {
        PickupDiscardAction() {
            putValue(Action.NAME, "Pickup Discard");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_I);
            putValue(Action.SHORT_DESCRIPTION, "Pickup the top discarded card");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            client.pickupDiscard();
            // Refresh hand
            pnlHand.setCards(client.getHand());
        }
    }

    public class PickupDrawAction extends AbstractAction
    {
        PickupDrawAction() {
            putValue(Action.NAME, "Pickup Draw");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
            putValue(Action.SHORT_DESCRIPTION, "Pickup the top draw card");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            client.pickupDraw();
            // Refresh hand
            pnlHand.setCards(client.getHand());
        }
    }

    public class PlayCardsAction extends AbstractAction
    {
        PlayCardsAction() {
            putValue(Action.NAME, "Play Cards");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_L);
            putValue(Action.SHORT_DESCRIPTION, "Play the current selected cards");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Card[] cards = pnlHand.getSelectedCards();
            client.playCards(cards);
            // Refresh hand
            pnlHand.setCards(client.getHand());
        }
    }

    public class RejectBuyAction extends AbstractAction
    {
        RejectBuyAction() {
            putValue(Action.NAME, "Reject Buy");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
            putValue(Action.SHORT_DESCRIPTION, "Reject current buy request");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(buyer != null) {
                client.rejectBuy(buyer);
                // Refresh hand
                pnlHand.setCards(client.getHand());
            }
            buyer = null;
        }
    }

    public class ResetPlaysAction extends AbstractAction
    {
        ResetPlaysAction() {
            putValue(Action.NAME, "Reset");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
            putValue(Action.SHORT_DESCRIPTION, "Reset current plays");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            client.resetPlays();
            // Refresh hand
            pnlHand.setCards(client.getHand());
        }
    }
}
