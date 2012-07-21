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
public class PlayerImplWithHandTest extends PlayerImplTest
{
    private Card newCard1, newCard2;
    private CardArray hand, newHand;

    static {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.ERROR);
        Logger.getLogger(Player.class).setLevel(Level.DEBUG);
    }

    public PlayerImplWithHandTest() {
        super();

        newCard1 = Card.createCard(Card.Suit.SPADES, Card.Value.ACE);
        newCard2 = Card.createCard(Card.Suit.DIAMONDS, Card.Value.FIVE);

        // Register playCards with pack
        TestPack tp = new TestPack();
        tp.add(newCard1);
        tp.add(newCard2);
        newCard1 = tp.findCard(newCard1);
        newCard2 = tp.findCard(newCard2);

        hand = new CardArray();
        hand.add(card);
        
        newHand = new CardArray();
        newHand.add(newCard1);
        newHand.add(newCard2);
    }

    @Override@Before
    public void setUp() {
        super.setUp();
        underTest.deal(hand);
        assertEquals("setUp: hand", 1, underTest.getHandSize());
    }

    @Override@Test
    public void testDefaultHand() {
        assertEquals("Hand", 1, underTest.getHand().length);
        assertEquals("Hand size", 1, underTest.getHandSize());
    }

    @Test
    public void testDealHand() {
        underTest.deal(newHand);
        assertEquals("Hand size", 2, underTest.getHandSize());
        assertNotContains("Old card no longer in hand", card, underTest.getHand());
        assertContains("New card in hand", newCard1, underTest.getHand());
    }

    @Override@Test
    public void testDiscard8C() throws CardNotFoundException {
        underTest.discard(card);
        assertNotContains("Card no longer in hand", card, underTest.getHand());
        assertEquals("Hand size", 0, underTest.getHandSize());
    }

    @Override@Test(expected = DuplicateCardException.class)
    public void testPickup8C() throws DuplicateCardException {
        underTest.pickup(card);
        fail("Picked up duplicate card, expected DuplicateCardException");
    }
}
