package uk.co.essarsoftware.par.engine.std;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.games.cards.TestPack;
import uk.co.essarsoftware.par.engine.PlayerState;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 13:14
 * To change this template use File | Settings | File Templates.
 */
public class PlayerImplTest
{
    private Card card;
    private String playerName;

    PlayerImpl underTest;

    public PlayerImplTest() {

        card = Card.createCard(Card.Suit.CLUBS, Card.Value.EIGHT);
        TestPack pack = new TestPack(card);
        card = pack.getCard(card);

        playerName = "Test Player";
    }

    @Before
    public void setUp() {
        underTest = new PlayerImpl(playerName);
        assertNotNull("setUp: card", card);
    }

    @After
    public void tearDown() {
        underTest = null;
    }

    @Test
    public void testDefaultHand() {
        assertEquals("Hand Size", 0, underTest.getHandSize());
    }

    @Test
    public void testDefaultPlayerName() {
        assertEquals("Player name", playerName, underTest.getPlayerName());
    }

    @Test
    public void testDefaultPenaltyCard() {
        assertNull("Penalty card", underTest.getPenaltyCard());
        assertFalse("Has penalty card", underTest.hasPenaltyCard());
    }

    @Test
    public void testDefaultPlayerState() {
        assertEquals("Player state", PlayerState.INIT, underTest.getPlayerState());
    }

    @Test
    public void testClearPenaltyCard() {
        underTest.setPenaltyCard(card);
        assertEquals("Pre-condition: penalty card set", card, underTest.getPenaltyCard());
        underTest.clearPenaltyCard();
        assertNull("Penalty card", underTest.getPenaltyCard());
        assertFalse("Has penalty card", underTest.hasPenaltyCard());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPenaltyCardNull() {
        underTest.setPenaltyCard(null);
        fail("Penalty card set to null, expected IllegalArgumentException");
    }

    @Test
    public void testSetPenaltyCard8C() {
        underTest.setPenaltyCard(card);
        assertEquals("Penalty card", card, underTest.getPenaltyCard());
        assertTrue("Has penalty card", underTest.hasPenaltyCard());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPlayerStateNull() {
        underTest.setPlayerState(null);
        fail("Player state set to null, expected IllegalArgumentException");
    }

    @Test
    public void testSetPlayerStateWatching() {
        underTest.setPlayerState(PlayerState.WATCHING);
        assertEquals("Player state", PlayerState.WATCHING, underTest.getPlayerState());
    }
}
