/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.par.engine;

import uk.co.essarsoftware.par.engine.scorecard.Scorecard;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 12:31
 * To change this template use File | Settings | File Templates.
 */
public interface Game
{
    public GameController getController();

    public Player getCurrentPlayer();

    public Round getCurrentRound();

    public Player getDealer();

    public Class<? extends PlayBuilder> getPlayBuilderClass();

    public Player[] getPlayers();

    public Scorecard getScorecard();

    public Table getTable();

    public int getTurn();

    public boolean isBuyAllowed();
}
