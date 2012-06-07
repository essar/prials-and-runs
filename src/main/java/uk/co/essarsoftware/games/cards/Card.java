package uk.co.essarsoftware.games.cards;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 12:39
 * To change this template use File | Settings | File Templates.
 */
public class Card implements Comparable<Card>
{
    private final Suit suit;
    private final Value value;

    final long packID;

    private Card(Suit suit, Value value) {
        this.packID = 0;
        this.suit = suit;
        this.value = value;
    }

    protected Card(long packID) {
        this(packID, null, null);
    }

    Card(long packID, Suit suit, Value value) {
        this.packID = packID;
        this.suit = suit;
        this.value = value;
    }
    
    @Override
    public int compareTo(Card c) {
        if(c == null) {
            return 1;
        }
        if(suit == c.suit) {
            return value.compareTo(c.value);
        } else {
            return suit.compareTo(c.suit);
        }
    }

    /**
     * Checks if two card objects are the same.
     * @param o another <tt>Card</tt> object to compare with this one.
     * @return <tt>true</tt> if both <tt>Card</tt>s are from the same pack and have same suit and value, <tt>false</tt>
     * otherwise, or if <tt>c</tt> is null, not a <tt>Card</tt> object or does not have a pack identifier.
     */
    @Override
    public boolean equals(Object o) {
        if(o == null) {
            return false;
        }
        if(! (o instanceof Card)) {
            return false;
        }
        Card c = (Card) o;
        return ((c.packID | packID) != 0 && c.packID == packID && sameCard(c));
    }

    public Suit getSuit() {
        return suit;
    }

    public Value getValue() {
        return value;
    }

    /**
     * Checks if another card has the same suit and value as this one.
     * @param c another <tt>Card</tt> object to check.
     * @return <tt>true</tt> if both cards have the same suit and value, <tt>false</tt> otherwise, or if <tt>c</tt> is null.
     */
    public boolean sameCard(Card c) {
        return sameSuit(c) && sameValue(c);
    }

    /**
     * Checks if another card has the same suit as this one.
     * @param c another <tt>Card</tt> object to check.
     * @return <tt>true</tt> if both cards have the same suit, <tt>false</tt> otherwise, or if <tt>c</tt> is null.
     */
    public boolean sameSuit(Card c) {
        return (c != null && c.suit.equals(suit));
    }

    /**
     * Checks if another card has the same value as this one.
     * @param c another <tt>Card</tt> object to check.
     * @return <tt>true</tt> if both cards have the same value, <tt>false</tt> otherwise, or if <tt>c</tt> is null.
     */
    public boolean sameValue(Card c) {
        return c != null && c.value.equals(value);
    }
    
    @Override
    public String toString() {
        return value + "" + suit;
    }

    public static Card createCard(Suit suit, Value value) {
        return new Card(suit, value);
    }

    public enum Suit
    {
        CLUBS, DIAMONDS, HEARTS, SPADES;

        public String toString() {
            return name().substring(0, 1);
        }
    }

    public enum Value
    {
        ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING;
        
        public String toString() {
            switch(this) {
                case ACE: return "A";
                case JACK: return "J";
                case QUEEN: return "Q";
                case KING: return "K";
                default: return "" + ordinal();
            }
        }
    }
}
