/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.par.engine.std;

import org.apache.log4j.Logger;
import uk.co.essarsoftware.games.cards.*;
import uk.co.essarsoftware.par.engine.Play;
import uk.co.essarsoftware.par.engine.Player;
import uk.co.essarsoftware.par.engine.Round;
import uk.co.essarsoftware.par.engine.Table;

import java.util.*;

/**
 * Table implementation class.
 * @author Steve Roberts <steve.roberts@essarsoftware.co.uk>
 * @version 1.0 (07-Jun-12)
 */
class TableImpl implements Table
{
    private static double overDealFactor = 1.65;
    private static int handSize = 11;
    private static Logger log = Logger.getLogger(Table.class);

    private final DiscardPile discardPile;
    private final DrawPile drawPile;
    private final SeatMap seats;

    TableImpl() {
        discardPile = new DiscardPile();
        drawPile = new DrawPile();
        seats = new SeatMap();
    }

    void createSeat(PlayerImpl player) {
        // Validate arguments
        if(player == null) {
            throw new IllegalArgumentException("Cannot add null player to table");
        }
        seats.put(player, null);
        log.debug(String.format("Seat created for %s", player));

        // Run assertions
        assert(seats.containsKey(player));
    }
    
    TableSeat getSeat(PlayerImpl player) {
        // Validate arguments
        if(player == null) {
            log.warn(String.format("Attempting to find a seat for null player"));
            return null;
        }
        if(! seats.containsPlayer(player)) {
            log.warn(String.format("Could not find a seat at the table for %s", player));
            return null;
        }
        return seats.getSeat(player);
    }

    Collection<TableSeat> getSeats() {
        return seats.values();
    }

    void removeSeat(PlayerImpl player) {
        // Validate arguments
        if(player == null) {
            log.warn(String.format("Attempting to remove seat for a null player"));
        }
        if(seats.remove(player) == null) {
            log.warn(String.format("Could not find seat for %s", player));
        }
        log.debug(String.format("Seat removed for %s", player));

        // Run assertions
        assert(! seats.containsKey(player));
    }

    void initialiseRound(Round round) {
        // Validate arguments
        if(round == null) {
            throw new IllegalArgumentException("Cannot initialise table with null Round");
        }

        // Initialise seats
        for(PlayerImpl player : seats.keySet()) {
            seats.put(player, new TableSeat(round));
        }

        // Flip top draw card to discard pile
        if(drawPile.size() > 0) {
            discardPile.discard(drawPile.pickup());
        }
    }

    CardArray[] deal() {
        CardArray[] hands = new CardArray[seats.size()];
        int cardsRequired = hands.length * handSize;
        if(drawPile.size() < cardsRequired) {
            log.warn(String.format("Unable to deal playCards, %d playCards in pile, %d needed", drawPile.size(), cardsRequired));
        } else {
            for(int i = 0; i < handSize; i ++) {
                for(int ii = 0; ii < hands.length; ii ++) {
                    if(hands[ii] == null) {
                        hands[ii] = new CardArray();
                    }
                    hands[ii].add(pickupDraw());
                }
            }
        }
        return hands;
    }

    void resetTable() {
        // Clear existing draw and discard piles
        drawPile.clear();
        discardPile.clear();

        // Repopulate the draw pile
        int minDeckSize = (int) ((double) seats.size() * (double) handSize * overDealFactor);
        log.debug(String.format("%d playCards are required as a minimum.", minDeckSize));

        while(drawPile.size() <= minDeckSize) {
            drawPile.add(Pack.generatePackWithJokers());
            log.debug(String.format("Draw pile now %d playCards, minimum of %d needed.", drawPile.size(), minDeckSize));
        }
        log.debug(String.format("Draw pile rebuilt with %d playCards.", drawPile.size()));

        // Shuffle the deck
        drawPile.shuffle();
    }


    void discard(Card card) {
        // Validate arguments
        if(card == null) {
            throw new IllegalArgumentException("Cannot add null to discard pile");
        }

        discardPile.discard(card);
        log.debug(String.format("Card %s added to discard pile, %d card(s) now on discard pile.", card, discardPile.size()));
    }

    Card pickupDiscard() {
        if(discardPile.size() == 0) {
            log.warn(String.format("Attempting to pick up from empty discard pile"));
            return null;
        }
        Card card = discardPile.pickup();
        log.debug(String.format("Card %s taken from discard pile, %d card(s) remain on discard pile.", card, discardPile.size()));

        return card;
    }

    Card pickupDraw() {
        if(drawPile.size() == 0) {
            log.warn(String.format("Attempting to pick up from empty draw pile"));
            return null;
        }
        Card card = drawPile.pickup();
        log.debug(String.format("Card %s taken from draw pile, %d card(s) remain on draw pile.", card, drawPile.size()));

        // If there are no playCards left, move playCards from the discard pile back to draw pile
        if(drawPile.size() == 0) {
            Card discard = discardPile.pickup();
            discardPile.resetTo(drawPile);
            discardPile.discard(discard);
            log.debug(String.format("Discard pile recycled, %d card(s) now on draw pile.", drawPile.size()));
        }

        return card;
    }

    @Override
    public Card getDiscard() {
        if(discardPile.size() == 0) {
            log.warn(String.format("Attempting to get top card from empty discard pile"));
            return null;
        }
        return discardPile.getDiscard();
    }

    @Override
    public PlayImpl[] getPlays(Player player) {
        // Validate arguments
        if(player == null) {
            log.warn(String.format("Attempting to get plays for plays for null player"));
            return null;
        }
        if(! seats.containsPlayer(player)) {
            log.warn(String.format("Could not find plays for %s", player));
            return null;
        }
        TableSeat s = seats.getSeat(player);
        return (s == null ? new PlayImpl[0] : s.getPlays());
    }

    @Override
    public PlayImpl[] getPlays() {
        return seats.getAllPlays();
    }

    private class SeatMap extends HashMap<PlayerImpl, TableSeat>
    {
        public PlayImpl[] getAllPlays() {
            ArrayList<PlayImpl> allPlays = new ArrayList<PlayImpl>();
            for(TableSeat ts : values()) {
                if(ts != null) {
                    allPlays.addAll(ts);
                }
            }
            return allPlays.toArray(new PlayImpl[allPlays.size()]);
        }
        
        public boolean containsPlayer(Player player) {
            return containsKey(player);
        }
        
        public TableSeat getSeat(Player player) {
            return get(player);
        }
    }
}
