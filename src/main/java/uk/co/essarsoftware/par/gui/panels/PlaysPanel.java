package uk.co.essarsoftware.par.gui.panels;

import uk.co.essarsoftware.par.client.GameClient;
import uk.co.essarsoftware.par.engine.Play;

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
    private GameClient client;

    private CardsPanel[] cards;
    private JLabel lblPlayerName;

    public PlaysPanel() {
        setLayout(new BorderLayout());

        initComponents();
        drawComponents();
    }

    private void initComponents() {
        lblPlayerName = new JLabel(client.getPlayerName());

        Play[] ps = client.getTable().getPlays(client.getPlayer());
        cards = new CardsPanel[ps.length];
        
        for(int i = 0; i < ps.length; i ++) {
            cards[i] = new CardsPanel() {
                @Override
                protected CardComponent createComponent() {
                    return new CardComponent(true, false);
                }
            };
            cards[i].setCards(ps[i].getCards());
        }
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
}
