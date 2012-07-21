/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.par.engine.std;

import uk.co.essarsoftware.par.engine.Engine;
import uk.co.essarsoftware.par.engine.Game;
import uk.co.essarsoftware.par.engine.GameFactory;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 09/06/12
 * Time: 21:34
 * To change this template use File | Settings | File Templates.
 */
public class StdGameFactory extends GameFactory
{
    @Override
    public Engine createEngine(Game game) {
        if(game instanceof GameImpl) {
            return new EngineImpl((GameImpl) game);
        }
        throw new IllegalArgumentException("Unsupported game class");
    }

    @Override
    public Game createGame() {
        return new GameImpl();
    }
}
