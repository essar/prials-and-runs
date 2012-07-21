/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.par.engine;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 09/06/12
 * Time: 21:30
 * To change this template use File | Settings | File Templates.
 */
public abstract class GameFactory
{
    public abstract Engine createEngine(Game game);

    public abstract Game createGame();

    public static GameFactory getInstance(Class<? extends GameFactory> clazz) {
        try {
            return clazz.newInstance();
        } catch(IllegalAccessException iae) {
            return null;
        } catch(InstantiationException ie) {
            return null;
        }
    }
}
