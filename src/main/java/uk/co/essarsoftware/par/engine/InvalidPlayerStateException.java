/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.par.engine;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 12:46
 * To change this template use File | Settings | File Templates.
 */
public class InvalidPlayerStateException extends EngineException
{
    private PlayerState actualState;
    private PlayerState[] expectedStates;
    
    public InvalidPlayerStateException() {
        super();
    }

    public InvalidPlayerStateException(String message) {
        super(message);
    }

    public InvalidPlayerStateException(String message, PlayerState actualState, PlayerState[] expectedStates) {
        super(message);
        this.actualState = actualState;
        this.expectedStates = expectedStates;
    }
    
    public PlayerState getActualState() {
        return actualState;
    }
    
    public PlayerState[] getExpectedStates() {
        return expectedStates;
    }
}
