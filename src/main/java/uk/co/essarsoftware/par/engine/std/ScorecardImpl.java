package uk.co.essarsoftware.par.engine.std;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.engine.Round;
import uk.co.essarsoftware.par.engine.scorecard.Scorecard;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/07/12
 * Time: 19:14
 * To change this template use File | Settings | File Templates.
 */
class ScorecardImpl extends Scorecard
{
    void initAllRounds() {
        for(Round r : Round.values()) {
            addRound(r);
        }
    }

    int calculateScore(Round round, PlayerImpl player) {
        int score = 0;
        Card[] cards = player.getHand();
        for(Card c : cards) {
            if(c.isJoker()) {
                addJoker(round, player);
                score += 20;
            } else {
                switch(c.getValue()) {
                    case KING: score += 10; break;
                    case QUEEN: score += 10; break;
                    case JACK: score += 10; break;
                    case TEN: score += 10; break;
                    case NINE: score += 9; break;
                    case EIGHT: score += 8; break;
                    case SEVEN: score += 7; break;
                    case SIX: score += 6; break;
                    case FIVE: score += 5; break;
                    case FOUR: score += 4; break;
                    case THREE: score += 3; break;
                    case TWO: score += 2; break;
                    case ACE: score += 1; break;
                }
            }
        }
        recordScore(player, round, score, player.isDown());

        return score;
    }

    void recordJoker(Round round, PlayerImpl player) {
        addJoker(round, player);
    }

    void unrecordJoker(Round round, PlayerImpl player) {
        removeJoker(round, player);
    }
}
