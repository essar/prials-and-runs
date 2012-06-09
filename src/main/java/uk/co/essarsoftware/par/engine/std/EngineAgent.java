package uk.co.essarsoftware.par.engine.std;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 09/06/12
 * Time: 15:50
 * To change this template use File | Settings | File Templates.
 */
interface EngineAgent extends Runnable
{
    public void startAgent();

    public void stopAgent();
}