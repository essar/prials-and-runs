package uk.co.essarsoftware.par.gui.debugtool;

import org.apache.log4j.Logger;
import uk.co.essarsoftware.par.gui.panels.LogPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 28/06/12
 * Time: 18:54
 * To change this template use File | Settings | File Templates.
 */
public class LogFrame extends JInternalFrame
{
    private LogPanel pnlLogs;

    public LogFrame() {
        super("Logs");
        setPreferredSize(new Dimension(1000, 300));

        initComponents();
        drawComponents();

        pack();
    }

    private void initComponents() {
        pnlLogs = new LogPanel();
    }

    private void drawComponents() {
        setContentPane(pnlLogs);
    }

    public void addLog(String logName, Logger log) {
        pnlLogs.addLogTab(logName, log);
    }
}
