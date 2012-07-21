/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.par.gui.panels;

import uk.co.essarsoftware.par.client.GameClient;
import uk.co.essarsoftware.par.engine.PlayerState;

import javax.swing.*;
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
        Dimension btnSize = new Dimension(150, 30);

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

    public void refreshButtons(GameClient client , int selectedCardCount) {
        System.out.println("Refreshing buttons");
        btnAcceptBuy.getAction().setEnabled(client.getPlayerState() == PlayerState.BUY_REQ);
        btnBuy.getAction().setEnabled(client.isBuyAllowed() && client.getPlayerState() == PlayerState.WATCHING);
        btnDiscard.getAction().setEnabled((client.getPlayerState() == PlayerState.DISCARD || client.getPlayerState() == PlayerState.PEGGING || client.getPlayerState() == PlayerState.PLAYED) && selectedCardCount == 1);
        btnPegCard.getAction().setEnabled(client.getPlayerState() == PlayerState.PEGGING && selectedCardCount == 1);
        btnPickupDiscard.getAction().setEnabled(client.getPlayerState() == PlayerState.PICKUP);
        btnPickupDraw.getAction().setEnabled(client.getPlayerState() == PlayerState.PICKUP);
        btnPlayCards.getAction().setEnabled((client.getPlayerState() == PlayerState.DISCARD || client.getPlayerState() == PlayerState.PLAYING) && selectedCardCount == 3);
        btnRejectBuy.getAction().setEnabled(client.getPlayerState() == PlayerState.BUY_REQ);
        btnResetPlays.getAction().setEnabled(client.getPlayerState() == PlayerState.PLAYING);
    }

    public static interface ClientActionFactory
    {
        public AbstractAction getClientAction(String actionClass);
    }
}
