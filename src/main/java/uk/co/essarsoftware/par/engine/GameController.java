package uk.co.essarsoftware.par.engine;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 09/06/12
 * Time: 21:41
 * To change this template use File | Settings | File Templates.
 */
public interface GameController
{
    public Player createPlayer(String playerName);

    public void removePlayer(Player player);
}
