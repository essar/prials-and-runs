package uk.co.essarsoftware.par.gui.panels;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.client.DirectClient;
import uk.co.essarsoftware.par.client.GameClient;
import uk.co.essarsoftware.par.engine.*;
import uk.co.essarsoftware.par.engine.std.StdGameFactory;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 15/06/12
 * Time: 00:21
 * To change this template use File | Settings | File Templates.
 */
public class CommandPanel extends JPanel
{
    private final GameClient client;
    private HandPanel pnlHand;
    
    // Swing components
    private JButton btnAcceptBuy, btnBuy, btnDiscard, btnPickupDiscard, btnPickupDraw, btnPegCard, btnPlayCards, btnRejectBuy, btnResetPlays;
    private JLabel lblPlayerName, lblPlayerState;

    public CommandPanel(GameClient client, HandPanel pnlHand) {
        this.client = client;
        this.pnlHand = pnlHand;
        setLayout(new BorderLayout());
        
        initComponents();
        drawComponents();
    }

    private void initComponents() {
        Dimension btnSize = new Dimension(150, 75);

        btnAcceptBuy = new JButton(new AcceptBuyAction());
        btnAcceptBuy.setPreferredSize(btnSize);

        btnBuy = new JButton(new BuyAction());
        btnBuy.setPreferredSize(btnSize);

        btnDiscard = new JButton(new DiscardAction());
        btnDiscard.setPreferredSize(btnSize);

        btnPickupDiscard = new JButton(new PickupDiscardAction());
        btnPickupDiscard.setPreferredSize(btnSize);

        btnPickupDraw = new JButton(new PickupDrawAction());
        btnPickupDraw.setPreferredSize(btnSize);

        btnPegCard = new JButton(new PegCardAction());
        btnPegCard.setPreferredSize(btnSize);

        btnPlayCards = new JButton(new PlayCardsAction());
        btnPlayCards.setPreferredSize(btnSize);

        btnRejectBuy = new JButton(new RejectBuyAction());
        btnRejectBuy.setPreferredSize(btnSize);

        btnResetPlays = new JButton(new ResetPlaysAction());
        btnResetPlays.setPreferredSize(btnSize);


        lblPlayerName = new JLabel(client.getPlayerName());
        lblPlayerName.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), BorderFactory.createEmptyBorder(2, 10, 2, 10)));
        lblPlayerName.setPreferredSize(new Dimension(150, 20));

        lblPlayerState = new JLabel(client.getPlayerState().name());
        lblPlayerState.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), BorderFactory.createEmptyBorder(2, 10, 2, 10)));
        lblPlayerState.setPreferredSize(new Dimension(80, 20));
    }

    private void drawComponents() {

        Box status = Box.createHorizontalBox();
        status.add(lblPlayerName);
        status.add(lblPlayerState);
        status.add(Box.createHorizontalGlue());
        add(status, BorderLayout.SOUTH);

        Container btns = new Container();
        btns.setLayout(new GridLayout(6, 2, 2, 2));

        btns.add(btnPickupDraw);
        btns.add(btnPickupDiscard);
        btns.add(btnDiscard);
        btns.add(btnBuy);
        btns.add(btnAcceptBuy);
        btns.add(btnRejectBuy);
        btns.add(btnPlayCards);
        btns.add(btnPegCard);
        btns.add(btnResetPlays);

        add(btns, BorderLayout.CENTER);
    }

    public void refreshCards() {
        for(Component c : getComponents()) {
            if(c instanceof AbstractButton) {
                AbstractButton btn = (AbstractButton) c;
                btn.setEnabled(btn.getAction().isEnabled());
            }
        }
    }

    public void setHandPanel(HandPanel pnlHand) {
        this.pnlHand = pnlHand;
    }
    
    
    private class AcceptBuyAction extends AbstractAction
    {
        AcceptBuyAction() {
            putValue(Action.NAME, "Accept Buy");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
            putValue(Action.SHORT_DESCRIPTION, "Accept current buy request");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Player buyer = null;
            Card card = client.approveBuy(buyer);
        }

        @Override
        public boolean isEnabled() {
            return client.isBuyAllowed()
                    && client.getPlayerState() == PlayerState.BUY_REQ;
        }
    }

    private class BuyAction extends AbstractAction
    {
        BuyAction() {
            putValue(Action.NAME, "Buy");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_B);
            putValue(Action.SHORT_DESCRIPTION, "Buy the top discarded card");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Card card = client.getTable().getDiscard();
            client.buy(client.getCurrentPlayer(), card);
        }

        @Override
        public boolean isEnabled() {
            return client.isBuyAllowed()
                    && client.getPlayerState() == PlayerState.WATCHING;
        }
    }

    private class DiscardAction extends AbstractAction
    {
        DiscardAction() {
            putValue(Action.NAME, "Discard");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);
            putValue(Action.SHORT_DESCRIPTION, "Discard the current selected card");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Card card = pnlHand.getSelectedCards()[0];
            client.discard(card);
        }

        @Override
        public boolean isEnabled() {
            return pnlHand.getSelectedCardCount() == 1
                    && (client.getPlayerState() == PlayerState.DISCARD
                        || client.getPlayerState() == PlayerState.PEGGING
                        || client.getPlayerState() == PlayerState.PLAYED);
        }
    }

    private class PickupDiscardAction extends AbstractAction
    {
        PickupDiscardAction() {
            putValue(Action.NAME, "Pickup Discard");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_I);
            putValue(Action.SHORT_DESCRIPTION, "Pickup the top discarded card");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Card card = client.pickupDiscard();
        }

        @Override
        public boolean isEnabled() {
            return client.getPlayerState() == PlayerState.PICKUP;
        }
    }

    private class PickupDrawAction extends AbstractAction
    {
        PickupDrawAction() {
            putValue(Action.NAME, "Pickup Draw");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
            putValue(Action.SHORT_DESCRIPTION, "Pickup the top draw card");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Card card = client.pickupDraw();
        }

        @Override
        public boolean isEnabled() {
            return client.getPlayerState() == PlayerState.PICKUP;
        }
    }

    private class PegCardAction extends AbstractAction
    {
        PegCardAction() {
            putValue(Action.NAME, "Peg Card");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);
            putValue(Action.SHORT_DESCRIPTION, "Peg the current selected card");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Play play = null;
            Card card = client.getTable().getDiscard();
            client.pegCard(play, card);
        }

        @Override
        public boolean isEnabled() {
            return client.isDown()
                    && client.getPlayerState() == PlayerState.PEGGING;
        }
    }

    private class PlayCardsAction extends AbstractAction
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
        }

        @Override
        public boolean isEnabled() {
            return pnlHand.getSelectedCardCount() == 1
                    && (client.getPlayerState() == PlayerState.DISCARD
                        || client.getPlayerState() == PlayerState.PLAYING);
        }
    }

    private class RejectBuyAction extends AbstractAction
    {
        RejectBuyAction() {
            putValue(Action.NAME, "Reject Buy");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
            putValue(Action.SHORT_DESCRIPTION, "Reject current buy request");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Player buyer = null;
            Card card = client.rejectBuy(buyer);
        }

        @Override
        public boolean isEnabled() {
            return pnlHand.getSelectedCardCount() == 3
                    && (client.getPlayerState() == PlayerState.DISCARD
                        || client.getPlayerState() == PlayerState.PLAYING);
        }
    }

    private class ResetPlaysAction extends AbstractAction
    {
        ResetPlaysAction() {
            putValue(Action.NAME, "Reset");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
            putValue(Action.SHORT_DESCRIPTION, "Reset current plays");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            client.resetPlays();
        }

        @Override
        public boolean isEnabled() {
            return client.getPlayerState() == PlayerState.PLAYING;
        }
    }
    
    public static void main(String[] args) {
        GameFactory gf = GameFactory.getInstance(StdGameFactory.class);

        Game game = gf.createGame();
        Engine engine = gf.createEngine(game);

        DemoClient cli1 = new DemoClient(engine, game.getController(), "Player 1");

        final CommandPanel cp = new CommandPanel(cli1, new HandPanel());

        final JFrame fr = new JFrame("CommandPanelTest");

        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    fr.setLayout(new BorderLayout());

                    fr.add(cp, BorderLayout.CENTER);

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

    static class DemoClient extends DirectClient implements PlayerUI
    {
        private GameClient client;

        DemoClient(Engine engine, GameController ctl, String playerName) {
            super(engine, ctl, playerName);
            setUI(this);
        }

        @Override
        public void buyApproved(Player player, Player buyer, Card card, boolean thisPlayer) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void buyRejected(Player player, Player buyer, Card card, boolean thisPlayer) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void buyRequest(Player player, Player buyer, Card card, boolean thisPlayer) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void cardDiscarded(Player player, Card card, boolean thisPlayer) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void cardPegged(Player player, Play play, Card card, boolean thisPlayer) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void cardsPlayed(Player player, Play[] play, boolean thisPlayer) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void discardPickup(Player player, Card card, boolean thisPlayer) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void drawPickup(Player player, boolean thisPlayer) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void playerStateChange(Player player, PlayerState oldState, PlayerState newState, boolean thisPlayer) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void playerOut(Player player, boolean thisPlayer) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void roundEnded(Round round) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void roundStarted(Round round) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void handleException(EngineException ee) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
