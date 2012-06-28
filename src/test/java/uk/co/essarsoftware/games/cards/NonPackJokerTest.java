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
