package uk.co.essarsoftware.par.gui.panels;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 14/06/12
 * Time: 16:15
 * To change this template use File | Settings | File Templates.
 */
public class LogPanel extends JTabbedPane
{
    public LogPanel() {
        super();
    }
    
    public void addLogTab(String tabName, Logger log) {
        addTab(tabName, new LogTableTabPanel(log));
    }
    
    public static void main(String[] args) {
        final Logger log = Logger.getLogger("demo");
        log.setLevel(Level.INFO);

        final JFrame fr = new JFrame("Log Panel Demo");
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    fr.setLayout(new BorderLayout());
                    fr.setPreferredSize(new Dimension(600, 400));
    
                    LogPanel lp = new LogPanel();
                    lp.addLogTab("Demo", log);
                    lp.addLogTab("Demo2", log);
    
                    fr.add(lp, BorderLayout.CENTER);
    
                    fr.pack();
                    fr.setVisible(true);
                }
            });
        } catch(InvocationTargetException ite) {
            System.err.println(ite);
        } catch(InterruptedException ie) {
            System.err.println(ie);
        }
        
        Exception e = null;
        try {
            Object o = null;
            o.toString();
        } catch(NullPointerException npe) {
            e = npe;
        }

        log.trace("This is a TRACE message");
        log.debug("This is a DEBUG message");
        log.info("This is an INFO message");
        log.warn("This is a WARN message");
        log.error("This is an ERROR message", e);
        log.fatal("This is a FATAL message");
    }
    
    static class LogTableTabPanel extends JPanel
    {
        private JLabel lblLoggerCounts, lblLoggerName, lblLoggerLevel;
        private LogTable tblLog;
        
        LogTableTabPanel(Logger log) {
            super();
            setLayout(new BorderLayout());

            lblLoggerName = new JLabel(log.getName());
            lblLoggerName.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), BorderFactory.createEmptyBorder(3, 10, 3, 10)));
            lblLoggerName.setPreferredSize(new Dimension(300, 20));

            lblLoggerLevel = new JLabel(log.getLevel().toString());
            lblLoggerLevel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), BorderFactory.createEmptyBorder(3, 10, 3, 10)));
            lblLoggerLevel.setPreferredSize(new Dimension(80, 20));


            lblLoggerCounts = new JLabel(String.format("F:%d E:%d W:%d I:%d D:%d", 0, 0, 0, 0, 0));
            lblLoggerCounts.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), BorderFactory.createEmptyBorder(3, 10, 3, 10)));
            lblLoggerCounts.setPreferredSize(new Dimension(170, 20));

            tblLog = new LogTable(this, log);

            // Build and add status bar
            Box statusBar = Box.createHorizontalBox();
            statusBar.add(lblLoggerName);
            statusBar.add(lblLoggerLevel);
            statusBar.add(lblLoggerCounts);
            statusBar.add(Box.createHorizontalGlue());
            add(statusBar, BorderLayout.SOUTH);

            // Add table in scroll pane
            add(new JScrollPane(tblLog), BorderLayout.CENTER);
        }

        public void setCounts(final int ctF, final int ctE, final int ctW, final int ctI, final int ctD) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    lblLoggerCounts.setText(String.format("F:%d E:%d W:%d I:%d D:%d", ctF, ctE, ctW, ctI, ctD));
                }
            });
        }
    }
}
