package uk.co.essarsoftware.par.engine.std;

import org.junit.Before;
import org.junit.Test;
import uk.co.essarsoftware.par.engine.Round;

import static org.junit.Assert.*;

/**
 * Test case for TableImpl class.
 * @see TableImpl
 */
public class TableImpl1PlayerTest extends TableImplTest
{
    public TableImpl1PlayerTest() {
        super();
    }

    @Override@Before
    public void setUp() {
        super.setUp();
        underTest.createSeat(player);
    }

    @Override@Test
    public void testGetPlayerPlaysPlayer() {
        assertNotNull("Player plays", underTest.getPlays(player));
        assertEquals("Player play size", 0, underTest.getPlays(player).length);
    }

    @Override@Test
    public void testInitialiseRoundPP() {
        underTest.initialiseRound(Round.PP);
        // TODO Change to throw exception if less than two players?

        assertNotNull("Discard pile", underTest.getDiscard());
        assertNotNull("Player seat", underTest.getSeat(player));
    }

    @Override@Test
    public void testDeal() {
        assertEquals("One hand dealt", 1, underTest.deal().length);
        assertNull("Empty hands dealt", underTest.deal()[0]);
    }
}
