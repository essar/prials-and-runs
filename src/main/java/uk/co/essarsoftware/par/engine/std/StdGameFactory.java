package uk.co.essarsoftware.par.engine.std;

import uk.co.essarsoftware.par.engine.Engine;
import uk.co.essarsoftware.par.engine.Game;
import uk.co.essarsoftware.par.engine.GameFactory;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 09/06/12
 * Time: 21:34
 * To change this template use File | Settings | File Templates.
 */
public class StdGameFactory extends GameFactory
{
    @Override
    public Engine createEngine(Game game) {
        if(game instanceof GameImpl) {
            return new EngineImpl((GameImpl) game);
        }
        throw new IllegalArgumentException("Unsupported game class");
    }

    @Override
    public Game createGame() {
        return new GameImpl();
    }
}
