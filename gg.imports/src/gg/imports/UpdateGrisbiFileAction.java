/*
 * UpdateGrisbiFileAction.java
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
package gg.imports;

import gg.application.Constants;
import gg.db.entities.FileImport;
import gg.wallet.Wallet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.NbBundle;

/**
 * Update Grisbi File action<BR/>
 * Permits to re-import the current Grisbi file into the embedded database
 */
public final class UpdateGrisbiFileAction implements ActionListener {

    /** Logger */
    private Logger log = Logger.getLogger(UpdateGrisbiFileAction.class.getName());

    @Override
    public void actionPerformed(ActionEvent e) {
        log.entering(this.getClass().getName(), "actionPerformed");

        // Get the current Grisbi file
        FileImport currentFileImport = Wallet.getInstance().getCurrentFileImport();

        // If there is no Grisbi file (the user never imported a Grisbi file in the DB)
        if (currentFileImport == null) {
            log.info("No grisbi file to update");
            NotifyDescriptor message = new NotifyDescriptor.Message(
                    NbBundle.getMessage(UpdateGrisbiFileAction.class, "UpdateGrisbiFileAction.NoGrisbiFile"),
                    NotifyDescriptor.ERROR_MESSAGE);
            message.setTitle(Constants.APPLICATION_TITLE);
            DialogDisplayer.getDefault().notify(message);
            return;
        }

        try {
            File grisbiFile = new File(currentFileImport.getFilePath());
            ImporterEngine importerEngine = new ImporterEngine(grisbiFile);

            // Import the Grisbi file in the DB
            Thread t = new Thread(importerEngine);
            t.start();
            
        } catch (FileNotFoundException ex) {
            log.log(Level.WARNING, "The Grisbi file '" + currentFileImport.getFilePath() + "' cannot be found", ex);
            NotifyDescriptor.Exception message = new NotifyDescriptor.Exception(ex);
            message.setTitle(Constants.APPLICATION_TITLE);
            DialogDisplayer.getDefault().notifyLater(message);
        }
        log.exiting(this.getClass().getName(), "actionPerformed");
    }
}
