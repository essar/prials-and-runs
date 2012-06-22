package uk.co.essarsoftware.par.gui.panels;

import uk.co.essarsoftware.par.client.GameClient;
import uk.co.essarsoftware.par.engine.Play;
import uk.co.essarsoftware.par.gui.components.CardComponent;
import uk.co.essarsoftware.par.gui.components.CardsPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 19/06/12
 * Time: 15:33
 * To change this template use File | Settings | File Templates.
 */
class PlaysPanel extends JPanel
{
    private CardsPanel[] cards;
    private JLabel lblPlayerName;

    public PlaysPanel() {
        setLayout(new BorderLayout());

        initComponents();
        drawComponents();
    }

    private void initComponents() {
        lblPlayerName = new JLabel("Player");

        cards = new CardsPanel[0];
    }
    
    private void drawComponents() {
        // Add player name
        add(lblPlayerName, BorderLayout.NORTH);

        // Add cards
        Container plays = new Container();
        plays.setLayout(new FlowLayout(20));
        for(CardsPanel cp : cards) {
            plays.add(cp);
        }
        add(plays, BorderLayout.CENTER);
    }
    
    public void setPlays(Play[] ps) {
        cards = new CardsPanel[ps.length];
        for(int i = 0; i < ps.length; i ++) {
            cards[i] = new CardsPanel();
            cards[i].setCards(ps[i].getCards());
        }
    }
    
    public void setPlayerName(String playerName) {
        lblPlayerName.setText(playerName);
    }
}
