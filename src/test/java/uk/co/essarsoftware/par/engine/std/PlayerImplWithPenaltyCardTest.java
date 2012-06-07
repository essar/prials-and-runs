package uk.co.essarsoftware.par.engine.std;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.games.cards.CardArray;
import uk.co.essarsoftware.games.cards.TestPack;
import uk.co.essarsoftware.par.engine.CardNotFoundException;
import uk.co.essarsoftware.par.engine.DuplicateCardException;
import uk.co.essarsoftware.par.engine.Player;
import uk.co.essarsoftware.par.engine.PlayerState;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 13:14
 * To change this template use File | Settings | File Templates.
 */
public class PlayerImplWithPenaltyCardTest extends PlayerImplTest
{
    public PlayerImplWithPenaltyCardTest() {
        super();
    }

    @Override@Before
    public void setUp() {
        super.setUp();
        underTest.setPenaltyCard(card);
        assertNotNull("setUp: penaltyCard", underTest.getPenaltyCard());
    }

    @Override@Test
    public void testDefaultPenaltyCard() {
        assertEquals("Penalty card", card, underTest.getPenaltyCard());
        assertTrue("Has penalty card", underTest.hasPenaltyCard());
    }
}
