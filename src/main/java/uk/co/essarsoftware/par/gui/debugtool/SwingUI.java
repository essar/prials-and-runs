package uk.co.essarsoftware.par.gui.debugtool;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.engine.*;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 03/07/12
 * Time: 12:54
 * To change this template use File | Settings | File Templates.
 */
public abstract class SwingUI implements PlayerUI
{
    protected abstract void asyncBuyApproved(Player player, Player buyer, Card card, boolean thisPlayer);

    protected abstract void asyncBuyRejected(Player player, Player buyer, Card card, boolean thisPlayer);

    protected abstract void asyncBuyRequest(Player player, Player buyer, Card card, boolean thisPlayer);

    protected abstract void asyncCardDiscarded(Player player, Card card, boolean thisPlayer);

    protected abstract void asyncCardPegged(Player player, Play play, Card card, boolean thisPlayer);

    protected abstract void asyncCardsPlayed(Player player, Play[] play, boolean thisPlayer);

    protected abstract void asyncDiscardPickup(Player player, Card card, boolean thisPlayer);

    protected abstract void asyncDrawPickup(Player player, boolean thisPlayer);

    protected abstract void asyncPlayerStateChange(Player player, PlayerState oldState, PlayerState newState, boolean thisPlayer);

    protected abstract void asyncPlayerOut(Player player, boolean thisPlayer);

    protected abstract void asyncRoundEnded(Round round);

    protected abstract void asyncRoundStarted(Round round);

    protected abstract void asyncHandleException(EngineException ee);

    @Override
    public void buyApproved(final Player player, final Player buyer, final Card card, final boolean thisPlayer) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                asyncBuyApproved(player, buyer, card, thisPlayer);
            }
        });
    }

    @Override
    public void buyRejected(final Player player, final Player buyer, final Card card, final boolean thisPlayer) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                asyncBuyRejected(player, buyer, card, thisPlayer);
            }
        });
    }

    @Override
    public void buyRequest(final Player player, final Player buyer, final Card card, final boolean thisPlayer) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                asyncBuyRequest(player, buyer, card, thisPlayer);
            }
        });
    }

    @Override
    public void cardDiscarded(final Player player, final Card card, final boolean thisPlayer) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                asyncCardDiscarded(player, card, thisPlayer);
            }
        });
    }

    @Override
    public void cardPegged(final Player player, final Play play, final Card card, final boolean thisPlayer) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                asyncCardPegged(player, play, card, thisPlayer);
            }
        });
    }

    @Override
    public void cardsPlayed(final Player player, final Play[] play, final boolean thisPlayer) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                asyncCardsPlayed(player, play, thisPlayer);
            }
        });
    }

    @Override
    public void discardPickup(final Player player, final Card card, final boolean thisPlayer) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                asyncDiscardPickup(player, card, thisPlayer);
            }
        });
    }

    @Override
    public void drawPickup(final Player player, final boolean thisPlayer) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                asyncDrawPickup(player, thisPlayer);
            }
        });
    }

    @Override
    public void playerStateChange(final Player player, final PlayerState oldState, final PlayerState newState, final boolean thisPlayer) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                asyncPlayerStateChange(player, oldState, newState, thisPlayer);
            }
        });
    }

    @Override
    public void playerOut(final Player player, final boolean thisPlayer) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                asyncPlayerOut(player, thisPlayer);
            }
        });
    }

    @Override
    public void roundEnded(final Round round) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                asyncRoundEnded(round);
            }
        });
    }

    @Override
    public void roundStarted(final Round round) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                asyncRoundStarted(round);
            }
        });
    }

    @Override
    public void handleException(final EngineException ee) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                asyncHandleException(ee);
            }
        });
    }
}
