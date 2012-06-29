package uk.co.essarsoftware.par.gui.panels;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 15/06/12
 * Time: 00:21
 * To change this template use File | Settings | File Templates.
 */
public class CommandPanel extends JPanel
{
    private ClientActionFactory caf;

    // Swing components
    private JButton btnAcceptBuy, btnBuy, btnDiscard, btnPickupDiscard, btnPickupDraw, btnPegCard, btnPlayCards, btnRejectBuy, btnResetPlays;

    public CommandPanel(ClientActionFactory caf) {
        this.caf = caf;
        setLayout(new BorderLayout());
        
        initComponents();
        drawComponents();
    }

    private void initComponents() {
        Dimension btnSize = new Dimension(150, 75);

        btnAcceptBuy = new JButton(caf.getClientAction("ApproveBuyAction"));
        btnAcceptBuy.setPreferredSize(btnSize);

        btnBuy = new JButton(caf.getClientAction("BuyAction"));
        btnBuy.setPreferredSize(btnSize);

        btnDiscard = new JButton(caf.getClientAction("DiscardAction"));
        btnDiscard.setPreferredSize(btnSize);

        btnPickupDiscard = new JButton(caf.getClientAction("PickupDiscardAction"));
        btnPickupDiscard.setPreferredSize(btnSize);

        btnPickupDraw = new JButton(caf.getClientAction("PickupDrawAction"));
        btnPickupDraw.setPreferredSize(btnSize);

        btnPegCard = new JButton(caf.getClientAction("PegCardAction"));
        btnPegCard.setPreferredSize(btnSize);

        btnPlayCards = new JButton(caf.getClientAction("PlayCardsAction"));
        btnPlayCards.setPreferredSize(btnSize);

        btnRejectBuy = new JButton(caf.getClientAction("RejectBuyAction"));
        btnRejectBuy.setPreferredSize(btnSize);

        btnResetPlays = new JButton(caf.getClientAction("ResetPlaysAction"));
        btnResetPlays.setPreferredSize(btnSize);
    }

    private void drawComponents() {
        // Create buttons
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

    public static interface ClientActionFactory
    {
        public AbstractAction getClientAction(String actionClass);
    }
}
