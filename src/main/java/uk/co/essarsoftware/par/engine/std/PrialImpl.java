/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.par.engine.std;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.engine.Prial;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 17:26
 * To change this template use File | Settings | File Templates.
 */
class PrialImpl extends PlayImpl implements Prial
{
    private Card.Value prialValue;

    public PrialImpl() {
        super(new Comparator<Card>() {
            @Override
            public int compare(Card c1, Card c2) {
                if(c1 == null) {
                    return -1;
                }
                if(c2 == null) {
                    return 1;
                }
                return c1.compareTo(c2);
            }
        });
    }

    @Override
    protected boolean addCard(Card card) {
        if(super.addCard(card)) {
            prialValue = card.getValue();
            return true;
        }
        return false;
    }

    @Override
    protected void cloneFrom(PlayImpl play) {
        PrialImpl prial = (PrialImpl) play;
        super.cloneFrom(prial);
        prialValue = prial.prialValue;
    }

    @Override
    protected void resetPlay() {
        super.resetPlay();
        prialValue = null;
    }

    @Override
    public Card[] getAllowableCards() {
        // Uninitialised prial, return null
        if(! isInitialised()) {
            return null;
        }
        // If prial value is not set or prial size is zero then return empty array - all cards allowed
        if(prialValue == null || size() == 0) {
            return new Card[0];
        }
        // Return an array of all cards with prial value
        ArrayList<Card> allowableCards = new ArrayList<Card>();
        for(Card.Suit s : Card.Suit.values()) {
            allowableCards.add(Card.createCard(s, prialValue));
        }
        return allowableCards.toArray(new Card[allowableCards.size()]);
    }

    @Override
    public boolean isPrial() {
        return true;
    }
}
