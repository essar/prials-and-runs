/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.par.gui.dialogs;

import uk.co.essarsoftware.games.cards.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/07/12
 * Time: 09:29
 * To change this template use File | Settings | File Templates.
 */
public class ResolveJokerDialog extends JDialog
{
    private Card[] possibleCards;
    private Card selectedCard;
    private int output = UNDEFINED;

    // Swing components
    private JButton btnCancel, btnOK;
    private JComboBox cmbCards;
    private JLabel lblMessage;

    // Singleton variable for static invocation
    private static ResolveJokerDialog rjd;
    private static final String lock = "LOCK";

    public static final int OK_BUTTON = 1;
    public static final int CANCEL_BUTTON = 2;
    public static final int UNDEFINED = -1;

    public ResolveJokerDialog(Frame parent, Card[] possibleCards) {
        super(parent, "Select Card");
        this.possibleCards = possibleCards;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                synchronized(lock) {
                    lock.notifyAll();
                }
            }
        });

        initComponents();
        drawComponents();

        pack();
    }

    private void initComponents() {
        // Create message
        lblMessage = new JLabel("Please select a card for the joker:");

        // Create drop down box
        cmbCards = new JComboBox(new CardComboBoxModel(possibleCards));

        // Create buttons
        btnOK = new JButton(new OKButtonAction());
        btnOK.setDefaultCapable(true);
        btnOK.setPreferredSize(new Dimension(100, 40));

        btnCancel = new JButton(new CancelButtonAction());
        btnCancel.setPreferredSize(new Dimension(100, 40));
    }

    private void drawComponents() {
        GridBagConstraints con = new GridBagConstraints();
        con.gridx = 0; con.gridy = 0;
        con.gridwidth = 1; con.gridheight = 1;
        con.anchor = GridBagConstraints.NORTH;
        con.fill = GridBagConstraints.HORIZONTAL;
        con.insets = new Insets(5, 5, 5, 5);

        Box btns = Box.createHorizontalBox();
        btns.add(btnOK);
        btns.add(Box.createHorizontalGlue());
        btns.add(btnCancel);

        // Set up content
        Container content = getContentPane();
        content.setLayout(new GridBagLayout());

        // Add message
        add(lblMessage, con);

        // Add drop down
        con.gridy = 1;
        add(cmbCards, con);

        // Add buttons
        con.gridy = 2;
        add(btns, con);

        // Set default button
        getRootPane().setDefaultButton(btnOK);
    }

    public Card getSelectedCard() {
        return selectedCard;
    }

    public static Card showDialog(final Frame parent, final Card[] possibleCards) {
        if(SwingUtilities.isEventDispatchThread()) {
            // Gonna have deadlock here...
            System.out.println("Not executing to prevent deadlock");
        } else {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        synchronized(lock) {
                            rjd = new ResolveJokerDialog(parent, possibleCards);
                            rjd.setVisible(true);
                        }
                    }
                });
            } catch(InvocationTargetException ite) {
                ite.printStackTrace(System.err);
            } catch(InterruptedException ie) {
                // Nothing
            }
            synchronized(lock) {
                while(rjd.output == UNDEFINED && rjd.isVisible()) {
                    try {
                        // Wait until a button has been pressed
                        lock.wait();
                    } catch(InterruptedException ie) {
                        // Nothing
                    }
                }
                if(rjd.output == OK_BUTTON) {
                    return rjd.getSelectedCard();
                }
            }
        }
        return null;
    }

    private class CancelButtonAction extends AbstractAction
    {
        CancelButtonAction() {
            putValue(AbstractAction.NAME, "Cancel");
            putValue(AbstractAction.MNEMONIC_KEY, KeyEvent.VK_C);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            synchronized(lock) {
                selectedCard = null;
                output = CANCEL_BUTTON;
                lock.notifyAll();
            }
            setVisible(false);
            dispose();
        }
    }

    private class OKButtonAction extends AbstractAction
    {
        OKButtonAction() {
            putValue(AbstractAction.NAME, "OK");
            putValue(AbstractAction.MNEMONIC_KEY, KeyEvent.VK_O);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            synchronized(lock) {
                selectedCard = possibleCards[cmbCards.getSelectedIndex()];
                output = OK_BUTTON;
                lock.notifyAll();
            }
            setVisible(false);
            dispose();
        }
    }

    private static class CardComboBoxModel extends DefaultComboBoxModel
    {
        CardComboBoxModel(Card[] cards) {
            setCards(cards);
        }

        public void addCard(Card card) {
            addElement(card.toString());
        }

        public void setCards(Card[] cards) {
            removeAllElements();
            for(Card c : cards) {
                addCard(c);
            }
        }
    }
}
