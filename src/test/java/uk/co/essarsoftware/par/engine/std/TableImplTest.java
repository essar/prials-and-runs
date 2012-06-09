package uk.co.essarsoftware.par.engine.std;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.games.cards.TestPack;
import uk.co.essarsoftware.par.engine.Round;
import uk.co.essarsoftware.par.engine.Table;

import static org.junit.Assert.*;

/**
 * Test case for TableImpl class.
 * @see TableImpl
 */
public class TableImplTest
{
    protected Card card, unregisteredCard;
    protected PlayerImpl player, player2;
    protected Round round;

    TableImpl underTest;

    static {
        BasicConfigurator.resetConfiguration();
        Logger.getRootLogger().setLevel(Level.ERROR);
        Logger.getLogger(Table.class).setLevel(Level.DEBUG);
    }

    public TableImplTest() {
        card = Card.createCard(Card.Suit.CLUBS, Card.Value.EIGHT);
        unregisteredCard = Card.createCard(Card.Suit.CLUBS, Card.Value.EIGHT);

        // Register card with pack
        TestPack tp = new TestPack();
        tp.add(card);
        card = tp.findCard(card);

        player = new PlayerImpl("Test Player");
        player2 = new PlayerImpl("Test Player 2");
        round = Round.PP;
    }

    @Before
    public void setUp() {
        underTest = new TableImpl();
    }

    @After
    public void tearDown() {
        underTest = null;
    }

    /* ** PUBLIC METHOD TESTS ** */

    @Test
    public void testPickupDiscard() {
        assertNull("Pickup Discard", underTest.pickupDiscard());
    }

    @Test
    public void testPickupDraw() {
        assertNull("Pickup Draw", underTest.pickupDraw());
    }

    @Test
    public void testGetPlays() {
        assertNotNull("Plays", underTest.getPlays());
        assertEquals("Plays size", 0, underTest.getPlays().length);
    }

    @Test
    public void testGetPlayerPlaysNull() {
        assertNull("Player plays", underTest.getPlays(null));
    }

    @Test
    public void testGetPlayerPlaysPlayer() {
        assertNull("Player plays", underTest.getPlays(player));
    }

    @Test
    public void testGetDiscard() {
        assertNull("Discard", underTest.getDiscard());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDiscardNull() {
        underTest.discard(null);
        fail("Discarded null, expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiscardUnregistered() {
        underTest.discard(unregisteredCard);
        fail("Discarded null, expected IllegalArgumentException");
    }
    
    @Test
    public void testDiscardCard() {
        underTest.discard(card);
        assertEquals("Discarded card", card, underTest.getDiscard());
    }


    /* ** PACKAGE METHOD TESTS ** */

    @Test(expected = IllegalArgumentException.class)
    public void testCreateSeatNull() {
        underTest.createSeat(null);
        fail("Created null seat, expected IllegalArgumentException");
    }

    @Test
    public void testCreateSeatPlayer() {
        underTest.createSeat(player);
        assertNotNull("Player plays not null", underTest.getPlays(player));
        assertNull("Player seat null", underTest.getSeat(player));
    }

    @Test
    public void testGetSeatNull() {
        assertNull("Player seat null", underTest.getSeat(null));
    }

    @Test
    public void testGetSeatPlayer() {
        assertNull("Player seat null", underTest.getSeat(player));
    }

    @Test
    public void testRemoveSeatNull() {
        underTest.removeSeat(null);
        assertTrue("No exception", true);
    }

    @Test
    public void testRemoveSeatPlayer() {
        underTest.removeSeat(player);
        assertTrue("No exception", true);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testInitialiseRoundNull() {
        underTest.initialiseRound(null);
        fail("Initialised with null Round, IllegalArgumentException expected");
    }

    @Test
    public void testInitialiseRoundPP() {
        underTest.initialiseRound(Round.PP);
        // TODO Change to throw exception if less than two players?
        assertTrue("No exception", true);
    }

    @Test
    public void testDeal() {
        assertEquals("No cards dealt", 0, underTest.deal().length);
    }

    @Test
    public void testResetTable() {
        assertTrue("No exception", true);
    }
}
