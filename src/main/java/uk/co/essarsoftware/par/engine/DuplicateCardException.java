/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.par.engine;

import uk.co.essarsoftware.games.cards.Card;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 06/06/12
 * Time: 15:10
 * To change this template use File | Settings | File Templates.
 */
public class DuplicateCardException extends EngineException
{
    private Card card;
    private Player player;

    public DuplicateCardException() {
        super();
    }

    public DuplicateCardException(String message) {
        super(message);
    }

    public DuplicateCardException(String message, Card card, Player player) {
        super(message);
        this.card = card;
        this.player = player;
    }
}
