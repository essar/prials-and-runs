package uk.co.essarsoftware.par.engine.std;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.essarsoftware.par.engine.Round;

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
    private PlayerImpl player = new PlayerImpl("Test Player");


    TableImpl underTest;

    public TableImplTest() {

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
        assertEquals("Plays size", 0, underTest.getPlays().length);
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
    public void testInitRoundNull() {
        underTest.initRound(null);
    }


    public void testInitRoundPP() {
        underTest.initRound(Round.PP);
    }
}
