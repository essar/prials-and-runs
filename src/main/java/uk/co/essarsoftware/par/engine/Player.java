package uk.co.essarsoftware.par.engine;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 12:31
 * To change this template use File | Settings | File Templates.
 */
public interface Player
{
    public int getHandSize();

    public boolean hasPenaltyCard();

    public String getPlayerName();

    public PlayerState getPlayerState();
}
