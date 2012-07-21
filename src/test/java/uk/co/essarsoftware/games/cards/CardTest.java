/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.games.cards;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test case for <tt>Card</tt>.
 * @see uk.co.essarsoftware.games.cards.Card
 */
public class CardTest
{
    protected final long packID = 13579;
    protected Card same, after, before;
    protected Card.Suit suit;
    protected Card.Value value;
    protected Card.Joker joker, boundJoker;

    Card underTest;

    public CardTest() {
        suit = Card.Suit.CLUBS;
        value = Card.Value.EIGHT;

        same = Card.createCard(suit, value);
        after = Card.createCard(Card.Suit.SPADES, value);
        before = Card.createCard(suit, Card.Value.THREE);

        joker = Card.createJoker();
        boundJoker = Card.createJoker();
        boundJoker.bindTo(same);
    }

    @Before
    public void setUp() {
        underTest = new Card(packID, suit, value);
    }

    @After
    public void tearDown() {
        underTest = null;
    }

    @Test
    public void testGetSuit() {
        assertEquals("Suit", suit, underTest.getSuit());
    }

    @Test
    public void testGetValue() {
        assertEquals("Value", value, underTest.getValue());
    }

    @Test
    public void testIsBoundJoker() {
        assertFalse("Is bound joker", underTest.isBoundJoker());
    }

    @Test
    public void testIsJoker() {
        assertFalse("Is Joker", underTest.isJoker());
    }

    @Test
    public void testCompareToNull() {
        assertTrue("Compare to null", underTest.compareTo(null) > 0);
    }

    @Test
    public void testCompareToBefore() {
        assertTrue("Compare to before", underTest.compareTo(before) > 0);
    }

    @Test
    public void testCompareToAfter() {
        assertTrue("Compare to after", underTest.compareTo(after) < 0);
    }

    @Test
    public void testCompareToSame() {
        assertTrue("Compare to same", underTest.compareTo(same) > 0);
    }

    @Test
    public void testCompareToSelf() {
        assertEquals("Compare to self", 0, underTest.compareTo(underTest));
    }

    @Test
    public void testCompareToJoker() {
        assertTrue("Compare to joker", underTest.compareTo(joker) > 0);
    }

    @Test
    public void testCompareToBoundJoker() {
        assertTrue("Compare to bound joker", underTest.compareTo(boundJoker) > 0);
    }

    @Test
    public void testEqualsNull() {
        assertFalse("Equals null", underTest.equals(null));
    }

    @Test
    public void testEqualsDifferent() {
        assertFalse("Equals different", underTest.equals(after));
    }

    @Test
    public void testEqualsSame() {
        assertFalse("Equals same", underTest.equals(same));
    }

    @Test
    public void testEqualsSelf() {
        assertTrue("Equals self", underTest.equals(underTest));
    }

    @Test
    public void testEqualsJoker() {
        assertFalse("Equals joker", underTest.equals(joker));
    }

    @Test
    public void testEqualsBoundJoker() {
        assertFalse("Equals bound joker", underTest.equals(joker));
    }

    @Test
    public void testSameCardNull() {
        assertFalse("Same as null", underTest.sameCard(null));
    }

    @Test
    public void testSameCardDifferent() {
        assertFalse("Same as after", underTest.sameCard(after));
    }

    @Test
    public void testSameCardSame() {
        assertTrue("Same as same", underTest.sameCard(same));
    }

    @Test
    public void testSameCardSelf() {
        assertTrue("Same as self", underTest.sameCard(underTest));
    }

    @Test
    public void testSameCardJoker() {
        assertFalse("Same as joker", underTest.sameCard(joker));
    }

    @Test
    public void testSameBoundJoker() {
        assertTrue("Same as bound joker", underTest.sameCard(boundJoker));
    }

    @Test
    public void testSameSuitNull() {
        assertFalse("Same suit as null", underTest.sameSuit(null));
    }

    @Test
    public void testSameSuitDifferent() {
        assertFalse("Same suit as after", underTest.sameSuit(after));
    }

    @Test
    public void testSameSuitSame() {
        assertTrue("Same suit as same", underTest.sameSuit(same));
    }

    @Test
    public void testSameSuitSelf() {
        assertTrue("Same suit as self", underTest.sameSuit(underTest));
    }

    @Test
    public void testSameSuitJoker() {
        assertFalse("Same suit as joker", underTest.sameSuit(joker));
    }

    @Test
    public void testSameSuitBoundJoker() {
        assertTrue("Same suit as bound joker", underTest.sameSuit(boundJoker));
    }

    @Test
    public void testSameValueNull() {
        assertFalse("Same value as null", underTest.sameValue(null));
    }

    @Test
    public void testSameValueDifferent() {
        assertFalse("Same value as before", underTest.sameValue(before));
    }

    @Test
    public void testSameValueSame() {
        assertTrue("Same value as same", underTest.sameValue(same));
    }

    @Test
    public void testSameValueSelf() {
        assertTrue("Same value as self", underTest.sameValue(underTest));
    }

    @Test
    public void testSameValueJoker() {
        assertFalse("Same value as joker", underTest.sameValue(joker));
    }

    @Test
    public void testSameValueBoundJoker() {
        assertTrue("Same value as bound joker", underTest.sameValue(boundJoker));
    }
}
