/*
 * ImportGrisbiFileAction.java
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import org.openide.NotifyDescriptor;
import org.openide.windows.WindowManager;

/**
 * Import Grisbi File action
 */
public final class ImportGrisbiFileAction implements ActionListener {

    /** Logger */
    private Logger log = Logger.getLogger(ImportGrisbiFileAction.class.getName());

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setDialogTitle("Select Grisbi file");
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.addChoosableFileFilter(new FilterGSB()); // The GSB filter is displayed (Grisbi files have the extension *.gsb)

        // TODO Remember file path (folder)

        // Display the file chooser to select the Grisbi file to import
        int returnVal = fileChooser.showOpenDialog(WindowManager.getDefault().getMainWindow());

        // If a file is selected, import the selected Grisbi file into the embedded database
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                ImporterEngine importerEngine = new ImporterEngine(fileChooser.getSelectedFile());
                Thread t = new Thread(importerEngine);
                t.start();
                
            } catch (FileNotFoundException ex) {
                log.log(Level.WARNING, "The Grisbi file '" + fileChooser.getSelectedFile().getAbsolutePath() + "' cannot be found", ex);
                NotifyDescriptor d = new NotifyDescriptor.Exception(ex);
            }
        }
    }
}