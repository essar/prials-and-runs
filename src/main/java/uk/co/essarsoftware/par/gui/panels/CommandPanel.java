package uk.co.essarsoftware.par.gui.panels;

import uk.co.essarsoftware.par.client.GameClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
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
    private GameClient client;
    
    // Swing components
    private JButton btnAcceptBuy, btnBuy, btnDiscard, btnPickupDiscard, btnPickupDraw, btnPegCard, btnPlayCards, btnRejectBuy, btnResetPlays;

    public CommandPanel(GameClient client) {
        this.client = client;
        setLayout(new GridBagLayout());
        
        initComponents();
        drawComponents();
    }

    private void initComponents() {
        btnAcceptBuy = new JButton(new AcceptBuyAction());

        btnBuy = new JButton(new BuyAction());
        btnDiscard = new JButton(new DiscardAction());
        btnPickupDiscard = new JButton(new PickupDiscardAction());
        btnPickupDraw = new JButton(new PickupDrawAction());
        btnPegCard = new JButton(new PegCardAction());
        btnPlayCards = new JButton(new PlayCardsAction());
        btnRejectBuy = new JButton(new RejectBuyAction());
        btnResetPlays = new JButton(new ResetPlaysAction());
    }

    private void drawComponents() {
        GridBagConstraints con = new GridBagConstraints();
        con.gridx = 0; con.gridy = 0;
        con.gridwidth = 1; con.gridheight = 1;
        con.fill = GridBagConstraints.BOTH;
        con.anchor = GridBagConstraints.CENTER;
        con.insets = new Insets(2, 2, 2, 2);
        
        con.gridx = 0; con.gridy = 0;
        add(btnPickupDraw, con);
        
        con.gridx = 1; con.gridy = 0;
        add(btnPickupDiscard, con);

        con.gridx = 0; con.gridy = 1;
        con.gridwidth = 2;
        add(btnDiscard, con);
        
        con.gridx = 0; con.gridy = 2;
        add(btnBuy, con);
        
        con.gridx = 0; con.gridy = 3;
        con.gridwidth = 1;
        add(btnAcceptBuy, con);

        con.gridx = 1; con.gridy = 3;
        add(btnRejectBuy, con);
        
        con.gridx = 0; con.gridy = 4;
        con.gridwidth = 1;
        add(btnPlayCards, con);

        con.gridx = 1; con.gridy = 4;
        add(btnPegCard, con);

        con.gridx = 0; con.gridy = 5;
        con.gridwidth = 2;
        add(btnResetPlays, con);
    }
    
    
    private class AcceptBuyAction extends AbstractAction
    {
        AcceptBuyAction() {
            putValue(Action.NAME, "Accept Buy");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    private class BuyAction extends AbstractAction
    {
        BuyAction() {
            putValue(Action.NAME, "Buy");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    private class DiscardAction extends AbstractAction
    {
        DiscardAction() {
            putValue(Action.NAME, "Discard");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    private class PickupDiscardAction extends AbstractAction
    {
        PickupDiscardAction() {
            putValue(Action.NAME, "Pickup Discard");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    private class PickupDrawAction extends AbstractAction
    {
        PickupDrawAction() {
            putValue(Action.NAME, "Pickup Draw");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    private class PegCardAction extends AbstractAction
    {
        PegCardAction() {
            putValue(Action.NAME, "Peg Card");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    private class PlayCardsAction extends AbstractAction
    {
        PlayCardsAction() {
            putValue(Action.NAME, "Play Cards");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    private class RejectBuyAction extends AbstractAction
    {
        RejectBuyAction() {
            putValue(Action.NAME, "Reject Buy");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    private class ResetPlaysAction extends AbstractAction
    {
        ResetPlaysAction() {
            putValue(Action.NAME, "Reset");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }
    
    public static void main(String[] args) {

        final CommandPanel cp = new CommandPanel(null);

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
}
