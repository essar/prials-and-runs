package uk.co.essarsoftware.games.cards;

/**
 * <p>Class representing a standard playing card.</p> Card objects must have a suit (CLUBS, DIAMONDS, HEARTS or SPADES) and value (ACE-KING).</p>
 * <p>Cards are optionally generated with an internal PackID that uniquely identifies a card instance within multi-pack scenarios.</p>
 * @author Steve Roberts <steve.roberts@essarsoftware.co.uk>
 * @version 1.0 (07-Jun-12)
 */
public class Card implements Comparable<Card>
{
    private final Suit suit;
    private final Value value;

    final long packID;

    /**
     * Create a new card object with specified suit and value.
     * @param suit the <tt>Suit</tt> of this card.
     * @param value the <tt>Value</tt> of this card.
     */
    private Card(Suit suit, Value value) {
        this(0, suit, value);
    }

    /**
     * Create a new card object with specified suit and value, belonging to the specified pack.
     * @param packID unique pack identifier for the pack this card belongs to.
     * @param suit the <tt>Suit</tt> of this card.
     * @param value the <tt>Value</tt> of this card.
     */
    Card(long packID, Suit suit, Value value) {
        this.packID = packID;
        this.suit = suit;
        this.value = value;
    }

    /**
     * Compares two playCards for sorting.  Default sort is by suit, then value.
     * @param c another <tt>Card</tt> to compare this object with.
     * @return an integer defining the relative position of this object to the specified object.
     */
    @Override
    public int compareTo(Card c) {
        if(c == null) {
            return 1;
        }
        // If identical object then zero
        if(equals(c)) {
            return 0;
        }
        // Unbounded joker so always first
        if(isJoker() && ! isBoundJoker()) {
            return -1;
        }
        // Unbounded jokers are always first
        if(c.isJoker() && ! c.isBoundJoker()) {
            return 1;
        }
        // Same card - put a joker first then sort using pack ID
        if(sameCard(c)) {
            return (c.isJoker() ? 1 : (packID == 0 ? -1 : c.packID == 0 ? 1 : (int) (c.packID - packID)));
        }
        // Same suit so sort by value, otherwise sort by suit
        if(sameSuit(c)) {
            return (c.getValue() == null ? 1 : getValue().compareTo(c.getValue()));
        } else {
            return (c.getSuit() == null ? 1 : getSuit().compareTo(c.getSuit()));
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
        System.out.println(String.format("Comparing %s to %s", this, o));
        // Cannot be equal to null
        if(o == null) {
            return false;
        }
        // Cannot equal non-card
        if(! (o instanceof Card)) {
            return false;
        }
        // Must be card with same pack ID (but not zero) and same card (same suit/value)
        Card c = (Card) o;
        return ((packID | c.packID) != 0 && c.packID == packID && sameCard(c));
    }

    /**
     * Returns the suit of this card.
     * @return the <tt>Suit</tt> of this card.
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Returns the value of this card.
     * @return the <tt>Value</tt> of this card.
     */
    public Value getValue() {
        return value;
    }

    public boolean isBoundJoker() {
        return false;
    }

    /**
     * Indicates if this card is a Joker.
     * @return Always returns false.
     */
    public boolean isJoker() {
        return false;
    }

    /**
     * Checks if another card has the same suit and value as this one.
     * @param c another <tt>Card</tt> object to check.
     * @return <tt>true</tt> if both cards have the same suit and value; <tt>false</tt> otherwise, or if this card is an unbound joker, or if <tt>c</tt> is null.
     */
    public boolean sameCard(Card c) {
        /*return c != null && ((isJoker() && !isBoundJoker())
                            || (c.isJoker() && !c.isBoundJoker())
                            || (sameSuit(c) && sameValue(c)));*/
        return c != null && !(isJoker() && !isBoundJoker()) && sameSuit(c) && sameValue(c);
    }

    /**
     * Checks if another card has the same suit as this one.
     * @param c another <tt>Card</tt> object to check.
     * @return <tt>true</tt> if both cards have the same suit; <tt>false</tt> otherwise, or if or this card is an unbound joker, or if <tt>c</tt> is null.
     */
    public boolean sameSuit(Card c) {
        /*return c != null && ((isJoker() && !isBoundJoker())
                            || (c.isJoker() && !c.isBoundJoker())
                            || getSuit().equals(c.getSuit()));*/
        return c != null && !(isJoker() && !isBoundJoker()) && getSuit().equals(c.getSuit());
    }

    /**
     * Checks if another card has the same value as this one.
     * @param c another <tt>Card</tt> object to check.
     * @return <tt>true</tt> if both cards have the same value; <tt>false</tt> otherwise, or if or this card is an unbound joker, or if <tt>c</tt> is null.
     */
    public boolean sameValue(Card c) {
        /*return c != null && ((isJoker() && !isBoundJoker())
                            || (c.isJoker() && !c.isBoundJoker())
                            || getValue().equals(c.getValue()));*/
        return c != null && !(isJoker() && !isBoundJoker()) && getValue().equals(c.getValue());
    }
    
    @Override
    public String toString() {
        return value + "" + suit;
    }

    /**
     * Create a new <tt>Card</tt> instance, with the specified suit and value.
     * @param suit the <tt>Suit</tt> of this card. Cannot be null.
     * @param value the <tt>Value</tt> of this card. Cannot be null.
     * @return the <tt>Card</tt> object created, unassociated with any pack.
     */
    public static Card createCard(Suit suit, Value value) {
        if(suit == null) {
            throw new IllegalArgumentException("Cannot create card with null suit");
        }
        if(value == null) {
            throw new IllegalArgumentException("Cannot create card with null value");
        }
        return new Card(suit, value);
    }

    public static Joker createJoker() {
        return new Joker();
    }

    /**
     * Enumeration defining possible card suits.
     */
    public enum Suit
    {
        CLUBS, DIAMONDS, HEARTS, SPADES;

        public String toString() {
            return name().substring(0, 1);
        }
    }

    /**
     * Enumeration defining possible card values.
     */
    public enum Value
    {
        ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING;
        
        public String toString() {
            switch(this) {
                case ACE: return "A";
                case JACK: return "J";
                case QUEEN: return "Q";
                case KING: return "K";
                default: return "" + (ordinal() + 1);
            }
        }
    }



    public static class Joker extends Card
    {
        private static int nextSerial = 1;

        private Card boundCard;
        private int serial;

        private Joker() {
            this(0);
            serial = nextSerial ++;
        }

        /**
         * Create a new Joker, associated with the specified pack.
         * @param packID unique pack identifier for the pack this card belongs to.
         */
        Joker(long packID) {
            super(packID, null, null);
            boundCard = null;
        }

        /**
         * Bind this Joker to another card.
         * @param boundCard the <tt>Card</tt> to bind this Joker to.
         */
        public void bindTo(Card boundCard) {
            if(boundCard.isJoker()) {
                throw new IllegalArgumentException("Cannot bind joker to joker");
            }
            this.boundCard = boundCard;
        }

        @Override
        public int compareTo(Card c) {
            if(c == null) {
                return 1;
            }
            // If identical object then zero
            if(equals(c)) {
                return 0;
            }
            // Otherwise unbounded jokers are always first
            if(isJoker() && ! isBoundJoker()) {
                return -1;
            }
            // Compare to bound card
            return boundCard.compareTo(c);
        }

        @Override
        public boolean equals(Object o) {
            // Cannot be equal to null
            if(o == null) {
                return false;
            }
            // Cannot equal non-joker
            if(! (o instanceof Joker)) {
                return false;
            }
            // Must be a joker with same pack ID (but not zero) and same serial
            Joker j = (Joker) o;
            return ((packID | j.packID) != 0 && j.packID == packID && (serial == j.serial));
        }

        @Override
        public Suit getSuit() {
            return (boundCard == null ? null : boundCard.getSuit());
        }

        @Override
        public Value getValue() {
            return (boundCard == null ? null : boundCard.getValue());
        }

        /**
         * Indicates if this Joker is bound to another card.
         * @return <tt>true</tt> if this Joker is bound to a card, false otherwise.
         */
        @Override
        public boolean isBoundJoker() {
            return boundCard != null;
        }

        /**
         * Indicates if this card is a Joker.
         * @return always returns true.
         */
        @Override
        public boolean isJoker() {
            return true;
        }

        @Override
        public String toString() {
            return String.format("J<%s>", (boundCard == null ? "*" : boundCard));
        }
    }
}
