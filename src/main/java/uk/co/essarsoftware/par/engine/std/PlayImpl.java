package uk.co.essarsoftware.par.engine.std;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.games.cards.OrderedCardArray;
import uk.co.essarsoftware.games.cards.Pack;
import uk.co.essarsoftware.par.engine.EngineException;
import uk.co.essarsoftware.par.engine.InvalidPlayException;
import uk.co.essarsoftware.par.engine.Play;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 17:26
 * To change this template use File | Settings | File Templates.
 */
abstract class PlayImpl implements Play
{
    protected OrderedCardArray playCards;

    private static Logger log = Logger.getLogger(Play.class);

    protected PlayImpl(Comparator<Card> comparator) {
        playCards = new OrderedCardArray(comparator);
    }

    private boolean validateCard(Card card) {
        // Validate arguments
        if(card == null) {
            log.info(String.format("ValidateCard: not allowed"));
            log.debug(String.format("Card: %s", card));
            return false;
        }
        // Reject uninitialised joker
        if(card.isJoker() && !card.isBoundJoker()) {
            log.info(String.format("ValidateCard: not allowed"));
            log.debug(String.format("Card: %s", card));
            return false;
        }
        // Allow a card if play is uninitialised
        if(! isInitialised() || getAllowableCards() == null) {
            log.info(String.format("ValidateCard: allowed"));
            log.debug(String.format("ValidateCard: %s OK, play not initialised", card));
            return true;
        }
        // Allow cards that appear within allowable cards array
        for(Card ac : getAllowableCards()) {
            if(ac.sameCard(card)) {
                log.info(String.format("ValidateCard: allowed"));
                log.debug(String.format("ValidateCard: %s OK, allowableCards: %s", card, Arrays.toString(getAllowableCards())));
                return true;
            }
        }
        log.info(String.format("ValidateCard: not allowed"));
        log.debug(String.format("ValidateCard: %s not OK, allowableCards: %s", card, Arrays.toString(getAllowableCards())));
        return false;
    }

    protected boolean addCard(Card card) {
        return validateCard(card) && playCards.add(card);
    }

    protected void resetPlay() {
        playCards.clear();
    }

    boolean initialise(Card[] cards) throws EngineException {
        // Validate arguments
        if(cards == null) {
            throw new IllegalArgumentException("Cannot initialise play with null array");
        }
        if(cards.length != 3) {
            throw new InvalidPlayException("Must supply three cards to initialise a play");
        }
        // Run play builder
        PlayImpl pl = PlayBuilder.build(this, cards);

        if(pl != null) {
            // Copy cards into play
            for(Card c : pl.playCards) {
                if(! addCard(c)) {
                    resetPlay();
                    return false;
                }
            }
            return true;
        }
        // No play built
        return false;
    }

    boolean pegCard(Card card) {
        return isInitialised() && addCard(card);
    }

    @Override
    public Card[] getCards() {
        return playCards.toCardArray().getCards();
    }

    @Override
    public boolean isInitialised() {
        return playCards.size() > 0;
    }

    @Override
    public boolean isPrial() {
        return false;
    }

    @Override
    public boolean isRun() {
        return false;
    }

    @Override
    public int size() {
        return playCards.size();
    }

    @Override
    public String toString() {
        return String.format("%s:%s", (isPrial() ? "Prial" : isRun() ? "Run" : "Play"), Arrays.toString(getCards()));
    }

    private static class PlayBuilder
    {
        // Class uses depth-first search algorithm to try and build a play

        private static PlayImpl initNewPlay(PlayImpl play) {
            try {
                log.debug(String.format("[PlayBuilder] Creating new %s instance", play.getClass().getName()));
                return play.getClass().newInstance();
            } catch(IllegalAccessException iae) {
                // Log exception
                log.error(String.format("[PlayBuilder] Unable to instantiate play : %s", iae.getMessage()));
                log.debug(iae.getMessage(), iae);
            } catch(InstantiationException ie) {
                // Log exception
                log.error(String.format("[PlayBuilder] Unable to instantiate play : %s", ie.getMessage()));
                log.debug(ie.getMessage(), ie);
            }
            return null;
        }

        private static boolean testCard(Card c, PlayImpl play, List<Card> remaining) {
            log.debug(String.format("[PlayBuilder] Testing %s against %s", c, play));
            // Attempt to add card
            if(play.addCard(c)) {
                log.info(String.format("[PlayBuilder] %s added OK", c));
                // Recursively try the next card
                return testNext(play, remaining);
            }
            log.info(String.format("[PlayBuilder] %s not added", c));
            // Card not added
            return false;
        }

        private static boolean testNext(PlayImpl play, List<Card> remaining) {
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

        public static PlayImpl build(PlayImpl play, Card[] cards) {

            ArrayList<Card> allCards = new ArrayList<Card>(Arrays.asList(cards));
            ArrayList<Card> remaining;
            int ct = 0;
            PlayImpl pl;

            do {
                // Increment counter
                ct ++;

                // Initialise new play
                pl = initNewPlay(play);

                // Initialise remaining card array and rotate all cards array
                remaining = new ArrayList<Card>(allCards);
                allCards.add(allCards.remove(0));

                // Try and build a play
                if(testNext(pl, remaining)) {
                    // Return the play
                    return pl;
                }

            } while(ct <= allCards.size());

            // No play could be built
            return null;

        }
    }

    public static void main(String[] args) throws EngineException {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.DEBUG);

        Pack pack = Pack.generatePack();

        System.out.println("-- PRIAL TEST --");
        {
            Card[] cards = new Card[] {
                    pack.get(4)
                  , pack.get(4 + 13)
                  , pack.get(4 + 13 + 13)
            };

            PrialImpl pl = new PrialImpl();

            System.out.println(pl.initialise(cards));
            System.out.println(pl.size());
            System.out.println(pl.pegCard(pack.get(4 + 13 + 13 + 13)));
            System.out.println(pl.size());
        }

        System.out.println("-- RUN TEST --");
        {
            Card[] cards = new Card[] {
                    pack.get(6)
                  , pack.get(7)
                  , pack.get(8)
            };

            RunImpl pl = new RunImpl();

            System.out.println(pl.initialise(cards));
            System.out.println(pl.size());
            System.out.println(pl.pegCard(pack.get(9)));
            System.out.println(pl.size());
        }
    }
}
