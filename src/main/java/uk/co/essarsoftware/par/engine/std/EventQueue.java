/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.par.engine.std;

import uk.co.essarsoftware.par.engine.events.GameEvent;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Queue holding <tt>GameEvent</tt>s for processing by game clients.
 * @author Steve Roberts <steve.roberts@essarsoftware.co.uk>
 * @version 1.0 (07-Jun-12)
 */
class EventQueue extends ArrayBlockingQueue<GameEvent>
{
    private static final int Q_SIZE = 20;
    private static final long WAIT = 1000L;

    public EventQueue() {
        super(Q_SIZE);
    }

    @Override
    public boolean add(GameEvent evt) {
        boolean r = super.add(evt);
        synchronized(this) {
            notifyAll();
        }
        return r;
    }

    @Override
    public void clear() {
        synchronized(this) {
            try {
                while(size() > 0) {
                    wait(WAIT);
                }
            } catch(InterruptedException ie) {
                // Empty catch
            }
        }
    }

    @Override
    public GameEvent take() throws InterruptedException {
        try {
            return super.take();
        } finally {
            synchronized(this) {
                notifyAll();
            }
        }
    }
}
