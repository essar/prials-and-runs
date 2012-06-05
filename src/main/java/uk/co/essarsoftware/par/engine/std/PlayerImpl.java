package uk.co.essarsoftware.par.engine.std;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.games.cards.CardArray;
import uk.co.essarsoftware.par.engine.Player;
import uk.co.essarsoftware.par.engine.PlayerState;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 13:04
 * To change this template use File | Settings | File Templates.
 */
class PlayerImpl implements Player
{
    private Card penaltyCard;
    private PlayerState playerState;
    private String playerName;

    private final Hand hand;

    public PlayerImpl(String playerName) {
        this.playerName = playerName;
        this.hand = new Hand();
        this.playerState = PlayerState.INIT;
    }

    void clearPenaltyCard() {
        this.penaltyCard = null;
    }

    Card getPenaltyCard() {
        return penaltyCard;
    }

    Hand getHand() {
        return hand;
    }

    void setHandCards(CardArray cards) {
        hand.setCards(cards);
    }

    void setPenaltyCard(Card penaltyCard) {
        if(penaltyCard == null) {
            throw new IllegalArgumentException("Cannot set penaltyCard to null");
        }
        this.penaltyCard = penaltyCard;
    }

    void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    void setPlayerState(PlayerState playerState) {
        if(playerState == null) {
            throw new IllegalArgumentException("Cannot set playerState to null");
        }
        this.playerState = playerState;
    }


    public int getHandSize() {
        return hand.size();
    }

    public String getPlayerName() {
        return playerName;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public boolean hasPenaltyCard() {
        return (penaltyCard != null);
    }
}
