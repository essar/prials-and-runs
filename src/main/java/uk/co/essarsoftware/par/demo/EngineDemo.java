/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.par.demo;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.client.DirectClient;
import uk.co.essarsoftware.par.client.GameClient;
import uk.co.essarsoftware.par.engine.*;
import uk.co.essarsoftware.par.engine.std.StdGameFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 09/06/12
 * Time: 21:12
 * To change this template use File | Settings | File Templates.
 */
public class EngineDemo
{
    private Game game;
    private Engine engine;
    
    private Card[] getSortedHand(GameClient cl) {
        List<Card> cards = Arrays.asList(cl.getHand());
        Collections.sort(cards, new Comparator<Card>() {
            @Override
            public int compare(Card c1, Card c2) {
                if(c1 == null || c1.isJoker()) {
                    return -1;
                }
                if(c2 == null || c2.isJoker()) {
                    return 1;
                }
                // Sort by ascending value
                return c1.getValue().compareTo(c2.getValue());
            }
        });
        return cards.toArray(new Card[cards.size()]);
    }

    public EngineDemo() {
        GameFactory gf = GameFactory.getInstance(StdGameFactory.class);

        game = gf.createGame();
        engine = gf.createEngine(game);

        DemoClient cli1 = new DemoClient("Player 1");
        DemoClient cli2 = new DemoClient("Player 2");
        DemoClient cli3 = new DemoClient("Player 3");

        try {
            long pause = 1000L;

            engine.startGame();
            System.out.println("Engine started");
            try {
                Thread.sleep(pause);
                Card c = null;
                if(game.getCurrentPlayer().equals(cli1.getPlayer())) {
                    c = cli1.pickupDraw();
                } else if(game.getCurrentPlayer().equals(cli2.getPlayer())) {
                    c = cli2.pickupDraw();
                } else if(game.getCurrentPlayer().equals(cli3.getPlayer())) {
                    c = cli3.pickupDraw();
                }
                System.out.println("You picked up " + c);

                Thread.sleep(pause);

                if(game.getCurrentPlayer().equals(cli1.getPlayer())) {
                    cli1.discard(cli1.getHand()[0]);
                } else if(game.getCurrentPlayer().equals(cli2.getPlayer())) {
                    cli2.discard(cli2.getHand()[0]);
                } else if(game.getCurrentPlayer().equals(cli3.getPlayer())) {
                    cli3.discard(cli3.getHand()[0]);
                }

                Thread.sleep(pause);

                if(game.getCurrentPlayer().equals(cli1.getPlayer())) {
                    c = cli1.pickupDiscard();
                } else if(game.getCurrentPlayer().equals(cli2.getPlayer())) {
                    c = cli2.pickupDiscard();
                } else if(game.getCurrentPlayer().equals(cli3.getPlayer())) {
                    c = cli3.pickupDiscard();
                }
                System.out.println("You picked up " + c);

                Thread.sleep(pause);

                if(game.getCurrentPlayer().equals(cli1.getPlayer())) {
                    cli1.discard(cli1.getHand()[0]);
                } else if(game.getCurrentPlayer().equals(cli2.getPlayer())) {
                    cli2.discard(cli2.getHand()[0]);
                } else if(game.getCurrentPlayer().equals(cli3.getPlayer())) {
                    cli3.discard(cli3.getHand()[0]);
                }

                Thread.sleep(pause);

                if(game.getCurrentPlayer().equals(cli1.getPlayer())) {
                    c = cli1.pickupDraw();
                } else if(game.getCurrentPlayer().equals(cli2.getPlayer())) {
                    c = cli2.pickupDraw();
                } else if(game.getCurrentPlayer().equals(cli3.getPlayer())) {
                    c = cli3.pickupDraw();
                }
                System.out.println("You picked up " + c);

                Thread.sleep(pause);

                if(game.getCurrentPlayer().equals(cli1.getPlayer())) {
                    Card[] hand = getSortedHand(cli1);
                    Card[] play = new Card[] {
                            hand[0]
                          , hand[1]
                          , hand[2]
                    };
                    cli1.playCards(play);
                    cli1.resetPlays();
                } else if(game.getCurrentPlayer().equals(cli2.getPlayer())) {
                    Card[] hand = getSortedHand(cli2);
                    Card[] play = new Card[] {
                            hand[0]
                          , hand[1]
                          , hand[2]
                    };
                    cli2.playCards(play);
                    cli2.resetPlays();
                } else if(game.getCurrentPlayer().equals(cli3.getPlayer())) {
                    Card[] hand = getSortedHand(cli3);
                    Card[] play = new Card[] {
                            hand[0]
                          , hand[1]
                          , hand[2]
                    };
                    cli3.playCards(play);
                    cli3.resetPlays();
                }

                Thread.sleep(pause);

                if(game.getCurrentPlayer().equals(cli1.getPlayer())) {
                    cli1.discard(cli1.getHand()[0]);
                } else if(game.getCurrentPlayer().equals(cli2.getPlayer())) {
                    cli2.discard(cli2.getHand()[0]);
                } else if(game.getCurrentPlayer().equals(cli3.getPlayer())) {
                    cli3.discard(cli3.getHand()[0]);
                }

                Thread.sleep(pause);


            } catch(InterruptedException ie) {
                //
            }

            engine.abortGame();

        } catch(EngineException ee) {

            engine.abortGame();
        }
    }



    public static void main(String[] args) throws ClassNotFoundException {
        BasicConfigurator.configure();
        Logger.getLogger(Game.class).setLevel(Level.DEBUG);

        new EngineDemo();
    }

    class DemoClient extends DirectClient
    {
        DemoClient(String playerName) {
            super(engine, game.getController(), playerName);
            setUI(new DemoPlayer(this));
        }
    }

    class DemoPlayer implements PlayerUI
    {
        private GameClient client;

        DemoPlayer(GameClient client) {
            this.client = client;
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
            if(thisPlayer) {
                System.out.println("Successfully played!");
            }
        }

        @Override
        public void discardPickup(Player player, Card card, boolean thisPlayer) {
            if(! thisPlayer) {
                System.out.println(String.format("[%s] Ooo... %s picked up %s from the discard pile", client.getPlayerName(), player.getPlayerName(), card));
            }
        }

        @Override
        public void drawPickup(Player player, boolean thisPlayer) {
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
        public void playerStateChange(Player player, PlayerState oldState, PlayerState newState, boolean thisPlayer) {
            if(thisPlayer) {
                System.out.println("I am now in " + newState);

                switch(newState) {
                    case PICKUP:
                        System.out.println("Time to pick up, here is your hand:");
                        System.out.println(Arrays.toString(client.getHand()));
                        break;
                    case DISCARD:
                        System.out.println("Now throw away, here is your hand:");
                        System.out.println(Arrays.toString(client.getHand()));
                        break;
                }
            }
        }

        @Override
        public void playerOut(Player player, boolean thisPlayer) {
            System.out.println(player + " is out!");
        }

        @Override
        public void handleException(EngineException ee) {
            System.err.println("Error: " + ee.getMessage());
        }
    }
}
