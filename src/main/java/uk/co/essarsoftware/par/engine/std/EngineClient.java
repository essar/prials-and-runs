/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.par.engine.std;

import org.apache.log4j.Logger;
import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.client.GameClient;
import uk.co.essarsoftware.par.engine.*;
import uk.co.essarsoftware.par.engine.events.*;
import uk.co.essarsoftware.par.engine.scorecard.Scorecard;

import java.util.Arrays;

/**
 * Client implementation that provides methods from the <tt>Engine</tt>, <tt>Game</tt>, and <tt>Player</tt> objects to
 * client classes.
 * @author Steve Roberts <steve.roberts@essarsoftware.co.uk>
 * @version 1.0 (07-Jun-12)
 */
class EngineClient implements GameClient
{
    private static Logger log = Logger.getLogger(EngineClient.class);
    
    private final EngineImpl engine;
    private final GameImpl game;
    private final PlayerImpl player;
    private final PlayerUI ui;

    private final EngineClientAgent agt;
    private final EventQueue queue;

    /**
     * Create a new <tt>EngineClient</tt>
     * @param engine the current <tt>Engine</tt> instance.
     * @param game the current <tt>Game</tt> instance.
     * @param player the <tt>Player</tt> this client relates to.
     * @param ui the <tt>PlayerUI</tt> that this client controls.
     */
    public EngineClient(EngineImpl engine, GameImpl game, PlayerImpl player, PlayerUI ui) {
        this.engine = engine;
        this.game = game;
        this.player = player;
        this.ui = ui;

        agt = new EngineClientAgent();
        queue = new EventQueue();
    }

    /**
     * Get the <tt>EngineAgent</tt> associated with this client.
     * @return an <tt>EngineAgent</tt> instance.
     */
    EngineAgent getAgent() {
        return agt;
    }
    
    GameEvent[] getQueue() {
        return queue.toArray(new GameEvent[queue.size()]);
    }

    /**
     * Add a <tt>GameEvent</tt> to the client's queue.
     * @param evt a <tt>GameEvent</tt> object to queue.
     * @return <tt>true</tt> if the event was queued successfully, false otherwise.
     * @see EventQueue#add(uk.co.essarsoftware.par.engine.events.GameEvent)
     */
    boolean addEvent(GameEvent evt) {
        boolean r = queue.add(evt);
        log.debug(String.format("[%s] Adding %s, %d events on queue.", player, evt, queue.size()));
        if(queue.remainingCapacity() < 5) {
            log.warn(String.format("[%s] Event queue capacity WARNING : %d events on queue, %d remaining.", player, queue.size(), queue.remainingCapacity()));
        }
        return r;
    }

    /**
     * Clear events from the client's queue. Method blocks until the queue is cleared.
     * @see uk.co.essarsoftware.par.engine.std.EventQueue#clear()
     */
    void clearQueue() {
        log.debug(String.format("[%s] Clearing queue (%d events queued)", player, queue.size()));
        queue.clear();
    }

    /**
     *
     */
    void endGame() {

    }
    
    @Override
    public Card approveBuy(Player buyer) {
        if(engine == null || player == null) {
            throw new IllegalStateException("Client has not been initialized with an Engine or Player");
        }
        try {
            log.debug(String.format("[%s] Approving buy from %s", player, buyer));
            return engine.approveBuy(player, buyer);
        } catch(EngineException ee) {
            // Log exception
            log.error(String.format("[%s] Unable to approve buy from %s : %s", player, buyer, ee.getMessage()));
            log.debug(ee.getMessage(), ee);

            // Inform UI of exception
            ui.handleException(ee);

            return null;
        }
    }

    @Override
    public boolean buy(Player currentPlayer, Card card) {
        if(engine == null || player == null) {
            throw new IllegalStateException("Client has not been initialized with an Engine or Player");
        }
        try {
            log.debug(String.format("[%s] Requesting to buy %s from %s", player, card, currentPlayer));
            return engine.buy(currentPlayer, player);
        } catch(EngineException ee) {
            // Log exception
            log.error(String.format("[%s] Unable to buy %s from %s : %s", player, card, currentPlayer, ee.getMessage()));
            log.debug(ee.getMessage(), ee);

            // Inform UI of exception
            ui.handleException(ee);

            return false;
        }
    }

    @Override
    public void discard(Card card) {
        if(engine == null || player == null) {
            throw new IllegalStateException("Client has not been initialized with an Engine or Player");
        }
        try {
            log.debug(String.format("[%s] Discarding %s", player, card));
            engine.discard(player, card);
        } catch(EngineException ee) {
            // Log exception
            log.error(String.format("[%s] Unable to discard %s : %s", player, card, ee.getMessage()));
            log.debug(ee.getMessage(), ee);

            // Inform UI of exception
            ui.handleException(ee);
        }
    }

    @Override
    public void pegCard(Play play, Card card) {
        if(engine == null || player == null) {
            throw new IllegalStateException("Client has not been initialized with an Engine or Player");
        }
        try {
            log.debug(String.format("[%s] Pegging %s onto %s", player, card, play));
            engine.pegCard(player, play, card);
        } catch(EngineException ee) {
            // Log exception
            log.error(String.format("[%s] Unable to peg %s onto %s : %s", player, card, play, ee.getMessage()));
            log.debug(ee.getMessage(), ee);

            // Inform UI of exception
            ui.handleException(ee);
        }
    }

    @Override
    public Card pickupDiscard() {
        if(engine == null || player == null) {
            throw new IllegalStateException("Client has not been initialized with an Engine or Player");
        }
        try {
            log.debug(String.format("[%s] Picking up from discard pile", player));
            return engine.pickupDiscard(player);
        } catch(EngineException ee) {
            // Log exception
            log.error(String.format("[%s] Unable to pickup from discard pile : %s", player, ee.getMessage()));
            log.debug(ee.getMessage(), ee);

            // Inform UI of exception
            ui.handleException(ee);

            return null;
        }
    }

    @Override
    public Card pickupDraw() {
        if(engine == null || player == null) {
            throw new IllegalStateException("Client has not been initialized with an Engine or Player");
        }
        try {
            log.debug(String.format("[%s] Picking up from draw pile", player));
            return engine.pickupDraw(player);
        } catch(EngineException ee) {
            // Log exception
            log.error(String.format("[%s] Unable to pickup from draw pile : %s", player, ee.getMessage()));
            log.debug(ee.getMessage(), ee);

            // Inform UI of exception
            ui.handleException(ee);

            return null;
        }
    }

    @Override
    public void playCards(Card[] cards) {
        if(engine == null || player == null) {
            throw new IllegalStateException("Client has not been initialized with an Engine or Player");
        }
        try {
            log.debug(String.format("[%s] Playing cards %s", player, Arrays.toString(cards)));
            engine.playCards(player, cards);
        } catch(EngineException ee) {
            // Log exception
            log.error(String.format("[%s] Unable to play cards %s : %s", player, Arrays.toString(cards), ee.getMessage()));
            log.debug(ee.getMessage(), ee);

            // Inform UI of exception
            ui.handleException(ee);
        }
    }

    @Override
    public Card rejectBuy(Player buyer) {
        if(engine == null || player == null) {
            throw new IllegalStateException("Client has not been initialized with an Engine or Player");
        }
        try {
            log.debug(String.format("[%s] Rejecting buy from %s", player, buyer));
            return engine.rejectBuy(player, buyer);
        } catch(EngineException ee) {
            // Log exception
            log.error(String.format("[%s] Unable to reject buy from %s : %s", player, buyer, ee.getMessage()));
            log.debug(ee.getMessage(), ee);

            // Inform UI of exception
            ui.handleException(ee);

            return null;
        }
    }

    @Override
    public void resetPlays() {
        if(engine == null || player == null) {
            throw new IllegalStateException("Client has not been initialized with an Engine or Player");
        }
        try {
            log.debug(String.format("[%s] Resetting plays", player));
            engine.resetPlays(player);
        } catch(EngineException ee) {
            // Log exception
            log.error(String.format("[%s] Unable to reset plays : %s", player, ee.getMessage()));
            log.debug(ee.getMessage(), ee);

            // Inform UI of exception
            ui.handleException(ee);
        }
    }

    @Override
    public Card[] getHand() {
        if(player == null) {
            throw new IllegalStateException("Client has not been initialized with a Player");
        }
        return player.getHand();
    }

    @Override
    public Play[] getPlays() {
        if(player == null) {
            throw new IllegalStateException("Client has not been initialized with a Player");
        }
        return getTable().getPlays(player);
    }

    @Override
    public Card getPenaltyCard() {
        if(player == null) {
            throw new IllegalStateException("Client has not been initialized with a Player");
        }
        return player.getPenaltyCard();
    }

    @Override
    public Player getPlayer() {
        if(player == null) {
            throw new IllegalStateException("Client has not been initialized with a Player");
        }
        return player;
    }

    @Override
    public GameController getController() {
        throw new UnsupportedOperationException("Cannot obtain game controller in this way");
    }

    @Override
    public Player getCurrentPlayer() {
        if(game == null) {
            throw new IllegalStateException("Client has not been initialized with a Game");
        }
        return game.getCurrentPlayer();
    }

    @Override
    public Round getCurrentRound() {
        if(game == null) {
            throw new IllegalStateException("Client has not been initialized with a Game");
        }
        return game.getCurrentRound();
    }

    @Override
    public Player getDealer() {
        if(game == null) {
            throw new IllegalStateException("Client has not been initialized with a Game");
        }
        return game.getDealer();
    }

    @Override
    public Class<? extends PlayBuilder> getPlayBuilderClass() {
        if(game == null) {
            throw new IllegalStateException("Client has not been initialized with a Game");
        }
        return game.getPlayBuilderClass();
    }

    @Override
    public Player[] getPlayers() {
        if(game == null) {
            throw new IllegalStateException("Client has not been initialized with a Game");
        }
        return game.getPlayers();
    }

    @Override
    public Scorecard getScorecard() {
        if(game == null) {
            throw new IllegalStateException("Client has not been initialized with a Game");
        }
        return game.getScorecard();
    }

    @Override
    public Table getTable() {
        if(game == null) {
            throw new IllegalStateException("Client has not been initialized with a Game");
        }
        return game.getTable();
    }

    @Override
    public int getTurn() {
        if(game == null) {
            throw new IllegalStateException("Client has not been initialized with a Game");
        }
        return game.getTurn();
    }

    @Override
    public boolean isBuyAllowed() {
        if(game == null) {
            throw new IllegalStateException("Client has not been initialized with a Game");
        }
        return game.isBuyAllowed();
    }

    @Override
    public int getHandSize() {
        if(player == null) {
            throw new IllegalStateException("Client has not been initialized with a Player");
        }
        return player.getHandSize();
    }

    @Override
    public String getPlayerName() {
        if(player == null) {
            throw new IllegalStateException("Client has not been initialized with a Player");
        }
        return player.getPlayerName();
    }

    @Override
    public PlayerState getPlayerState() {
        if(player == null) {
            throw new IllegalStateException("Client has not been initialized with a Player");
        }
        return player.getPlayerState();
    }

    @Override
    public boolean hasPenaltyCard() {
        if(player == null) {
            throw new IllegalStateException("Client has not been initialized with a Player");
        }
        return player.hasPenaltyCard();
    }

    @Override
    public boolean isDown() {
        if(player == null) {
            throw new IllegalStateException("Client has not been initialized with a Player");
        }
        return player.isDown();
    }

    /**
     * Internal client that serially proceses <tt>GameEvent</tt>s stored on the client's queue.
     */
    private class EngineClientAgent extends Thread implements EngineAgent, GameEventProcessor
    {
        private boolean running = true;

        /**
         * Create a new <tt>EngineClientAgent</tt>.
         */
        EngineClientAgent() {
            super("Agt-" + player);
        }

        @Override
        public void processEvent(GameEvent evt) {
            log.debug(String.format("[%s] Processing %s", player, evt.getClass().getName()));
            if(evt instanceof PlayerEvent) {
                PlayerEvent pEvt = (PlayerEvent) evt;
                boolean thisPlayer = player.equals(pEvt.getPlayer());


                if(pEvt instanceof BuyApprovedEvent) {
                    BuyApprovedEvent baEvt = (BuyApprovedEvent) pEvt;
                    ui.buyApproved(baEvt.getPlayer(), baEvt.getBuyer(), baEvt.getCard(), thisPlayer);
                }
                if(pEvt instanceof BuyRejectedEvent) {
                    BuyRejectedEvent brEvt = (BuyRejectedEvent) pEvt;
                    ui.buyRejected(brEvt.getPlayer(), brEvt.getBuyer(), brEvt.getCard(), thisPlayer);
                }
                if(pEvt instanceof BuyRequestEvent) {
                    BuyRequestEvent brEvt = (BuyRequestEvent) pEvt;
                    ui.buyRequest(brEvt.getPlayer(), brEvt.getBuyer(), brEvt.getCard(), thisPlayer);
                }
                if(pEvt instanceof DiscardEvent) {
                    DiscardEvent dEvt = (DiscardEvent) pEvt;
                    ui.cardDiscarded(dEvt.getPlayer(), dEvt.getDiscard(), thisPlayer);
                }
                if(pEvt instanceof PegCardEvent) {
                    PegCardEvent pcEvt = (PegCardEvent) pEvt;
                    ui.cardPegged(pcEvt.getPlayer(), pcEvt.getPlay(), pcEvt.getCard(), thisPlayer);
                }
                if(pEvt instanceof PickupDiscardEvent) {
                    PickupDiscardEvent pdEvt = (PickupDiscardEvent) pEvt;
                    ui.discardPickup(pdEvt.getPlayer(), pdEvt.getPickup(), thisPlayer);
                }
                if(pEvt instanceof PickupDrawEvent) {
                    PickupDrawEvent pdEvt = (PickupDrawEvent) pEvt;
                    ui.drawPickup(pdEvt.getPlayer(), thisPlayer);
                }
                if(pEvt instanceof PlayCardsEvent) {
                    PlayCardsEvent pcEvt = (PlayCardsEvent) pEvt;
                    ui.cardsPlayed(pcEvt.getPlayer(), pcEvt.getPlays(), thisPlayer);
                }
                if(pEvt instanceof PlayerOutEvent) {
                    PlayerOutEvent poEvt = (PlayerOutEvent) pEvt;
                    ui.playerOut(poEvt.getPlayer(), thisPlayer);
                }
                if(pEvt instanceof PlayerStateChangeEvent) {
                    PlayerStateChangeEvent pscEvt = (PlayerStateChangeEvent) pEvt;
                    ui.playerStateChange(pscEvt.getPlayer(), pscEvt.getOldState(), pscEvt.getNewState(), thisPlayer);
                }
                if(pEvt instanceof RoundEndedEvent) {
                    RoundEndedEvent reEvt = (RoundEndedEvent) pEvt;
                    ui.roundEnded(reEvt.getCurrentRound());
                }
                if(pEvt instanceof RoundStartedEvent) {
                    RoundStartedEvent rsEvt = (RoundStartedEvent) pEvt;
                    ui.roundStarted(rsEvt.getCurrentRound());
                }
            }
        }

        @Override
        public void run() {
            log.info(String.format("[%s] agent starting", player));
            while(running) {
                try {
                    GameEvent evt = queue.take();
                    if(evt instanceof PlayerEvent) {
                        processEvent((PlayerEvent) evt);
                    }
                } catch(InterruptedException ie) {
                    running = false;
                    log.debug(String.format("[%s] agent interrupted", player));
                } catch(RuntimeException re) {
                    // Log exception
                    log.error(String.format("[%s] %s while processing game event: %s", player, re.getClass().getName(), re.getMessage()));
                    log.debug(re.getMessage(), re);
                }
            }
            log.info(String.format("[%s] agent stopping", player));
        }

        @Override
        public boolean isRunning() {
            return running;
        }

        @Override
        public void startAgent() {
            running = true;
            start();
        }

        @Override
        public void stopAgent() {
            running = false;
            interrupt();
        }
    }
}
