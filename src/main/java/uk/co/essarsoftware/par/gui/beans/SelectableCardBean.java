package uk.co.essarsoftware.par.gui.beans;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 26/06/12
 * Time: 11:33
 * To change this template use File | Settings | File Templates.
 */
public class SelectableCardBean extends CardBean
{
    private boolean selected = false;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
