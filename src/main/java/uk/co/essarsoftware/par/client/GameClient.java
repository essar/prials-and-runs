/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.par.client;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.engine.Game;
import uk.co.essarsoftware.par.engine.Play;
import uk.co.essarsoftware.par.engine.Player;

/**
 * Interface defining methods available to game clients.
 * @author Steve Roberts <steve.roberts@essarsoftware.co.uk>
 * @version 1.0 (07-Jun-12)
 */
public interface GameClient extends Game, Player
{
    /* ** GAME METHODS **/
    public Card approveBuy(Player buyer);
    
    public boolean buy(Player player, Card card);

    public void discard(Card card);

    public void pegCard(Play play, Card card);

    public Card pickupDiscard();
    
    public Card pickupDraw();

    public void playCards(Card[] cards);

    public Card rejectBuy(Player buyer);

    public void resetPlays();

    /* ** PLAYER METHODS ** */
    public Card[] getHand();

    public Play[] getPlays();

    public Card getPenaltyCard();

    public Player getPlayer();
}
