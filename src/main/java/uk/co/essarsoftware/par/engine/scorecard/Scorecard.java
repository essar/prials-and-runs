package uk.co.essarsoftware.par.engine.scorecard;

import uk.co.essarsoftware.par.engine.Player;
import uk.co.essarsoftware.par.engine.Round;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/07/12
 * Time: 18:49
 * To change this template use File | Settings | File Templates.
 */
public class Scorecard
{
    private PlayerScores scores;

    public Scorecard() {
        scores = new PlayerScores();
    }

    protected void recordScore(Player player, Round round, int score, boolean down) {
        scores.setScore(player, round, score, down);
    }

    protected void addJoker(Round round, Player player) {
        scores.addJoker(round, player);
    }

    protected void removeJoker(Round round, Player player) {
        scores.removeJoker(round, player);
    }

    public void addPlayer(Player player) {
        scores.addPlayer(player);
    }

    public void addRound(Round round) {
        scores.addRound(round);
    }

    public boolean getDown(Player player, Round round) {
        return scores.getDown(player, round);
    }

    public int getJokers(Player player, Round round) {
        return scores.getJokers(player, round);
    }

    public int getScore(Player player, Round round) {
        return scores.getScore(player, round);
    }

    public int getRoundSCore(Round round) {
        return 0;
    }

    public int getTotalSCore(Player player) {
        return 0;
    }
}
