/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.games.cards;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 12:39
 * To change this template use File | Settings | File Templates.
 */
abstract class Pile
{
    protected CardStack cards;

    protected Pile() {
        cards = new CardStack();
    }

    public void clear() {
        cards.clear();
    }

    public Card pickup() {
        return cards.pop();
    }

    public int size() {
        return cards.size();
    }

}
