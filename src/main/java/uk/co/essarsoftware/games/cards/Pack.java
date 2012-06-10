package uk.co.essarsoftware.games.cards;

import java.util.UUID;

/**
 * Class representing a pack of 52 or 54 playing playCards.
 */
public class Pack extends CardStack
{
    /** Internal unique pack identifier. */
    private final long packID;

    /**
     * Create a new pack with a random identifier.
     */
    protected Pack() {
        this(UUID.randomUUID().getLeastSignificantBits());
    }

    /**
     * Create a new pack with the specified identifier.
     * @param packID a unique number identifying playCards from this pack.
     */
    protected Pack(long packID) {
        this.packID = packID;
    }

    /**
     * Create a new pack of 52 playCards.
     * @return a <tt>Pack</tt> object containing all 52 playing playCards.
     */
    public static Pack generatePack() {
        Pack p = new Pack();

        for(Card.Suit s : Card.Suit.values()) {
            for(Card.Value v : Card.Value.values()) {
                p.add(new Card(p.packID, s, v));
            }
        }

        assert(p.size() == 52);
        return p;
    }

    /**
     * Create a new pack of 54 playCards, including jokers.
     * @return a <tt>Pack</tt> object containing all 52 playing playCards plus 2 jokers.
     */
    public static Pack generatePackWithJokers() {
        Pack p = generatePack();
        p.add(new Card.Joker(p.packID));
        p.add(new Card.Joker(p.packID));

        assert(p.size() == 54);
        return p;
    }
}
