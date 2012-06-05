package uk.co.essarsoftware.par.engine.std;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.games.cards.CardArray;
import uk.co.essarsoftware.games.cards.DiscardPile;
import uk.co.essarsoftware.games.cards.DrawPile;
import uk.co.essarsoftware.par.engine.Play;
import uk.co.essarsoftware.par.engine.Player;
import uk.co.essarsoftware.par.engine.Round;
import uk.co.essarsoftware.par.engine.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 16:14
 * To change this template use File | Settings | File Templates.
 */
class TableImpl implements Table
{
    private final DiscardPile discardPile;
    private final DrawPile drawPile;
    private final PlayMap plays;

    TableImpl() {
        discardPile = new DiscardPile();
        drawPile = new DrawPile();
        plays = new PlayMap();
    }

    private void recyclePack() {
        synchronized(drawPile) {
            synchronized(discardPile) {
                Card discard = discardPile.pickup();
                discardPile.resetTo(drawPile);
                discardPile.discard(discard);
            }
        }
    }

    void addPlayer(PlayerImpl player) {
        synchronized(plays) {
            plays.put(player, null);
        }
    }

    CardArray[] initRound(Round round) {
        // Reset all cards into draw pile
        discardPile.resetTo(drawPile);

        // Shuffle deck
        drawPile.shuffle();

        // Deal hands
        CardArray[] hands = drawPile.deal(plays.size(), 11);
        for(Player p : plays.keySet()) {
            PlaySet ps = new PlaySet();
            for(int i = 0; i < round.getPrials(); i ++) {
                ps.add(new PrialImpl());
            }
            for(int i = 0; i < round.getRuns(); i ++) {
                ps.add(new RunImpl());
            }
            plays.put(p, ps);
        }
        return hands;
    }

    void removePlayer(PlayerImpl player) {
        synchronized(plays) {
            plays.remove(player);
        }
    }


    void discard(Card card) {
        synchronized(discardPile) {
            discardPile.discard(card);
        }
    }

    Card pickupDiscard() {
        synchronized(discardPile) {
            if(discardPile.size() > 0) {
                return discardPile.pickup();
            }
            return null;
        }
    }

    Card pickupDraw() {
        synchronized(drawPile) {
            if(drawPile.size() > 0) {
                try {
                    return drawPile.pickup();
                } finally {
                    if(drawPile.size() == 0) {
                        recyclePack();
                    }
                }
            }
            return null;
        }
    }


    public Card getDiscard() {
        synchronized(discardPile) {
            if(discardPile.size() > 0) {
                return discardPile.getDiscard();
            }
            return null;
        }
    }

    public Play[] getPlays() {
        synchronized(plays) {
            return plays.getAllPlays();
        }
    }

    public Play[] getPlays(Player player) {
        synchronized(plays) {
            if(plays.containsKey(player)) {
                return plays.get(player).getPlays();
            }
            return null;
        }
    }


    private class PlaySet extends HashSet<Play>
    {
        public Play[] getPlays() {
            return toArray(new Play[size()]);
        }
    }

    private class PlayMap extends HashMap<Player, PlaySet>
    {
        public Play[] getAllPlays() {
            ArrayList<Play> allPlays = new ArrayList<Play>();
            for(PlaySet ps : values()) {
                allPlays.addAll(ps);
            }
            return allPlays.toArray(new Play[allPlays.size()]);
        }
    }
}
