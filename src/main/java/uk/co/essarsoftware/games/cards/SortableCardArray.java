package uk.co.essarsoftware.games.cards;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 15:10
 * To change this template use File | Settings | File Templates.
 */
public class SortableCardArray extends CardArray
{
    public void moveCardAfter(Card card, Card after) {
        int index = indexOf(findCard(after)) + 1;
        moveCardTo(card, index);
    }

    public void moveCardBefore(Card card, Card before) {
        int index = indexOf(findCard(before));
        if(index < 0) {
            index = size();
        }
        moveCardTo(card, index);
    }

    public void moveCardDown(Card card) {
        int index = indexOf(findCard(card)) - 1;
        moveCardTo(card, index);
    }

    public void moveCardUp(Card card) {
        int index = indexOf(findCard(card)) + 1;
        moveCardTo(card, index);
    }

    public void moveCardTo(Card card, int index) {
        if(remove(card)) {
            add(index, card);
        }
    }

    public void sortBySuit() {

    }

    public void sortByValue() {

    }
}
