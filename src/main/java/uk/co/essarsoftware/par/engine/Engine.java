/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.par.engine;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.client.GameClient;

/**
 * Interface defining a class that controls the game play of a Prials And Runs game.
 *
 * The Engine is responsible for enforcing the rules of the game and maintaining game object states.  Methods on the
 * engine are called by client classes to execute a particular game process.
 */
public interface Engine
{
    /**
     * Discard a card from the hand.
     * @param player the <tt>Player</tt> calling this action.  Must be current player and in <tt>DISCARD</tt> or <tt>PLAYED</tt> state.
     * @param card the <tt>Card</tt> to discard. Must exist within the player's hand.
     * @throws EngineException if a pre-condition fails or an error occurs within the engine.
     */
    public void discard(Player player, Card card) throws EngineException;

    /**
     * Pickup the top card from the discard pile on the table.
     * @param player the <tt>Player</tt> calling this action.  Must be current player and in <tt>PICKUP</tt> state.
     * @return the <tt>Card</tt> that was picked up.
     * @throws EngineException if a pre-condition fails or an error occurs within the engine.
     */
    public Card pickupDiscard(Player player) throws EngineException;

    /**
     * Pickup the top card from the draw pile on the table.
     * @param player the <tt>Player</tt> calling this action.  Must be current player and in <tt>PICKUP</tt> state.
     * @return the <tt>Card</tt> that was picked up.
     * @throws EngineException if a pre-condition fails or an error occurs within the engine.
     */
    public Card pickupDraw(Player player) throws EngineException;


    /**
     * Play a set of playCards onto the table.
     * @param player the <tt>Player</tt> calling this action.  Must be current player and in <tt>DISCARD</tt> or <tt>PLAYING</tt> state.
     * @param cards an array of <tt>Card</tt>s that form the play.  Must either be a valid prial or run.
     * @throws EngineException if a pre-condition fails or an error occurs within the engine.
     */
    public void playCards(Player player, Card[] cards) throws EngineException;

    /**
     * Peg a card onto an existing play on the table.
     * @param player the <tt>Player</tt> calling this action.  Must be the current player and in <tt>PEGGING</tt> state.
     * @param play the <tt>Play</tt> to put this pegging card on to.
     * @param card the <tt>Card</tt> to peg.  Must be a valid pegging card for the play.
     * @throws EngineException if a pre-condition fails or an error occurs within the engine.
     */
    public void pegCard(Player player, Play play, Card card) throws EngineException;

    /**
     * Reset the current player and remove any plays.
     * @param player the <tt>Player</tt> to reset.  Must be the current player and in <tt>PLAYING</tt> state.
     * @throws EngineException if a pre-condition fails or an error occurs within the engine.
     */
    public void resetPlays(Player player) throws EngineException;

    /**
     * Request to buy the top card from the discard pile on the table.  Buys are not allowed in PP round or in turn 1 of any round.
     * @param player the <tt>Player</tt> to buy the card from.  Must be the current player and in <tt>PICKUP</tt> state.
     * @param buyer the <tt>Player</tt> who is buying this card.  Must not be the current player and must be in <tt>WATCHING</tt> state.
     * @return <tt>true</tt> if the buy request is valid, <tt>false</tt> otherwise.
     * @throws EngineException if a pre-condition fails or an error occurs within the engine.
     */
    public boolean buy(Player player, Player buyer) throws EngineException;

    /**
     * Approve the buy request received.
     * @param player the <tt>Player</tt> approving the buy request.  Must be the current player and in <TT>BUY_REQ</TT> state.
     * @param buyer the <tt>Player</tt> buying the card.  Must not be the current player and must be in <TT>BUYING</TT> state.
     * @return the <tt>Card</tt> that is picked up by the current player.
     * @throws EngineException if a pre-condition fails or an error occurs within the engine.
     */
    public Card approveBuy(Player player, Player buyer) throws EngineException;

    /**
     * Reject the buy request received.
     * @param player the <tt>Player</tt> rejecting the buy request.  Must be the current player and in <TT>BUY_REQ</TT> state.
     * @param buyer the <tt>Player</tt> buying the card.  Must not be the current player and must be in <TT>BUYING</TT> state.
     * @return the <TT>Card</TT> that is picked up by the current player.
     * @throws EngineException if a pre-condition fails or an error occurs within the engine.
     */
    public Card rejectBuy(Player player, Player buyer) throws EngineException;


    public GameClient createClient(Player player, PlayerUI ui);

    public void abortGame();

    public void startGame() throws EngineException;
}
