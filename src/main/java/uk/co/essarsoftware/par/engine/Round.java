package uk.co.essarsoftware.par.engine;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 12:33
 * To change this template use File | Settings | File Templates.
 */
public enum Round
{
    START(0,0), PP(2,0), PR(1,1), RR(0,2), PPR(2,1), PRR(1,2), PPP(3,0), RRR(0,3), END(0,0);

    private final int prials, runs;

    private Round(int prials, int runs) {
        this.prials = prials;
        this.runs = runs;
    }

    public int getPrials() {
        return prials;
    }

    public int getRuns() {
        return runs;
    }
}
