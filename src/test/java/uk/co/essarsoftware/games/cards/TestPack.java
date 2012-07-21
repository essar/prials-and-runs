/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.games.cards;

/**
 * Test harness class for Pack.
 */
public class TestPack extends Pack
{
    static long packID = 13579L;
    /**
     * Create a new pack from a list of playCards.
     * @param cards a list of <tt>Card</tt>s to hold in the pack.
     */
    public TestPack(Card... cards) {
        super();

        for(Card c : cards) {
            super.add(new Card(packID, c.getSuit(), c.getValue()));
        }
    }

    @Override
    public boolean add(Card c) {
        return super.add(new Card(packID, c.getSuit(), c.getValue()));
    }

    /**
     * Get a card instance from the pack.
     * @param card the <tt>Card</tt> to look for.
     * @return a <tt>Card</tt> from the pack that matches the card supplied, or null if the card is not in the pack.
     */
    public Card findCard(Card card) {
        for(Card c : this) {
            if(c.sameCard(card)) {
                return c;
            }
        }
        return null;
    }
}
