package uk.co.essarsoftware.par.gui.panels;

import uk.co.essarsoftware.games.cards.Card;
import uk.co.essarsoftware.par.engine.Play;
import uk.co.essarsoftware.par.engine.Player;
import uk.co.essarsoftware.par.engine.Round;
import uk.co.essarsoftware.par.gui.components.CardComponent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

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
    private HashMap<Player, PlaysPanel> plays;

    public TablePanel() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        initComponents();
        drawComponents();
    }
    
    private void initComponents() {
        discardPile = new CardComponent(true, false);
        discardPile.addActionListener(new DiscardPileActionListener());
        
        drawPile = new CardComponent(false, false);
        drawPile.addActionListener(new DrawPileActionListener());
        drawPile.setRenderNull(true);

        plays = new HashMap<Player, PlaysPanel>();
    }
    
    private void drawComponents() {
        JPanel piles = new JPanel();
        piles.add(discardPile);
        piles.add(drawPile);
        add(piles);

        for(Player p: plays.keySet()) {
            add(new JLabel(p.getPlayerName()));
            add(plays.get(p));
        }
    }

    public void addPlayer(Player player, Play[] playerPlays) {
        // Create plays panel
        PlaysPanel pp = new PlaysPanel(0);
        pp.setPlays(playerPlays);
        plays.put(player, pp);

        // Add components
        add(new JLabel(player.getPlayerName()));
        add(pp);

        // Flag container as changed so resizing can occur
        invalidate();
    }

    public void setDiscard(Card card) {
        discardPile.setCard(card);
    }

    public void setPlays(Player player, Play[] playerPlays) {
        PlaysPanel pp = plays.get(player);
        if(pp != null) {
            pp.setPlays(playerPlays);
        }
    }

    public void initRound(Round round) {
        for(PlaysPanel pp : plays.values()) {
            pp.reset(round.getPrials() + round.getRuns());
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
