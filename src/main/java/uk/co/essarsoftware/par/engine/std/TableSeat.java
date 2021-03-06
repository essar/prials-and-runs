/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.par.engine.std;

import uk.co.essarsoftware.par.engine.Play;
import uk.co.essarsoftware.par.engine.Round;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 06/06/12
 * Time: 14:56
 * To change this template use File | Settings | File Templates.
 */
class TableSeat extends ArrayList<PlayImpl>
{
    TableSeat(Round round) {
        // Validate arguments
        if(round == null) {
            throw new IllegalArgumentException("Cannot create PlaySet with null Round");
        }

        // Initialise prials
        for(int i = 0; i < round.getPrials(); i ++) {
            add(new PrialImpl());
        }

        // Initialise runs
        for(int i = 0; i < round.getRuns(); i ++) {
            add(new RunImpl());
        }
    }
    
    PlayImpl lookupPlay(Play play) {
        return (indexOf(play) < 0 ? null : get(indexOf(play)));
    }

    void resetAll() {
        for(PlayImpl pi : this) {
            pi.resetPlay();
        }
    }

    public PlayImpl[] getPlays() {
        return toArray(new PlayImpl[size()]);
    }

    public int getUninitialisedPlayCount() {
        int ct = 0;
        for(PlayImpl play : this) {
            ct += ! play.isInitialised() ? 1 : 0;
        }
        return ct;
    }
}
