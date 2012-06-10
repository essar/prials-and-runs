package uk.co.essarsoftware.par.engine.std;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.engine.*;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 10/06/12
 * Time: 13:20
 * To change this template use File | Settings | File Templates.
 */
class TestPlayerUI implements PlayerUI
{
    @Override
    public void buyApproved(Player player, Player buyer, Card card, boolean thisPlayer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void buyRejected(Player player, Player buyer, Card card, boolean thisPlayer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void buyRequest(Player player, Player buyer, Card card, boolean thisPlayer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void cardDiscarded(Player player, Card card, boolean thisPlayer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void cardPegged(Player player, Play play, Card card, boolean thisPlayer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void cardsPlayed(Player player, Play[] play, boolean thisPlayer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void discardPickup(Player player, Card card, boolean thisPlayer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void drawPickup(Player player, boolean thisPlayer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void playerStateChange(Player player, PlayerState oldState, PlayerState newState, boolean thisPlayer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void playerOut(Player player, boolean thisPlayer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void roundEnded(Round round) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void roundStarted(Round round) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void handleException(EngineException ee) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
