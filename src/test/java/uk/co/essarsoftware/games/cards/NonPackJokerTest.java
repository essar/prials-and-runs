/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.games.cards;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test case for <tt>Card</tt>.
 * @see uk.co.essarsoftware.games.cards.Card
 */
public class NonPackJokerTest extends JokerTest
{
    public NonPackJokerTest() {
        super();
    }

    @Override@Before
    public void setUp() {
        underTest = new Card.Joker(0);
    }

    @Override@Test
    public void testCompareToSelf() {
        assertTrue("Compare to self", underTest.compareTo(underTest) < 0);
    }

    @Override@Test
    public void testEqualsSelf() {
        assertFalse("Equals self", underTest.equals(underTest));
    }
}
