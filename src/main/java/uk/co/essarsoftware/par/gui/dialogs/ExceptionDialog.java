package uk.co.essarsoftware.par.gui.dialogs;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 12/06/12
 * Time: 09:55
 * To change this template use File | Settings | File Templates.
 */
public class ExceptionDialog extends JDialog
{
    private Exception e;
    
    // Swing components
    private Icon icon;
    private JButton btnDetails, btnClose;
    private JLabel lblMessage;
    private JScrollPane srlTrace;
    private ExceptionTracePane txtTrace;

    public ExceptionDialog(Exception e) {
        this(null, e);
    }
    
    public ExceptionDialog(Frame parent, Exception e) {
        super(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.e = e;
        setMinimumSize(new Dimension(400, 150));
        setTitle(e == null ? "Exception" : e.getClass().getName());

        initComponents();
        loadComponents();

        pack();
    }

    private void initComponents() {
        // Message - JLabel
        lblMessage = new JLabel(String.format("An unexpected error has occurred: %s", e == null ? "Unexpected exception" : e.getMessage()));
        lblMessage.setIcon(icon);
        lblMessage.setPreferredSize(new Dimension(360, 40));
        lblMessage.setBorder(BorderFactory.createLineBorder(Color.red));

        // txtTrace - ExceptionTracePane
        txtTrace = new ExceptionTracePane();
        txtTrace.setBackground(new Color(92, 0, 0));
        txtTrace.setException(e);

        // srlTrace - JScrollPane
        JPanel traceWrapper = new JPanel(new BorderLayout());
        traceWrapper.add(txtTrace, BorderLayout.CENTER);
        srlTrace = new JScrollPane(traceWrapper);
        srlTrace.setPreferredSize(new Dimension(360, 200));
        srlTrace.setVisible(false);
        
        // btnDetails - JButton
        btnDetails = new JButton("Details >");
        btnDetails.setAction(new DetailsButtonAction());
        btnDetails.setPreferredSize(new Dimension(100, 40));
        
        // btnClose - JButton
        btnClose = new JButton("OK");
        btnClose.setAction(new CloseButtonAction());
        btnClose.setDefaultCapable(true);
        btnClose.setPreferredSize(new Dimension(100, 40));
    }

    private void loadComponents() {
        GridBagConstraints con = new GridBagConstraints();
        con.gridx = 0; con.gridy = 0;
        con.gridwidth = 1; con.gridheight = 1;
        con.anchor = GridBagConstraints.NORTH;
        con.fill = GridBagConstraints.HORIZONTAL;
        con.insets = new Insets(5, 5, 5, 5);
        con.weightx = 0; con.weighty = 0;

        Box btns = Box.createHorizontalBox();
        btns.add(btnDetails);
        btns.add(Box.createHorizontalGlue());
        btns.add(btnClose);

        // Set up content
        Container content = getContentPane();
        content.setLayout(new GridBagLayout());

        // Add message label
        con.weightx = 1; con.weighty = 1;
        content.add(lblMessage, con);

        // Add button box
        con.gridy = 1;
        con.weighty = 0;
        content.add(btns, con);

        // Add trace pane
        con.gridy = 2;

        content.add(srlTrace, con);

        // Set default button
        getRootPane().setDefaultButton(btnClose);
    }

    private class CloseButtonAction extends AbstractAction
    {
        CloseButtonAction() {
            // Set action values
            putValue(AbstractAction.MNEMONIC_KEY, KeyEvent.VK_C);
            putValue(AbstractAction.NAME, "Close");
            putValue(AbstractAction.SHORT_DESCRIPTION, "Close dialog");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Close & dispose dialog
            setVisible(false);
            dispose();
        }
    }

    private class DetailsButtonAction extends AbstractAction
    {
        DetailsButtonAction() {
            // Set action values
            putValue(AbstractAction.MNEMONIC_KEY, KeyEvent.VK_D);
            putValue(AbstractAction.NAME, "Details >");
            putValue(AbstractAction.SHORT_DESCRIPTION, "Show exception trace details");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Toggle trace pane
            if(srlTrace.isVisible()) {
                // Set Hide trace pane
                srlTrace.setVisible(false);
                // Change button values
                putValue(AbstractAction.NAME, "Details >");
                putValue(AbstractAction.SHORT_DESCRIPTION, "Show exception trace details");
                // Change minimum dialog size
                setMinimumSize(new Dimension(400, 150));

            } else {
                // Set Hide trace pane
                srlTrace.setVisible(true);
                // Change button values
                putValue(AbstractAction.NAME, "< Details");
                putValue(AbstractAction.SHORT_DESCRIPTION, "Hide exception trace details");
                // Change minimum dialog size
                setMinimumSize(new Dimension(400, 350));
            }
            // Resize dialog to fit visible components
            pack();
        }
    }
    
    private static class ExceptionTracePane extends JTextPane
    {
        private String[] traceLines;
        private Exception e;

        public ExceptionTracePane() {
            loadStyles(getStyledDocument());
            setEditable(false);
        }

        private void loadStyles(StyledDocument doc) {
            Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
            StyleConstants.setFontFamily(def, "Courier New");

            // Add normal style
            Style normal = doc.addStyle("normal", def);
            StyleConstants.setForeground(def, Color.LIGHT_GRAY);

            // Add highlighted style from normal
            Style red = doc.addStyle("red", normal);
            StyleConstants.setForeground(red, Color.RED);
        }
        
        private void parseException() {
            StringWriter sw = new StringWriter(1024);
            if(e != null) {
                e.printStackTrace(new PrintWriter(sw));
            }
            traceLines = new String(sw.getBuffer()).split("\n");    
        }
        
        private void setOutput() {
            String[] styles = new String[traceLines.length];
            Pattern p1 = Pattern.compile("uk.co.essarsoftware.par.", Pattern.LITERAL);
            for(int i = 0; i < styles.length; i ++) {
                styles[i] = (p1.matcher(traceLines[i]).find() ? "red" : "normal");
            }

            StyledDocument doc = getStyledDocument();
            try {
                for(int i = 0; i < traceLines.length; i ++) {
                    doc.insertString(doc.getLength(), traceLines[i] + "\n", doc.getStyle(styles[i]));
                }
            } catch(BadLocationException ble) {
                setText("Unexpected exception loading stack trace");
            }
        }

        public Exception getException() {
            return e;
        }

        public void setException(Exception e) {
            this.e = e;
            parseException();
            setOutput();
        } 
    }
    
    
    public static void main(String[] args) {
        // Generate an exception with a trace...
        try {
            String[] dummy = new String[0];
            dummy[0].substring(0);  // Will throw ArrayIndexOutOfBoundException
        } catch(RuntimeException re) {
            ExceptionDialog ed = new ExceptionDialog(re);
            ed.setVisible(true);
        }
    }
}
