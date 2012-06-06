package uk.co.essarsoftware.par.engine.std;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.essarsoftware.par.engine.Round;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 16:36
 * To change this template use File | Settings | File Templates.
 */
public class TableImpl1PlayerTest
{
    private PlayerImpl player;

    TableImpl underTest;

    public TableImpl1PlayerTest() {
        player = new PlayerImpl("Test Player");
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
    public void testDefaultPlays() {
        assertEquals("Plays size", 0, underTest.getSeats().length);
    }

    @Test
    public void testDefaultPlayerPlays() {
        assertNull("Player plays", underTest.getPlays(player));
    }

    @Test
    public void testDefaultDiscard() {
        assertNull("Discard", underTest.getDiscard());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitialiseRoundNull() {
        underTest.initialiseRound(null);
    }


    public void testInitialiseRoundPP() {
        underTest.initialiseRound(Round.PP);
    }
}
