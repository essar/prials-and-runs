package uk.co.essarsoftware.par.engine.std;

import org.apache.log4j.Logger;
import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.client.GameClient;
import uk.co.essarsoftware.par.engine.*;
import uk.co.essarsoftware.par.engine.events.GameEvent;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 06/06/12
 * Time: 15:19
 * To change this template use File | Settings | File Templates.
 */
class EngineClient implements GameClient
{
    private static Logger log = Logger.getLogger(EngineClient.class);
    
    private final EngineImpl engine;
    private final GameImpl game;
    private final PlayerImpl player;

    final EventQueue queue;

    public EngineClient(EngineImpl engine, GameImpl game, PlayerImpl player) {
        this.engine = engine;
        this.game = game;
        this.player = player;
        
        queue = new EventQueue();
    }

    boolean addEvent(GameEvent evt) {
        boolean ret = queue.add(evt);
        log.debug(String.format("[%s] Adding %s, %d events on queue.", player, evt, queue.size()));
        if(queue.remainingCapacity() < 5) {
            log.warn(String.format("[%s] Event queue capacity WARNING : %d events on queue, %d remaining.", player, queue.size(), queue.remainingCapacity()));
        }
        return ret;
    }
    
    GameEvent getNextGameEvent() {
        synchronized(queue) {
            try {
                GameEvent evt = queue.take();
                queue.notifyAll();
                return evt;
            } catch(InterruptedException ie) {
                return null;
            }
        }
    }
    
    int getQueueSize() {
        return queue.size();
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
            log.error(String.format("[%s] Unable to approve buy from %s : %s", player, buyer, ee.getMessage()));
            log.debug(ee.getMessage(), ee);
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
            log.error(String.format("[%s] Unable to buy %s from %s : %s", player, card, currentPlayer, ee.getMessage()));
            log.debug(ee.getMessage(), ee);
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
            log.error(String.format("[%s] Unable to discard %s : %s", player, card, ee.getMessage()));
            log.debug(ee.getMessage(), ee);
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
            log.error(String.format("[%s] Unable to peg %s onto %s : %s", player, card, play, ee.getMessage()));
            log.debug(ee.getMessage(), ee);
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
            log.error(String.format("[%s] Unable to pickup from discard pile : %s", player, ee.getMessage()));
            log.debug(ee.getMessage(), ee);
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
            log.error(String.format("[%s] Unable to pickup from draw pile : %s", player, ee.getMessage()));
            log.debug(ee.getMessage(), ee);
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
            log.error(String.format("[%s] Unable to play cards %s : %s", player, Arrays.toString(cards), ee.getMessage()));
            log.debug(ee.getMessage(), ee);
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
            log.error(String.format("[%s] Unable to reject buy from %s : %s", player, buyer, ee.getMessage()));
            log.debug(ee.getMessage(), ee);
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
            log.error(String.format("[%s] Unable to reset plays : %s", player, ee.getMessage()));
            log.debug(ee.getMessage(), ee);
        }
    }

    @Override
    public Card[] getHand() {
        if(player == null) {
            throw new IllegalStateException("Client has not been initialized with a Player");
        }
        return player.getHand().getCards();
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
    public Player[] getPlayers() {
        if(game == null) {
            throw new IllegalStateException("Client has not been initialized with a Game");
        }
        return game.getPlayers();
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
}
