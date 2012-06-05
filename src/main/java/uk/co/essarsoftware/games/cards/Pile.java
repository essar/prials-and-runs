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

    public Card pickup() {
        return cards.pop();
    }

    public int size() {
        return cards.size();
    }

}
