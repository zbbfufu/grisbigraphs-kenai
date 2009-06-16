/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gg.imports;

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

public final class UpdateGrisbiFileAction implements ActionListener {

    /** Logger */
    private Logger log = Logger.getLogger(UpdateGrisbiFileAction.class.getName());

    @Override
    public void actionPerformed(ActionEvent e) {
        FileImport currentFileImport = Wallet.getInstance().getCurrentFileImport();

        if (currentFileImport == null) {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    "No current file",
                    NotifyDescriptor.ERROR_MESSAGE);
            DialogDisplayer.getDefault().notify(d);
            return;
        }

        try {
            File grisbiFile = new File(currentFileImport.getFilePath());
            ImporterEngine importerEngine = new ImporterEngine(grisbiFile);
            Thread t = new Thread(importerEngine);
            t.start();
        } catch (FileNotFoundException ex) {
            log.log(Level.WARNING, "The Grisbi file '" + currentFileImport.getFilePath() + "' cannot be found", ex);
            NotifyDescriptor d = new NotifyDescriptor.Exception(ex);
        }
    }
}
