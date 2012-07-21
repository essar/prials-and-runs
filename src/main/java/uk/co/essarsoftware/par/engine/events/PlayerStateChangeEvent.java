/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.par.engine.events;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.engine.Player;
import uk.co.essarsoftware.par.engine.PlayerState;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 06/06/12
 * Time: 16:05
 * To change this template use File | Settings | File Templates.
 */
public class PlayerStateChangeEvent extends AbstractGameEvent
{
    private PlayerState newState, oldState;

    public PlayerStateChangeEvent(Player player, PlayerState oldState, PlayerState newState) {
        super(player);
        this.oldState = oldState;
        this.newState = newState;
    }

    public PlayerState getNewState() {
        return newState;
    }

    public PlayerState getOldState() {
        return oldState;
    }
}
