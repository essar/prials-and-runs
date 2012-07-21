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
 * Test case for <tt>Card.Joker</tt>.
 * @see Card.Joker
 */
public class BoundedJokerTest extends CardTest
{
    public BoundedJokerTest() {
        super();
    }

    @Override@Before
    public void setUp() {
        underTest = new Card.Joker(packID);
        ((Card.Joker) underTest).bindTo(same);
    }

    @Override@Test
    public void testIsBoundJoker() {
        assertTrue("Is bound joker", underTest.isBoundJoker());
    }

    @Override@Test
    public void testIsJoker() {
        assertTrue("Is Joker", underTest.isJoker());
    }

    @Override@Test
    public void testCompareToSame() {
        assertTrue("Compare to same", underTest.compareTo(same) < 0);
    }
}
