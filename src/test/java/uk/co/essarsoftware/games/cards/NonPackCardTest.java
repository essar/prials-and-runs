package uk.co.essarsoftware.games.cards;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test case for <tt>Card</tt>.
 * @see Card
 */
public class NonPackCardTest extends CardTest
{
    public NonPackCardTest() {
        super();
    }

    @Override@Before
    public void setUp() {
        underTest = new Card(0, suit, value);
    }

    @Override@Test
    public void testCompareToSame() {
        assertTrue("Compare to same", underTest.compareTo(same) < 0);
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
