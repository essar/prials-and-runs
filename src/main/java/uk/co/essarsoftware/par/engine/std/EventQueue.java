package uk.co.essarsoftware.par.engine.std;

import uk.co.essarsoftware.par.engine.events.GameEvent;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 06/06/12
 * Time: 15:18
 * To change this template use File | Settings | File Templates.
 */
class EventQueue extends ArrayBlockingQueue<GameEvent>
{
    private static final int Q_SIZE = 20;

    public EventQueue() {
        super(Q_SIZE);
    }

    @Override
    public void clear() {
        synchronized(this) {
            super.clear();
        }
    }
    
    public GameEvent nextEvent() {
        synchronized(this) {
            try {
                return take();
            } catch(InterruptedException ie) {
                return null;
            } finally {
                if(isEmpty()) {
                    // Notify any waiting threads
                    notifyAll();
                }
            }
        }
    }
}
