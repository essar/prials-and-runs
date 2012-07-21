/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.par.gui.dialogs;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.gui.components.CardComponent;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/07/12
 * Time: 10:38
 * To change this template use File | Settings | File Templates.
 */
public class CardCellRenderer implements ListCellRenderer
{
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if(value == null) {
            return null;
        }
        if(value instanceof Card) {
            Card card = (Card) value;
            CardComponent cc = new CardComponent(true, false);
            cc.setCard(card);
            return cc;
        }
        return null;
    }
}
