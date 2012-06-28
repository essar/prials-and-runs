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
