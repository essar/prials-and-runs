package uk.co.essarsoftware.par.gui.debugtool;

import org.apache.log4j.BasicConfigurator;
import uk.co.essarsoftware.par.client.DirectClient;
import uk.co.essarsoftware.par.client.GameClient;
import uk.co.essarsoftware.par.engine.*;
import uk.co.essarsoftware.par.engine.std.StdGameFactory;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 28/06/12
 * Time: 20:10
 * To change this template use File | Settings | File Templates.
 */
public class DebugToolFrame extends JFrame
{
    private JDesktopPane desktopPane;

    public DebugToolFrame() {
        super("Prials and Runs");
        setPreferredSize(new Dimension(1024, 768));

        initComponents();
        drawComponents();
        pack();
    }

    private void initComponents() {
        // Create desktop pane
        desktopPane = new JDesktopPane();
    }

    private void drawComponents() {
        setContentPane(desktopPane);
    }

    public void addClient(DirectClient client) {
        PlayerFrame pf1 = new PlayerFrame(this, client);
        client.setUI(pf1.generateUI());
        pf1.setVisible(true);
        desktopPane.add(pf1);
    }

    public static void main(String[] args) {
        BasicConfigurator.configure();

        // Init game
        GameFactory gf = GameFactory.getInstance(StdGameFactory.class);

        final Game game = gf.createGame();
        final Engine engine = gf.createEngine(game);

        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    DebugToolFrame dtf = new DebugToolFrame();
                    dtf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

                    dtf.addClient(new DirectClient(engine, game.getController(), "Player 1"));
                    dtf.addClient(new DirectClient(engine, game.getController(), "Player 2"));
                    dtf.addClient(new DirectClient(engine, game.getController(), "Player 3"));

                    dtf.setVisible(true);
                }
            });
        } catch(InterruptedException ie) {
        } catch(InvocationTargetException ite) {}

        try {
            engine.startGame();
            System.out.println("Engine started");
        } catch(EngineException ee) {
            System.err.println(ee);
        }

    }


}
