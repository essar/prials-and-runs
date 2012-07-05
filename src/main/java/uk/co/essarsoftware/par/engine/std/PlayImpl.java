package uk.co.essarsoftware.par.engine.std;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.games.cards.OrderedCardArray;
import uk.co.essarsoftware.games.cards.Pack;
import uk.co.essarsoftware.par.engine.EngineException;
import uk.co.essarsoftware.par.engine.Play;
import uk.co.essarsoftware.par.engine.PlayBuilder;

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

    protected void cloneFrom(PlayImpl play) {
        playCards.addAll(play.playCards);
    }

    protected void resetPlay() {
        playCards.clear();
    }

    boolean initialise(Card[] cards) {
        // Validate arguments
        if(cards == null) {
            throw new IllegalArgumentException("Cannot initialise play with null array");
        }
        // Run play builder
        PlayImplBuilder pb = new PlayImplBuilder();
        PlayImpl pl = (PlayImpl) pb.build(this, cards);

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

    public static class PlayImplBuilder extends PlayBuilder
    {
        @Override
        public boolean addCard(Play play, Card c) {
            PlayImpl pi = (PlayImpl) play;
            return pi.addCard(c);
        }

        @Override
        public PlayImpl initNewPlay(Play play, boolean clone) {
            PlayImpl pi = (PlayImpl) play;
            try {
                log.debug(String.format("[PlayBuilder] Creating new %s instance", play.getClass().getName()));
                PlayImpl inst = pi.getClass().newInstance();
                if(clone) {
                    inst.cloneFrom(pi);
                }
                return inst;
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
    }

    private static class StaticPlayBuilder
    {
        // Class uses depth-first search algorithm to try and build a play

        private static boolean addCardToList(Card card, List<Card> list) {
            for(Card c : list) {
                if(c.sameCard(card)) {
                    return false;
                }
            }
            return list.add(card);
        }

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
            if(play.addCard(c)) {
                log.info(String.format("[PlayBuilder] %s added OK", c));
                // Recursively try the next card
                return testNext(play, remaining);
            }
            log.info(String.format("[PlayBuilder] %s not added", c));
            // Card not added
            remaining.add(0, c);
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
            PlayImpl pl;

            // Initial validation
            if(cards.length != 3) {
                log.info(String.format("[PlayBuilder] Unable to build a play using %d cards, need 3 cards", cards.length));
                return null;
            }

            do {
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

            } while(! cards[0].equals(allCards.get(0)));

            // No play could be built
            return null;
        }

        public static Card[] getPossibleCards(Card.Joker joker, PlayImpl play, Card[] cards) {
            ArrayList<Card> possibleCards = new ArrayList<Card>();
            ArrayList<Card> allCards = new ArrayList<Card>(Arrays.asList(cards));
            ArrayList<Card> remaining;
            PlayImpl pl;

            do {
                // Initialise new play
                pl = initNewPlay(play);

                // Initialise remaining card array and rotate all cards array
                remaining = new ArrayList<Card>(allCards);
                allCards.add(allCards.remove(0));

                // Try and build a play
                testNext(pl, remaining);

                // Try and see if we can add the allowable card and any remaining cards
                for(Card ac : pl.getAllowableCards()) {
                    log.debug(String.format("[PlayBuilder] Testing allowable card: %s", ac));

                    // Set up local copies of remaining cards list and play
                    ArrayList<Card> allowableRemaining = new ArrayList<Card>(remaining);
                    PlayImpl p2 = initNewPlay(play);
                    p2.cloneFrom(pl);

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

            return possibleCards.toArray(new Card[possibleCards.size()]);
        }

        public static boolean isPeggable(PlayImpl play, Card pegger) {
            for(Card c : play.getAllowableCards()) {
                if(c.sameCard(pegger)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static void main(String[] args) throws EngineException {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.DEBUG);

        Pack pack = Pack.generatePackWithJokers();

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

        System.out.println("-- UNORDERED RUN TEST --");
        {
            Card[] cards = new Card[] {
                    pack.get(8)
                    , pack.get(6)
                    , pack.get(7)
            };

            RunImpl pl = new RunImpl();

            System.out.println(pl.initialise(cards));
            System.out.println(pl.size());
            System.out.println(pl.pegCard(pack.get(9)));
            System.out.println(pl.size());
        }

        System.out.println("-- JOKER PRIAL TEST --");
        {
            Card[] cards = new Card[] {
                    pack.get(4)
                  , pack.get(4 + 13)
            };

            Card.Joker j = (Card.Joker) pack.get(53);

            PrialImpl pl = new PrialImpl();

            System.out.println(Arrays.toString(new PlayImplBuilder().getPossibleCards(j, new Play[] {pl}, cards)));
        }

        System.out.println("-- JOKER RUN TEST --");
        {
            Card[] cards = new Card[] {
                    pack.get(6)
                  , pack.get(7)
            };

            Card.Joker j = (Card.Joker) pack.get(53);

            RunImpl pl = new RunImpl();

            System.out.println(Arrays.toString(new PlayImplBuilder().getPossibleCards(j, new Play[] {pl}, cards)));
        }

        System.out.println("-- JOKER UNORDERED RUN TEST --");
        {
            Card[] cards = new Card[] {
                    pack.get(6)
                  , pack.get(8)
            };

            Card.Joker j = (Card.Joker) pack.get(53);

            RunImpl pl = new RunImpl();

            System.out.println(Arrays.toString(new PlayImplBuilder().getPossibleCards(j, new Play[] {pl}, cards)));
        }

        System.out.println("-- JOKER INVALID RUN TEST --");
        {
            Card[] cards = new Card[] {
                    pack.get(6)
                  , pack.get(9)
            };

            Card.Joker j = (Card.Joker) pack.get(53);

            RunImpl pl = new RunImpl();

            System.out.println(Arrays.toString(new PlayImplBuilder().getPossibleCards(j, new Play[] {pl}, cards)));
            System.out.println(j);
        }
    }
}
