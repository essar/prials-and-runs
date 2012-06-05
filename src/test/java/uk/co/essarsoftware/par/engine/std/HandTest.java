package uk.co.essarsoftware.par.engine.std;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.games.cards.TestPack;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 15:16
 * To change this template use File | Settings | File Templates.
 */
public class HandTest
{
    private Card card, packCard;

    Hand underTest;

    public HandTest() {
        card = Card.createCard(Card.Suit.CLUBS, Card.Value.EIGHT);

        TestPack pack = new TestPack(card);
        packCard = pack.getCard(card);
    }

    @Before
    public void setUp() {
        underTest = new Hand();

        assertNotNull("setUp: card", card);
        assertNotNull("setUp: packCard", card);
    }

    @After
    public void tearDown() {
        underTest = null;
    }

    @Test
    public void testDefaultSize() {
        assertEquals("Size", 0, underTest.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPickupNull() {
        underTest.pickup(null);
        fail("Pickup null, expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPickupDuplicate() {
        underTest.pickup(packCard);
        assertEquals("Pre-condition: size", 1, underTest.size());
        underTest.pickup(packCard);
        fail("Pickup duplicate, expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPickupNonPackCard() {
        underTest.pickup(card);
        fail("Pickup non-pack card, expected IllegalArgumentException");
    }

    @Test
    public void testPickup8C() {
        underTest.pickup(packCard);
        assertEquals("Size", 1, underTest.size());
        assertTrue("Contains", underTest.contains(packCard));
    }
}
