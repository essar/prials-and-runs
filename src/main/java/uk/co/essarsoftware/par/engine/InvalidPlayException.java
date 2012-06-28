package uk.co.essarsoftware.par.engine;

import uk.co.essarsoftware.games.cards.Card;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 06/06/12
 * Time: 15:10
 * To change this template use File | Settings | File Templates.
 */
public class InvalidPlayException extends EngineException
{
    public InvalidPlayException() {
        super();
    }

    public InvalidPlayException(String message) {
        super(message);
    }
}
