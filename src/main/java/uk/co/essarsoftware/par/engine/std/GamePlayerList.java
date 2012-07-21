/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.par.engine.std;

import uk.co.essarsoftware.par.engine.Player;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 06/06/12
 * Time: 13:45
 * To change this template use File | Settings | File Templates.
 */
class GamePlayerList extends ArrayList<PlayerImpl>
{
    /**
     * Get the next player in the player list.
     * @param thisPlayer the current player, or player to look for the next player.
     * @return a <tt>PlayerImpl</tt> that is the next player from the one provided, or null if <tt>thisPlayer</tt> is not found or is null.
     */
    PlayerImpl getNextPlayer(PlayerImpl thisPlayer) {
        if(thisPlayer == null) {
            return null;
        }
        if(contains(thisPlayer)) {
            int index = indexOf(thisPlayer) + 1;
            return get(index >= size() ? 0 : index);
        }
        return null;
    }
    
    PlayerImpl[] getPlayers() {
        return toArray(new PlayerImpl[size()]);
    }
    
    PlayerImpl getRandomPlayer() {
        int index = (int) (Math.random() * (double) size());
        return get(index);
    }
    
    PlayerImpl lookupPlayer(Player player) {
        return (indexOf(player) < 0 ? null : get(indexOf(player)));
    }
}
