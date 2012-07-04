package uk.co.essarsoftware.par.engine.std;

import org.apache.log4j.Logger;
import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.games.cards.CardArray;
import uk.co.essarsoftware.par.engine.*;
import uk.co.essarsoftware.par.engine.events.*;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

/**
 * Engine implementation.
 * @author Steve Roberts <steve.roberts@essarsoftware.co.uk>
 * @version 1.0 (07-Jun-12)
 */
class EngineImpl implements Engine
{
    // Game and engine logs
    private static Logger log = Logger.getLogger(Engine.class);
    private static Logger gameLog = Logger.getLogger("uk.co.essarsoftware.par.gamelog");

    // Internal engine elements
    private final EngineClientList clients;
    private final GameImpl game;
    private final InternalEventProcessor iep;

    /**
     * Create a new Engine for the specified game.
     * @param game a <tt>GameImpl</tt> instance that this Engine will be running. An IllegalArgumentException will be thrown if this is <tt>null</tt>.
     */
    public EngineImpl(GameImpl game) {
        // Validate arguments
        if(game == null) {
            throw new IllegalArgumentException("Cannot create engine on null game");
        }
        this.game = game;
        clients = new EngineClientList();
        iep = new InternalEventProcessor();
    }
    
    private void dumpToLog() {
        if(! log.isDebugEnabled()) {
            log.info(String.format("Engine dump requested"));
        } else {
            long start = System.currentTimeMillis();
            
            log.debug(String.format("====== ENGINE DUMP ======"));
            log.debug(String.format("%s", DateFormat.getDateTimeInstance().format(new Date(start))));
            log.debug(String.format("-------------------------"));
            log.debug(String.format("  GAME"));
            log.debug(String.format("-------------------------"));
            log.debug(String.format("Current round:  %s", game.getCurrentRound()));
            log.debug(String.format("Current turn:   %d", game.getTurn()));
            log.debug(String.format("Current player: %s", game.getCurrentPlayer()));
            log.debug(String.format("Next player:    %s", game.getNextPlayer(game.getCurrentPlayer())));
            log.debug(String.format("Dealer:         %s", game.getDealer()));

            log.debug(String.format("-------------------------"));
            log.debug(String.format("  TABLE"));
            log.debug(String.format("-------------------------"));
            log.debug(String.format("Discard: %s", game.getTable().getDiscard()));
            for(PlayerImpl pl : game.getPlayers()) {
                log.debug(String.format("[%s]", pl));
                log.debug(String.format("  Seat:    %s", game.getTable().getSeat(pl)));
            }

            log.debug(String.format("-------------------------"));
            log.debug(String.format("  PLAYERS"));
            log.debug(String.format("-------------------------"));
            for(PlayerImpl pl : clients.keySet()) {
                log.debug(String.format("Player name:  %s", pl.getPlayerName()));
                log.debug(String.format("Player state: %s", pl.getPlayerState()));
                log.debug(String.format("Is down:      %s", pl.isDown()));
                log.debug(String.format("Hand:         (%d) %s", pl.getHandSize(), Arrays.toString(pl.getHand())));
                log.debug(String.format("Penalty card: %s", pl.getPenaltyCard()));

                EngineClient ec = clients.get(pl);
                log.debug(String.format("[Client]"));
                log.debug(String.format("  Running: %b", ec.getAgent().isRunning()));
                for(GameEvent evt : ec.getQueue()) {
                    log.debug(String.format("         : %s", evt));
                }
                log.debug(String.format("-------------------------"));
            }
            
            log.debug(String.format("Log dump completed in %.3f seconds", ((double) (System.currentTimeMillis() - start) / 1000.0)));
            log.debug(String.format("==== END ENGINE DUMP ===="));
        }
    }

    /**
     * Write a message to the game log, for audit and recording purposes.
     * @param msg the message to record.
     */
    private void gameLog(String msg) {
        gameLog.info(String.format("[%s|Turn %d] %s", game.getCurrentRound(), game.getTurn(), msg));
    }

    /**
     * Retrieve a <tt>PlayerImpl</tt> that relates to the given Player object.  Ensures that only known players are used.
     * @param player a reference to the player required.
     * @return the specified player, as a <tt>PlayerImpl</tt> instance.
     * @throws EngineException if the player specified is not part of this game.
     */
    private PlayerImpl getPlayerImpl(Player player) throws EngineException {
        PlayerImpl pl = game.lookupPlayer(player);
        if(pl == null) {
            throw new EngineException("Unknown player");
        }
        return game.lookupPlayer(player);
    }

    /**
     * Add a <tt>GameEvent</tt> to all of the player event queues.
     * @param evt the <tt>GameEvent</tt> to queue.
     */
    private void queueEvent(GameEvent evt) {
        if(evt == null) {
            log.warn(String.format("Attempting to queue null event"));
        } else {
            clients.queueEvent(evt);
            iep.queue.add(evt);
        }
    }

    /**
     * Update a player's state and notify all clients of the change.
     * @param player the player whose state to change.
     * @param playerState the new state.
     */
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

    /**
     * Validate the specified player against given criteria.
     * @param player the player to validate.
     * @param currentPlayer check whether player is the current player?
     * @param states check whether the player is in one of the specified states.
     * @return <tt>true</tt> if the player meets all criteria.
     * @throws InvalidPlayerStateException if the player does not meet all specified criteria.
     */
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

    /**
     * Activates a player for the start of their turn. Moves a player into the PICKUP state unless they bought, in which
     * case their penalty card is added to their hand and turn ended.
     * @param player the player to activate.
     * @throws EngineException if a problem occurs during the activation process.
     */
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

    /**
     * Process the end of game event, notifying all clients and stopping engine processes.
     * @throws EngineException if a problem occurs during the end game process.
     */
    void endGame() throws EngineException {

        if(game.getCurrentRound() != Round.END) {
            log.warn(String.format("Cannot end game in %s state", game.getCurrentRound()));
            throw new EngineException("Game not finished");
        } else {
            // Notify clients of end of game
            clients.endGame();

            // Stop all game processes
            abortGame();

            gameLog("Game ended");
        }
    }

    /**
     * End a players turn. Ensures that all players have processed all queued events
     * @param player the player whose turn to end.
     * @throws EngineException if a problem occurs during the end turn process.
     */
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
                if(pl.getPlayerState() == PlayerState.PLAYING) {
                    // Reset plays
                    resetPlays(player);
                }
                // Validate pre-requisites
                if(validatePlayer(pl, true, PlayerState.DISCARD, PlayerState.PEGGING, PlayerState.PLAYED)) {
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
                if(game.getTurn() <= 1) {
                    throw new EngineException("Cannot play cards on first turn");
                }
                if(validatePlayer(pl, true, PlayerState.DISCARD, PlayerState.PLAYING)) {
                    // Update player state
                    setPlayerState(pl, PlayerState.PLAYING);

                    // Get available plays from table
                    TableSeat seat = game.getTable().getSeat(pl);
                    {
                        log.debug(String.format("%d plays found, %d uninitialised", seat.size(), seat.getUninitialisedPlayCount()));
                        Iterator<PlayImpl> plays = seat.iterator();

                        boolean played = false;
                        while(! played && plays.hasNext()) {
                            // Get next play
                            PlayImpl play = plays.next();
                            log.debug(String.format("Attempting to initialise play %s", play));

                            // If play is uninitialised, try and initialise it
                            played = ! play.isInitialised() && play.initialise(cards);
                            log.debug(String.format("Play %s, %s", (played ? "initialised" : "not initialised"), play));
                        }

                        if(played) {
                            // Remove cards from player hand
                            for(Card c : cards) {
                                pl.discard(c);
                            }
                        } else {
                            // Unable to initialise any play
                            log.warn(String.format("Unable to play cards %s", Arrays.toString(cards)));
                            throw new InvalidPlayException("Unable to play cards");
                        }
                    }
                    
                    log.debug(String.format("Play created, %d uninitialised plays remain", seat.getUninitialisedPlayCount()));
                    // Determine next state
                    if(seat.getUninitialisedPlayCount() == 0) {
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
                throw new EngineException("Unable to play playCards", re);
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
                    // Get player plays from the table
                    Iterator<TableSeat> iter = game.getTable().getSeats().iterator();

                    // Lookup play
                    PlayImpl pi = null;
                    while(iter.hasNext() && pi == null) {
                        pi = iter.next().lookupPlay(play);
                    }
                    if(pi == null) {
                        log.warn(String.format("Could not find play %s for %s", play, player));
                        throw new InvalidPlayException("Could not find play");
                    }
                    
                    // Attempt to peg card
                    if(pi.pegCard(card)) {
                        // Remove from hand
                        pl.discard(card);

                    } else {
                        log.warn(String.format("Could not peg %s onto %s", card, play));
                        throw new InvalidPlayException("Could not peg card onto play");
                    }

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
                    // Get player plays from the table
                    TableSeat seat = game.getTable().getSeat(pl);

                    // Add cards back to player hand
                    for(PlayImpl pi : seat.getPlays()) {
                        if(pi.isInitialised()) {
                            for(Card c : pi.getCards()) {
                                pl.pickup(c);
                            }
                        }
                    }

                    // Reset plays
                    seat.resetAll();

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
        if(game.getCurrentRound() != Round.START) {
            log.warn(String.format("Attempting to start game already in progress, in %s round", game.getCurrentRound()));
            throw new EngineException("Game already in progress");
        } else {
            // Start internal processor
            iep.startProcessor();

            // Start client agents
            clients.startAllAgents();

            // Start first round
            startRound();
        }
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

        // Deal playCards
        {
            int handIndex = 0;
            PlayerImpl dealer = game.getDealer();
            PlayerImpl player = dealer;
            CardArray[] hands = game.getTable().deal();

            do {
                player = game.getNextPlayer(player);
                log.debug(String.format("Dealing playCards for %s", player));
                player.deal(hands[handIndex ++]);

                // Set player states
                player.setDown(false);
                player.setPlayerState(PlayerState.WATCHING);  // Don't use local private method so don't send out events
            } while(! player.equals(dealer) && handIndex < hands.length);

            log.debug(String.format("Cards dealt to %d players", handIndex));
        }

        // Initialise table objects
        game.getTable().initialiseRound(round);

        // Queue notifications
        queueEvent(new RoundStartedEvent(game.getCurrentPlayer(), round));
        gameLog(String.format("Round started"));

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

                    dumpToLog();
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
