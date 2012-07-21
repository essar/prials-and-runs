/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

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
public class PlayerImplTest
{
    private Card unregisteredCard;
    private CardArray hand;
    private String playerName;
    
    protected Card card;

    PlayerImpl underTest;
    
    static {
        BasicConfigurator.resetConfiguration();
        Logger.getRootLogger().setLevel(Level.ERROR);
        Logger.getLogger(Player.class).setLevel(Level.DEBUG);
    }

    public PlayerImplTest() {
        card = Card.createCard(Card.Suit.CLUBS, Card.Value.EIGHT);
        unregisteredCard = Card.createCard(Card.Suit.CLUBS, Card.Value.EIGHT);

        // Register card with pack
        TestPack tp = new TestPack();
        tp.add(card);
        card = tp.findCard(card);

        hand = new CardArray();
        hand.add(card);

        playerName = "Test Player";
    }

    protected static <E> void assertContains(String message, E o, E[] a) {
        Arrays.sort(a);
        assertTrue(message, Arrays.binarySearch(a, o) >= 0);
    }

    protected static <E> void assertNotContains(String message, E o, E[] a) {
        Arrays.sort(a);
        assertFalse(message, Arrays.binarySearch(a, o) >= 0);
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
        assertEquals("Hand", 0, underTest.getHand().length);
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

    @Test(expected = IllegalArgumentException.class)
    public void testDealNull() {
        underTest.deal(null);
        fail("Hand set to null, expected IllegalArgumentException");
    }
    
    @Test
    public void testDealHand() {
        underTest.deal(hand);
        assertEquals("Hand", 1, underTest.getHand().length);
        assertEquals("Hand Size", 1, underTest.getHandSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiscardNull() throws CardNotFoundException {
        underTest.discard(null);
        fail("Discarded null, expected IllegalArgumentException");
    }

    @Test(expected = CardNotFoundException.class)
    public void testDiscard8C() throws CardNotFoundException {
        underTest.discard(card);
        fail("Unknown card discarded, expected CardNotFoundException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPickupNull() throws DuplicateCardException {
        underTest.pickup(null);
        fail("Picked up null, expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPickupUnregistered() throws DuplicateCardException {
        underTest.pickup(unregisteredCard);
        fail("Unregistered card picked up, expected IllegalArgumentException");
    }

    @Test
    public void testPickup8C() throws DuplicateCardException {
        underTest.pickup(card);
        assertContains("Card in hand", card, underTest.getHand());
    }

    @Test
    public void testClearPenaltyCard() {
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
