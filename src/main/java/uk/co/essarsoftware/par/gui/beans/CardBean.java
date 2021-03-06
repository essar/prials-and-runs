/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.par.gui.beans;

import uk.co.essarsoftware.games.cards.Card;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 26/06/12
 * Time: 11:31
 * To change this template use File | Settings | File Templates.
 */
public class CardBean
{
    private Card card = null;
    private boolean faceUp = true;

    @Override
    public boolean equals(Object o) {
        if(o == null) {
            return false;
        }
        if(o instanceof CardBean) {
            CardBean cb = (CardBean) o;
            return card != null && card.equals(cb.card) && faceUp == cb.faceUp;
        }
        return false;
    }
    
    public Card getCard() {
        return card;
    }
    
    public boolean isFaceUp() {
        return faceUp;
    }
    
    public void setCard(Card card) {
        this.card = card;
    }

    public void setFaceUp(boolean faceUp) {
        this.faceUp = faceUp;
    }
}
