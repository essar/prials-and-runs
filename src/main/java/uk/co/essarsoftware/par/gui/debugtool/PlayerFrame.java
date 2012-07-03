package uk.co.essarsoftware.par.gui.debugtool;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.client.GameClient;
import uk.co.essarsoftware.par.engine.*;
import uk.co.essarsoftware.par.gui.dialogs.ExceptionDialog;
import uk.co.essarsoftware.par.gui.panels.CommandPanel;
import uk.co.essarsoftware.par.gui.panels.HandPanel;
import uk.co.essarsoftware.par.gui.panels.TablePanel;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;

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

    private Frame main;
    private CommandPanel pnlCmd;
    private HandPanel pnlHand;
    private JLabel lblPlayerName, lblPlayerState, lblRound;

    public PlayerFrame(Frame main, GameClient client) {
        super(client.getPlayerName());
        this.main = main;
        this.client = client;

        initComponents();
        drawComponents();

        pack();
    }

    private void initComponents() {
        pnlHand = new HandPanel();

        pnlCmd = new CommandPanel(this);

        lblPlayerName = new JLabel(client.getPlayerName());
        lblPlayerName.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), BorderFactory.createEmptyBorder(2, 10, 2, 10)));
        lblPlayerName.setPreferredSize(new Dimension(150, 20));

        lblPlayerState = new JLabel(client.getPlayerState().name());
        lblPlayerState.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), BorderFactory.createEmptyBorder(2, 10, 2, 10)));
        lblPlayerState.setPreferredSize(new Dimension(80, 20));

        lblRound = new JLabel(client.getCurrentRound().name());
        lblRound.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), BorderFactory.createEmptyBorder(2, 10, 2, 10)));
        lblRound.setPreferredSize(new Dimension(100, 20));
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

    public PlayerUI generateUI() {
        return new DebugToolUI();
    }


    class DebugToolUI extends SwingUI
    {
        @Override
        public void asyncBuyApproved(Player player, Player buyer, Card card, boolean thisPlayer) {
            // Display info dialog if I am the buyer
        }

        @Override
        public void asyncBuyRejected(Player player, Player buyer, Card card, boolean thisPlayer) {
            // Display info dialog if I am the buyer
        }

        @Override
        public void asyncBuyRequest(Player player, Player buyer, Card card, boolean thisPlayer) {
            // Display decision dialog if I am the player
        }

        @Override
        public void asyncCardDiscarded(Player player, Card card, boolean thisPlayer) {
        }

        @Override
        public void asyncCardPegged(Player player, Play play, Card card, boolean thisPlayer) {
        }

        @Override
        public void asyncCardsPlayed(Player player, Play[] play, boolean thisPlayer) {
        }

        @Override
        public void asyncDiscardPickup(Player player, Card card, boolean thisPlayer) {
        }

        @Override
        public void asyncDrawPickup(Player player, boolean thisPlayer) {
        }

        @Override
        public void asyncPlayerStateChange(Player player, PlayerState oldState, PlayerState newState, boolean thisPlayer) {
            if(thisPlayer) {
                lblPlayerState.setText(newState.name());
            }
        }

        @Override
        public void asyncPlayerOut(Player player, boolean thisPlayer) {
            // Display notification dialog
        }

        @Override
        public void asyncRoundEnded(Round round) {
            // Display notification dialog
        }

        @Override
        public void asyncRoundStarted(Round round) {
            // Set initial player hand
            System.out.println("Starting round ***");
            //pnlHand.reset();
            lblRound.setText(client.getCurrentRound().name());
            pnlHand.setCards(client.getHand());
        }

        @Override
        public void asyncHandleException(EngineException ee) {
            // Display error dialog
            ExceptionDialog ed = new ExceptionDialog(PlayerFrame.this.main, ee);
            ed.setVisible(true);
        }
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
            //TODO calculate/lookup play
            Play play = null;
            Card card = pnlHand.getSelectedCards()[0];
            client.pegCard(play, card);
            // Refresh hand
            pnlHand.setCards(client.getHand());
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
