package uk.co.essarsoftware.par.engine.std;

import uk.co.essarsoftware.par.engine.Round;

import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 06/06/12
 * Time: 14:56
 * To change this template use File | Settings | File Templates.
 */
class TableSeat extends HashSet<PlayImpl>
{
    TableSeat(Round round) {
        // Validate arguments
        if(round == null) {
            throw new IllegalArgumentException("Cannot create PlaySet with null Round");
        }

        // Initialise prials
        for(int i = 0; i < round.getPrials(); i ++) {
            add(new PrialImpl());
        }

        // Initialise runs
        for(int i = 0; i < round.getRuns(); i ++) {
            add(new RunImpl());
        }
    }

    public PlayImpl[] getPlays() {
        return toArray(new PlayImpl[size()]);
    }

    public int getUninitialisedPlayCount() {
        int ct = 0;
        for(PlayImpl play : this) {
            ct += !play.isInitialised() ? 1 : 0;
        }
        return ct;
    }
}
