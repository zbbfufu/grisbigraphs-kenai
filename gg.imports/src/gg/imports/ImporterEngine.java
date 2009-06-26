/*
 * ImporterEngine.java
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
import gg.db.datamodel.Datamodel;
import gg.db.entities.Account;
import gg.db.entities.Currency;
import gg.db.entities.FileImport;
import gg.db.datamodel.DateFormatException;
import gg.db.entities.Transaction;
import gg.view.overview.OverviewTopComponent;
import gg.wallet.Wallet;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.joda.time.DateTime;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.StatusDisplayer;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * <B>ImporterEngine</B>
 * <UL>
 * <LI>Permits to import Grisbi files</LI>
 * <LI>The correct importer object is used to import the Grisbi file</LI>
 * </UL>
 * @author Francois Duchemin
 */
public class ImporterEngine implements Runnable {

    /** Grisbi file to import */
    private File grisbiFile;
    /** Is the import task cancelled by the user? */
    private boolean importCancelled;
    /** Logger */
    private Logger log = Logger.getLogger(ImporterEngine.class.getName());

    /**
     * Creates a new instance of ImporterEngine
     * @param grisbiFile Grisbi file to import
     * @throws FileNotFoundException If the Grisbi file to import does not exist
     */
    public ImporterEngine(File grisbiFile) throws FileNotFoundException {
        log.entering(this.getClass().getName(), "ImporterEngine", grisbiFile.getAbsolutePath());

        setGrisbiFile(grisbiFile);
        setImportCancelled(false);

        log.exiting(this.getClass().getName(), "ImporterEngine");
    }

    /**
     * Gets the Grisbi file to import
     * @return Grisbi file to import
     */
    public File getGrisbiFile() {
        return grisbiFile;
    }

    /**
     * Sets the Grisbi file to import
     * @param grisbiFile Grisbi file to import
     * @throws FileNotFoundException If the Grisbi file to import cannot be found
     */
    public void setGrisbiFile(File grisbiFile) throws FileNotFoundException {
        if (grisbiFile == null) {
            throw new IllegalArgumentException("The parameter 'grisbiFile' is null");
        }

        // Make sure that the Grisbi file can be found
        if (!grisbiFile.exists()) {
            throw new FileNotFoundException("The Grisbi file '" + grisbiFile.getAbsolutePath() + "' cannot be found");
        }

        this.grisbiFile = grisbiFile;
    }

    /**
     * Is the import task cancelled by the user?
     * @return true if the import task is cancelled
     */
    public boolean isImportCancelled() {
        return importCancelled;
    }

    /**
     * Sets the cancel flag
     * @param importCancelled true if the user cancels the import task
     */
    public void setImportCancelled(boolean importCancelled) {
        this.importCancelled = importCancelled;
    }

    /**
     * Parses the Grisbi file and imports the file into a document (that supports XPath)
     * @return Document in which the Grisbi file is imported
     * @throws DocumentException If there is an error parsing the Grisbi file
     */
    private Document getGrisbiFileDocument() throws DocumentException {
        log.entering(this.getClass().getName(), "getGrisbiFileDocument");
        long startParsingTime = System.currentTimeMillis();
        SAXReader reader = new SAXReader();

        // Try to parse the Grisbi file and import it into a DOM document (which support XPath)
        Document grisbiFileDocument = reader.read(grisbiFile); // Throws a DocumentException if there is an error parsing the Grisbi file
        long endParsingTime = System.currentTimeMillis();

        log.info("The Grisbi file '" + grisbiFile.getAbsolutePath() + "' has been successfully imported into a document in " + (endParsingTime - startParsingTime) + " ms");

        log.exiting(this.getClass().getName(), "getGrisbiFileDocument");
        return grisbiFileDocument;
    }

    /**
     * Gets the version of the Grisbi file
     * @param grisbiFileDocument Document of the Grisbi file for which the version is wanted
     * @return Version of the Grisbi file (<CODE>FileVersion.UNSUPPORTED_VERSION</CODE> if the file is not supported)
     */
    private FileVersion getFileVersion(Document grisbiFileDocument) {
        log.entering(this.getClass().getName(), "getFileVersion");
        assert (grisbiFileDocument != null);

        FileVersion fileVersion = FileVersion.UNSUPPORTED_VERSION;

        // Get the version of the grisbi file
        Node fileVersionNode = grisbiFileDocument.selectSingleNode("/Grisbi/Generalites/Version_fichier");
        if (fileVersionNode != null) {
            String fileVersionStr = fileVersionNode.getText();
            if (fileVersionStr.compareToIgnoreCase("0.5.0") == 0) {
                fileVersion = FileVersion.VERSION_0_5_0;
            }
        }

        log.exiting(this.getClass().getName(), "getFileVersion", fileVersion);
        return fileVersion;
    }

    /**
     * Imports the Grisbi file in the embedded database
     * @return Number of miliseconds needed to import the Grisbi file
     * @throws DocumentException If there is an error during the XML parsing
     * @throws ParsingException If there is a problem finding the needed nodes
     * @throws NumberFormatException If a string is read when a number is expected
     * @throws DateFormatException If a date is wrongly formatted
     */
    public long importFile() throws DocumentException, ParsingException, NumberFormatException, DateFormatException {
        log.entering(this.getClass().getName(), "importFile");
        setImportCancelled(false);

        // Import the Grisbi file into a Document (that supports XPath)
        Document grisbiFileDocument = getGrisbiFileDocument();

        // Get the version of the Grisbi file
        FileVersion fileVersion = getFileVersion(grisbiFileDocument);

        // Depending on the Grisbi file version, use the correct importer class to import the file into the embedded database
        Importer grisbiFileImporter;
        switch (fileVersion) {
            case VERSION_0_5_0:
                log.finest("GrisbiFile050 importer used to import '" + grisbiFile.getAbsolutePath() + "'");
                grisbiFileImporter = new GrisbiFile050(grisbiFileDocument, grisbiFile.getAbsolutePath());
                break;
            case VERSION_0_6_0:
                log.severe("This version of Grisbi file is not supported yet");
                throw new AssertionError("This version of Grisbi file is not supported yet");
            case UNSUPPORTED_VERSION:
                log.severe("This version of Grisbi file is not supported");
                throw new AssertionError("This version of Grisbi file is not supported");
            default:
                log.severe("This version of Grisbi file is not supported");
                throw new AssertionError("This version of Grisbi file is not supported");
        }
        long importDuration = grisbiFileImporter.importFile();
        setImportCancelled(grisbiFileImporter.isImportCancelled());

        log.exiting(this.getClass().getName(), "importFile", importDuration);
        return importDuration;
    }

    @Override
    public void run() {
        try {
            // Import the Grisbi file
            long importDuration = importFile();

            // Display a message in the status bar
            boolean success = false; // Is the Grisbi file is correcty imported into the database?
            if (isImportCancelled()) { // Import cancelled by the user
                log.info("Import cancelled by the user");
                Datamodel.emptyDatabase();
                StatusDisplayer.getDefault().setStatusText(
                        NbBundle.getMessage(ImporterEngine.class, "ImporterEngine.ImportCancelled"));
            } else {
                if (checkDatabase()) {
                    log.info("Grisbi file correctly imported");
                    StatusDisplayer.getDefault().setStatusText(
                            NbBundle.getMessage(ImporterEngine.class, "ImporterEngine.GrisbiFileImported"));
                    success = true;
                } else { // Errors during the consistency checks
                    log.info("Consistency check errors found");
                    StatusDisplayer.getDefault().setStatusText(
                            NbBundle.getMessage(ImporterEngine.class, "ImporterEngine.ConsistencyCheckError"));
                }
            }

            // Log the import in the database
            FileImport fileImport = new FileImport(
                    new DateTime(), // Import date
                    grisbiFile.getAbsolutePath(),
                    grisbiFile.getName(),
                    new DateTime(grisbiFile.lastModified()), // Last modification date of the grisbi file
                    importDuration, // Import duration in ms
                    success);
            Datamodel.saveFileImport(fileImport);

            // Update content of the wallet with the new database content
            Wallet.getInstance().updateContent();

            // Close all windows and display Overview window
            Runnable worker = new Runnable() {

                @Override
                public void run() {
                    // Close all opened editor TopComponents (when an editor is closed, its group is also closed)
                    TopComponent[] tc = TopComponent.getRegistry().getOpened().toArray(new TopComponent[0]);
                    for (int i = tc.length - 1; i >= 0; i--) {
                        if (WindowManager.getDefault().isEditorTopComponent(tc[i])) {
                            tc[i].close();
                        }
                    }

                    // Refresh content of "Search filter" TopComponent
                    TopComponent searchFilterTc = WindowManager.getDefault().findTopComponent("SearchFilterTopComponent");
                    if (searchFilterTc != null) {
                        searchFilterTc.close();
                        searchFilterTc.open();
                    }

                    // Display Overview window and refresh its content
                    OverviewTopComponent overviewTc = (OverviewTopComponent) WindowManager.getDefault().findTopComponent("OverviewTopComponent");
                    if (overviewTc != null) {
                        overviewTc.open();
                        overviewTc.requestActive();

                        overviewTc.displayData();
                    }
                }
            };
            EventQueue.invokeLater(worker);

        } catch (DocumentException ex) {
            log.log(Level.SEVERE, "DocumentException catched", ex);
            NotifyDescriptor.Exception message = new NotifyDescriptor.Exception(ex);
            message.setTitle(Constants.APPLICATION_TITLE);
            DialogDisplayer.getDefault().notifyLater(message);

        } catch (ParsingException ex) {
            log.log(Level.SEVERE, "ParsingException catched", ex);
            NotifyDescriptor.Exception message = new NotifyDescriptor.Exception(ex);
            message.setTitle(Constants.APPLICATION_TITLE);
            DialogDisplayer.getDefault().notifyLater(message);

        } catch (NumberFormatException ex) {
            log.log(Level.SEVERE, "NumberFormatException catched", ex);
            NotifyDescriptor.Exception message = new NotifyDescriptor.Exception(ex);
            message.setTitle(Constants.APPLICATION_TITLE);
            DialogDisplayer.getDefault().notifyLater(message);

        } catch (DateFormatException ex) {
            log.log(Level.SEVERE, "DateFormatException catched", ex);
            NotifyDescriptor.Exception message = new NotifyDescriptor.Exception(ex);
            message.setTitle(Constants.APPLICATION_TITLE);
            DialogDisplayer.getDefault().notifyLater(message);
        }
    }

    /**
     * Run consistency checks on the database
     * @return true if the database is consistent, false otherwise
     */
    public boolean checkDatabase() {
        log.entering(this.getClass().getName(), "checkDatabase");

        List<Currency> currencies = Datamodel.getActiveCurrencies();
        for (Currency currency : currencies) {
            BigDecimal currencyBalance = currency.getBalance();
            BigDecimal currencyTotalBalance = Datamodel.getCurrencyTotalBalance(currency);

            // Check Currency.getBalance() with Datamodel.getCurrencyTotalBalance()
            if (currencyBalance.compareTo(currencyTotalBalance) != 0) {
                String errorDetails = NbBundle.getMessage(
                        ImporterEngine.class,
                        "ImporterEngine.ConsistencyCheckErrorCurrencyGetCurrencyTotalBalance",
                        new Object[] {currency.getName(), currencyBalance, currencyTotalBalance});
                log.info(errorDetails);
                NotifyDescriptor message = new NotifyDescriptor.Message(errorDetails, NotifyDescriptor.ERROR_MESSAGE);
                message.setTitle(Constants.APPLICATION_TITLE);
                DialogDisplayer.getDefault().notify(message);
                return false;
            }

            // Check Currency.getBalance() with getBalance()
            BigDecimal initialCurrencyAmount = currency.getInitialAmount();
            BigDecimal currencyWalletBalance = getBalance(Datamodel.getCurrencyTransactions(currency)).add(initialCurrencyAmount);
            if (currencyBalance.compareTo(currencyWalletBalance) != 0) {
                String errorDetails = NbBundle.getMessage(
                        ImporterEngine.class,
                        "ImporterEngine.ConsistencyCheckErrorCurrencyGetCurrencyTransactions",
                        new Object[] {currency.getName(), currencyBalance, currencyWalletBalance});
                log.info(errorDetails);
                NotifyDescriptor message = new NotifyDescriptor.Message(errorDetails, NotifyDescriptor.ERROR_MESSAGE);
                message.setTitle(Constants.APPLICATION_TITLE);
                DialogDisplayer.getDefault().notify(message);
                return false;
            }

            // Check active accounts of the currency
            for (Account account : Datamodel.getActiveAccounts(currency)) {
                BigDecimal accountBalance = account.getBalance();
                BigDecimal accountTotalBalance = Datamodel.getAccountTotalBalance(account).add(account.getInitialAmount());

                // Check Account.getBalance() with Datamodel.getAccountTotalBalance()
                if (accountBalance.compareTo(accountTotalBalance) != 0) {
                    String errorDetails = NbBundle.getMessage(
                            ImporterEngine.class,
                            "ImporterEngine.ConsistencyCheckErrorAccountGetAccountTotalBalance",
                            new Object[] {account.getName(), accountBalance, accountTotalBalance});
                    log.info(errorDetails);
                    NotifyDescriptor message = new NotifyDescriptor.Message(errorDetails, NotifyDescriptor.ERROR_MESSAGE);
                    message.setTitle(Constants.APPLICATION_TITLE);
                    DialogDisplayer.getDefault().notify(message);
                    return false;
                }

                // Check Account.getBalance() with getBalance()
                BigDecimal accountWalletBalance = getBalance(Datamodel.getAccountTransactions(account)).add(account.getInitialAmount());
                if (accountBalance.compareTo(accountWalletBalance) != 0) {
                    String errorDetails = NbBundle.getMessage(
                            ImporterEngine.class,
                            "ImporterEngine.ConsistencyCheckErrorAccountGetAccountTransactions",
                            new Object[] {account.getName(), accountBalance, accountWalletBalance});
                    log.info(errorDetails);
                    NotifyDescriptor message = new NotifyDescriptor.Message(errorDetails, NotifyDescriptor.ERROR_MESSAGE);
                    message.setTitle(Constants.APPLICATION_TITLE);
                    DialogDisplayer.getDefault().notify(message);
                    return false;
                }
            }
        }

        log.exiting(this.getClass().getName(), "checkDatabase");
        return true;
    }

    /**
     * Gets the balance of a list of transactions
     * @param transactions List of transactions
     * @return Total balance (0 if the list of transactions is empty)
     */
    private static BigDecimal getBalance(List<Transaction> transactions) {
        if (transactions == null) {
            throw new IllegalArgumentException("The parameter 'transactions' is null");
        }

        BigDecimal totalBalance = new BigDecimal(0); // Sum of the balances of each transaction

        // Compute the total balance of the list of transactions
        for (Transaction transaction : transactions) {
            assert (transaction != null);

            totalBalance = totalBalance.add(transaction.getAmount());
        }

        // Return the total balance
        return totalBalance;
    }
}
