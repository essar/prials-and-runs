package uk.co.essarsoftware.par.engine.std;

import org.apache.log4j.Logger;
import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.games.cards.CardArray;
import uk.co.essarsoftware.par.engine.Player;
import uk.co.essarsoftware.par.engine.PlayerState;
import uk.co.essarsoftware.par.engine.Table;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 13:04
 * To change this template use File | Settings | File Templates.
 */
class PlayerImpl implements Player
{
    private static Logger log = Logger.getLogger(Player.class);

    private boolean down;
    private Card penaltyCard;
    private PlayerState playerState;
    private String playerName;

    private final Hand hand;

    public PlayerImpl(String playerName) {
        this.playerName = playerName;
        this.hand = new Hand();
        this.playerState = PlayerState.INIT;
        this.down = false;
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

    void deal(CardArray cards) {
        hand.setCards(cards);
    }

    void setDown(boolean down) {
        this.down = down;
    }

    void setPenaltyCard(Card penaltyCard) {
        if(penaltyCard == null) {
            throw new IllegalArgumentException("Cannot set penaltyCard to null");
        }
        this.penaltyCard = penaltyCard;
    }

    void setPlayerState(PlayerState playerState) {
        if(playerState == null) {
            throw new IllegalArgumentException("Cannot set playerState to null");
        }
        this.playerState = playerState;
    }

    @Override
    public int getHandSize() {
        return hand.size();
    }

    @Override
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public PlayerState getPlayerState() {
        return playerState;
    }

    @Override
    public boolean hasPenaltyCard() {
        return (penaltyCard != null);
    }

    @Override
    public boolean isDown() {
        return down;
    }
}
