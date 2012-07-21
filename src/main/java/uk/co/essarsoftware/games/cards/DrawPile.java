/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.games.cards;

import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 15:36
 * To change this template use File | Settings | File Templates.
 */
public class DrawPile extends Pile
{
    public DrawPile() {

    }

    public void add(Pack pack) {
        cards.addAll(pack);
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }
}
