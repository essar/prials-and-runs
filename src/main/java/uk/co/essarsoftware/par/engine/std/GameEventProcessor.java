package uk.co.essarsoftware.par.engine.std;

import uk.co.essarsoftware.par.engine.events.GameEvent;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 09/06/12
 * Time: 15:02
 * To change this template use File | Settings | File Templates.
 */
public interface GameEventProcessor
{
    public void processEvent(GameEvent evt);
}
