package uk.co.essarsoftware.par.engine.std;

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
public class TableImpl1PlayerPPRoundTest extends TableImplTest
{
    public TableImpl1PlayerPPRoundTest() {
        super();
    }

    @Override@Before
    public void setUp() {
        super.setUp();
        underTest.createSeat(player);
        underTest.resetTable();
        underTest.initialiseRound(Round.PP);
    }

    @Override@Test
    public void testDefaultGetPlayerPlays() {
        assertNotNull("Player plays", underTest.getPlays(player));
        assertEquals("Player play size", 0, underTest.getPlays(player).length);
    }

    @Override@Test
    public void testCreateSeatPlayer() {
        underTest.createSeat(player);
        assertNotNull("Player plays not null", underTest.getPlays(player));
        assertNull("Player seat null", underTest.getSeat(player));
    }

    @Override@Test
    public void testInitialiseRoundPP() {
        underTest.initialiseRound(Round.PP);
        // TODO Change to throw exception if less than two players?
        assertTrue("No exception", true);
    }

    @Override@Test
    public void testDeal() {
        assertEquals("One hand dealt", 1, underTest.deal().length);
        assertNull("Empty hands dealt", underTest.deal()[0]);
    }
}
