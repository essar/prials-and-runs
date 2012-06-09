package uk.co.essarsoftware.par.engine.std;

import org.apache.log4j.Logger;
import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.games.cards.CardArray;
import uk.co.essarsoftware.par.engine.*;
import uk.co.essarsoftware.par.engine.events.*;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 18:56
 * To change this template use File | Settings | File Templates.
 */
class EngineImpl implements Engine
{
    private static Logger log = Logger.getLogger(Engine.class);
    private static Logger gameLog = Logger.getLogger("uk.co.essarsoftware.par.gamelog");

    private final EngineClientList clients;
    private final GameImpl game;
    private final InternalEventProcessor iep;

    public EngineImpl(GameImpl game) {
        this.game = game;
        clients = new EngineClientList();
        iep = new InternalEventProcessor();
    }

    private void gameLog(String msg) {
        gameLog.info(String.format("[%s|Turn %d]", game.getCurrentRound(), game.getTurn()) + msg);
    }

    private PlayerImpl getPlayerImpl(Player player) throws EngineException {
        PlayerImpl pl = game.lookupPlayer(player);
        if(pl == null) {
            throw new EngineException("Unknown player");
        }
        return game.lookupPlayer(player);
    }
    
    private void queueEvent(GameEvent evt) {
        if(evt == null) {
            log.warn(String.format("Attempting to queue null event"));
        } else {
            clients.queueEvent(evt);
            iep.queue.add(evt);
        }
    }

    private void setPlayerState(PlayerImpl player, PlayerState playerState) {
        PlayerState oldPlayerState = player.getPlayerState();
        if(oldPlayerState != playerState) {
            synchronized(clients.get(player)) {
                player.setPlayerState(playerState);
                log.debug(String.format("%s changed from %s to %s", player, oldPlayerState, playerState));
                // Notify player clients about state change
                queueEvent(new PlayerStateChangeEvent(player, oldPlayerState, playerState));
            }
        }
    }

    private boolean validatePlayer(PlayerImpl player, boolean currentPlayer, PlayerState... states) throws InvalidPlayerStateException {
        synchronized(clients.get(player)) {
            log.debug(String.format("Validating %s; currentPlayer:=%s, playerState=%s", player, game.getCurrentPlayer(), player.getPlayerState()));
            if(currentPlayer) {
                if(! game.isCurrentPlayer(player)) {
                    log.warn(String.format("%s is not current player", player));
                    throw new InvalidPlayerStateException("Not current player");
                }
                log.debug(String.format("%s is current player", player));
            }
            for(PlayerState ps : states) {
                if(player.getPlayerState() == ps) {
                    return true;
                }
            }
            log.warn(String.format("%s is in %s but was expecting %s", player, player.getPlayerState(), Arrays.toString(states)));
            throw new InvalidPlayerStateException("Player not in correct state", player.getPlayerState(), states);
        }
    }

    void activate(Player player) throws EngineException {
        PlayerImpl pl = getPlayerImpl(player);
        synchronized(clients.get(pl)) {
            log.trace(String.format("Entering activate(%s)", player));
            try {
                // Validate pre-requisites
                PlayerState[] expected = new PlayerState[] {PlayerState.WATCHING, PlayerState.BOUGHT};
                if(validatePlayer(pl, true, expected)) {
                    if(pl.getPlayerState() == PlayerState.WATCHING) {

                        // Set player state
                        setPlayerState(pl, PlayerState.PICKUP);
                    } else if(player.getPlayerState() == PlayerState.BOUGHT) {
                        // Player bought last turn, so will be skipping a turn
                        
                        // Add penalty card to hand
                        pl.pickup(pl.getPenaltyCard());
                        pl.clearPenaltyCard();
    
                        // Queue notifications
                        queueEvent(new PickupDrawEvent(player));
                        gameLog(String.format("%s picked up a penalty card", player));
    
                        // Set player state
                        setPlayerState(pl, PlayerState.END_TURN);
                    } else {
                        log.warn(String.format("%s is in %s but was expecting %s", player, player.getPlayerState(), Arrays.toString(expected)));
                        throw new InvalidPlayerStateException("Unexpected player state", player.getPlayerState(), expected);
                    }
                }
            } catch(RuntimeException re) {
                log.error(String.format("%s during activate method: %s", re.getClass().getName(), re.getMessage()));
                log.debug(re.getMessage(), re);
                throw new EngineException("Unable to activate", re);
            } finally {
                log.trace(String.format("Leaving activate(%s)", player));
            }
        }
    }

    void endGame() throws EngineException {
        // Notify clients of end of game
        clients.endGame();

        // Stop all game processes
        abortGame();

        gameLog("Game ended");
    }
    
    void endTurn(Player player) throws EngineException {
        PlayerImpl pl = getPlayerImpl(player);
        synchronized(clients.get(pl)) {
            log.trace(String.format("Entering endTurn(%s)", player));
            try {
                // Validate pre-requisites
                if(validatePlayer(pl, true, PlayerState.END_TURN)) {
                    // Make sure the event queues for each player are empty
                    clients.clearAll();

                    // Check whether player is out

                    log.debug(String.format("%s down:=%b, handSize=%d", pl, pl.isDown(), pl.getHandSize()));
                    if(pl.isDown() && pl.getHandSize() == 0) {
                        // Player is out
                        setPlayerState(pl, PlayerState.FINISHED);
                    } else {
                        setPlayerState(pl, PlayerState.WATCHING);
                    }
                }
            } catch(RuntimeException re) {
                log.error(String.format("%s during endTurn method: %s", re.getClass().getName(), re.getMessage()));
                log.debug(re.getMessage(), re);
                throw new EngineException("Unable to end turn", re);
            } finally {
                log.trace(String.format("Leaving endTurn(%s)", player));
            }
        }
    }

    @Override
    public void discard(Player player, Card card) throws EngineException {
        PlayerImpl pl = getPlayerImpl(player);
        synchronized(clients.get(pl)) {
            log.trace(String.format("Entering discard(%s, %s)", player, card));
            try {
                // Validate pre-requisites
                if(validatePlayer(pl, true, PlayerState.DISCARD, PlayerState.PLAYED)) {
                    // Remove card from hand
                    pl.discard(card);

                    // Add card to table
                    game.getTable().discard(card);

                    // Queue notifications
                    queueEvent(new DiscardEvent(player, card));
                    gameLog(String.format("%s discarded %s", player, card));

                    // Set player state
                    setPlayerState(pl, PlayerState.END_TURN);
                }
            } catch(RuntimeException re) {
                log.error(String.format("%s during discard method: %s", re.getClass().getName(), re.getMessage()));
                log.debug(re.getMessage(), re);
                throw new EngineException("Unable to discard", re);
            } finally {
                log.trace(String.format("Leaving discard(%s, %s)", player, card));
            }
        }
    }

    @Override
    public Card pickupDiscard(Player player) throws EngineException {
        PlayerImpl pl = getPlayerImpl(player);
        synchronized(clients.get(pl)) {
            log.trace(String.format("Entering pickupDiscard(%s)", player));
            try {
                // Validate pre-requisites
                if(validatePlayer(pl, true, PlayerState.PICKUP)) {
                    // Take card from table
                    Card card = game.getTable().pickupDiscard();

                    // Add card to hand
                    pl.pickup(card);

                    // Queue notifications
                    queueEvent(new PickupDiscardEvent(player, card));
                    gameLog(String.format("%s picked up %s from discard pile", player, card));

                    // Set player state
                    if(pl.isDown()) {
                        setPlayerState(pl, PlayerState.PEGGING);
                    } else {
                        setPlayerState(pl, PlayerState.DISCARD);
                    }

                    return card;
                }
                // Should never reach this point
                return null;
            } catch(RuntimeException re) {
                log.error(String.format("%s during pickupDiscard method: %s", re.getClass().getName(), re.getMessage()));
                log.debug(re.getMessage(), re);
                throw new EngineException("Unable to pickup discard", re);
            } finally {
                log.trace(String.format("Leaving pickupDiscard(%s)", player));
            }
        }
    }

    @Override
    public Card pickupDraw(Player player) throws EngineException {
        PlayerImpl pl = getPlayerImpl(player);
        synchronized(clients.get(pl)) {
            log.trace(String.format("Entering pickupDraw(%s)", player));
            try {
                // Validate pre-requisites
                if(validatePlayer(pl, true, PlayerState.PICKUP)) {
                    // Take card from table
                    Card card = game.getTable().pickupDraw();

                    // Add card to hand
                    pl.pickup(card);

                    // Queue notifications
                    queueEvent(new PickupDrawEvent(player));
                    gameLog(String.format("%s picked up from draw pile", player));

                    // Set player state
                    if(pl.isDown()) {
                        setPlayerState(pl, PlayerState.PEGGING);
                    } else {
                        setPlayerState(pl, PlayerState.DISCARD);
                    }

                    return card;
                }
                return null;
            } catch(RuntimeException re) {
                log.error(String.format("%s during pickupDraw method: %s", re.getClass().getName(), re.getMessage()));
                log.debug(re.getMessage(), re);
                throw new EngineException("Unable to pickup draw", re);
            } finally {
                log.trace(String.format("Leaving pickupDraw(%s)", player));
            }
        }
    }

    @Override
    public void playCards(Player player, Card[] cards) throws EngineException {
        PlayerImpl pl = getPlayerImpl(player);
        synchronized(clients.get(pl)) {
            log.trace(String.format("Entering playCards(%s, %s)", player, Arrays.toString(cards)));
            try {
                // Validate pre-requisites
                if(validatePlayer(pl, true, PlayerState.DISCARD, PlayerState.PLAYING)) {
    
                    if(game.getTable().getSeat(pl).getUninitialisedPlayCount() > 0) {
                        // More plays needed

                        // Set player state
                        setPlayerState(pl, PlayerState.PLAYING);
                    } else {
                        // All plays done, time to discard now
                        Play[] plays = game.getTable().getPlays(pl);
    
                        // Queue notifications
                        queueEvent(new PlayCardsEvent(player, plays));
                        gameLog(String.format("%s played %s", player, Arrays.toString(plays)));

                        // Set player state
                        pl.setDown(true);
                        setPlayerState(pl, PlayerState.PLAYED);
                    }
                }
            } catch(RuntimeException re) {
                log.error(String.format("%s during playCards method: %s", re.getClass().getName(), re.getMessage()));
                log.debug(re.getMessage(), re);
                throw new EngineException("Unable to play cards", re);
            } finally {
                log.trace(String.format("Leaving playCards(%s, %s)", player, Arrays.toString(cards)));
            }
        }
    }

    @Override
    public void pegCard(Player player, Play play, Card card) throws EngineException {
        PlayerImpl pl = getPlayerImpl(player);
        synchronized(clients.get(pl)) {
            log.trace(String.format("Entering pegCard(%s, %s, %s)", player, play, card));
            try {
                // Validate pre-requisites
                if(validatePlayer(pl, true, PlayerState.PEGGING)) {

                    // Queue notifications
                    queueEvent(new PegCardEvent(player, play, card));
                    gameLog(String.format("%s pegged %s onto %s", player, card, play));

                    // Set player state
                    if(pl.getHandSize() == 0) {
                        setPlayerState(pl, PlayerState.END_TURN);
                    } else {
                        setPlayerState(pl, PlayerState.PEGGING);
                    }
                }
            } catch(RuntimeException re) {
                log.error(String.format("%s during pegCard method: %s", re.getClass().getName(), re.getMessage()));
                log.debug(re.getMessage(), re);
                throw new EngineException("Unable to peg card", re);
            } finally {
                log.trace(String.format("Leaving pegCard(%s, %s, %s)", player, play, card));
            }
        }
    }

    @Override
    public void resetPlays(Player player) throws EngineException {
        PlayerImpl pl = getPlayerImpl(player);
        synchronized(clients.get(pl)) {
            log.trace(String.format("Entering resetPlays(%s)", player));
            try {
                // Validate pre-requisites
                if(validatePlayer(pl, true, PlayerState.PLAYING)) {

                    gameLog(String.format("%s reset plays", player));

                    // Set player state
                    setPlayerState(pl, PlayerState.DISCARD);
                }
            } catch(RuntimeException re) {
                log.error(String.format("%s during resetPlays method: %s", re.getClass().getName(), re.getMessage()));
                log.debug(re.getMessage(), re);
                throw new EngineException("Unable to reset plays", re);
            } finally {
                log.trace(String.format("Leaving resetPlays(%s)", player));
            }
        }
    }

    @Override
    public boolean buy(Player player, Player buyer) throws EngineException {
        PlayerImpl pl = getPlayerImpl(player);
        PlayerImpl by = getPlayerImpl(buyer);
        synchronized(clients.get(pl)) {
            synchronized(clients.get(by)) {
                log.trace(String.format("Entering buy(%s, %s)", player, buyer));
                try {
                    // Validate pre-requisites
                    if(validatePlayer(pl, true, PlayerState.PICKUP)) {
                        if(validatePlayer(by, false, PlayerState.WATCHING)) {
                            if(game.isBuyAllowed()) {
                                Card card = game.getTable().getDiscard();

                                // Queue notifications
                                queueEvent(new BuyRequestEvent(player, buyer, card));
                                gameLog(String.format("%s requested to buy %s from %s", buyer, game.getTable().getDiscard(), player));

                                // Set player states
                                setPlayerState(pl, PlayerState.BUY_REQ);
                                setPlayerState(by, PlayerState.BUYING);

                                return true;
                            } else {
                                log.warn(String.format("%s attempting to buy from %s in round %s, turn %d", buyer, player, game.getCurrentRound(), game.getTurn()));
                            }
                        }
                    }
                    return false;
                } catch(RuntimeException re) {
                    log.error(String.format("%s during buy method: %s", re.getClass().getName(), re.getMessage()));
                    log.debug(re.getMessage(), re);
                    throw new EngineException("Unable to buy", re);
                } finally {
                    log.trace(String.format("Leaving buy(%s, %s)", player, buyer));
                }
            }
        }
    }

    @Override
    public Card approveBuy(Player player, Player buyer) throws EngineException {
        PlayerImpl pl = getPlayerImpl(player);
        PlayerImpl by = getPlayerImpl(buyer);
        synchronized(clients.get(pl)) {
            synchronized(clients.get(by)) {
                log.trace(String.format("Entering approveBuy(%s, %s)", player, buyer));
                try {
                    // Validate pre-requisites
                    if(validatePlayer(pl, true, PlayerState.BUY_REQ)) {
                        if(validatePlayer(by, false, PlayerState.BUYING)) {
                            if(game.isBuyAllowed()) {
                                // Take card from table
                                Card bought = game.getTable().pickupDiscard();

                                // Add card to buyer hand
                                by.pickup(bought);

                                // Take penalty card and add to buyer hand
                                by.setPenaltyCard(game.getTable().pickupDraw());

                                // Take card from table
                                Card card = game.getTable().pickupDraw();

                                // Add card to player hand
                                pl.pickup(card);

                                // Queue notifications
                                queueEvent(new BuyApprovedEvent(player, buyer, bought));
                                gameLog(String.format("%s approved buy of %s from %s", player, bought, buyer));

                                // Set player states
                                setPlayerState(by, PlayerState.BOUGHT);
                                if(pl.isDown()) {
                                    setPlayerState(pl, PlayerState.PEGGING);
                                } else {
                                    setPlayerState(pl, PlayerState.DISCARD);
                                }

                                return card;
                            }
                        } else {
                            log.warn(String.format("%s attempting to approve buy from %s in round %s, turn %d", buyer, player, game.getCurrentRound(), game.getTurn()));
                        }
                    }
                    return null;
                } catch(RuntimeException re) {
                    log.error(String.format("%s during approveBuy method: %s", re.getClass().getName(), re.getMessage()));
                    log.debug(re.getMessage(), re);
                    throw new EngineException("Unable to approve buy", re);
                } finally {
                    log.trace(String.format("Leaving approveBuy(%s, %s)", player, buyer));
                }
            }
        }
    }

    @Override
    public Card rejectBuy(Player player, Player buyer) throws EngineException {
        PlayerImpl pl = getPlayerImpl(player);
        PlayerImpl by = getPlayerImpl(buyer);
        synchronized(clients.get(pl)) {
            synchronized(clients.get(by)) {
                log.trace(String.format("Entering rejectBuy(%s, %s)", player, buyer));
                try {
                    // Validate pre-requisites
                    if(validatePlayer(pl, true, PlayerState.BUY_REQ)) {
                        if(validatePlayer(by, false, PlayerState.BUYING)) {
                            if(game.isBuyAllowed()) {
                                // Take card from table
                                Card card = game.getTable().pickupDiscard();

                                // Add card to player hand
                                pl.pickup(card);

                                // Queue notifications
                                queueEvent(new BuyRejectedEvent(player, buyer, card));
                                gameLog(String.format("%s rejected buy of %s from %s", player, card, buyer));

                                // Set player states
                                setPlayerState(by, PlayerState.WATCHING);
                                if(pl.isDown()) {
                                    setPlayerState(pl, PlayerState.PEGGING);
                                } else {
                                    setPlayerState(pl, PlayerState.PLAYING);
                                }

                                return card;
                            } else {
                                log.warn(String.format("%s attempting to reject buy from %s in round %s, turn %d", buyer, player, game.getCurrentRound(), game.getTurn()));
                            }
                        }
                    }
                    return null;
                } catch(RuntimeException re) {
                    log.error(String.format("%s during rejectBuy method: %s", re.getClass().getName(), re.getMessage()));
                    log.debug(re.getMessage(), re);
                    throw new EngineException("Unable to reject buy", re);
                } finally {
                    log.trace(String.format("Leaving rejectBuy(%s, %s)", player, buyer));
                }
            }
        }
    }

    @Override
    public void abortGame() {
        //To change body of implemented methods use File | Settings | File Templates.

        // Stop all client agents
        clients.stopAllAgents();

        // Stop internal processor
        iep.stopProcessor();
    }

    @Override
    public EngineClient createClient(Player player, PlayerUI ui) {

        if(player instanceof PlayerImpl) {
            PlayerImpl pl = (PlayerImpl) player;
            // Create new client
            EngineClient cli = new EngineClient(this, game, pl, ui);
            // Add to client list
            clients.put(pl, cli);

            return cli;
        }
        throw new IllegalArgumentException("Unsupported player class");
    }

    @Override
    public void startGame() throws EngineException {
        // Start internal processor
        iep.startProcessor();

        // Start client agents
        clients.startAllAgents();

        // Start first round
        startRound();
    }

    @Override
    public void startRound() throws EngineException {
        log.trace(String.format("Entering startRound"));

        // Move to next round
        Round round = game.nextRound();
        if(round == Round.END) {
            // End of game
            endGame();
            return;
        }

        // Set up table
        game.getTable().resetTable();

        // Deal cards
        {
            int handIndex = 0;
            PlayerImpl dealer = game.getDealer();
            PlayerImpl player = dealer;
            CardArray[] hands = game.getTable().deal();

            do {
                player = game.getNextPlayer(player);
                log.debug(String.format("Dealing cards for %s", player));
                player.deal(hands[handIndex ++]);

                // Set player states
                player.setDown(false);
                player.setPlayerState(PlayerState.WATCHING);  // Don't use local private method so don't send out events
            } while(! player.equals(dealer) && handIndex < hands.length);

            log.debug(String.format("Cards dealt to %d players", handIndex));
        }

        // Initialise table objects
        game.getTable().initialiseRound(round);

        // Move to next player
        activate(game.nextPlayer());

        log.trace(String.format("Leaving startRound"));
    }

    private class InternalEventProcessor extends Thread implements GameEventProcessor
    {
        private boolean running = true;

        private final EventQueue queue;

        InternalEventProcessor() {
            super("Internal-Event-Processor");
            queue = new EventQueue();
        }

        @Override
        public void processEvent(GameEvent evt) {
            if(evt instanceof PlayerStateChangeEvent) {
                PlayerStateChangeEvent pscEvt = (PlayerStateChangeEvent) evt;

                switch(pscEvt.getNewState()) {
                    case END_TURN:
                        try {
                            // End player turn and activate next player
                            endTurn(pscEvt.getPlayer());
                            activate(game.nextPlayer());
                        } catch(EngineException ee) {
                            log.error(String.format("%s when attempting to complete end turn: %s", ee.getClass().getName(), ee.getMessage()));
                            log.debug(ee.getMessage(), ee);
                        }
                        break;
                    case FINISHED:
                        queueEvent(new PlayerOutEvent(pscEvt.getPlayer()));
                        break;
                }
            }
        }

        @Override
        public void run() {
            log.info(String.format("Internal processor starting"));
            while(running) {
                try {
                    GameEvent evt = queue.take();
                    processEvent(evt);
                } catch(InterruptedException ie) {
                    running = false;
                    log.debug(String.format("Internal processor interrupted"));
                } catch(RuntimeException re) {
                    // Log exception
                    log.error(String.format("%s while processing game event: %s", re.getClass().getName(), re.getMessage()));
                    log.debug(re.getMessage(), re);
                }
            }
            log.info(String.format("Internal processor stopping"));
        }

        public void startProcessor() {
            running = true;
            start();
        }

        public void stopProcessor() {
            running = false;
            interrupt();
        }
    }
}
