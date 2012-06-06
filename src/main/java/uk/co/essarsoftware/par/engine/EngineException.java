package uk.co.essarsoftware.par.engine;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 05/06/12
 * Time: 12:46
 * To change this template use File | Settings | File Templates.
 */
public class EngineException extends Exception
{
    public EngineException() {
        super();
    }

    public EngineException(String message) {
        super(message);
    }
    
    public EngineException(Throwable cause) {
        super(cause);
    }
    
    public EngineException(String message, Throwable cause) {
        super(message, cause);
    }
}
