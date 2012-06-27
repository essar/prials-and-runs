package uk.co.essarsoftware.par.gui.components;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 26/06/12
 * Time: 11:34
 * To change this template use File | Settings | File Templates.
 */
abstract class BaseProperty
{
    protected final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }
    
    public void addPropertyChangeListener(String name, PropertyChangeListener l) {
        pcs.addPropertyChangeListener(name, l);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }
    
    public void removePropertyChangeListener(String name, PropertyChangeListener l) {
        pcs.removePropertyChangeListener(name, l);
    }
}
