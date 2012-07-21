/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.games.cards;

import java.util.Collection;
import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 12:41
 * To change this template use File | Settings | File Templates.
 */
class CardStack extends Stack<Card>
{

    @Override
    public boolean add(Card card) {
        if(card.packID == 0) {
            throw new IllegalArgumentException("Card must have pack ID");
        }
        return super.add(card);
    }

    @Override
    public void add(int index, Card card) {
        if(card.packID == 0) {
            throw new IllegalArgumentException("Card must have pack ID");
        }
        super.add(index, card);
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

    @Override
    public void addElement(Card card) {
        if(card.packID == 0) {
            throw new IllegalArgumentException("Card must have pack ID");
        }
        super.add(card);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Card> cards) {
        for(Card c : cards) {
            if(c.packID == 0) {
                throw new IllegalArgumentException("Card must have pack ID");
            }
        }
        return super.addAll(index, cards);
    }
    @Override
    public synchronized void insertElementAt(Card card, int index) {
        if(card.packID == 0) {
            throw new IllegalArgumentException("Card must have pack ID");
        }

        super.insertElementAt(card, index);
    }

    @Override
    public Card push(Card card) {
        if(card.packID == 0) {
            throw new IllegalArgumentException("Card must have pack ID");
        }
        return super.push(card);
    }

    @Override
    public synchronized Card set(int index, Card card) {
        if(card.packID == 0) {
            throw new IllegalArgumentException("Card must have pack ID");
        }
        return super.set(index, card);
    }

    @Override
    public synchronized void setElementAt(Card card, int index) {
        if(card.packID == 0) {
            throw new IllegalArgumentException("Card must have pack ID");
        }
        super.setElementAt(card, index);
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("|");
        for(Card c : this) {
            buf.append(c);
            buf.append("|");
        }
        return new String(buf);
    }
}
