/*
 * GrisbiFile050.java
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

import gg.db.datamodel.DateFormatException;
import gg.db.entities.GrisbiCategory;
import gg.db.datamodel.Datamodel;
import gg.db.entities.Account;
import gg.db.entities.Category;
import gg.db.entities.Currency;
import gg.db.entities.Payee;
import gg.db.entities.Transaction;
import gg.db.datamodel.Period;
import gg.utilities.Utilities;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.dom4j.Document;
import org.dom4j.Node;
import org.joda.time.LocalDate;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Cancellable;
import org.openide.util.NbBundle;

/**
 * <B>GrisbiFile050</B>
 * <UL>
 * <LI>Permits to import Grisbi 0.5.x files</LI>
 * <LI>The public method <CODE>importFile()</CODE> imports the XML document into the embedded database</LI>
 * </UL>
 * @author Francois Duchemin
 */
public class GrisbiFile050 implements Importer {

    /** Document (supports XPath), which contains the XML Grisbi document to import */
    private Document grisbiFileDocument;
    /** Path to the Grisbi file to import*/
    private String pathToGrisbiFile;
    /** Is the import task cancelled? */
    private boolean importCancelled;
    /** Current work unit (for the progress bar) */
    private int workUnit;
    /** Logger */
    private Logger log = Logger.getLogger(GrisbiFile050.class.getName());

    /**
     * Creates a new instance of GrisbiFile050
     * @param grisbiFileDocument Document which contains the Grisbi file to import into the embedded database
     * @param pathToGrisbiFile Path to the Grisbi file to import
     */
    public GrisbiFile050(Document grisbiFileDocument, String pathToGrisbiFile) {
        if (grisbiFileDocument == null) {
            throw new IllegalArgumentException("The parameter 'grisbiFileDocument' is null");
        }
        if (pathToGrisbiFile == null) {
            throw new IllegalArgumentException("The parameter 'pathToGrisbiFile' is null");
        }
        this.grisbiFileDocument = grisbiFileDocument;
        this.pathToGrisbiFile = pathToGrisbiFile;
        this.importCancelled = false;
    }

    /**
     * Gets expected number of payees
     * @return Expected number of payees
     * @throws ParsingException Expected number of payees not found
     * @throws NumberFormatException The expected number of payees is not numeric
     */
    private int getExpectedNumberOfPayees() throws ParsingException, NumberFormatException {
        log.entering(this.getClass().getName(), "getExpectedNumberOfPayees");
        Node expectedNumberOfPayeesNode = grisbiFileDocument.selectSingleNode("/Grisbi/Tiers/Generalites/Nb_tiers");
        if (expectedNumberOfPayeesNode == null) {
            throw new ParsingException("The expected number of payees has not been found in the Grisbi file '" + pathToGrisbiFile + "'");
        }
        String expectedNumberOfPayeesValue = expectedNumberOfPayeesNode.getStringValue();
        assert (expectedNumberOfPayeesValue != null);
        int expectedNumberOfPayees = Integer.parseInt(expectedNumberOfPayeesValue);

        log.exiting(this.getClass().getName(), "getExpectedNumberOfPayees", expectedNumberOfPayees);
        return expectedNumberOfPayees;
    }

    /**
     * Gets expected number of categories
     * @return Expected number of categories
     * @throws ParsingException Expected number of categories not found
     * @throws NumberFormatException The expected number of categories is not numeric
     */
    private int getExpectedNumberOfCategories() throws ParsingException, NumberFormatException {
        log.entering(this.getClass().getName(), "getExpectedNumberOfCategories");
        Node expectedNumberOfCategoriesNode = grisbiFileDocument.selectSingleNode("/Grisbi/Categories/Generalites/Nb_categories");
        if (expectedNumberOfCategoriesNode == null) {
            throw new ParsingException("The expected number of categories has not been found in the Grisbi file '" + pathToGrisbiFile + "'");
        }
        String expectedNumberOfCategoriesValue = expectedNumberOfCategoriesNode.getStringValue();
        assert (expectedNumberOfCategoriesValue != null);
        int expectedNumberOfCategories = Integer.parseInt(expectedNumberOfCategoriesValue);

        log.exiting(this.getClass().getName(), "getExpectedNumberOfCategories", expectedNumberOfCategories);
        return expectedNumberOfCategories;
    }

    /**
     * Gets expected number of currencies
     * @return Expected number of currencies
     * @throws ParsingException Expected number of currencies not found
     * @throws NumberFormatException The expected number of currencies is not numeric
     */
    private int getExpectedNumberOfCurrencies() throws ParsingException, NumberFormatException {
        log.entering(this.getClass().getName(), "getExpectedNumberOfCurrencies");
        Node expectedNumberOfCurrenciesNode = grisbiFileDocument.selectSingleNode("/Grisbi/Devises/Generalites/Nb_devises");
        if (expectedNumberOfCurrenciesNode == null) {
            throw new ParsingException("The expected number of currencies has not been found in the Grisbi file '" + pathToGrisbiFile + "'");
        }
        String expectedNumberOfCurrenciesValue = expectedNumberOfCurrenciesNode.getStringValue();
        assert (expectedNumberOfCurrenciesValue != null);
        int expectedNumberOfCurrencies = Integer.parseInt(expectedNumberOfCurrenciesValue);

        log.exiting(this.getClass().getName(), "getExpectedNumberOfCurrencies", expectedNumberOfCurrencies);
        return expectedNumberOfCurrencies;
    }

    /**
     * Gets expected number of transactions (for all accounts)
     * @return Expected number of transactions
     * @throws ParsingException Expected number of transactions not found
     * @throws NumberFormatException The expected number of transactions is not numeric
     */
    private int getExpectedNumberOfTransactions() throws ParsingException, NumberFormatException {
        log.entering(this.getClass().getName(), "getExpectedNumberOfTransactions");
        int expectedNumberOfTransactions = 0;

        List listOfAccounts = grisbiFileDocument.selectNodes("/Grisbi/Comptes/Compte");
        for (Object accountObject : listOfAccounts) {
            Node accountNode = (Node) accountObject;
            assert (accountNode != null);

            // Get the name of the current account
            Node accountNameNode = accountNode.selectSingleNode("Details/Nom");
            assert (accountNameNode != null);
            String accountName = accountNameNode.getStringValue();
            assert (accountName != null);

            // Get the expected number of transactions of the account
            Node expectedNumberOfTransactionsNode = accountNode.selectSingleNode("Details/Nb_operations");
            if (expectedNumberOfTransactionsNode == null) {
                throw new ParsingException("The expected number of transactions has not been found for the account '" + accountName + "' in the Grisbi file '" + pathToGrisbiFile + "'");
            }
            String expectedNumberOfTransactionsValue = expectedNumberOfTransactionsNode.getStringValue();
            assert (expectedNumberOfTransactionsValue != null);
            int expectedNumberOfTransactionsForCurrentAccount = Integer.parseInt(expectedNumberOfTransactionsValue);

            expectedNumberOfTransactions += expectedNumberOfTransactionsForCurrentAccount;
        }

        log.exiting(this.getClass().getName(), "getExpectedNumberOfTransactions", expectedNumberOfTransactions);
        return expectedNumberOfTransactions;
    }

    /**
     * Imports the payees into the embedded database
     * @param p Progress handle for the progress bar
     * @param expectedNumberOfPayees Expected number of payees: the number of imported payees should be equal to this number
     * @throws ParsingException
     * <UL>
     * <LI>If there is a problem finding the needed nodes</LI>
     * <LI>If the number of imported payees is not equal to the number of payees defined in the Grisbi file</LI>
     * </UL>
     * @throws NumberFormatException If a string is read when a number is expected
     */
    private void importPayees(ProgressHandle p, int expectedNumberOfPayees) throws ParsingException, NumberFormatException {
        log.entering(this.getClass().getName(), "importPayees");
        long startImportingPayeesTime = System.currentTimeMillis();

        // Save default payee in the database
        Datamodel.savePayee(Payee.NO_PAYEE); // This constant is used to search the transactions for which no payee is defined

        // Import the payees from the Grisbi file into the embedded database
        int numberOfImportedPayees = 0;
        List listOfPayees = grisbiFileDocument.selectNodes("/Grisbi/Tiers/Detail_des_tiers/Tiers");
        Iterator payeesIterator = listOfPayees.iterator();
        while (payeesIterator.hasNext() && !isImportCancelled()) { // Go through the list of payees found in the Grisbi XML file
            // Get the current payee node
            Node payeeNode = (Node) payeesIterator.next();

            // Get the ID and the name of the current payee
            String payeeIdValue = payeeNode.valueOf("@No");
            assert (payeeIdValue != null);
            long payeeId = Long.parseLong(payeeIdValue);
            assert (payeeId > 0);
            String payeeName = payeeNode.valueOf("@Nom");
            assert (payeeName != null);

            // Create a new payee and save it in the embedded database
            Payee payee = new Payee(payeeId, payeeName, false);
            Datamodel.savePayee(payee);

            numberOfImportedPayees++;
            p.progress(workUnit++);
        }

        // Make sure that all payees have been imported
        if (!isImportCancelled() && numberOfImportedPayees != expectedNumberOfPayees) {
            throw new ParsingException("The number of imported payees (" + numberOfImportedPayees + ") is not equal to the expected number of payees (" + expectedNumberOfPayees + ") written in the Grisbi file '" + pathToGrisbiFile + "'");
        }

        long endImportingPayeesTime = System.currentTimeMillis();
        log.info(numberOfImportedPayees + " payees have been successfully imported in " + (endImportingPayeesTime - startImportingPayeesTime) + " ms");
        log.exiting(this.getClass().getName(), "importPayees");
    }

    /**
     * Imports the categories into the embedded database
     * @param p Progress handle for the progress bar
     * @param expectedNumberOfCategories Expected number of categories: the number of imported categories should be equal to this number
     * @throws ParsingException
     * <UL>
     * <LI>If there is a problem finding the needed nodes</LI>
     * <LI>If the number of imported categories is not equal to the number of categories defined in the Grisbi file</LI>
     * </UL>
     * @throws NumberFormatException If a string is read when a number is expected
     */
    private void importCategories(ProgressHandle p, int expectedNumberOfCategories) throws ParsingException, NumberFormatException {
        log.entering(this.getClass().getName(), "importCategories");
        long startImportingCategoriesTime = System.currentTimeMillis();

        // Save system categories in the embedded database
        Datamodel.saveCategory(Category.TRANSFER);
        Datamodel.saveCategory(Category.BREAKDOWN_OF_TRANSACTIONS);
        Datamodel.saveCategory(Category.NO_CATEGORY);

        // Import the categories from the Grisbi file into the embedded database
        int numberOfImportedCategories = 0;
        List listOfCategories = grisbiFileDocument.selectNodes("/Grisbi/Categories/Detail_des_categories/Categorie");
        Iterator categoriesIterator = listOfCategories.iterator();
        while (categoriesIterator.hasNext() && !isImportCancelled()) { // Go through the list of categories written in the Grisbi file
            // Get the current category node
            Node categoryNode = (Node) categoriesIterator.next();

            // Get the ID and the name of the current category
            String categoryIdValue = categoryNode.valueOf("@No");
            assert (categoryIdValue != null);
            long categoryId = Long.parseLong(categoryIdValue); // Get the ID of the category
            assert (categoryId > 0);
            String categoryName = categoryNode.valueOf("@Nom"); // Get the name of the category
            assert (categoryName != null);

            // Create a new category and save it in the embedded database
            Category category = new Category(categoryId, 0L, categoryName, null, false); // There is no sub-category - the field "grisbi_sub_category_id" is set to 0
            Datamodel.saveCategory(category);

            // Import the sub-categories into the database
            List listOfSubCategories = categoryNode.selectNodes("Sous-categorie");
            Iterator subCategoriesIterator = listOfSubCategories.iterator();
            while (subCategoriesIterator.hasNext() && !isImportCancelled()) { // Go through the list of sub-categories of the current category
                // Get the current sub-category node
                Node subCategoryNode = (Node) subCategoriesIterator.next();

                // Get the ID and the name of the current sub-category
                String subCategoryIdValue = subCategoryNode.valueOf("@No");
                assert (subCategoryIdValue != null);
                long subCategoryId = Long.parseLong(subCategoryIdValue); // Get the ID of the sub-category
                assert (subCategoryId > 0);
                String subCategoryName = subCategoryNode.valueOf("@Nom"); // Get the name of the sub-category
                assert (subCategoryName != null);

                // Create a new sub-category and save it in the embedded database
                Category subCategory = new Category(categoryId, subCategoryId, subCategoryName, category, false);
                Datamodel.saveCategory(subCategory);
            }

            // For each category, save an empty sub-category
            Category noSubCategory = new Category(categoryId, Category.NO_SUB_CATEGORY_ID, "No sub-category", category, false);
            Datamodel.saveCategory(noSubCategory);

            numberOfImportedCategories++;
            p.progress(workUnit++);
        }

        // Make sure that all categories have been imported
        if (!isImportCancelled() && numberOfImportedCategories != expectedNumberOfCategories) {
            throw new ParsingException("The number of imported categories (" + numberOfImportedCategories + ") is not equal to the expected number of categories (" + expectedNumberOfCategories + ") written in the Grisbi file '" + pathToGrisbiFile + "'");
        }

        long endImportingCategoriesTime = System.currentTimeMillis();
        log.info(numberOfImportedCategories + " categories have been successfully imported in " + (endImportingCategoriesTime - startImportingCategoriesTime) + " ms");
        log.exiting(this.getClass().getName(), "importCategories");
    }

    /**
     * Imports the currencies into the database
     * @param p Progress handle for the progress bar
     * @param expectedNumberOfCurrencies Expected number of currencies: the number of imported currencies should be equal to this number
     * @throws ParsingException
     * <UL>
     * <LI>If there is a problem finding the needed nodes</LI>
     * <LI>If the number of imported currencies is not equal to the number of currencies defined in the Grisbi file</LI>
     * </UL>
     * @throws NumberFormatException If a string is read when a number is expected
     */
    private void importCurrencies(ProgressHandle p, int expectedNumberOfCurrencies) throws ParsingException, NumberFormatException {
        log.entering(this.getClass().getName(), "importCurrencies");
        long startImportingCurrenciesTime = System.currentTimeMillis();

        // Import the currencies into the embedded database
        int numberOfImportedCurrencies = 0;
        List listOfCurrencies = grisbiFileDocument.selectNodes("/Grisbi/Devises/Detail_des_devises/Devise");
        Iterator currenciesIterator = listOfCurrencies.iterator();
        while (currenciesIterator.hasNext() && !isImportCancelled()) { // Go through the list of currencies
            // Get the current currency node
            Node currencyNode = (Node) currenciesIterator.next();

            // Get the ID, the name and the ISO code of the current currency
            String currencyIdValue = currencyNode.valueOf("@No");
            assert (currencyIdValue != null);
            long currencyId = Long.parseLong(currencyIdValue); // Get the ID of the currency
            assert (currencyId > 0);
            String currencyName = currencyNode.valueOf("@Nom"); // Get the name of the currency
            assert (currencyName != null);
            String currencyCode = currencyNode.valueOf("@Code"); // Get the code of the currency
            assert (currencyCode != null);
            String currencyIsoCode = currencyNode.valueOf("@IsoCode"); // Get the code ISO of the currency
            assert (currencyIsoCode != null);

            // Create a new currency and save the currency in the database
            // By default the currencies are not active
            // When the accounts are imported, the currencies are activated
            Currency currency = new Currency(currencyId, currencyName, currencyCode, currencyIsoCode, new BigDecimal(0), new BigDecimal(0), false);
            Datamodel.saveCurrency(currency);

            numberOfImportedCurrencies++;
            p.progress(workUnit++);
        }

        // Make sure that all currencies have been imported
        if (!isImportCancelled() && numberOfImportedCurrencies != expectedNumberOfCurrencies) {
            throw new ParsingException("The number of imported currencies (" + numberOfImportedCurrencies + ") is not equal to the expected number of currencies (" + expectedNumberOfCurrencies + ") in the Grisbi file '" + pathToGrisbiFile + "'");
        }

        long endImportingCurrenciesTime = System.currentTimeMillis();
        log.info(numberOfImportedCurrencies + " currencies have been successfully imported in " + (endImportingCurrenciesTime - startImportingCurrenciesTime) + " ms");
        log.exiting(this.getClass().getName(), "importCurrencies");
    }

    /**
     * Imports the accounts into the database<BR/>
     * The method <CODE>importCurrencies()</CODE> has to be called before
     * @throws ParsingException If there is a problem finding the needed nodes
     * @throws NumberFormatException If a string is read when a number is expected
     * @throws DateFormatException If the format of the date of the first transaction is invalid
     */
    private void importAccounts() throws ParsingException, NumberFormatException, DateFormatException {
        log.entering(this.getClass().getName(), "importAccounts");
        long startImportingAccountsTime = System.currentTimeMillis();

        // Get all the currencies already saved in the database (the method importCurrencies() has to be called before)
        Map<Long, Currency> currencies = Datamodel.getCurrenciesWithId();

        // Import the accounts in the embedded database
        long numberOfImportedAccounts = 0;
        List listOfAccounts = grisbiFileDocument.selectNodes("/Grisbi/Comptes/Compte");
        Iterator accountsIterator = listOfAccounts.iterator();
        while (accountsIterator.hasNext() && !isImportCancelled()) {

            // Get the account node
            Node accountNode = (Node) accountsIterator.next();

            // Get the "Active" property of the account: is the account closed?
            Node accountActiveNode = accountNode.selectSingleNode("Details/Compte_cloture");
            assert (accountActiveNode != null);
            String accountActiveValue = accountActiveNode.getStringValue();
            assert (accountActiveValue != null);
            boolean accountActive = (accountActiveValue.compareTo("1") != 0);

            // Get the ID of the account
            Node accountIdNode = accountNode.selectSingleNode("Details/No_de_compte");
            assert (accountIdNode != null);
            String accountIdValue = accountIdNode.getStringValue();
            assert (accountIdValue != null);
            long accountId = Long.parseLong(accountIdValue);
            assert (accountId >= 0);

            // Get the name of the account
            Node accountNameNode = accountNode.selectSingleNode("Details/Nom");
            assert (accountNameNode != null);
            String accountName = accountNameNode.getStringValue();
            assert (accountName != null);

            // Get the currency of the account
            Node currencyIdNode = accountNode.selectSingleNode("Details/Devise");
            assert (currencyIdNode != null);
            String currencyIdValue = currencyIdNode.getStringValue();
            assert (currencyIdValue != null);
            long currencyId = Long.parseLong(currencyIdValue);
            assert (currencyId > 0);

            // Get the corresponding Currency object
            Currency accountCurrency = currencies.get(currencyId); // An account has always a currency in Grisbi
            assert (accountCurrency != null);

            // Activate the currency if needed
            if (accountActive && !accountCurrency.getActive()) {
                accountCurrency.setActive(true);
            }

            // Get the initial amount of the account
            Node accountInitialAmountNode = accountNode.selectSingleNode("Details/Solde_initial");
            assert (accountInitialAmountNode != null);
            String accountInitialAmountValue = accountInitialAmountNode.getStringValue();
            assert (accountInitialAmountValue != null);
            BigDecimal accountInitialAmount = new BigDecimal(accountInitialAmountValue.replace(',', '.'));
            accountInitialAmount = accountInitialAmount.setScale(2, RoundingMode.HALF_EVEN);

            // Get the current balance of the account
            Node accountBalanceNode = accountNode.selectSingleNode("Details/Solde_courant");
            assert (accountBalanceNode != null);
            String accountBalanceValue = accountBalanceNode.getStringValue();
            assert (accountBalanceValue != null);
            BigDecimal accountBalance = new BigDecimal(accountBalanceValue.replace(',', '.'));
            accountBalance = accountBalance.setScale(2, RoundingMode.HALF_EVEN);

            // Create a new account and save the account in the embedded database
            Account account = new Account(accountId, accountName, accountCurrency, accountInitialAmount, accountBalance, accountActive);
            Datamodel.saveAccount(account);

            // Update the currency's balance and the currency's initial amount
            if (accountActive) {
                accountCurrency.setBalance(accountCurrency.getBalance().add(accountBalance));
                accountCurrency.setInitialAmount(accountCurrency.getInitialAmount().add(accountInitialAmount));
            }

            numberOfImportedAccounts++;
        }

        // Update the currencies (active flag, balance, initial amount)
        for (Currency currency : currencies.values()) {
            Datamodel.saveCurrency(currency);
        }

        long endImportingAccountsTime = System.currentTimeMillis();
        log.info(numberOfImportedAccounts + " accounts have been successfully imported in " + (endImportingAccountsTime - startImportingAccountsTime) + " ms");
        log.exiting(this.getClass().getName(), "importAccounts");
    }

    /**
     * Imports the transactions into the embedded database<BR/>
     * The methods <CODE>importCurrencies()</CODE>, <CODE>importPayees()</CODE>,
     * <CODE>importAccounts()</CODE>, and <CODE>importCategories()</CODE> have to be called before
     * @param p Progress handle for the progress bar
     * @throws ParsingException If there is a problem finding the needed nodes
     * @throws NumberFormatException If a string is read when a number is expected
     * @throws DateFormatException If the date format of a transaction is invalid
     */
    private void importTransactions(ProgressHandle p) throws ParsingException, NumberFormatException, DateFormatException {
        log.entering(this.getClass().getName(), "importTransactions");
        long startImportingTotalTransactionsTime = System.currentTimeMillis();

        Map<Long, Account> accounts = Datamodel.getAccountsWithId();
        Map<Long, Payee> payees = Datamodel.getPayeesWithId();
        Map<GrisbiCategory, Category> categories = Datamodel.getCategoriesWithGrisbiCategory();
        Map<Long, Currency> currencies = Datamodel.getCurrenciesWithId();

        // Import the transactions from each account into the embedded database
        long totalNumberOfImportedTransactions = 0;
        List listOfAccounts = grisbiFileDocument.selectNodes("/Grisbi/Comptes/Compte");
        Iterator accountsIterator = listOfAccounts.iterator();
        while (accountsIterator.hasNext() && !isImportCancelled()) { // Go through the accounts
            long startImportingTransactionsTime = System.currentTimeMillis();

            // Get the account's node
            Node accountNode = (Node) accountsIterator.next();
            assert (accountNode != null);

            // Get the ID of the account
            Node accountIdNode = accountNode.selectSingleNode("Details/No_de_compte");
            assert (accountIdNode != null);
            String accountIdValue = accountIdNode.getStringValue();
            assert (accountIdValue != null);
            long accountId = Long.parseLong(accountIdValue);
            assert (accountId >= 0);

            // Get the name of the current account
            Node accountNameNode = accountNode.selectSingleNode("Details/Nom");
            assert (accountNameNode != null);
            String accountName = accountNameNode.getStringValue();
            assert (accountName != null);

            // Display on the progress bar the account from which transactions are imported
            p.progress("Importing transactions from " + accountName);

            // Get the corresponding Account object
            Account account = accounts.get(accountId);
            assert (account != null);

            // Get the expected number of transactions of the account
            Node expectedNumberOfTransactionsNode = accountNode.selectSingleNode("Details/Nb_operations");
            if (expectedNumberOfTransactionsNode == null) {
                throw new ParsingException("The expected number of transactions has not been found for the account '" + accountName + "' in the Grisbi file '" + pathToGrisbiFile + "'");
            }
            String expectedNumberOfTransactionsValue = expectedNumberOfTransactionsNode.getStringValue();
            assert (expectedNumberOfTransactionsValue != null);
            int expectedNumberOfTransactions = Integer.parseInt(expectedNumberOfTransactionsValue);

            // Import the transactions of the account into the embedded database
            int numberOfImportedTransactions = 0;
            Map<Long, Transaction> transactions = new HashMap<Long, Transaction>(); // Map containing all the saved transactions - the key of the map is the transaction's ID
            List listOfTransactions = accountNode.selectNodes("Detail_des_operations/Operation");
            Iterator transactionsIterator = listOfTransactions.iterator();
            while (transactionsIterator.hasNext() && !isImportCancelled()) { // Go through the list of transactions of the account

                // Get the transaction node
                Node transactionNode = (Node) transactionsIterator.next();

                // Get the ID of the transaction
                assert (transactionNode != null);
                String transactionIdValue = transactionNode.valueOf("@No");
                assert (transactionIdValue != null);
                long transactionId = Long.parseLong(transactionIdValue);
                assert (transactionId > 0);

                // Get the date of the transaction
                String transactionDateValue = transactionNode.valueOf("@D");
                assert (transactionDateValue != null && transactionDateValue.compareTo("") != 0);
                // Get the date from the string
                LocalDate transactionDate = Period.getDate(transactionDateValue); // Throws a DateFormatException if the format of 'transactionDateValue' is not valid
                assert (transactionDate != null);

                // Get the exchange rate
                String exchangeRateValue = transactionNode.valueOf("@Tc");
                assert (exchangeRateValue != null);
                BigDecimal exchangeRate = new BigDecimal(exchangeRateValue); // exchangeRate = 0 if there is no echange rate

                // Get the fees
                String feesValue = transactionNode.valueOf("@Fc");
                assert (feesValue != null);
                BigDecimal fees = new BigDecimal(feesValue);

                // Get the amount of the transaction
                String transactionAmountValue = transactionNode.valueOf("@M");
                assert (transactionAmountValue != null);
                BigDecimal transactionAmount = new BigDecimal(transactionAmountValue.replace(',', '.'));

                // Update the amount of the transaction if the currency of the transaction is different from the currency of the account
                // Method found in the Grisbi source file: devises.c/calcule_montant_devise_renvoi
                if (exchangeRate.compareTo(BigDecimal.ZERO) != 0) {

                    // Get the currency of the transaction
                    String transactionCurrencyIdValue = transactionNode.valueOf("@De");
                    assert (transactionCurrencyIdValue != null);
                    long transactionCurrencyId = Long.parseLong(transactionCurrencyIdValue); // Get the ID of the currency
                    Currency transactionCurrency = currencies.get(transactionCurrencyId);
                    assert (transactionCurrency != null);

                    if (transactionCurrency.getId().compareTo(account.getCurrency().getId()) != 0) {
                        String transactionRdcValue = transactionNode.valueOf("@Rdc");
                        assert (transactionRdcValue != null);
                        long transactionRdc = Long.parseLong(transactionRdcValue);

                        if (transactionRdc == 1) {
                            transactionAmount = (transactionAmount.divide(exchangeRate, RoundingMode.HALF_EVEN)).subtract(fees);
                        } else {
                            transactionAmount = (transactionAmount.multiply(exchangeRate)).subtract(fees);
                        }
                    }
                }
                transactionAmount = transactionAmount.setScale(2, RoundingMode.HALF_EVEN);

                // If the current transaction is a transfer:
                String twinTransaction = transactionNode.valueOf("@Ro"); // Twin transaction
                assert (twinTransaction != null);
                String accountOfTransfer = transactionNode.valueOf("@Rc"); // Account where the amount has been transfered (account of the twin transaction)
                assert (accountOfTransfer != null);

                // Is the current transaction a breakdown of transactions?
                String breakdownOfTransaction = transactionNode.valueOf("@Ov"); // breakdownOfTransaction = 1 if the current trasnsaction is a breakdown of transactions
                assert (breakdownOfTransaction != null);

                // Get the category and the sub-category of the transaction
                String transactionGrisbiCategoryIdValue = transactionNode.valueOf("@C"); // Get the category ID ('0' means that the category is not defined)
                assert (transactionGrisbiCategoryIdValue != null);
                long transactionGrisbiCategoryId = Long.parseLong(transactionGrisbiCategoryIdValue);
                assert (transactionGrisbiCategoryId >= 0);

                String transactionGrisbiSubCategoryIdValue = transactionNode.valueOf("@Sc"); // Get the sub-category ID ('0' means that the sub-category is not defined)
                assert (transactionGrisbiSubCategoryIdValue != null);
                long transactionGrisbiSubCategoryId = Long.parseLong(transactionGrisbiSubCategoryIdValue);
                assert (transactionGrisbiSubCategoryId >= 0);

                // Check if the category is "Transfer" or "Breakdown of transactions"
                Category transactionCategory = null;
                GrisbiCategory transactionGrisbiCategory = new GrisbiCategory(0, 0);
                if (transactionGrisbiCategoryId == 0 && (twinTransaction.compareTo("0") != 0 || accountOfTransfer.compareTo("0") != 0)) {
                    // The current transaction is a transfer
                    transactionCategory = Category.TRANSFER;
                } else if (transactionGrisbiCategoryId == 0 && breakdownOfTransaction.compareTo("1") == 0) {
                    // The current transaction is a breakdown of transactions
                    transactionCategory = Category.BREAKDOWN_OF_TRANSACTIONS;
                } else if (transactionGrisbiSubCategoryId != 0) {
                    transactionGrisbiCategory.setCategoryId(transactionGrisbiCategoryId);
                    transactionGrisbiCategory.setSubCategoryId(transactionGrisbiSubCategoryId);
                    transactionCategory = categories.get(transactionGrisbiCategory);
                    assert (transactionCategory != null);
                } else if (transactionGrisbiCategoryId != 0) {
                    // Else, if a category is defined, get the category
                    transactionGrisbiCategory.setCategoryId(transactionGrisbiCategoryId);
                    transactionGrisbiCategory.setSubCategoryId(Category.NO_SUB_CATEGORY_ID);
                    transactionCategory = categories.get(transactionGrisbiCategory);
                    assert (transactionCategory != null);
                } else {
                    // Else, no category is defined
                    transactionCategory = Category.NO_CATEGORY;
                }
                assert (transactionCategory != null);

                // Get the comment of the transaction
                String transactionComment = transactionNode.valueOf("@N");
                assert (transactionComment != null);

                // Get the payee of the transaction
                String transactionPayeeIdValue = transactionNode.valueOf("@T"); // 'transactionPayeeIdValue' contains '0' if no payee is defined
                assert (transactionPayeeIdValue != null);
                long transactionPayeeId = Long.parseLong(transactionPayeeIdValue);
                assert (transactionPayeeId >= 0);
                Payee transactionPayee = null;
                if (transactionPayeeId != 0) { // Get the payee if it has been defined
                    transactionPayee = payees.get(transactionPayeeId);
                    assert (transactionPayee != null);
                } else { // No payee has been defined
                    transactionPayee = Payee.NO_PAYEE;
                }

                // Get the parent transaction
                String transactionParentIdValue = transactionNode.valueOf("@Va"); // '0' means that the transaction is a top-transaction ; not '0' means that the transaction is a sub-transaction
                assert (transactionParentIdValue != null);
                long transactionParentId = Long.parseLong(transactionParentIdValue);
                assert (transactionParentId >= 0);
                Transaction transactionParent = null;
                if (transactionParentId != 0) {
                    // Get the parent transaction
                    transactionParent = transactions.get(transactionParentId); // 'transactionParentId' is the ID of the parent transaction (in Grisbi files, parent transactions are always BEFORE sub-transactions)
                    assert (transactionParent != null);
                } else {
                    // The current transaction has no parent transaction
                    // The current transaction is a top transaction
                    transactionParent = null;
                }

                // Create a new transaction and save the transaction in the embedded database
                Transaction transaction = new Transaction(transactionDate, account, transactionAmount, transactionCategory, transactionComment, transactionPayee, transactionParent);
                Datamodel.saveTransaction(transaction);
                transactions.put(transactionId, transaction); // Save the transaction in the map, so that parent transactions can be found

                numberOfImportedTransactions++;
                p.progress(workUnit++);
            }

            // Make sure that the number of imported transactions and sub-transactions is the expected number
            if (!isImportCancelled() && numberOfImportedTransactions != expectedNumberOfTransactions) {
                throw new ParsingException("For the account '" + accountName + "', the number of imported transactions (" + numberOfImportedTransactions + ") is not equal to the expected number of transactions (" + expectedNumberOfTransactions + ") in the Grisbi file '" + pathToGrisbiFile + "'");
            }

            long endImportingTransactionsTime = System.currentTimeMillis();
            log.info(numberOfImportedTransactions + " transactions have been successfully imported in the account '" + accountName + "' in " + (endImportingTransactionsTime - startImportingTransactionsTime) + " ms");
            totalNumberOfImportedTransactions += numberOfImportedTransactions;
        }

        long endImportingTotalTransactionsTime = System.currentTimeMillis();
        log.info(totalNumberOfImportedTransactions + " transactions have been successfully imported in " + (endImportingTotalTransactionsTime - startImportingTotalTransactionsTime) + " ms");
        log.exiting(this.getClass().getName(), "importTransactions");
    }

    /**
     * Imports the Grisbi file into the database<BR/>
     * <UL>
     * <LI>Import the payees</LI>
     * <LI>Import the categories</LI>
     * <LI>Import the currencies</LI>
     * <LI>Import the accounts</LI>
     * <LI>Import the transactions and the sub-transactions</LI>
     * </UL>
     * @return Number of miliseconds needed to import the Grisbi file into the database
     * @throws ParsingException If there is a problem finding the needed nodes
     * @throws NumberFormatException If a string is read when a number is expected
     * @throws DateFormatException If a the format of a date is invalid
     */
    @Override
    public long importFile() throws ParsingException, NumberFormatException, DateFormatException {
        log.entering(this.getClass().getName(), "importFile");
        importCancelled = false;
        workUnit = 0;
        ProgressHandle p = ProgressHandleFactory.createHandle(
                NbBundle.getMessage(GrisbiFile050.class, "GrisbiFile050.ImportingGrisbiFile"),
                new Cancellable() {

                    @Override
                    public boolean cancel() {
                        Utilities.changeCursorWaitStatus(false);
                        importCancelled = true;
                        log.info("Import of '" + pathToGrisbiFile + "' has been cancelled");
                        return true;
                    }
                });

        long startImportingFileTime = System.currentTimeMillis();

        // Start progress bar
        p.setInitialDelay(0);
        p.start();

        // Get number of expected entities
        int expectedNumberOfPayees = getExpectedNumberOfPayees();
        int expectedNumberOfCategories = getExpectedNumberOfCategories();
        int expectedNumberOfCurrencies = getExpectedNumberOfCurrencies();
        int expectedNumberOfTransactions = getExpectedNumberOfTransactions();
        int totalEntities = expectedNumberOfPayees + expectedNumberOfCategories +
                expectedNumberOfCurrencies + expectedNumberOfTransactions;
        log.info("Expected number of entities (payees, categories, currencies, transactions): " + totalEntities);

        p.switchToDeterminate(totalEntities);

        // Empty the embedded database
        p.progress(NbBundle.getMessage(GrisbiFile050.class, "GrisbiFile050.EmptyingDatabase"));
        Datamodel.emptyDatabase();

        // Import the Grisbi file into the embedded database
        if (!isImportCancelled()) {
            p.progress(NbBundle.getMessage(GrisbiFile050.class, "GrisbiFile050.ImportingPayees"));
            importPayees(p, expectedNumberOfPayees);
        }

        if (!isImportCancelled()) {
            p.progress(NbBundle.getMessage(GrisbiFile050.class, "GrisbiFile050.ImportingCategories"));
            importCategories(p, expectedNumberOfCategories);
        }

        if (!isImportCancelled()) {
            p.progress(NbBundle.getMessage(GrisbiFile050.class, "GrisbiFile050.ImportingCurrencies"));
            importCurrencies(p, expectedNumberOfCurrencies);
        }

        if (!isImportCancelled()) {
            p.progress(NbBundle.getMessage(GrisbiFile050.class, "GrisbiFile050.ImportingAccounts"));
            importAccounts();
        }

        if (!isImportCancelled()) {
            p.progress(NbBundle.getMessage(GrisbiFile050.class, "GrisbiFile050.ImportingTransactions"));
            importTransactions(p);
        }

        p.finish();
        long endImportingFileTime = System.currentTimeMillis();
        long importDuration = endImportingFileTime - startImportingFileTime;

        log.exiting(this.getClass().getName(), "importFile", importDuration);
        return importDuration;
    }

    @Override
    public boolean isImportCancelled() {
        return importCancelled;
    }
}
