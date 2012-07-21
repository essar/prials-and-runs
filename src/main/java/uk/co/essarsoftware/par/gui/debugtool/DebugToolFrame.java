/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.par.gui.debugtool;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.client.DirectClient;
import uk.co.essarsoftware.par.client.GameClient;
import uk.co.essarsoftware.par.engine.*;
import uk.co.essarsoftware.par.engine.std.StdGameFactory;
import uk.co.essarsoftware.par.gui.dialogs.ExceptionDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 28/06/12
 * Time: 20:10
 * To change this template use File | Settings | File Templates.
 */
public class DebugToolFrame extends JFrame
{
    private Game game;
    private int winX, winY;

    private JDesktopPane desktopPane;
    private HashMap<GameClient, PlayerFrame> players;
    private LogFrame frLogs;
    private TableFrame frTable;

    public DebugToolFrame(Game game) {
        super("Prials and Runs");
        this.game = game;
        setPreferredSize(new Dimension(1024, 768));

        winX = 0;
        winY = 128;
        players = new HashMap<GameClient, PlayerFrame>();

        initComponents();
        drawComponents();

        pack();
    }

    private void initComponents() {
        // Create desktop pane
        desktopPane = new JDesktopPane();

        // Create table
        frTable = new TableFrame(game.getTable());
        frTable.setLocation(800, 0);
        frTable.setVisible(true);

        // Create log frame
        frLogs = new LogFrame();
        frLogs.setLocation(0, 438);
        frLogs.setVisible(true);
    }

    private void drawComponents() {
        desktopPane.add(frTable);
        desktopPane.add(frLogs);

        setContentPane(desktopPane);
    }

    public void addClient(DirectClient client) {
        PlayerFrame pf1 = new PlayerFrame(client);
        players.put(client, pf1);
        client.setUI(new DebugToolUI(client));
        pf1.setLocation(winX, winY);
        Dimension d = pf1.getSize();
        pf1.setSize(d.width + 40, d.height);

        winX += 32;
        winY -= 32;
        pf1.setVisible(true);
        desktopPane.add(pf1);

        // Add to table
        frTable.addPlayer(client.getPlayer());
    }

    public void addLog(String logName, Logger log) {
        frLogs.addLog(logName, log);
    }

    class DebugToolUI extends SwingUI
    {
        private GameClient client;

        DebugToolUI(GameClient client) {
            this.client = client;
        }

        @Override
        public void asyncBuyApproved(Player player, Player buyer, Card card, boolean thisPlayer) {
            // Display info dialog if I am the buyer
            // Only respond to this player events to prevent every player updating the same table elements
            if(thisPlayer) {
                frTable.refresh();
            }
        }

        @Override
        public void asyncBuyRejected(Player player, Player buyer, Card card, boolean thisPlayer) {
            // Display info dialog if I am the buyer
            // Only respond to this player events to prevent every player updating the same table elements
            if(thisPlayer) {
                frTable.refresh();
            }
        }

        @Override
        public void asyncBuyRequest(Player player, Player buyer, Card card, boolean thisPlayer) {
            // Display decision dialog if I am the player
        }

        @Override
        public void asyncCardDiscarded(Player player, Card card, boolean thisPlayer) {
            // Only respond to this player events to prevent every player updating the same table elements
            if(thisPlayer) {
                frTable.refresh();
            }
        }

        @Override
        public void asyncCardPegged(Player player, Play play, Card card, boolean thisPlayer) {
            // Only respond to this player events to prevent every player updating the same table elements
            if(thisPlayer) {
                frTable.refresh(client.getPlayers());
            }
        }

        @Override
        public void asyncCardsPlayed(Player player, Play[] play, boolean thisPlayer) {
            // Only respond to this player events to prevent every player updating the same table elements
            if(thisPlayer) {
                frTable.refresh(player);
            }
        }

        @Override
        public void asyncDiscardPickup(Player player, Card card, boolean thisPlayer) {
            // Only respond to this player events to prevent every player updating the same table elements
            if(thisPlayer) {
                frTable.refresh();
            }
        }

        @Override
        public void asyncDrawPickup(Player player, boolean thisPlayer) {
        }

        @Override
        public void asyncPlayerStateChange(Player player, PlayerState oldState, PlayerState newState, boolean thisPlayer) {
            PlayerFrame pf = players.get(client);
            if(thisPlayer) {
                if(pf != null) {
                    pf.lblPlayerState.setText(newState.name());
                    pf.refreshButtons();

                    // Bring player frame to front
                    if(newState == PlayerState.PICKUP) {
                        try {
                            pf.setSelected(true);
                        } catch(PropertyVetoException pve) {
                            // TODO Handle this exception
                            pve.printStackTrace(System.err);
                        }
                    }
                }
            }
        }

        @Override
        public void asyncPlayerOut(Player player, boolean thisPlayer) {
            // Only respond to this player events to prevent every player executing
            // Display notification dialog
            if(thisPlayer) {
                JOptionPane.showMessageDialog(DebugToolFrame.this, "Round won by " + player.getPlayerName(), "Round Finished", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        @Override
        public void asyncRoundEnded(Round round) {
            // Display notification dialog

            // End of round dialog
            if(JOptionPane.showConfirmDialog(DebugToolFrame.this, "Begin next round?", "Prials and Runs", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                // Start next round in utility thread
            }
        }

        @Override
        public void asyncRoundStarted(Round round) {
            PlayerFrame pf = players.get(client);
            // Set initial player hand
            //pnlHand.reset();
            if(pf != null) {
                pf.lblRound.setText(client.getCurrentRound().name());
                pf.pnlHand.setCards(client.getHand());
                pf.refreshButtons();
            }
            // Only respond to this player events to prevent every player updating the same table elements
            if(client.getPlayer().equals(client.getCurrentPlayer())) {
                frTable.refresh();
                frTable.pnlTable.initRound(round);
                frTable.pack();
            }
        }

        @Override
        public void asyncHandleException(EngineException ee) {
            // Display error dialog
            ExceptionDialog ed = new ExceptionDialog(DebugToolFrame.this, ee);
            ed.setVisible(true);
        }
    }

    public static void main(String[] args) {
        BasicConfigurator.configure();

        // Init game
        GameFactory gf = GameFactory.getInstance(StdGameFactory.class);

        final Game game = gf.createGame();
        final Engine engine = gf.createEngine(game);

        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    DebugToolFrame dtf = new DebugToolFrame(game);
                    dtf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

                    dtf.addClient(new DirectClient(engine, game.getController(), "Player 1"));
                    dtf.addClient(new DirectClient(engine, game.getController(), "Player 2"));
                    dtf.addClient(new DirectClient(engine, game.getController(), "Player 3"));

                    dtf.addLog("Root", Logger.getRootLogger());

                    dtf.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            engine.abortGame();
                        }
                    });

                    dtf.setVisible(true);
                }
            });
        } catch(InterruptedException ie) {
            ie.printStackTrace(System.err);
        } catch(InvocationTargetException ite) {
            ite.printStackTrace(System.err);
        }

        try {
            engine.startGame();
            System.out.println("Engine started");
        } catch(EngineException ee) {
            System.err.println(ee);
        }
    }
}
