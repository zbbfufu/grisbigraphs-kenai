/*
 * Installer.java
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
package gg.application;

import org.openide.modules.ModuleInstall;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Installs the application's module
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        // Set the version of the program when the application is started
        System.setProperty("netbeans.buildnumber", Constants.VERSION);
    }

    @Override
    public boolean closing() {
        // When the application is closed: activate the first editor TopComponent
        // so that the SearchFilter TopComponent is not active when the application is closed
        // (otherwise its listener is not registered correctly when the application is re-opened)

        // Get the list of opened TopComponents (not necessarily 'editor' TopComponents)
        TopComponent[] openedTopComponents = TopComponent.getRegistry().getOpened().toArray(new TopComponent[0]);
        // 'openedTopComponents' contains the opened TopComponents in a reverse order
        int i = openedTopComponents.length - 1;
        boolean tcFound = false;
        while (i >= 0 && !tcFound) {
            if (WindowManager.getDefault().isOpenedEditorTopComponent(openedTopComponents[i])) { // TopComponent is an 'editor' TopComponent
                openedTopComponents[i].requestActive(); // Activate the first 'editor' TopComponent
                tcFound = true;
            }
            i--;
        }

        // Close application
        return super.closing();
    }
}
