package uk.co.essarsoftware.par.engine;

import org.apache.log4j.Logger;
import uk.co.essarsoftware.games.cards.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/07/12
 * Time: 12:28
 * To change this template use File | Settings | File Templates.
 */
public abstract class PlayBuilder
{
    private static Logger log = Logger.getLogger(Play.class);

    // Class uses depth-first search algorithm to try and build a play

    public abstract boolean addCard(Play play, Card c);

    public abstract Play initNewPlay(Play play, boolean clone);

    private boolean addCardToList(Card card, List<Card> list) {
        for(Card c : list) {
            if(c.sameCard(card)) {
                return false;
            }
        }
        return list.add(card);
    }

    private boolean testCard(Card c, Play play, List<Card> remaining) {
        // Validate arguments
        if(c == null) {
            log.info(String.format("[PlayBuilder] Cannot test null card"));
            return false;
        }
        if(play == null) {
            log.info(String.format("[PlayBuilder] Cannot test null play"));
            return false;
        }

        log.debug(String.format("[PlayBuilder] Testing %s against %s (%s)", c, play, Arrays.toString(play.getAllowableCards())));
        // Attempt to add card
        if(addCard(play, c)) {
            log.info(String.format("[PlayBuilder] %s added OK", c));
            // Recursively try the next card
            return testNext(play, remaining);
        }
        log.info(String.format("[PlayBuilder] %s not added", c));
        // Card not added
        remaining.add(0, c);
        return false;
    }

    private boolean testNext(Play play, List<Card> remaining) {
        if(remaining.size() == 0) {
            log.info(String.format("[PlayBuilder] All cards played"));
            log.debug(String.format("[PlayBuilder] Play: %s", play));
            // All cards played so return true
            return true;
        }
        // Take first card from remaining list
        Card c = remaining.remove(0);

        log.debug(String.format("[PlayBuilder] Next card: %s", c));
        log.debug(String.format("[PlayBuilder] Play: %s", play));
        log.debug(String.format("[PlayBuilder] Remaining: %s", remaining));
        return testCard(c, play, remaining);
    }

    public Play build(Play play, Card[] cards) {
        ArrayList<Card> allCards = new ArrayList<Card>(Arrays.asList(cards));
        ArrayList<Card> remaining;
        Play pl;

        // Initial validation
        if(cards.length != 3) {
            log.info(String.format("[PlayBuilder] Unable to build a play using %d cards, need 3 cards", cards.length));
            return null;
        }

        do {
            // Initialise new play
            pl = initNewPlay(play, false);

            // Initialise remaining card array and rotate all cards array
            remaining = new ArrayList<Card>(allCards);
            allCards.add(allCards.remove(0));

            // Try and build a play
            if(testNext(pl, remaining)) {
                // Return the play
                return pl;
            }

        } while(! cards[0].equals(allCards.get(0)));

        // No play could be built
        return null;
    }

    private List<Card> getPossibleCards(Card.Joker joker, Play play, Card[] cards) {
        ArrayList<Card> possibleCards = new ArrayList<Card>();
        ArrayList<Card> allCards = new ArrayList<Card>(Arrays.asList(cards));
        ArrayList<Card> remaining;
        Play pl;

        do {
            // Initialise new play
            pl = initNewPlay(play, false);

            // Initialise remaining card array and rotate all cards array
            remaining = new ArrayList<Card>();
            for(Card c : cards) {
                // Add all non-jokers to the all cards array
                if(! (c.isJoker() && ! c.isBoundJoker())) {
                    remaining.add(c);
                }
            }
            allCards.add(allCards.remove(0));

            // Try and build a play
            testNext(pl, remaining);

            // Try and see if we can add the allowable card and any remaining cards
            for(Card ac : pl.getAllowableCards()) {
                log.debug(String.format("[PlayBuilder] Testing allowable card: %s", ac));

                // Set up local copies of remaining cards list and play
                ArrayList<Card> allowableRemaining = new ArrayList<Card>(remaining);
                Play p2 = initNewPlay(play, true);

                // Create bound joker and add to remaining cards list
                Card.Joker j2 = new Card.Joker(joker, ac);
                allowableRemaining.add(0, j2);

                // Test build
                if(testNext(p2, allowableRemaining)) {
                    log.info(String.format("[PlayBuilder] Adding allowable card: %s", ac));
                    addCardToList(ac, possibleCards);
                }
            }

        } while(! cards[0].equals(allCards.get(0)));

        return possibleCards;
    }

    public Card[] getPossibleCards(Card.Joker joker, Play[] plays, Card[] cards) {
        ArrayList<Card> possibleCards = new ArrayList<Card>();
        for(Play p : plays) {
            List<Card> cs = getPossibleCards(joker, p, cards);
            for(Card c : cs) {
                addCardToList(c, possibleCards);
            }
        }
        return possibleCards.toArray(new Card[possibleCards.size()]);
    }

    public boolean isPeggable(Play play, Card pegger) {
        for(Card c : play.getAllowableCards()) {
            if(c.sameCard(pegger)) {
                return true;
            }
        }
        return false;
    }

    public static PlayBuilder createPlayBuilder(Game game) {
        Class<? extends PlayBuilder> clz = game.getPlayBuilderClass();
        try {
            return clz.newInstance();
        } catch(InstantiationException ie) {
            log.fatal(String.format("Unable to initialise play builder: %s", ie.getMessage()), ie);
        } catch(IllegalAccessException iae) {
            log.fatal(String.format("Unable to initialise play builder: %s", iae.getMessage()), iae);
        }
        return null;
    }
}
