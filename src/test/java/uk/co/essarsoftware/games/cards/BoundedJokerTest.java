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
}
