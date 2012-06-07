package uk.co.essarsoftware.par.engine.std;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.games.cards.TestPack;
import uk.co.essarsoftware.par.engine.Player;
import uk.co.essarsoftware.par.engine.Round;
import uk.co.essarsoftware.par.engine.Table;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 16:36
 * To change this template use File | Settings | File Templates.
 */
public class TableImplTest
{
    protected Card card, unregisteredCard;
    protected PlayerImpl player;
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

    @Test
    public void testDefaultPickupDiscard() {
        assertNull("Pickup Discard", underTest.pickupDiscard());
    }

    @Test
    public void testDefaultPickupDraw() {
        assertNull("Pickup Draw", underTest.pickupDraw());
    }

    @Test
    public void testDefaultGetPlays() {
        assertNotNull("Plays", underTest.getPlays());
        assertEquals("Plays size", 0, underTest.getPlays().length);
    }

    @Test
    public void testDefaultGetPlayerPlaysNull() {
        assertNull("Player plays", underTest.getPlays(null));
    }

    @Test
    public void testDefaultGetPlayerPlays() {
        assertNull("Player plays", underTest.getPlays(player));
    }

    @Test
    public void testDefaultGetDiscard() {
        assertNull("Discard", underTest.getDiscard());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDefaultDiscardNull() {
        underTest.discard(null);
        fail("Discarded null, expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultDiscardUnregistered() {
        underTest.discard(unregisteredCard);
        fail("Discarded null, expected IllegalArgumentException");
    }
    
    @Test
    public void testDefaultDiscard() {
        underTest.discard(card);
        assertEquals("Discarded card", card, underTest.getDiscard());
    }

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
        assertNull("Player seat null", underTest.getSeat(player));
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
