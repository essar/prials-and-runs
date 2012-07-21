/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.games.cards;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test suite for playCards package.
 * @author Steve Roberts <steve.roberts@essarsoftware.co.uk>
 * @version 1.0 (07-Jun-12)
 * @see CardTest
 * @see JokerTest
 * @see BoundedJokerTest
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({CardTest.class, NonPackCardTest.class, JokerTest.class, NonPackJokerTest.class, BoundedJokerTest.class})
public class CardsTestSuite
{
    // Empty class body
}
