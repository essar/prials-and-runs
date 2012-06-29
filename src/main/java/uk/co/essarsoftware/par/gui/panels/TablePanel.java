package uk.co.essarsoftware.par.gui.panels;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.gui.components.CardComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 19/06/12
 * Time: 11:14
 * To change this template use File | Settings | File Templates.
 */
public class TablePanel extends JPanel
{
    // Swing components
    private CardComponent discardPile, drawPile;
    private PlaysPanel[] plays;

    public TablePanel() {
        setLayout(new GridBagLayout());
        
        initComponents();
        drawComponents();
    }
    
    private void initComponents() {
        discardPile = new CardComponent(true, false);
        discardPile.addActionListener(new DiscardPileActionListener());
        
        drawPile = new CardComponent(false, false);
        drawPile.addActionListener(new DrawPileActionListener());

        plays = new PlaysPanel[0];
    }
    
    private void drawComponents() {
        GridBagConstraints con = new GridBagConstraints();
        con.gridwidth = 1;
        con.gridx = 0; con.gridy = 0;
        con.anchor = GridBagConstraints.EAST;
        add(discardPile, con);
        
        con.gridx = 1;
        con.anchor = GridBagConstraints.WEST;
        add(drawPile, con);
        
        con.gridx = 0;
        con.gridwidth = 2;
        con.anchor = GridBagConstraints.NORTH;
        con.gridy = GridBagConstraints.RELATIVE;
        for(PlaysPanel pp : plays) {
            add(pp, con);
        }
    }

    public void setDiscard(Card card) {
        discardPile.setCard(card);
    }
    
    public static void main(String[] args) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    JFrame fr = new JFrame("TablePanel Test");
                    fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    fr.setLayout(new BorderLayout());

                    TablePanel tp = new TablePanel();
                    fr.add(tp, BorderLayout.CENTER);

                    fr.pack();
                    fr.setVisible(true);
                }
            });
        } catch(InvocationTargetException ite) {
            System.err.println(ite);
        } catch(InterruptedException ie) {
            System.err.println(ie);
        }
    }

    private class DiscardPileActionListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Discard pile clicked");
        }
    }
    
    private class DrawPileActionListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Draw pile clicked");
        }
    }
    
}
