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
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * <B>ImporterEngine</B>
 * <UL>
 * <LI>Permits to import Grisbi files</LI>
 * <LI>The correct importer class is used depending on the Grisbi file version written in the Grisbi file</LI>
 * </UL>
 * @author Francois Duchemin
 */
public class ImporterEngine implements Runnable {

    /** Grisbi file to import */
    private File grisbiFile;
    /** Is the import task cancelled? */
    private boolean importCancelled;
    /** Logger */
    private Logger log = Logger.getLogger(ImporterEngine.class.getName());

    /**
     * Creates a new instance of ImporterEngine
     * @param grisbiFile Grisbi file to import
     * @throws FileNotFoundException If the Grisbi file to import does not exist
     */
    public ImporterEngine(File grisbiFile) throws FileNotFoundException {
        setGrisbiFile(grisbiFile);
        setImportCancelled(false);
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
        long startParsingTime = System.currentTimeMillis();
        SAXReader reader = new SAXReader();

        // Try to parse the Grisbi file and import it into a DOM document (which support XPath)
        Document grisbiFileDocument = reader.read(grisbiFile); // Throws a DocumentException if there is an error parsing the Grisbi file
        long endParsingTime = System.currentTimeMillis();

        log.info("The Grisbi file '" + grisbiFile.getAbsolutePath() + "' has been successfully imported into a document in " + (endParsingTime - startParsingTime) + " ms");

        return grisbiFileDocument;
    }

    /**
     * Gets the version of the Grisbi file
     * @param grisbiFileDocument Document of the Grisbi file for which the version is wanted
     * @return Version of the Grisbi file (<CODE>FileVersion.UNSUPPORTED_VERSION</CODE> if the file is not supported)
     */
    private static FileVersion getFileVersion(Document grisbiFileDocument) {
        assert (grisbiFileDocument != null);

        // Get the version of the grisbi file
        Node fileVersionNode = grisbiFileDocument.selectSingleNode("/Grisbi/Generalites/Version_fichier");
        if (fileVersionNode != null) {
            String fileVersion = fileVersionNode.getText();
            if (fileVersion.compareToIgnoreCase("0.5.0") == 0) {
                return FileVersion.VERSION_0_5_0;
            }
        }

        return FileVersion.UNSUPPORTED_VERSION;
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

        return importDuration;
    }

    @Override
    public void run() {
        try {
            // Import the Grisbi file into the embedded database using the correct importer object
            long importDuration = importFile();
            boolean success = false; // Is the Grisbi file is correcty imported into the database?
            
            if (isImportCancelled()) { // Import cancelled manually by the user
                Datamodel.emptyDatabase();
                StatusDisplayer.getDefault().setStatusText("Import cancelled");
            } else {
                if (checkDatabase()) {
                    StatusDisplayer.getDefault().setStatusText("Grisbi file imported");
                    success = true;
                } else { // Errors during the consistency checks
                    StatusDisplayer.getDefault().setStatusText("Consistency check error");
                }
            }

            // Log the import in the database
            FileImport fileImport = new FileImport(new DateTime(), grisbiFile.getAbsolutePath(), grisbiFile.getName(), new DateTime(grisbiFile.lastModified()), importDuration, success);
            Datamodel.saveFileImport(fileImport);

            // Update content of the wallet
            Wallet.getInstance().updateContent();

            // Display overview window
            Runnable worker = new Runnable() {

                @Override
                public void run() {
                    // Close all opened editor TopComponents
                    TopComponent[] tc = TopComponent.getRegistry().getOpened().toArray(new TopComponent[0]);
                    for (int i = tc.length - 1; i >= 0; i--) {
                        if (WindowManager.getDefault().isEditorTopComponent(tc[i])) {
                            tc[i].close();
                        }
                    }

                    // Refresh "Search filter" topcomponent
                    TopComponent searchFilterTc = WindowManager.getDefault().findTopComponent("SearchFilterTopComponent");
                    if (searchFilterTc != null) {
                        searchFilterTc.close();
                        searchFilterTc.open();
                    }

                    // Display overview
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
            NotifyDescriptor d = new NotifyDescriptor.Exception(ex);
        } catch (ParsingException ex) {
            log.log(Level.SEVERE, "ParsingException catched", ex);
            NotifyDescriptor d = new NotifyDescriptor.Exception(ex);
        } catch (NumberFormatException ex) {
            log.log(Level.SEVERE, "NumberFormatException catched", ex);
            NotifyDescriptor d = new NotifyDescriptor.Exception(ex);
        } catch (DateFormatException ex) {
            log.log(Level.SEVERE, "DateFormatException catched", ex);
            NotifyDescriptor d = new NotifyDescriptor.Exception(ex);
        }
    }

    /**
     * Run consistency checks on the database
     * @return true if the database is consistent, false otherwise
     */
    public boolean checkDatabase() {
        BigDecimal currencyBalance;
        BigDecimal currencyTotalBalance;
        BigDecimal currencyWalletBalance;
        BigDecimal accountBalance;
        BigDecimal accountTotalBalance;
        BigDecimal accountWalletBalance;

        // Check active currencies
        List<Currency> currencies = Datamodel.getActiveCurrencies();
        for (Currency currency : currencies) {
            currencyBalance = currency.getBalance();
            currencyTotalBalance = Datamodel.getCurrencyTotalBalance(currency);

            // Check Currency.getBalance() with Datamodel.getCurrencyTotalBalance()
            if (currencyBalance.compareTo(currencyTotalBalance) != 0) {
                NotifyDescriptor d = new NotifyDescriptor.Message(
                        "Consistency error for the currency '" + currency + "': currency.getBalance() (" + currencyBalance + ") is different from Datamodel.getCurrencyTotalBalance(currency) (" + currencyTotalBalance + ")",
                        NotifyDescriptor.ERROR_MESSAGE);
                DialogDisplayer.getDefault().notify(d);
                return false;
            }

            // Check Currency.getBalance() with getBalance()
            BigDecimal initialCurrencyAmount = new BigDecimal(0); // Find out the initial amount of the currency
            for (Account account : Datamodel.getActiveAccounts(currency)) {
                initialCurrencyAmount = initialCurrencyAmount.add(account.getInitialAmount());
            }
            currencyWalletBalance = getBalance(Datamodel.getCurrencyTransactions(currency)).add(initialCurrencyAmount);
            if (currencyBalance.compareTo(currencyWalletBalance) != 0) {
                NotifyDescriptor d = new NotifyDescriptor.Message(
                        "Consistency error for the currency '" + currency + "': currency.getBalance() (" + currencyBalance + ") is different from getBalance(Datamodel.getCurrencyTransactions(currency)).add(initialCurrencyAmount) (" + currencyWalletBalance + ")",
                        NotifyDescriptor.ERROR_MESSAGE);
                DialogDisplayer.getDefault().notify(d);
                return false;
            }

            // Check active accounts
            for (Account account : Datamodel.getActiveAccounts(currency)) {
                accountBalance = account.getBalance();
                accountTotalBalance = Datamodel.getAccountTotalBalance(account).add(account.getInitialAmount());
                accountWalletBalance = getBalance(Datamodel.getAccountTransactions(account)).add(account.getInitialAmount());

                // Check Account.getBalance() with Datamodel.getAccountTotalBalance()
                if (accountBalance.compareTo(accountTotalBalance) != 0) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(
                            "Consistency error the the account '" + account + "': account.getBalance() (" + accountBalance + ") is different from Datamodel.getAccountTotalBalance(account).add(account.getInitialAmount()) (" + accountTotalBalance + ")",
                            NotifyDescriptor.ERROR_MESSAGE);
                    DialogDisplayer.getDefault().notify(d);
                    return false;
                }

                // Check Account.getBalance() with getBalance()
                if (accountBalance.compareTo(accountWalletBalance) != 0) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(
                            "Consistency error for the account '" + account + "': account.getBalance() (" + accountBalance + ") is different from getBalance(Datamodel.getAccountTransactions(account)).add(account.getInitialAmount()) (" + accountWalletBalance + ")",
                            NotifyDescriptor.ERROR_MESSAGE);
                    DialogDisplayer.getDefault().notify(d);
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Gets the balance of a list of transactions
     * @param transactions List of transactions
     * @return Total balance (0 if the list of transactions is empty)
     */
    public static BigDecimal getBalance(List<Transaction> transactions) {
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
