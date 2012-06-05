package uk.co.essarsoftware.games.cards;

/**
 * Test harness class for Pack.
 */
public class TestPack extends Pack
{
    /**
     * Create a new pack from a list of cards.
     * @param cards a list of <tt>Card</tt>s to hold in the pack.
     */
    public TestPack(Card... cards) {
        super();

        for(Card c : cards) {
            add(new Card(13579, c.getSuit(), c.getValue()));
        }
    }

    /**
     * Get a card instance from the pack.
     * @param card the <tt>Card</tt> to look for.
     * @return a <tt>Card</tt> from the pack that matches the card supplied, or null if the card is not in the pack.
     */
    public Card getCard(Card card) {
        for(Card c : this) {
            if(c.sameCard(card)) {
                return c;
            }
        }
        return null;
    }
}
