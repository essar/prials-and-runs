package uk.co.essarsoftware.par.engine.std;

import uk.co.essarsoftware.par.engine.events.GameEvent;

import java.util.HashMap;

/**
 * Map class that holds <tt>EngineClient</tt> objects.
 * @author Steve Roberts <steve.roberts@essarsoftware.co.uk>
 * @version 1.0 (07-Jun-12)
 */
class EngineClientList extends HashMap<PlayerImpl, EngineClient>
{
    /**
     * Clears the queue on each client. Method blocks until all queues are empty.
     */
    synchronized void clearAll() {
        for(EngineClient cli : values()) {
            cli.clearQueue();
        }
    }

    /**
     * Call <tt>endGame</tt> on all clients.
     * @see uk.co.essarsoftware.par.engine.std.EngineClient#endGame()
     */
    synchronized void endGame() {
        for(EngineClient cli : values()) {
            cli.endGame();
        }
    }

    /**
     * Queue an event on all clients.
     * @param evt the <tt>GameEvent</tt> to queue.
     * @see EngineClient#addEvent(uk.co.essarsoftware.par.engine.events.GameEvent)
     */
    synchronized void queueEvent(GameEvent evt) {
        for(EngineClient cli : values()) {
            cli.addEvent(evt);
        }
    }

    /**
     * Start all agent threads.
     * @see uk.co.essarsoftware.par.engine.std.EngineAgent#startAgent()
     */
    synchronized void startAllAgents() {
        for(EngineClient cli : values()) {
            cli.getAgent().startAgent();
        }
    }

    /**
     * Stop all agent threads.
     * @see uk.co.essarsoftware.par.engine.std.EngineAgent#stopAgent()
     */
    synchronized void stopAllAgents() {
        for(EngineClient cli : values()) {
            cli.getAgent().stopAgent();
        }
    }
}
