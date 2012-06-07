package uk.co.essarsoftware.par.engine.std;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 06/06/12
 * Time: 15:40
 * To change this template use File | Settings | File Templates.
 */
class EngineClientList extends HashMap<PlayerImpl, EngineClient>
{
    void emptyQueues(long wait) {

        // Make sure the event queues for each player are empty
        for(EngineClient cli : values()) {


            cli.waitAndClear(2000L);
        }

    }
}
