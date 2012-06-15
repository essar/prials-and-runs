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

    public EngineDemo() {
        GameFactory gf = GameFactory.getInstance(StdGameFactory.class);

        game = gf.createGame();
        engine = gf.createEngine(game);

        DemoClient cli1 = new DemoClient("Player 1");
        DemoClient cli2 = new DemoClient("Player 2");
        DemoClient cli3 = new DemoClient("Player 3");

        try {
            engine.startGame();
            System.out.println("Engine started");
            try {
                Thread.sleep(5000L);
                Card c = null;
                if(game.getCurrentPlayer().equals(cli1.getPlayer())) {
                    c = cli1.pickupDraw();
                } else if(game.getCurrentPlayer().equals(cli2.getPlayer())) {
                    c = cli2.pickupDraw();
                } else if(game.getCurrentPlayer().equals(cli3.getPlayer())) {
                    c = cli3.pickupDraw();
                }
                System.out.println("You picked up " + c);

                Thread.sleep(5000L);

                if(game.getCurrentPlayer().equals(cli1.getPlayer())) {
                    cli1.discard(cli1.getHand()[0]);
                } else if(game.getCurrentPlayer().equals(cli2.getPlayer())) {
                    cli2.discard(cli2.getHand()[0]);
                } else if(game.getCurrentPlayer().equals(cli3.getPlayer())) {
                    cli3.discard(cli3.getHand()[0]);
                }

                Thread.sleep(5000L);

                if(game.getCurrentPlayer().equals(cli1.getPlayer())) {
                    c = cli1.pickupDraw();
                } else if(game.getCurrentPlayer().equals(cli2.getPlayer())) {
                    c = cli2.pickupDraw();
                } else if(game.getCurrentPlayer().equals(cli3.getPlayer())) {
                    c = cli3.pickupDraw();
                }
                System.out.println("You picked up " + c);

                Thread.sleep(5000L);

                if(game.getCurrentPlayer().equals(cli1.getPlayer())) {
                    cli1.discard(cli1.getHand()[0]);
                } else if(game.getCurrentPlayer().equals(cli2.getPlayer())) {
                    cli2.discard(cli2.getHand()[0]);
                } else if(game.getCurrentPlayer().equals(cli3.getPlayer())) {
                    cli3.discard(cli3.getHand()[0]);
                }

                Thread.sleep(5000L);

                if(game.getCurrentPlayer().equals(cli1.getPlayer())) {
                    c = cli1.pickupDraw();
                } else if(game.getCurrentPlayer().equals(cli2.getPlayer())) {
                    c = cli2.pickupDraw();
                } else if(game.getCurrentPlayer().equals(cli3.getPlayer())) {
                    c = cli3.pickupDraw();
                }
                System.out.println("You picked up " + c);

                Thread.sleep(5000L);

                if(game.getCurrentPlayer().equals(cli1.getPlayer())) {
                    cli1.discard(cli1.getHand()[0]);
                } else if(game.getCurrentPlayer().equals(cli2.getPlayer())) {
                    cli2.discard(cli2.getHand()[0]);
                } else if(game.getCurrentPlayer().equals(cli3.getPlayer())) {
                    cli3.discard(cli3.getHand()[0]);
                }

                Thread.sleep(5000L);


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