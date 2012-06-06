package uk.co.essarsoftware.par.engine;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 12:31
 * To change this template use File | Settings | File Templates.
 */
public interface Game
{
    public Player getCurrentPlayer();

    public Round getCurrentRound();

    public Player getDealer();

    public Player[] getPlayers();

    public Table getTable();

    public int getTurn();

    public boolean isBuyAllowed();
}
