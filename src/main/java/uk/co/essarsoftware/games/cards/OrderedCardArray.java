/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.games.cards;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 15:10
 * To change this template use File | Settings | File Templates.
 */
public class OrderedCardArray extends TreeSet<Card>
{
    public OrderedCardArray(Comparator<Card> comparator) {
        super(comparator);
    }

    @Override
    public boolean add(Card card) {
        if(card.packID == 0) {
            throw new IllegalArgumentException("Card must have pack ID");
        }
        return super.add(card);
    }

    @Override
    public boolean addAll(Collection<? extends Card> cards) {
        for(Card c : cards) {
            if(c.packID == 0) {
                throw new IllegalArgumentException("Card must have pack ID");
            }
        }
        return super.addAll(cards);
    }

    public CardArray toCardArray() {
        CardArray cs = new CardArray();
        cs.addAll(this);
        return cs;
    }
}
