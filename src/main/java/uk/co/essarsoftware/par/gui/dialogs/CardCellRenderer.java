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
