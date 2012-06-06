package uk.co.essarsoftware.par.engine.std;

import org.apache.log4j.Logger;
import uk.co.essarsoftware.games.cards.Card;
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
    private static Logger log = Logger.getLogger(EngineImpl.class);
    private static Logger gameLog = Logger.getLogger("uk.co.essarsoftware.par.gamelog");

    private final EngineClientList clients;
    private final GameImpl game;

    public EngineImpl(GameImpl game) {
        this.game = game;
        clients = new EngineClientList();
    }

    private void gameLog(String msg) {
        gameLog.info(String.format("[%s|Turn %d]", game.getCurrentRound(), game.getTurn()) + msg);
    }

    private PlayerImpl getPlayerImpl(Player player) {
        return game.lookupPlayer(player);
    }
    
    private void queueEvent(GameEvent evt) {
        if(evt == null) {
            log.warn(String.format("Attempting to queue null event"));
        } else {
            for(EngineClient cli : clients.values()) {
                cli.addEvent(evt);
            }
        }
    }

    private boolean validatePlayer(PlayerImpl player, boolean currentPlayer, PlayerState... states) {
        if(currentPlayer) {
            log.debug(String.format("Checking if %s is current player", player));
            if(! game.isCurrentPlayer(player)) {
                log.warn(String.format("%s is not current player", player));
                throw new IllegalArgumentException("Not current player");
            }
        }
        for(PlayerState ps : states) {
            if(player.getPlayerState() == ps) {
                return true;
            }
        }
        throw new IllegalArgumentException("Not correct state");
    }
    
    
    void activate(Player player) throws EngineException {
        PlayerImpl pl = getPlayerImpl(player);
        synchronized(pl.getPlayerState()) {
            // Validate pre-requisites
            if(validatePlayer(pl, true, PlayerState.WATCHING, PlayerState.BOUGHT)) {
                
                if(pl.getPlayerState() == PlayerState.WATCHING) {

                    // Set player state
                    pl.setPlayerState(PlayerState.PICKUP);
                } else if(pl.getPlayerState() == PlayerState.BOUGHT) {
                    
                    // Player bought last turn, so will be skipping a turn
                    
                    // Add penalty card to hand
                    pl.getHand().pickup(pl.getPenaltyCard());
                    pl.clearPenaltyCard();

                    // Set player state
                    pl.setPlayerState(PlayerState.END_TURN);

                    // Queue notifications
                    queueEvent(new PickupEvent(player));

                    gameLog(String.format("%s picked up a penalty card", player));
                } else {
                    throw new IllegalStateException("Unexpected player state");
                }
            }
        }
    }
    
    void endTurn(Player player) throws EngineException {
        PlayerImpl pl = getPlayerImpl(player);
        synchronized(pl.getPlayerState()) {
            // Validate pre-requisites
            if(validatePlayer(pl, true, PlayerState.END_TURN)) {
                
                // TODO Stuff here to make sure everyone is in sync

                // Make sure the event queues for each player are empty
                for(EngineClient cli : clients.values()) {
                    synchronized(cli.queue) {
                        while(cli.getQueueSize() > 0) {
                            try {
                                cli.queue.wait(2000L);
                            } catch(InterruptedException ie) { }
                        }
                    }
                }
                
                // Check whether player is out
                
                if(pl.getHandSize() == 0) {
                    // Player is out
                    pl.setPlayerState(PlayerState.END_ROUND);
                }
                
                pl.setPlayerState(PlayerState.WATCHING);
            }
        }
    }
    

    public void discard(Player player, Card card) throws EngineException {
        PlayerImpl pl = getPlayerImpl(player);
        synchronized(pl.getPlayerState()) {
            // Validate pre-requisites
            if(validatePlayer(pl, true, PlayerState.DISCARD, PlayerState.PLAYED)) {
                // Remove card from hand
                pl.getHand().discard(card);

                // Add card to table
                game.getTable().discard(card);

                // Set player state
                pl.setPlayerState(PlayerState.END_TURN);

                // Queue notifications
                queueEvent(new DiscardEvent(player, card));

                gameLog(String.format("%s discarded %s", player, card));
            }
        }
    }

    public Card pickupDiscard(Player player) throws EngineException {
        PlayerImpl pl = getPlayerImpl(player);
        synchronized(pl.getPlayerState()) {
            // Validate pre-requisites
            if(validatePlayer(pl, true, PlayerState.PICKUP)) {
                // Take card from table
                Card card = game.getTable().pickupDiscard();

                // Add card to hand
                pl.getHand().pickup(card);

                // Set player state
                if(pl.isDown()) {
                    pl.setPlayerState(PlayerState.PEGGING);
                } else {
                    pl.setPlayerState(PlayerState.DISCARD);
                }

                // Queue notifications
                queueEvent(new PickupEvent(player, card));

                gameLog(String.format("%s picked up %s from discard pile", player, card));
                return card;
            } else {
                return null;
            }
        }
    }

    public Card pickupDraw(Player player) throws EngineException {
        PlayerImpl pl = getPlayerImpl(player);
        synchronized(pl.getPlayerState()) {
            // Validate pre-requisites
            if(validatePlayer(pl, true, PlayerState.PICKUP)) {
                // Take card from table
                Card card = game.getTable().pickupDraw();

                // Add card to hand
                pl.getHand().pickup(card);

                // Set player state
                if(pl.isDown()) {
                    pl.setPlayerState(PlayerState.PEGGING);
                } else {
                    pl.setPlayerState(PlayerState.DISCARD);
                }

                // Queue notifications
                queueEvent(new PickupEvent(player, null));

                gameLog(String.format("%s picked up from draw pile", player));
                return card;
            } else {
                return null;
            }
        }
    }

    public void playCards(Player player, Card[] cards) throws EngineException {
        PlayerImpl pl = getPlayerImpl(player);
        synchronized(pl.getPlayerState()) {
            // Validate pre-requisites
            if(validatePlayer(pl, true, PlayerState.DISCARD, PlayerState.PLAYING)) {

                if(game.getTable().getSeat(pl).getUninitialisedPlayCount() > 0) {
                    // Set player state
                    pl.setPlayerState(PlayerState.PLAYING);
                } else {
                    Play[] plays = game.getTable().getPlays(pl);

                    // Set player state
                    pl.setPlayerState(PlayerState.PLAYED);
                    pl.setDown(true);

                    // Queue notifications
                    queueEvent(new PlayCardsEvent(player, plays));

                    gameLog(String.format("%s played %s", player, Arrays.toString(plays)));
                }
            }
        }
    }

    public void pegCard(Player player, Play play, Card card) throws EngineException {
        PlayerImpl pl = getPlayerImpl(player);
        synchronized(pl.getPlayerState()) {
            // Validate pre-requisites
            if(validatePlayer(pl, true, PlayerState.PEGGING)) {

                // Set player state
                pl.setPlayerState(PlayerState.PEGGING);

                // Queue notifications
                queueEvent(new PegCardEvent(player, play, card));

                gameLog(String.format("%s pegged %s onto %s", player, card, play));
            }
        }
    }

    public void resetPlays(Player player) throws EngineException {
        PlayerImpl pl = getPlayerImpl(player);
        synchronized(pl.getPlayerState()) {
            // Validate pre-requisites
            if(validatePlayer(pl, true, PlayerState.PLAYING)) {

                // Set player state
                pl.setPlayerState(PlayerState.DISCARD);

                gameLog(String.format("%s reset plays", player));
            }
        }
    }

    public boolean buy(Player player, Player buyer) throws EngineException {
        PlayerImpl pl = getPlayerImpl(player);
        synchronized(pl.getPlayerState()) {
            // Validate pre-requisites
            if(validatePlayer(pl, true, PlayerState.PICKUP)) {
                PlayerImpl by = getPlayerImpl(buyer);
                synchronized(buyer.getPlayerState()) {
                    if(validatePlayer(by, false, PlayerState.WATCHING)) {
                        if(game.isBuyAllowed()) {
                            by.setPlayerState(PlayerState.BUYING);
                            pl.setPlayerState(PlayerState.BUY_REQ);

                            // Queue notifications
                            queueEvent(new BuyRequestEvent(player, buyer));

                            gameLog(String.format("%s requested to buy %s from %s", buyer, game.getTable().getDiscard(), player));
                            return true;
                        } else {
                            log.warn(String.format("%s attempting to buy from %s in round %s, turn %d", buyer, player, game.getCurrentRound(), game.getTurn()));
                        }
                    }
                }
            }
        }
        return false;
    }

    public Card approveBuy(Player player, Player buyer) throws EngineException {
        PlayerImpl pl = getPlayerImpl(player);
        synchronized(pl.getPlayerState()) {
            // Validate pre-requisites
            if(validatePlayer(pl, true, PlayerState.BUY_REQ)) {
                PlayerImpl by = getPlayerImpl(buyer);
                synchronized(buyer.getPlayerState()) {
                    if(validatePlayer(by, false, PlayerState.BUYING)) {
                        if(game.isBuyAllowed()) {
                            // Take card from table
                            Card bought = game.getTable().pickupDiscard();

                            // Add card to buyer hand
                            by.getHand().pickup(bought);

                            // Take penalty card and add to buyer hand
                            by.setPenaltyCard(game.getTable().pickupDraw());

                            // Take card from table
                            Card card = game.getTable().pickupDraw();

                            // Add card to player hand
                            pl.getHand().pickup(card);

                            // Set player states
                            by.setPlayerState(PlayerState.BOUGHT);
                            if(pl.isDown()) {
                                pl.setPlayerState(PlayerState.PEGGING);
                            } else {
                                pl.setPlayerState(PlayerState.DISCARD);
                            }

                            // Queue notifications
                            queueEvent(new BuyApprovedEvent(player, buyer, bought));

                            gameLog(String.format("%s approved buy of %s from %s", player, bought, buyer));
                            return card;
                        } else {
                            log.warn(String.format("%s attempting to buy from %s in round %s, turn %d", buyer, player, game.getCurrentRound(), game.getTurn()));
                        }
                    }
                }
            }
            return null;
        }
    }

    public Card rejectBuy(Player player, Player buyer) throws EngineException {
        PlayerImpl pl = getPlayerImpl(player);
        synchronized(pl.getPlayerState()) {
            // Validate pre-requisites
            if(validatePlayer(pl, true, PlayerState.BUY_REQ)) {
                PlayerImpl by = getPlayerImpl(buyer);
                synchronized(buyer.getPlayerState()) {
                    if(validatePlayer(by, false, PlayerState.BUYING)) {
                        if(game.isBuyAllowed()) {
                            // Take card from table
                            Card card = game.getTable().pickupDiscard();

                            // Add card to player hand
                            pl.getHand().pickup(card);

                            // Set player states
                            by.setPlayerState(PlayerState.WATCHING);
                            if(pl.isDown()) {
                                pl.setPlayerState(PlayerState.PEGGING);
                            } else {
                                pl.setPlayerState(PlayerState.PLAYING);
                            }
                            
                            // Queue notifications
                            queueEvent(new BuyRejectedEvent(player, buyer, card));

                            gameLog(String.format("%s rejected buy of %s from %s", player, card, buyer));
                            return card;
                        } else {
                            log.warn(String.format("%s attempting to buy from %s in round %s, turn %d", buyer, player, game.getCurrentRound(), game.getTurn()));
                        }
                    }
                }
            }
            return null;
        }
    }

    public void abortGame() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void startGame() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void startRound() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
