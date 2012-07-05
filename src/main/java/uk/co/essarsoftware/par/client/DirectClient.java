package uk.co.essarsoftware.par.client;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.engine.*;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 09/06/12
 * Time: 21:56
 * To change this template use File | Settings | File Templates.
 */
public class DirectClient extends DefaultClient implements PlayerUI
{
    private PlayerUI ui;

    private ArrayBlockingQueue<ClientAction> actionQueue;

    public DirectClient(Engine engine, GameController ctl, String playerName) {
        this.client = engine.createClient(ctl.createPlayer(playerName), this);
        actionQueue = new ArrayBlockingQueue<ClientAction>(25);

        new ClientActionThread().start();
    }

    public void executeAction(ClientAction action) {
        actionQueue.add(action);
    }

    public void setUI(PlayerUI ui) {
        this.ui = ui;
    }

    @Override
    public void buyApproved(Player player, Player buyer, Card card, boolean thisPlayer) {
        if(ui == null) {
            throw new IllegalStateException("Client not yet initialized with a child UI");
        }
        ui.buyApproved(player, buyer, card, thisPlayer);
    }

    @Override
    public void buyRejected(Player player, Player buyer, Card card, boolean thisPlayer) {
        if(ui == null) {
            throw new IllegalStateException("Client not yet initialized with a child UI");
        }
        ui.buyRejected(player, buyer, card, thisPlayer);
    }

    @Override
    public void buyRequest(Player player, Player buyer, Card card, boolean thisPlayer) {
        if(ui == null) {
            throw new IllegalStateException("Client not yet initialized with a child UI");
        }
        ui.buyRequest(player, buyer, card, thisPlayer);
    }

    @Override
    public void cardDiscarded(Player player, Card card, boolean thisPlayer) {
        if(ui == null) {
            throw new IllegalStateException("Client not yet initialized with a child UI");
        }
        ui.cardDiscarded(player, card, thisPlayer);
    }

    @Override
    public void cardPegged(Player player, Play play, Card card, boolean thisPlayer) {
        if(ui == null) {
            throw new IllegalStateException("Client not yet initialized with a child UI");
        }
        ui.cardPegged(player, play, card, thisPlayer);
    }

    @Override
    public void cardsPlayed(Player player, Play[] play, boolean thisPlayer) {
        if(ui == null) {
            throw new IllegalStateException("Client not yet initialized with a child UI");
        }
        ui.cardsPlayed(player, play, thisPlayer);
    }

    @Override
    public void discardPickup(Player player, Card card, boolean thisPlayer) {
        if(ui == null) {
            throw new IllegalStateException("Client not yet initialized with a child UI");
        }
        ui.discardPickup(player, card, thisPlayer);
    }

    @Override
    public void drawPickup(Player player, boolean thisPlayer) {
        if(ui == null) {
            throw new IllegalStateException("Client not yet initialized with a child UI");
        }
        ui.drawPickup(player, thisPlayer);
    }

    @Override
    public void playerStateChange(Player player, PlayerState oldState, PlayerState newState, boolean thisPlayer) {
        if(ui == null) {
            throw new IllegalStateException("Client not yet initialized with a child UI");
        }
        ui.playerStateChange(player, oldState, newState, thisPlayer);
    }

    @Override
    public void playerOut(Player player, boolean thisPlayer) {
        if(ui == null) {
            throw new IllegalStateException("Client not yet initialized with a child UI");
        }
        ui.playerOut(player, thisPlayer);
    }

    @Override
    public void roundEnded(Round round) {
        if(ui == null) {
            throw new IllegalStateException("Client not yet initialized with a child UI");
        }
        ui.roundEnded(round);
    }

    @Override
    public void roundStarted(Round round) {
        if(ui == null) {
            throw new IllegalStateException("Client not yet initialized with a child UI");
        }
        ui.roundStarted(round);
    }

    @Override
    public void handleException(EngineException ee) {
        if(ui == null) {
            throw new IllegalStateException("Client not yet initialized with a child UI");
        }
        ui.handleException(ee);
    }

    class ClientActionThread extends Thread
    {
        private boolean running = true;

        public ClientActionThread() {
            super("Client-" + client.getPlayerName());
        }

        public void run() {
            while(running) {
                try {
                    ClientAction r = actionQueue.take();
                    r.run(DirectClient.this);
                } catch(InterruptedException ie) {
                    running = false;
                }
            }
        }

        public void stopThread() {
            running = false;
            interrupt();
        }
    }
}
