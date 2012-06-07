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
