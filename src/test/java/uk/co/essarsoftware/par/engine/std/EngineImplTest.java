/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.par.engine.std;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.client.DirectClient;
import uk.co.essarsoftware.par.client.GameClient;
import uk.co.essarsoftware.par.engine.*;

import static org.junit.Assert.*;

/**
 * Test case for <tt>EngineImpl</tt>.
 * @author Steve Roberts <steve.roberts@essarsoftware.co.uk>
 * @version 1.0 (07-Jun-12)
 */
public class EngineImplTest
{
    protected Card card;
    protected PlayImpl play;
    protected GameImpl game;
    protected PlayerImpl player1, player2;
    protected GameClient client1, client2;

    EngineImpl underTest;

    public EngineImplTest() {
        card = Card.createCard(Card.Suit.CLUBS, Card.Value.EIGHT);
        play = new PrialImpl();
    }

    @Before
    public void setUp() {
        GameFactory gf = GameFactory.getInstance(StdGameFactory.class);
        game = (GameImpl) gf.createGame();
        underTest = (EngineImpl) gf.createEngine(game);
        client1 = new DirectClient(underTest, game.getController(), "Player 1");
        player1 = (PlayerImpl) client1.getPlayer();
        client2 = new DirectClient(underTest, game.getController(), "Player 2");
        player2 = (PlayerImpl) client2.getPlayer();
    }

    @After
    public void tearDown() {
        underTest.abortGame();
        underTest = null;
    }

    @Test(expected = InvalidPlayerStateException.class)
    public void testDiscard() throws EngineException {
        underTest.discard(player1, card);
    }

    @Test(expected = InvalidPlayerStateException.class)
    public void testPickupDiscard() throws EngineException {
        underTest.pickupDiscard(player1);
    }

    @Test(expected = InvalidPlayerStateException.class)
    public void testPickupDraw() throws EngineException {
        underTest.pickupDraw(player1);
    }

    @Test(expected = InvalidPlayerStateException.class)
    public void testPlayCards() throws EngineException {
        underTest.playCards(player1, play.getCards());
    }

    @Test(expected = InvalidPlayerStateException.class)
    public void testPegCard() throws EngineException {
        underTest.pegCard(player1, play, card);
    }

    @Test(expected = InvalidPlayerStateException.class)
    public void testResetPlays() throws EngineException {
        underTest.resetPlays(player1);
    }

    @Test(expected = InvalidPlayerStateException.class)
    public void testBuy() throws EngineException {
        underTest.buy(player1, player2);
    }

    @Test(expected = InvalidPlayerStateException.class)
    public void testApproveBuy() throws EngineException {
        underTest.approveBuy(player1, player2);
    }

    @Test(expected = InvalidPlayerStateException.class)
    public void testRejectBuy() throws EngineException {
        underTest.rejectBuy(player1, player2);
    }


    @Test(expected = InvalidPlayerStateException.class)
    public void testActivate() throws EngineException {
        underTest.activate(player1);
    }

    @Test(expected = InvalidPlayerStateException.class)
    public void testEndTurn() throws EngineException {
        underTest.endTurn(player1);
    }

    @Test(expected = EngineException.class)
    public void testEndGame() throws EngineException {
        underTest.endGame();
    }

    @Test
    public void testAboutGame() throws EngineException {
        underTest.abortGame();
    }

    @Test
    public void testStartGame() throws EngineException {
        underTest.startGame();

        assertEquals("Game in PP round", Round.PP, game.getCurrentRound());
        assertEquals("Game in first turn", 1, game.getTurn());
        assertNotNull("Current player not null", game.getCurrentPlayer());
        assertEquals("Current player in PICKUP state", PlayerState.PICKUP, game.getCurrentPlayer().getPlayerState());
    }

    @Test
    public void testStartRound() throws EngineException {
        underTest.startRound();
    }
}
