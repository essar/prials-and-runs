package uk.co.essarsoftware.par.gui.panels;

import uk.co.essarsoftware.games.cards.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 18/06/12
 * Time: 09:30
 * To change this template use File | Settings | File Templates.
 */
public class CardComponent extends JToggleButton
{
    public static final float G_RATIO = 1.6180339887f;

    private boolean faceUp, selectable, selected;
    private Card card;

    private float cardWidth = 50f;
    private float cardHeight = cardWidth * G_RATIO;
    private float selectOffset = 20f;

    public CardComponent(boolean faceUp, boolean selectable) {
        this.selectable = selectable;
        this.faceUp = faceUp;
    }
    
    public Card getCard() {
        return card;
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Math.round(cardWidth) + 1, Math.round(cardHeight) + 1 + (selectable ? Math.round(selectOffset) : 0));
    }

    @Override
    public boolean isBorderPainted() {
        return false;
    }

    public boolean isFaceUp() {
        return faceUp;
    }

    @Override
    public boolean isOpaque() {
        return false;
    }

    public boolean isSelectable() {
        return selectable;
    }

    @Override
    public boolean isSelected() {
        return isSelectable() && selected;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;

        // Shift card drawing up by offset
        if(! isSelected()) {
            // Move card drawing down
            g2D.translate(0, selectOffset);
        }

        // Create objects
        float cornerRoundedness = 10;
        Shape bg = new RoundRectangle2D.Float(1.0f, 1.0f, cardWidth - 2.0f, cardHeight - 2.0f, cornerRoundedness, cornerRoundedness);

        // Draw
        if(! (isFaceUp() && card == null)) {
            // Draw containing rectangle
            g2D.setColor(Color.WHITE);
            g2D.fill(bg);
        }

        // Draw outline
        g2D.setColor(Color.BLACK);
        g2D.draw(bg);

        if(isFaceUp()) {
            if(card != null) {
                // Draw text
                g2D.setColor(card.getSuit() == Card.Suit.CLUBS || card.getSuit() == Card.Suit.SPADES ? Color.BLACK : Color.RED);
                g2D.drawString(card.toString(), 5, 20);
                g2D.rotate(Math.PI, cardWidth / 2, cardHeight / 2);
                g2D.drawString(card.toString(), 5, 20);
            }
        } else {
            // Draw background of card
            g2D.setColor(Color.RED);
            for(float y = 10.0f; y < (cardHeight - 20.0f); y += 5.0f) {
                g2D.draw(new Line2D.Float(10.0f, y, (cardWidth - 10.0f), y + 10.0f));
            }
            g2D.setColor(Color.BLUE);
            for(float y = 10.0f; y < (cardHeight - 20.0f); y += 5.0f) {
                g2D.draw(new Line2D.Float((cardWidth - 10.0f), y, 10.0f, y + 10.0f));
            }
        }
    }

    public void setCard(Card card) {
        this.card = card;
        repaint();
    }

    public void setFaceUp(boolean faceUp) {
        this.faceUp = faceUp;
        repaint();
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }
}
