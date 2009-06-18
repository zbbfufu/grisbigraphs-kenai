/*
 * GeneralOptionsPanelController.java
 *
 * Copyright (C) 2009 Francois Duchemin
 *
 * This file is part of GrisbiGraphs.
 *
 * GrisbiGraphs is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * GrisbiGraphs is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GrisbiGraphs; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package gg.options;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JComponent;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;

/**
 * Controller for the General options panel
 * @author Francois Duchemin
 */
public final class GeneralOptionsPanelController extends OptionsPanelController {

    /** General options panel */
    private GeneralPanel panel;
    /** Property change */
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    /** Has something been changed on the general options panel */
    private boolean changed;

    /** Component should load its data here */
    public void update() {
        getPanel().load();
        changed = false;
    }

    /** This method is called when Options Dialog "OK" button is pressed */
    public void applyChanges() {
        getPanel().store();
        changed = false;
    }

    /** This method is called when Options Dialog "Cancel" button is pressed */
    public void cancel() {
        // need not do anything special, if no changes have been persisted yet
    }

    /**
     * Should return true if some option value in this category is valid
     * @return true if some option value in this category is valid
     */
    public boolean isValid() {
        return getPanel().valid();
    }

    /**
     * Should return true if some option value in this category has been changed
     * @return true if some option value in this category has been changed
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * Get current help context asociated with this panel
     * @return current help context
     */
    public HelpCtx getHelpCtx() {
        return null; // new HelpCtx("...ID") if you have a help set
    }

    /**
     * Returns visual component representing this options category. This method is called before update() method
     * @param masterLookup master lookup composed from lookups provided by individual OptionsPanelControllers - getLookup()
     * @return visual component representing this options category
     */
    public JComponent getComponent(Lookup masterLookup) {
        return getPanel();
    }

    /**
     * Registers new listener
     * @param l a new listener
     */
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    /**
     * Unregisters given listener
     * @param l a listener to be removed
     */
    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    /**
     * Gets General options panel
     * @return General options panel
     */
    private GeneralPanel getPanel() {
        if (panel == null) {
            panel = new GeneralPanel(this);
        }
        return panel;
    }

    /**  Fire property change events. Should be called when a field is modified. */
    public void changed() {
        if (!changed) {
            changed = true;
            pcs.firePropertyChange(OptionsPanelController.PROP_CHANGED, false, true);
        }
        pcs.firePropertyChange(OptionsPanelController.PROP_VALID, null, null);
    }
}
