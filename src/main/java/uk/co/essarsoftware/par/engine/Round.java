/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.par.engine;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 12:33
 * To change this template use File | Settings | File Templates.
 */
public enum Round
{
    START(0,0), PP(2,0), PR(1,1), RR(0,2), PPR(2,1), PRR(1,2), PPP(3,0), RRR(0,3), END(0,0);

    private final int prials, runs;

    private Round(int prials, int runs) {
        this.prials = prials;
        this.runs = runs;
    }

    public int getPrials() {
        return prials;
    }

    public int getRuns() {
        return runs;
    }
}
