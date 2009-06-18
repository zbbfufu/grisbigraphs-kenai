/*
 * FormUtility.java
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
package gg.searchfilter;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Utility class that permits to add fields in a component that has a GridBagLayout
 * @author Francois Duchemin
 */
public class FormUtility {

    /** Grid bag constraints for labels */
    private GridBagConstraints labelConstraints = null;
    /** Grid bag constraints for fields */
    private GridBagConstraints fieldConstraints = null;

    /** Creates a new instance of FormUtility */
    public FormUtility() {
        // Set up the constraints for the "fields"

        // weightx is 1.0 for fields, 0.0 for labels
        // gridwidth is REMAINDER for fields, 1 for labels
        fieldConstraints = new GridBagConstraints();

        // Stretch components horizontally (but not vertically)
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;

        // Components that are too short or narrow for their space
        // Should be pinned to the northwest (upper left) corner
        fieldConstraints.anchor = GridBagConstraints.NORTHWEST;

        // Give the "field" component as much space as possible
        fieldConstraints.weightx = 1.0;

        // Give the "field" component the remainder of the row
        fieldConstraints.gridwidth = GridBagConstraints.REMAINDER;

        // Add a little padding
        fieldConstraints.insets = new Insets(5, 5, 5, 5);

        // Set up the "label" constrains
        labelConstraints = (GridBagConstraints) fieldConstraints.clone();
        labelConstraints.insets = new Insets(9, 5, 1, 5);

        // Give these as little space as necessary
        labelConstraints.weightx = 0.0;
        labelConstraints.gridwidth = 1;
    }

    /**
     * Adds a label component
     * @param labelComponent Label component to add
     * @param parentComponent Parent component on which the label must be added
     */
    public void addLabel(Component labelComponent, Container parentComponent) {
        GridBagLayout gbl = (GridBagLayout) parentComponent.getLayout();
        gbl.setConstraints(labelComponent, labelConstraints);
        parentComponent.add(labelComponent);
    }

    /**
     * Adds a field component
     * @param fieldComponent Field component to add
     * @param parentComponent Parent component on which the field must be added
     */
    public void addField(Component fieldComponent, Container parentComponent) {
        GridBagLayout gbl = (GridBagLayout) parentComponent.getLayout();
        gbl.setConstraints(fieldComponent, fieldConstraints);
        parentComponent.add(fieldComponent);
    }
}
