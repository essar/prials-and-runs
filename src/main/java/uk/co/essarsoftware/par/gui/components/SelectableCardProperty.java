/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.par.gui.components;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.gui.beans.SelectableCardBean;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 26/06/12
 * Time: 11:37
 * To change this template use File | Settings | File Templates.
 */
class SelectableCardProperty extends BaseProperty
{
    private final SelectableCardBean cardBean = new SelectableCardBean();

    public Card getCard() {
        return cardBean.getCard();
    }
    
    public boolean isFaceUp() {
        return cardBean.isFaceUp();
    }
    
    public boolean isSelected() {
        return cardBean.isSelected();
    }
    
    public void setCard(Card card) {
        Card oldCard = cardBean.getCard();
        cardBean.setCard(card);
        pcs.firePropertyChange("card", oldCard, card);
    }

    public void setFaceUp(boolean faceUp) {
        boolean oldFaceUp = cardBean.isFaceUp();
        cardBean.setFaceUp(faceUp);
        pcs.firePropertyChange("faceUp", oldFaceUp, faceUp);
    }

    public void setSelected(boolean selected) {
        boolean oldSelected = cardBean.isSelected();
        cardBean.setSelected(selected);
        pcs.firePropertyChange("selected", oldSelected, selected);
    }
}
