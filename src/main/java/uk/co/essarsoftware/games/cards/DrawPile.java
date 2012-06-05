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

    void add(Pack pack) {
        cards.addAll(pack);
    }

    public CardArray[] deal(int hands, int handSize) {
        CardArray[] hs = new CardArray[hands];
        for(int i = 0; i < handSize; i ++) {
            for(int ii = 0; ii < hands; ii ++) {
                if(hs[ii] == null) {
                    hs[ii] = new CardArray();
                }
                hs[ii].add(cards.pop());
            }
        }
        return hs;
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }
}
