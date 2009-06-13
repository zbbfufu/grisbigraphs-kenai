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
        // Set the version of the program
        System.setProperty("netbeans.buildnumber", Constants.VERSION);
    }

    @Override
    public boolean closing() {
        // Activate the first editor tc so that the Searchfilter tc is not active when the app is closed (otherwise its listener is not registered correctly)
        boolean tcFound = false;
        TopComponent[] tc = TopComponent.getRegistry().getOpened().toArray(new TopComponent[0]);
        int i = tc.length - 1;
        while (i >= 0 && !tcFound) {
            if (WindowManager.getDefault().isOpenedEditorTopComponent(tc[i])) {
                tc[i].requestActive();
                tcFound = true;
            }
            i--;
        }

        return super.closing();
    }
}
