package uk.co.essarsoftware.par.engine.std;

import org.apache.log4j.Logger;
import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.engine.*;

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

    private boolean down;

    private final GameImpl game;

    public EngineImpl(GameImpl game) {
        this.game = game;
    }

    private void gameLog(String msg) {
        gameLog.info(String.format("[%s|Turn %d]", game.getCurrentRound(), game.getTurn()) + msg);
    }

    private PlayerImpl getPlayerImpl(Player player) {
        return (PlayerImpl) player;
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

                // TODO Queue notifications
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
                if(down) {
                    pl.setPlayerState(PlayerState.PEGGING);
                } else {
                    pl.setPlayerState(PlayerState.DISCARD);
                }

                // TODO Queue notifications

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
                if(down) {
                    pl.setPlayerState(PlayerState.PEGGING);
                } else {
                    pl.setPlayerState(PlayerState.DISCARD);
                }

                // TODO Queue notifications

                gameLog(String.format("%s picked up from draw pile", player));
                return card;
            } else {
                return null;
            }
        }
    }

    public void playCards(Player player, Card[] cards) throws EngineException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void pegCard(Player player, Play play, Card card) throws EngineException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void reset(Player player) throws EngineException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void buy(Player player, Player buyer) throws EngineException {
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

                            // TODO Queue notifications

                            gameLog(String.format("%s requested to buy %s from %s", buyer, game.getTable().getDiscard(), player));
                        } else {
                            log.warn(String.format("%s attempting to buy from %s in round %s, turn %d", buyer, player, game.getCurrentRound(), game.getTurn()));
                        }
                    }
                }
            }
        }
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
                            if(down) {
                                pl.setPlayerState(PlayerState.PEGGING);
                            } else {
                                pl.setPlayerState(PlayerState.DISCARD);
                            }

                            // TODO Queue notifications

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

                            // TODO Queue notifications

                            // Set player states
                            by.setPlayerState(PlayerState.WATCHING);
                            if(down) {
                                pl.setPlayerState(PlayerState.PEGGING);
                            } else {
                                pl.setPlayerState(PlayerState.PLAYING);
                            }

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
