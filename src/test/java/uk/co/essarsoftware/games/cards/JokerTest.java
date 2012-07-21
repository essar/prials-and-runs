/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.games.cards;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test case for <tt>Card.Joker</tt>.
 * @see Card.Joker
 */
public class JokerTest extends CardTest
{
    public JokerTest() {
        super();
        same = Card.createJoker();
    }

    @Override@Before
    public void setUp() {
        underTest = new Card.Joker(packID);
    }

    @Override@Test
    public void testGetSuit() {
        assertNull("Suit", underTest.getSuit());
    }

    @Override@Test
    public void testGetValue() {
        assertNull("Value", underTest.getValue());
    }

    @Override@Test
    public void testIsJoker() {
        assertTrue("Is Joker", underTest.isJoker());
    }

    @Override@Test
    public void testCompareToBefore() {
        assertTrue("Compare to before", underTest.compareTo(before) < 0);
    }

    @Override@Test
    public void testCompareToSame() {
        assertTrue("Compare to same", underTest.compareTo(same) < 0);
    }

    @Override@Test
    public void testCompareToSelf() {
        assertEquals("Compare to self", 0, underTest.compareTo(underTest));
    }

    @Override@Test
    public void testCompareToJoker() {
        assertTrue("Compare to joker", underTest.compareTo(joker) < 0);
    }

    @Override@Test
    public void testCompareToBoundJoker() {
        assertTrue("Compare to bound joker", underTest.compareTo(boundJoker) < 0);
    }

    @Override@Test
    public void testSameCardSame() {
        assertFalse("Same as same", underTest.sameCard(same));
    }

    @Override@Test
    public void testSameCardSelf() {
        assertFalse("Same as self", underTest.sameCard(underTest));
    }

    @Override@Test
    public void testSameBoundJoker() {
        assertFalse("Same as bound joker", underTest.sameCard(boundJoker));
    }

    @Override@Test
    public void testSameSuitSame() {
        assertFalse("Same suit as same", underTest.sameSuit(same));
    }

    @Override@Test
    public void testSameSuitSelf() {
        assertFalse("Same suit as self", underTest.sameSuit(underTest));
    }

    @Override@Test
    public void testSameSuitBoundJoker() {
        assertFalse("Same suit as bound joker", underTest.sameSuit(boundJoker));
    }

    @Override@Test
    public void testSameValueSame() {
        assertFalse("Same value as same", underTest.sameValue(same));
    }

    @Override@Test
    public void testSameValueSelf() {
        assertFalse("Same value as self", underTest.sameValue(underTest));
    }

    @Override@Test
    public void testSameValueBoundJoker() {
        assertFalse("Same value as bound joker", underTest.sameValue(boundJoker));
    }
}
