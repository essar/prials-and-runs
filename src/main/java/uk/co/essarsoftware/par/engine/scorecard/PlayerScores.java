/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.par.engine.scorecard;

import uk.co.essarsoftware.par.engine.Player;
import uk.co.essarsoftware.par.engine.Round;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/07/12
 * Time: 18:50
 * To change this template use File | Settings | File Templates.
 */
class PlayerScores extends HashMap<Player, HashMap<Round, RoundScore>>
{
    RoundScore getRoundScore(Player player, Round round) {
        if(containsKey(player)) {
            RoundScore rs = get(player).get(round);
            if(rs == null) {
                rs = new RoundScore();
                get(player).put(round, rs);
            }
            return rs;
        }
        return null;
    }

    public void addJoker(Round round, Player player) {
        RoundScore rs = getRoundScore(player, round);
        if(rs != null) {
            rs.jokers ++;
        }
    }

    public void removeJoker(Round round, Player player) {
        RoundScore rs = getRoundScore(player, round);
        if(rs != null) {
            rs.jokers = Math.max(0, rs.jokers - 1);
        }
    }

    public void addPlayer(Player player) {
        if(! containsKey(player)) {
            put(player, new HashMap<Round, RoundScore>());
        }
    }

    public void addRound(Round round) {
        for(Player p : keySet()) {
            if(! get(p).containsKey(round)) {
                RoundScore rs = new RoundScore();
                get(p).put(round, rs);
            }
        }
    }

    public boolean getDown(Player player, Round round) {
        if(containsKey(player)) {
            if(get(player).containsKey(round)) {
                return get(player).get(round).down;
            }
        }
        return false;
    }

    public int getJokers(Player player, Round round) {
        if(containsKey(player)) {
            if(get(player).containsKey(round)) {
                return get(player).get(round).jokers;
            }
        }
        return 0;
    }

    public int getScore(Player player, Round round) {
        if(containsKey(player)) {
            if(get(player).containsKey(round)) {
                return get(player).get(round).cardScore;
            }
        }
        return 0;
    }

    public void setScore(Player player, Round round, int score, boolean down) {
        RoundScore rs = getRoundScore(player, round);
        if(rs != null) {
            rs.cardScore = score;
            rs.down = down;
        }
    }

}
