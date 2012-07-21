/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.par.engine.std;

import org.apache.log4j.Logger;
import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.games.cards.CardArray;
import uk.co.essarsoftware.par.engine.CardNotFoundException;
import uk.co.essarsoftware.par.engine.DuplicateCardException;
import uk.co.essarsoftware.par.engine.Player;
import uk.co.essarsoftware.par.engine.PlayerState;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 13:04
 * To change this template use File | Settings | File Templates.
 */
class PlayerImpl implements Player
{
    private static Logger log = Logger.getLogger(Player.class);

    private boolean down;
    private Card penaltyCard;
    private PlayerState playerState;
    private String playerName;

    private final CardArray hand;

    static {
        log.debug("** DEBUG ENABLED, card information may appear in log output **");
    }

    public PlayerImpl(String playerName) {
        this.playerName = playerName;
        this.hand = new CardArray();
        this.playerState = PlayerState.INIT;
        this.down = false;
        
        log.debug(String.format("PlayerImpl created for %s", playerName));
    }

    void deal(CardArray cards) {
        if(cards == null) {
            throw new IllegalArgumentException("Cannot deal null");
        }
        synchronized(hand) {
            hand.clear();
            hand.addAll(cards);
            log.debug(String.format("[%s] hand now: %s", this, hand));
        }
    }

    Card[] getHand() {
        return hand.getCards();
    }
    
    //boolean handContains(Card card) {
    //    return hand.contains(card);
    //}

    void discard(Card card) throws CardNotFoundException {
        if(card == null) {
            throw new IllegalArgumentException("Cannot discard null");
        }
        synchronized(hand) {
            if(! hand.contains(card)) {
                throw new CardNotFoundException("Card not found in hand", card, this);
            }
            hand.remove(card);
            log.debug(String.format("[%s] discarded %s", this, card));
            log.debug(String.format("[%s] hand now: %s", this, hand));
        }
    }
    
    void pickup(Card card) throws DuplicateCardException {
        if(card == null) {
            throw new IllegalArgumentException("Cannot pickup null");
        }
        synchronized(hand) {
            if(hand.contains(card)) {
                throw new DuplicateCardException("Card already in hand", card, this);
            }
            hand.add(card);
            log.debug(String.format("[%s] picked up %s", this, card));
            log.debug(String.format("[%s] hand now: %s", this, hand));
        }
    }

    void setDown(boolean down) {
        this.down = down;
        log.debug(String.format("[%s] set %s", this, (down ? "down" : "not down")));
    }

    void clearPenaltyCard() {
        this.penaltyCard = null;
        log.debug(String.format("[%s] penaltyCard set to %s", this, penaltyCard));
    }

    Card getPenaltyCard() {
        return penaltyCard;
    }

    void setPenaltyCard(Card penaltyCard) {
        if(penaltyCard == null) {
            throw new IllegalArgumentException("Cannot set penaltyCard to null");
        }
        this.penaltyCard = penaltyCard;
        log.debug(String.format("[%s] penaltyCard set to %s", this, penaltyCard));
    }

    void setPlayerState(PlayerState playerState) {
        if(playerState == null) {
            throw new IllegalArgumentException("Cannot set playerState to null");
        }
        synchronized(this) {
            this.playerState = playerState;
            log.debug(String.format("[%s] playerState set to %s", this, playerState));
        }
    }

    @Override
    public int getHandSize() {
        return hand.size();
    }

    @Override
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public PlayerState getPlayerState() {
        return playerState;
    }

    @Override
    public boolean hasPenaltyCard() {
        return (penaltyCard != null);
    }

    @Override
    public boolean isDown() {
        return down;
    }
    
    @Override
    public String toString() {
        return playerName;
    }
}
