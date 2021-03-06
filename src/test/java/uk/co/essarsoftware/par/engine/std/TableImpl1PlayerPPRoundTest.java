/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.par.engine.std;

import org.junit.Before;
import org.junit.Test;
import uk.co.essarsoftware.par.engine.Round;

import static org.junit.Assert.*;

/**
 * Test case for TableImpl class.
 * @see TableImpl
 */
public class TableImpl1PlayerPPRoundTest extends TableImpl1PlayerTest
{
    protected Round round;

    public TableImpl1PlayerPPRoundTest() {
        super();
        round = Round.PP;
    }

    @Override@Before
    public void setUp() {
        super.setUp();
        underTest.resetTable();
        underTest.initialiseRound(round);
    }

    @Override@Test
    public void testGetPlays() {
        assertNotNull("Plays", underTest.getPlays(player));
        assertEquals("Play size", round.getPrials() + round.getRuns(), underTest.getPlays().length);
    }

    @Override@Test
    public void testGetPlayerPlaysPlayer() {
        assertNotNull("Player plays", underTest.getPlays(player));
        assertEquals("Player play size", round.getPrials() + round.getRuns(), underTest.getPlays(player).length);
    }

    @Override@Test
    public void testGetSeatPlayer() {
        assertNotNull("Player seat not null", underTest.getSeat(player));
        assertEquals("Player seat plays", (round.getPrials() + round.getRuns()), underTest.getSeat(player).getPlays().length);
        assertEquals("Player seat uninitialised plays", (round.getPrials() + round.getRuns()), underTest.getSeat(player).getUninitialisedPlayCount());
    }

    @Override@Test
    public void testDeal() {
        assertEquals("One hand dealt", 1, underTest.deal().length);
        assertNotNull("Valid hand dealt", underTest.deal()[0]);
        assertEquals("11 playCards dealt", 11, underTest.deal()[0].size());
    }

    @Override@Test
    public void testPickupDiscard() {
        assertNotNull("Pickup discard not null", underTest.pickupDiscard());
    }

    @Override@Test
    public void testPickupDraw() {
        assertNotNull("Pickup discard not null", underTest.pickupDraw());
    }

    @Override@Test
    public void testGetDiscard() {
        assertNotNull("Discard card", underTest.getDiscard());
    }
}
