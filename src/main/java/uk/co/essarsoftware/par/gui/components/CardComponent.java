package uk.co.essarsoftware.par.gui.components;

import uk.co.essarsoftware.games.cards.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * <p>Java Sing component to display a playing card object.</p>
 * <p>Extends JToggleButton to provide standard functionality.  Card can be displayed either face up or face down and
 * can be in a selected or unselected state.</p>
 */
public class CardComponent extends JToggleButton
{
    // Constants
    /** Golden ratio for setting card proportions. */
    public static final float G_RATIO = 1.6180339887f;

    // Component properties
    private boolean renderNull, selectable;
    private final SelectableCardProperty scp;

    // Drawing parameters
    private float cardWidth = 50f;
    private float cardHeight = cardWidth * G_RATIO;
    private float selectOffset = 20f;

    public CardComponent() {
        this(true, false);
    }
    
    /**
     * Create a new CardComponent object, specifying whether the card is face up or face down and whether it can be selected.
     * @param faceUp <tt>true</tt> to specify face up, <tt>false</tt> to specify face down.
     * @param selectable <tt>true</tt> to specify the card can be selected, <tt>false</tt> to specify it cannot be selected.
     */
    public CardComponent(boolean faceUp, boolean selectable) {
        scp = new SelectableCardProperty();
        scp.setFaceUp(faceUp);
        this.selectable = selectable;

        // Create listener on bean to repaint object when properties change
        scp.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                repaint();
                firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            }
        });

        // Create action listener to toggle selected state
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isSelectable()) {
                    setSelected(!isSelected());
                }
            }
        });
    }

    /**
     * Get the <tt>Card</tt> object this component is currently displaying.
     * @return the <tt>Card</tt> object currently being displayed.
     */
    public Card getCard() {
        return scp.getCard();
    }

    /**
     * Get the minimum size of this card component.
     * @return the same dimensions as @see getPreferredSize().
     */
    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    /**
     * Get the preferred size of this card component.
     * @return the calculated component size.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Math.round(cardWidth) + 1, Math.round(cardHeight) + Math.round(selectOffset) + 1);
    }

    @Override
    public boolean isBorderPainted() {
        return false;
    }

    public boolean isFaceUp() {
        return scp.isFaceUp();
    }

    @Override
    public boolean isOpaque() {
        return false;
    }

    public boolean isRenderNull() {
        return renderNull;
    }

    public boolean isSelectable() {
        return selectable;
    }

    @Override
    public boolean isSelected() {
        return scp.isSelected();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;

        // Shift card drawing up by offset unless selected
        if(! isSelected()) {
            // Move card drawing down
            g2D.translate(0, selectOffset);
        }

        Card card = scp.getCard();
        
        // Create objects
        float cornerRoundedness = 10;
        Shape bg = new RoundRectangle2D.Float(1.0f, 1.0f, cardWidth - 2.0f, cardHeight - 2.0f, cornerRoundedness, cornerRoundedness);

        // Draw
        if(isFaceUp() || card != null || renderNull) {
            // Draw containing rectangle
            g2D.setColor(Color.WHITE);
            g2D.fill(bg);
        }

        // Draw outline
        g2D.setColor(Color.BLACK);
        g2D.draw(bg);

        if(isFaceUp() && card != null) {
            // Draw face of card
            g2D.setColor(card.getSuit() == Card.Suit.CLUBS || card.getSuit() == Card.Suit.SPADES ? Color.BLACK : Color.RED);
            g2D.drawString(card.toString(), 5, 20);
            g2D.rotate(Math.PI, cardWidth / 2, cardHeight / 2);
            g2D.drawString(card.toString(), 5, 20);
        } else if(card != null || renderNull) {
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

    /**
     * Specify the <tt>Card</tt> object that this component displays.
     * @param card a Card to display.
     */
    public void setCard(Card card) {
        scp.setCard(card);
    }

    /**
     * Specify whether this component should be face up or face down.
     * @param faceUp <tt>true</tt> to specify face up, <tt>false</tt> to specify face down.
     */
    public void setFaceUp(boolean faceUp) {
        scp.setFaceUp(faceUp);
    }

    /**
     * Specify whether this card should be rendered, even when null.
     * @param renderNull <tt>true</tt> to draw this card when null and face down, <tt>false</tt> otherwise.
     */
    public void setRenderNull(boolean renderNull) {
        this.renderNull = renderNull;
    }

    /**
     * Specify whether this component should be selected.
     * @param selected <tt>true</tt> to specify the card is selected, false to specify unselected.
     */
    @Override
    public void setSelected(boolean selected) {
        if(selectable) {
            scp.setSelected(selected);
        }
    }
}
