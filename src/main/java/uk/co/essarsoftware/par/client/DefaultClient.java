package uk.co.essarsoftware.par.client;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.engine.*;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 09/06/12
 * Time: 21:47
 * To change this template use File | Settings | File Templates.
 */
public abstract class DefaultClient implements GameClient
{
    protected GameClient client;

    DefaultClient() {
        // Empty
    }

    protected DefaultClient(Engine engine, GameController ctl, String playerName, PlayerUI ui) {
        this.client = (engine.createClient(ctl.createPlayer(playerName), ui));
    }

    @Override
    public Card approveBuy(Player buyer) {
        if(client == null) {
            throw new IllegalStateException("Client has not yet been initialised with a child client");
        }
        return client.approveBuy(buyer);
    }

    @Override
    public boolean buy(Player player, Card card) {
        if(client == null) {
            throw new IllegalStateException("Client has not yet been initialised with a child client");
        }
        return client.buy(player, card);
    }

    @Override
    public void discard(Card card) {
        if(client == null) {
            throw new IllegalStateException("Client has not yet been initialised with a child client");
        }
        client.discard(card);
    }

    @Override
    public void pegCard(Play play, Card card) {
        if(client == null) {
            throw new IllegalStateException("Client has not yet been initialised with a child client");
        }
        client.pegCard(play, card);
    }

    @Override
    public Card pickupDiscard() {
        if(client == null) {
            throw new IllegalStateException("Client has not yet been initialised with a child client");
        }
        return client.pickupDiscard();
    }

    @Override
    public Card pickupDraw() {
        if(client == null) {
            throw new IllegalStateException("Client has not yet been initialised with a child client");
        }
        return client.pickupDraw();
    }

    @Override
    public void playCards(Card[] cards) {
        if(client == null) {
            throw new IllegalStateException("Client has not yet been initialised with a child client");
        }
        client.playCards(cards);
    }

    @Override
    public Card rejectBuy(Player buyer) {
        if(client == null) {
            throw new IllegalStateException("Client has not yet been initialised with a child client");
        }
        return client.rejectBuy(buyer);
    }

    @Override
    public void resetPlays() {
        if(client == null) {
            throw new IllegalStateException("Client has not yet been initialised with a child client");
        }
        client.resetPlays();
    }

    @Override
    public Card[] getHand() {
        if(client == null) {
            throw new IllegalStateException("Client has not yet been initialised with a child client");
        }
        return client.getHand();
    }

    @Override
    public Class<? extends PlayBuilder> getPlayBuilderClass() {
        if(client == null) {
            throw new IllegalStateException("Client has not been initialized with a child client");
        }
        return client.getPlayBuilderClass();
    }

    @Override
    public Play[] getPlays() {
        if(client == null) {
            throw new IllegalStateException("Client has not yet been initialised with a child client");
        }
        return client.getPlays();
    }

    @Override
    public Card getPenaltyCard() {
        if(client == null) {
            throw new IllegalStateException("Client has not yet been initialised with a child client");
        }
        return client.getPenaltyCard();
    }

    @Override
    public Player getPlayer() {
        if(client == null) {
            throw new IllegalStateException("Client has not yet been initialised with a child client");
        }
        return client.getPlayer();
    }

    @Override
    public GameController getController() {
        if(client == null) {
            throw new IllegalStateException("Client has not yet been initialised with a child client");
        }
        return client.getController();
    }

    @Override
    public Player getCurrentPlayer() {
        if(client == null) {
            throw new IllegalStateException("Client has not yet been initialised with a child client");
        }
        return client.getCurrentPlayer();
    }

    @Override
    public Round getCurrentRound() {
        if(client == null) {
            throw new IllegalStateException("Client has not yet been initialised with a child client");
        }
        return client.getCurrentRound();
    }

    @Override
    public Player getDealer() {
        if(client == null) {
            throw new IllegalStateException("Client has not yet been initialised with a child client");
        }
        return client.getDealer();
    }

    @Override
    public Player[] getPlayers() {
        if(client == null) {
            throw new IllegalStateException("Client has not yet been initialised with a child client");
        }
        return client.getPlayers();
    }

    @Override
    public Table getTable() {
        if(client == null) {
            throw new IllegalStateException("Client has not yet been initialised with a child client");
        }
        return client.getTable();
    }

    @Override
    public int getTurn() {
        if(client == null) {
            throw new IllegalStateException("Client has not yet been initialised with a child client");
        }
        return client.getTurn();
    }

    @Override
    public boolean isBuyAllowed() {
        if(client == null) {
            throw new IllegalStateException("Client has not yet been initialised with a child client");
        }
        return client.isBuyAllowed();
    }

    @Override
    public int getHandSize() {
        if(client == null) {
            throw new IllegalStateException("Client has not yet been initialised with a child client");
        }
        return client.getHandSize();
    }

    @Override
    public String getPlayerName() {
        if(client == null) {
            throw new IllegalStateException("Client has not yet been initialised with a child client");
        }
        return client.getPlayerName();
    }

    @Override
    public PlayerState getPlayerState() {
        if(client == null) {
            throw new IllegalStateException("Client has not yet been initialised with a child client");
        }
        return client.getPlayerState();
    }

    @Override
    public boolean hasPenaltyCard() {
        if(client == null) {
            throw new IllegalStateException("Client has not yet been initialised with a child client");
        }
        return client.hasPenaltyCard();
    }

    @Override
    public boolean isDown() {
        if(client == null) {
            throw new IllegalStateException("Client has not yet been initialised with a child client");
        }
        return client.isDown();
    }
}
