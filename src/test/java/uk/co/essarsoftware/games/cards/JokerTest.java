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

    @Override
    public void testCompareToSame() {
        // No test
    }

    @Override@Test
    public void testCompareToJoker() {
        assertEquals("Compare to joker", 0, underTest.compareTo(joker));
    }

    @Override@Test
    public void testCompareToBoundJoker() {
        assertTrue("Compare to bound joker", underTest.compareTo(boundJoker) < 0);
    }

    @Override
    public void testEqualsSame() {
        // No test
    }

    @Override@Test
    public void testSameCardDifferent() {
        assertTrue("Same as after", underTest.sameCard(after));
    }

    @Override
    public void testSameCardSame() {
        // No test
    }

    @Override@Test
    public void testSameSuitDifferent() {
        assertTrue("Same suit as after", underTest.sameSuit(after));
    }

    @Override
    public void testSameSuitSame() {
        // No test
    }

    @Override@Test
    public void testSameValueDifferent() {
        assertTrue("Same value as before", underTest.sameValue(before));
    }

    @Override
    public void testSameValueSame() {
        // No test
    }
}
