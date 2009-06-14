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

/**
 * <B>GrisbiFile050</B>
 * <UL>
 * <LI>Permits to import Grisbi 0.5.x filesx</LI>
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
     * Imports the payees into the embedded database
     * @throws ParsingException
     * <UL>
     * <LI>If there is a problem finding the needed nodes</LI>
     * <LI>If the number of imported payees is not equal to the number of payees defined in the Grisbi file</LI>
     * </UL>
     * @throws NumberFormatException If a string is read when a number is expected
     */
    private void importPayees() throws ParsingException, NumberFormatException {
        Node expectedNumberOfPayeesNode;    // Node containing the expected number of payees
        String expectedNumberOfPayeesValue; // String containing the expected number of imported payees
        long expectedNumberOfPayees;        // Expected number of payees - taken from the Grisbi XML file (computed by Grisbi)
        long numberOfImportedPayees;        // Number of imported payees (computed by GrisbiGraphs)
        List listOfPayees;                  // List of payees found in the Grisbi XML file
        Iterator payeesIterator;            // Iterator to go through the payees
        Node payeeNode;                     // Node containing a payee
        String payeeIdValue;                // String containing the ID of a payee
        long payeeId;                       // Payee's ID (specific to Grisbi)
        String payeeName;                   // Payee's name
        Payee payee = null;                 // Payee to save in the database
        long startImportingPayeesTime;      // Time when the import of payees starts
        long endImportingPayeesTime;        // Time when the import of payees ends

        startImportingPayeesTime = System.currentTimeMillis();

        // Save default payee in the database
        Datamodel.savePayee(Payee.NO_PAYEE); // This constant is used to search the transactions for which no payee is defined

        // Get the expected number of payees written in the Grisbi file
        expectedNumberOfPayeesNode = grisbiFileDocument.selectSingleNode("/Grisbi/Tiers/Generalites/Nb_tiers");
        if (expectedNumberOfPayeesNode == null) {
            throw new ParsingException("The expected number of payees has not been found in the Grisbi file '" + pathToGrisbiFile + "'");
        }
        expectedNumberOfPayeesValue = expectedNumberOfPayeesNode.getStringValue();
        assert (expectedNumberOfPayeesValue != null);
        expectedNumberOfPayees = Long.parseLong(expectedNumberOfPayeesValue); // Number of expected payees ; the number of imported payees should be equal to this number

        // Import the payees from the Grisbi file into the embedded database
        numberOfImportedPayees = 0;
        listOfPayees = grisbiFileDocument.selectNodes("/Grisbi/Tiers/Detail_des_tiers/Tiers");
        payeesIterator = listOfPayees.iterator();
        while (payeesIterator.hasNext() && !isImportCancelled()) { // Go through the list of payees found in the Grisbi XML file
            // Get the current payee node
            payeeNode = (Node) payeesIterator.next();

            // Get the ID and the name of the current payee
            payeeIdValue = payeeNode.valueOf("@No");
            assert (payeeIdValue != null);
            payeeId = Long.parseLong(payeeIdValue);
            assert (payeeId > 0);
            payeeName = payeeNode.valueOf("@Nom");
            assert (payeeName != null);

            // Create a new payee and save it in the embedded database
            payee = new Payee(payeeId, payeeName, false);
            Datamodel.savePayee(payee);

            numberOfImportedPayees++;
        }

        // Make sure that all payees have been imported
        if (!isImportCancelled() && numberOfImportedPayees != expectedNumberOfPayees) {
            throw new ParsingException("The number of imported payees (" + numberOfImportedPayees + ") is not equal to the expected number of payees (" + expectedNumberOfPayees + ") written in the Grisbi file '" + pathToGrisbiFile + "'");
        }

        endImportingPayeesTime = System.currentTimeMillis();
        log.info(numberOfImportedPayees + " payees have been successfully imported in " + (endImportingPayeesTime - startImportingPayeesTime) + " ms");
    }

    /**
     * Imports the categories into the embedded database
     * @throws ParsingException
     * <UL>
     * <LI>If there is a problem finding the needed nodes</LI>
     * <LI>If the number of imported categories is not equal to the number of categories defined in the Grisbi file</LI>
     * </UL>
     * @throws NumberFormatException If a string is read when a number is expected
     */
    private void importCategories() throws ParsingException, NumberFormatException {
        Node expectedNumberOfCategoriesNode;        // Node containing the expected number of categories
        String expectedNumberOfCategoriesValue;     // String containing the expected number of categories
        long expectedNumberOfCategories;            // Expected number of categories - taken from the Grisbi XML file (computed by Grisbi)
        long numberOfImportedCategories;            // Number of imported categories (computed by GrisbiGraphs)
        List listOfCategories;                      // List of categories found in the Grisbi file
        Iterator categoriesIterator;                // Iterator to go through the categories
        Node categoryNode;                          // Node containing a category
        String categoryIdValue;                     // String containing the ID of the category
        long categoryId;                            // ID of the category
        String categoryName;                        // Name of the category
        Category category = null;                   // Category to save in the database
        List listOfSubCategories;                   // List of sub-categories of a category found in the Grisbi file
        Iterator subCategoriesIterator;             // Iterator to go through the sub-categories
        Node subCategoryNode;                       // Node containing the sub-category
        String subCategoryIdValue;                  // String containing the ID of the sub-category
        long subCategoryId;                         // ID of the sub-category
        String subCategoryName;                     // Name of the sub-category
        Category subCategory = null;                // Sub-category to save in the database
        long startImportingCategoriesTime;          // Time when the import of categories starts
        long endImportingCategoriesTime;            // Time when the import of categories ends

        startImportingCategoriesTime = System.currentTimeMillis();

        // Save default categories in the embedded database
        Datamodel.saveCategory(Category.TRANSFER);
        Datamodel.saveCategory(Category.BREAKDOWN_OF_TRANSACTIONS);
        Datamodel.saveCategory(Category.NO_CATEGORY);

        // Get the expected number of categories written in the Grisbi file
        expectedNumberOfCategoriesNode = grisbiFileDocument.selectSingleNode("/Grisbi/Categories/Generalites/Nb_categories");
        if (expectedNumberOfCategoriesNode == null) {
            throw new ParsingException("The expected number of categories has not been found in the Grisbi file '" + pathToGrisbiFile + "'");
        }
        expectedNumberOfCategoriesValue = expectedNumberOfCategoriesNode.getStringValue();
        assert (expectedNumberOfCategoriesValue != null);
        expectedNumberOfCategories = Long.parseLong(expectedNumberOfCategoriesValue); // Number of expected categories ; the number of imported categories should be equal to this number

        // Import the categories from the Grisbi file into the embedded database
        numberOfImportedCategories = 0;
        listOfCategories = grisbiFileDocument.selectNodes("/Grisbi/Categories/Detail_des_categories/Categorie");
        categoriesIterator = listOfCategories.iterator();
        while (categoriesIterator.hasNext() && !isImportCancelled()) { // Go through the list of categories written in the Grisbi file
            // Get the current category node
            categoryNode = (Node) categoriesIterator.next();

            // Get the ID and the name of the current category
            categoryIdValue = categoryNode.valueOf("@No");
            assert (categoryIdValue != null);
            categoryId = Long.parseLong(categoryIdValue); // Get the ID of the category
            assert (categoryId > 0);
            categoryName = categoryNode.valueOf("@Nom"); // Get the name of the category
            assert (categoryName != null);

            // Create a new category and save it in the embedded database
            category = new Category(categoryId, 0L, categoryName, null, false); // There is no sub-category - the field "grisbi_sub_category_id" is set to 0 (this is not a sub-category)
            Datamodel.saveCategory(category);

            // Import the sub-categories into the database
            listOfSubCategories = categoryNode.selectNodes("Sous-categorie");
            subCategoriesIterator = listOfSubCategories.iterator();
            while (subCategoriesIterator.hasNext() && !isImportCancelled()) { // Go through the list of sub-categories of the current category
                // Get the current sub-category node
                subCategoryNode = (Node) subCategoriesIterator.next();

                // Get the ID and the name of the current sub-category
                subCategoryIdValue = subCategoryNode.valueOf("@No");
                assert (subCategoryIdValue != null);
                subCategoryId = Long.parseLong(subCategoryIdValue); // Get the ID of the sub-category
                assert (subCategoryId > 0);
                subCategoryName = subCategoryNode.valueOf("@Nom"); // Get the name of the sub-category
                assert (subCategoryName != null);

                // Create a new sub-category and save it in the embedded database
                subCategory = new Category(categoryId, subCategoryId, subCategoryName, category, false);
                Datamodel.saveCategory(subCategory);
            }

            // For each category, save an empty sub-category
            Category noSubCategory = new Category(categoryId, 10000L, "No sub-category", category, false);
            Datamodel.saveCategory(noSubCategory);

            numberOfImportedCategories++;
        }

        // Make sure that all categories have been imported
        if (!isImportCancelled() && numberOfImportedCategories != expectedNumberOfCategories) {
            throw new ParsingException("The number of imported categories (" + numberOfImportedCategories + ") is not equal to the expected number of categories (" + expectedNumberOfCategories + ") written in the Grisbi file '" + pathToGrisbiFile + "'");
        }

        endImportingCategoriesTime = System.currentTimeMillis();
        log.info(numberOfImportedCategories + " categories have been successfully imported in " + (endImportingCategoriesTime - startImportingCategoriesTime) + " ms");
    }

    /**
     * Imports the currencies into the database
     * @throws ParsingException
     * <UL>
     * <LI>If there is a problem finding the needed nodes</LI>
     * <LI>If the number of imported currencies is not equal to the number of currencies defined in the Grisbi file</LI>
     * </UL>
     * @throws NumberFormatException If a string is read when a number is expected
     */
    private void importCurrencies() throws ParsingException, NumberFormatException {
        Node expectedNumberOfCurrenciesNode;    // Node containing the expected number of currencies
        String expectedNumberOfCurrenciesValue; // String containing the expected number of currencies
        long expectedNumberOfCurrencies;        // Expected number of currencies - taken from the Grisbi XML file (computed by Grisbi)
        long numberOfImportedCurrencies;        // Number of imported currencies (computed by GrisbiGraphs)
        List listOfCurrencies;                  // List of currencies found in the Grisbi XML file
        Iterator currenciesIterator;            // Iterator to go through the currencies
        Node currencyNode;                      // Node containing a currency
        String currencyIdValue;                 // String containing the ID of the currency
        long currencyId;                        // ID of a currency
        String currencyName;                    // Name of a currency
        String currencyCode;                    // Code of a currency
        String currencyIsoCode;                 // Code ISO of a currency
        Currency currency;                      // Currency to save in the database
        long startImportingCurrenciesTime;      // Time when the import of currencies starts
        long endImportingCurrenciesTime;        // Time when the import of currencies ends

        startImportingCurrenciesTime = System.currentTimeMillis();

        // Get the expected number of currencies written in the Grisbi file
        expectedNumberOfCurrenciesNode = grisbiFileDocument.selectSingleNode("/Grisbi/Devises/Generalites/Nb_devises");
        if (expectedNumberOfCurrenciesNode == null) {
            throw new ParsingException("The expected number of currencies has not been found in the Grisbi file '" + pathToGrisbiFile + "'");
        }
        expectedNumberOfCurrenciesValue = expectedNumberOfCurrenciesNode.getStringValue();
        assert (expectedNumberOfCurrenciesValue != null);
        expectedNumberOfCurrencies = Long.parseLong(expectedNumberOfCurrenciesValue); // Number of expected currencies ; the number of imported currencies should be equal to this number

        // Import the currencies into the embedded database
        numberOfImportedCurrencies = 0;
        listOfCurrencies = grisbiFileDocument.selectNodes("/Grisbi/Devises/Detail_des_devises/Devise");
        currenciesIterator = listOfCurrencies.iterator();
        while (currenciesIterator.hasNext() && !isImportCancelled()) { // Go through the list of currencies
            // Get the current currency node
            currencyNode = (Node) currenciesIterator.next();

            // Get the ID, the name and the ISO code of the current currency
            currencyIdValue = currencyNode.valueOf("@No");
            assert (currencyIdValue != null);
            currencyId = Long.parseLong(currencyIdValue); // Get the ID of the currency
            assert (currencyId > 0);
            currencyName = currencyNode.valueOf("@Nom"); // Get the name of the currency
            assert (currencyName != null);
            currencyCode = currencyNode.valueOf("@Code"); // Get the code of the currency
            assert (currencyCode != null);
            currencyIsoCode = currencyNode.valueOf("@IsoCode"); // Get the code ISO of the currency
            assert (currencyIsoCode != null);

            // Create a new currency and save the currency in the database
            // By default the currencies are not active
            // When the accounts are imported, the currencies are eventualy activated
            currency = new Currency(currencyId, currencyName, currencyCode, currencyIsoCode, new BigDecimal(0), new BigDecimal(0), false);
            Datamodel.saveCurrency(currency);

            numberOfImportedCurrencies++;
        }

        // Make sure that all currencies have been imported
        if (!isImportCancelled() && numberOfImportedCurrencies != expectedNumberOfCurrencies) {
            throw new ParsingException("The number of imported currencies (" + numberOfImportedCurrencies + ") is not equal to the expected number of currencies (" + expectedNumberOfCurrencies + ") in the Grisbi file '" + pathToGrisbiFile + "'");
        }

        endImportingCurrenciesTime = System.currentTimeMillis();
        log.info(numberOfImportedCurrencies + " currencies have been successfully imported in " + (endImportingCurrenciesTime - startImportingCurrenciesTime) + " ms");
    }

    /**
     * Imports the accounts into the database<BR/>
     * The method <CODE>importCurrencies()</CODE> has to be called before
     * @throws ParsingException If there is a problem finding the needed nodes
     * @throws NumberFormatException If a string is read when a number is expected
     * @throws DateFormatException If the format of the date of the first transaction is invalid
     */
    private void importAccounts() throws ParsingException, NumberFormatException, DateFormatException {
        List listOfAccounts;                    // List of accounts found in the Grisbi XML file
        Iterator accountsIterator;              // Iterator to go through the accounts
        Node accountNode;                       // Node containing an account
        Node accountNameNode;                   // Node containing the name of the account
        String accountName;                     // Name of the account
        Node accountIdNode;                     // Node containing the ID of the account
        String accountIdValue;                  // String containing the ID of the account
        long accountId;                         // ID of the account
        Node currencyIdNode;                    // Node containing the ID of the currency to which the account belongs
        String currencyIdValue;                 // String containing the ID of the currency to which the account belongs
        long currencyId;                        // ID of the currency to which the account belongs
        Map<Long, Currency> currencies;         // Map containing all the saved currencies (that's why the method importCurrencies() has to be called before) - the key of the map is the currency's ID
        Currency accountCurrency;               // Currency of the account to add
        Node accountInitialAmountNode;          // Node containing the account's initial amount
        String accountInitialAmountValue;       // String containing the account's initial amount
        BigDecimal accountInitialAmount;        // Initial amount of the account
        Node accountBalanceNode;                // Node containing the account's current balance
        String accountBalanceValue;             // String containing the current balance of the account
        BigDecimal accountBalance;              // Current balance of the account
        Node accountActiveNode;                 // Node containing the active property of the account
        String accountActiveValue;              // Active property (account active if 'strAccountActive' different from "1")
        boolean accountActive;                  // Is the account active or closed
        Account account;                        // Account to add to a currency (in the walletDAO)
        long numberOfImportedAccounts;          // Number of imported accounts (computed by GrisbiGraphs)
        long startImportingAccountsTime;        // Time when the import of accounts starts
        long endImportingAccountsTime;          // Time when the import of accounts ends

        startImportingAccountsTime = System.currentTimeMillis();

        // Get all the currencies already saved in the database (the method importCurrencies() has to be called before)
        currencies = Datamodel.getCurrenciesWithId();

        // Import the accounts in the embedded database
        numberOfImportedAccounts = 0;
        listOfAccounts = grisbiFileDocument.selectNodes("/Grisbi/Comptes/Compte");
        accountsIterator = listOfAccounts.iterator();
        while (accountsIterator.hasNext() && !isImportCancelled()) {

            // Get the account node
            accountNode = (Node) accountsIterator.next();

            // Get the "Active" property of the account: is the account closed?
            accountActiveNode = accountNode.selectSingleNode("Details/Compte_cloture");
            assert (accountActiveNode != null);
            accountActiveValue = accountActiveNode.getStringValue();
            assert (accountActiveValue != null);
            accountActive = (accountActiveValue.compareTo("1") != 0);

            // Get the ID of the account
            accountIdNode = accountNode.selectSingleNode("Details/No_de_compte");
            assert (accountIdNode != null);
            accountIdValue = accountIdNode.getStringValue();
            assert (accountIdValue != null);
            accountId = Long.parseLong(accountIdValue);
            assert (accountId >= 0);

            // Get the name of the account
            accountNameNode = accountNode.selectSingleNode("Details/Nom");
            assert (accountNameNode != null);
            accountName = accountNameNode.getStringValue();
            assert (accountName != null);

            // Get the currency of the account
            currencyIdNode = accountNode.selectSingleNode("Details/Devise");
            assert (currencyIdNode != null);
            currencyIdValue = currencyIdNode.getStringValue();
            assert (currencyIdValue != null);
            currencyId = Long.parseLong(currencyIdValue);
            assert (currencyId > 0);

            // Get the corresponding Currency object
            accountCurrency = currencies.get(currencyId); // An account has always a currency in Grisbi
            assert (accountCurrency != null);

            // Activate the currency if needed
            if (accountActive && !accountCurrency.getActive()) {
                accountCurrency.setActive(true);
            }

            // Get the initial amount of the account
            accountInitialAmountNode = accountNode.selectSingleNode("Details/Solde_initial");
            assert (accountInitialAmountNode != null);
            accountInitialAmountValue = accountInitialAmountNode.getStringValue();
            assert (accountInitialAmountValue != null);
            accountInitialAmount = new BigDecimal(accountInitialAmountValue.replace(',', '.'));
            accountInitialAmount = accountInitialAmount.setScale(2, RoundingMode.HALF_EVEN);

            // Get the current balance of the account
            accountBalanceNode = accountNode.selectSingleNode("Details/Solde_courant");
            assert (accountBalanceNode != null);
            accountBalanceValue = accountBalanceNode.getStringValue();
            assert (accountBalanceValue != null);
            accountBalance = new BigDecimal(accountBalanceValue.replace(',', '.'));
            accountBalance = accountBalance.setScale(2, RoundingMode.HALF_EVEN);

            // Create a new account and save the account in the embedded database
            account = new Account(accountId, accountName, accountCurrency, accountInitialAmount, accountBalance, accountActive);
            Datamodel.saveAccount(account);

            accountCurrency.setBalance(accountCurrency.getBalance().add(accountBalance));
            accountCurrency.setInitialAmount(accountCurrency.getInitialAmount().add(accountInitialAmount));

            numberOfImportedAccounts++;
        }

        for (Currency currency : currencies.values()) {
            Datamodel.saveCurrency(currency);
        }

        endImportingAccountsTime = System.currentTimeMillis();
        log.info(numberOfImportedAccounts + " accounts have been successfully imported in " + (endImportingAccountsTime - startImportingAccountsTime) + " ms");
    }

    /**
     * Imports the transactions into the embedded database<BR/>
     * The methods <CODE>importCurrencies()</CODE>, <CODE>importPayees()</CODE>,
     * <CODE>importAccounts()</CODE>, and <CODE>importCategories()</CODE> have to be called before
     * @throws ParsingException If there is a problem finding the needed nodes
     * @throws NumberFormatException If a string is read when a number is expected
     * @throws DateFormatException If the date format of a transaction is invalid
     */
    private void importTransactions(ProgressHandle p) throws ParsingException, NumberFormatException, DateFormatException {
        List listOfAccounts;                        // List of the accounts read from the Grisbi file
        Iterator accountsIterator;                  // Iterator to go through the accounts
        Node accountNode;                           // Node of the account for which transactions have to be imported
        Node accountNameNode;                       // Node of the name of the account for which transactions have to be imported
        String accountName;                         // Name of the account for which transactions have to be imported
        Node accountIdNode;                         // Node of the ID of the account for which transactions have to be imported
        String accountIdValue;                      // String containing the ID of the account
        long accountId;                             // ID of the account for which transactions have to be imported
        Map<Long, Account> accounts;                // Map containing all the saved accounts (that's why the method importAccounts() has to be called before) - the key of the map is the Account's ID
        Account account;                            // Current account, which contains transactions to import
        Node expectedNumberOfTransactionsNode;      // Node containing the expected number of transactions in the current account
        String expectedNumberOfTransactionsValue;   // String containing the expected number of transactions in the current account
        long expectedNumberOfTransactions;          // Expected number of transactions in the current account
        long numberOfImportedTransactions;          // Number of imported transactions for the current account
        long totalNumberOfImportedTransactions;     // Number of imported transactions (for all accounts)
        List listOfTransactions;                    // List of the transactions and sub-transactions of the account
        Iterator transactionsIterator;              // Iterator to go through the transactions
        Node transactionNode;                       // Node containing the transaction or sub-transaction to import
        Transaction transaction;                    // Transaction or sub-transaction to import
        String transactionIdValue;                  // String containing the ID of the transaction to import
        long transactionId;                         // ID of the transaction to import
        String transactionDateValue;                // String containing the date of the transaction
        LocalDate transactionDate;                  // Date of the transaction
        String transactionAmountValue;              // String containing the amount of the transaction
        BigDecimal transactionAmount;               // Amount of the transaction
        String exchangeRateValue;                   // String containing the exchange rate
        BigDecimal exchangeRate;                    // Exchange rate of the transaction
        String feesValue;                           // String containing the fees
        BigDecimal fees;                            // Fees of the transaction
        String transactionGrisbiCategoryIdValue;    // String containing the ID of the category of the transaction - '0' means that the category is not defined
        long transactionGrisbiCategoryId;           // ID of the category of the transaction - 0 means that the category is not defined
        String transactionGrisbiSubCategoryIdValue; // String containing the ID of the sub-category of the transaction - '0' means that the sub-category is not defined
        long transactionGrisbiSubCategoryId;        // ID of the sub-category of the transaction - 0 means that the sub-category is not defined
        Category transactionCategory;               // Category of the transaction: can be either Category.TRANSFER, Category.BREAKDOWN_OF_TRANSACTIONS, the sub-category (if the transaction has a sub-category) or a category
        Map<GrisbiCategory, Category> categories;   // Map containing all the saved categories (that's why the method importCategories() has to be called before) - the key of the map is the Grisbi's category (GrisbiCategory)
        GrisbiCategory transactionGrisbiCategory = new GrisbiCategory(0, 0); // Grisbi category of the transaction
        String twinTransaction;                     // If the transaction is a transfer, twin transaction on the other account
        String accountOfTransfer;                   // If the transaction if a transfer, account of the twin transaction
        String breakdownOfTransaction;              // Does the transaction contain sub-transactions? - '1' means that the transaction is a 'breakdown of transactions'
        String transactionComment;                  // Comment of the transaction
        String transactionPayeeIdValue;             // String containing the ID of the payee
        long transactionPayeeId;                    // ID of the payee
        Payee transactionPayee;                     // Payee of the transaction
        Map<Long, Payee> payees;                    // Map containing all the saved payees (that's why the method importPayees() has to be called before) - the key of the map is the payee's ID
        String transactionParentIdValue;            // String containing the ID of the parent's transaction (if the transaction to import is a sub-transaction)
        long transactionParentId;                   // ID of the parent's transaction
        Transaction transactionParent;              // Parent's transaction
        Map<Long, Transaction> transactions = new HashMap<Long, Transaction>(); // Map containing all the saved transactions - the key of the map is the transaction's ID
        Map<Long, Currency> currencies = new HashMap<Long, Currency>(); // List of currencies
        String transactionCurrencyIdValue;          // String containing the currency ID of a transaction
        long transactionCurrencyId;                 // Currency ID of a transaction
        Currency transactionCurrency;               // Transaction currency
        String transactionRdcValue;                 // une_devise_compte_egale_x_devise_ope
        long transactionRdc;
        long startImportingTotalTransactionsTime;   // Time when the import of transactions starts
        long endImportingTotalTransactionsTime;     // Time when the import of transactions ends
        long startImportingTransactionsTime;        // Time when the import of transactions for the current account starts
        long endImportingTransactionsTime;          // Time when the import of transactions for the current account ends

        startImportingTotalTransactionsTime = System.currentTimeMillis();

        // Get all the accounts, all the payees, and all the categories already saved in the embedded database
        accounts = Datamodel.getAccountsWithId();
        payees = Datamodel.getPayeesWithId();
        categories = Datamodel.getCategoriesWithGrisbiCategory();
        currencies = Datamodel.getCurrenciesWithId();

        // Import the transactions from each account into the embedded database
        totalNumberOfImportedTransactions = 0;
        listOfAccounts = grisbiFileDocument.selectNodes("/Grisbi/Comptes/Compte");
        accountsIterator = listOfAccounts.iterator();
        while (accountsIterator.hasNext() && !isImportCancelled()) { // Go through the accounts

            startImportingTransactionsTime = System.currentTimeMillis();

            // Get the account's node
            accountNode = (Node) accountsIterator.next();
            assert (accountNode != null);

            // Get the ID of the account
            accountIdNode = accountNode.selectSingleNode("Details/No_de_compte");
            assert (accountIdNode != null);
            accountIdValue = accountIdNode.getStringValue();
            assert (accountIdValue != null);
            accountId = Long.parseLong(accountIdValue);
            assert (accountId >= 0);

            // Get the name of the current account
            accountNameNode = accountNode.selectSingleNode("Details/Nom");
            assert (accountNameNode != null);
            accountName = accountNameNode.getStringValue();
            assert (accountName != null);

            // Display on the progress bar the account from which transactions are imported
            p.progress("Importing transactions from " + accountName);

            // Get the corresponding Account object
            account = accounts.get(accountId);
            assert (account != null);

            // Get the expected number of transactions of the account
            expectedNumberOfTransactionsNode = accountNode.selectSingleNode("Details/Nb_operations");
            if (expectedNumberOfTransactionsNode == null) {
                throw new ParsingException("The expected number of transactions has not been found for the account '" + accountName + "' in the Grisbi file '" + pathToGrisbiFile + "'");
            }
            expectedNumberOfTransactionsValue = expectedNumberOfTransactionsNode.getStringValue();
            assert (expectedNumberOfTransactionsValue != null);
            expectedNumberOfTransactions = Long.parseLong(expectedNumberOfTransactionsValue);

            // Import the transactions of the account into the embedded database
            numberOfImportedTransactions = 0;
            listOfTransactions = accountNode.selectNodes("Detail_des_operations/Operation");
            transactionsIterator = listOfTransactions.iterator();
            while (transactionsIterator.hasNext() && !isImportCancelled()) { // Go through the list of transactions of the account

                // Get the transaction node
                transactionNode = (Node) transactionsIterator.next();

                // Get the ID of the transaction
                assert (transactionNode != null);
                transactionIdValue = transactionNode.valueOf("@No");
                assert (transactionIdValue != null);
                transactionId = Long.parseLong(transactionIdValue);
                assert (transactionId > 0);

                // Get the date of the transaction
                transactionDateValue = transactionNode.valueOf("@D");
                assert (transactionDateValue != null && transactionDateValue.compareTo("") != 0);
                // Get the date from the string
                transactionDate = Period.getDate(transactionDateValue); // Throws a DateFormatException if the format of 'transactionDateValue' is not valid
                assert (transactionDate != null);

                // Get the exchange rate
                exchangeRateValue = transactionNode.valueOf("@Tc");
                assert (exchangeRateValue != null);
                exchangeRate = new BigDecimal(exchangeRateValue); // exchangeRate = 0 if there is no echange rate

                // Get the fees
                feesValue = transactionNode.valueOf("@Fc");
                assert (feesValue != null);
                fees = new BigDecimal(feesValue);

                // Get the amount of the transaction
                transactionAmountValue = transactionNode.valueOf("@M");
                assert (transactionAmountValue != null);
                transactionAmount = new BigDecimal(transactionAmountValue.replace(',', '.'));

                // Update the amount of the transaction if the currency of the transaction is different from the currency of the account
                // Method found in the Grisbi source file: devises.c/calcule_montant_devise_renvoi
                if (exchangeRate.compareTo(BigDecimal.ZERO) != 0) {

                    // Get the currency of the transaction
                    transactionCurrencyIdValue = transactionNode.valueOf("@De");
                    assert (transactionCurrencyIdValue != null);
                    transactionCurrencyId = Long.parseLong(transactionCurrencyIdValue); // Get the ID of the currency
                    transactionCurrency = currencies.get(transactionCurrencyId);
                    assert (transactionCurrency != null);

                    if (transactionCurrency.getId().compareTo(account.getCurrency().getId()) != 0) {
                        transactionRdcValue = transactionNode.valueOf("@Rdc");
                        assert (transactionRdcValue != null);
                        transactionRdc = Long.parseLong(transactionRdcValue);

                        if (transactionRdc == 1) {
                            transactionAmount = (transactionAmount.divide(exchangeRate, RoundingMode.HALF_EVEN)).subtract(fees);
                        } else {
                            transactionAmount = (transactionAmount.multiply(exchangeRate)).subtract(fees);
                        }
                    }
                }
                transactionAmount = transactionAmount.setScale(2, RoundingMode.HALF_EVEN);

                // If the current transaction is a transfer:
                twinTransaction = transactionNode.valueOf("@Ro"); // Twin transaction
                assert (twinTransaction != null);
                accountOfTransfer = transactionNode.valueOf("@Rc"); // Account where the amount has been transfered (account of the twin transaction)
                assert (accountOfTransfer != null);

                // Is the current transaction a breakdown of transactions?
                breakdownOfTransaction = transactionNode.valueOf("@Ov"); // breakdownOfTransaction = 1 if the current trasnsaction is a breakdown of transactions
                assert (breakdownOfTransaction != null);

                // Get the category and the sub-category of the transaction
                transactionGrisbiCategoryIdValue = transactionNode.valueOf("@C"); // Get the category ID ('0' means that the category is not defined)
                assert (transactionGrisbiCategoryIdValue != null);
                transactionGrisbiCategoryId = Long.parseLong(transactionGrisbiCategoryIdValue);
                assert (transactionGrisbiCategoryId >= 0);

                transactionGrisbiSubCategoryIdValue = transactionNode.valueOf("@Sc"); // Get the sub-category ID ('0' means that the sub-category is not defined)
                assert (transactionGrisbiSubCategoryIdValue != null);
                transactionGrisbiSubCategoryId = Long.parseLong(transactionGrisbiSubCategoryIdValue);
                assert (transactionGrisbiSubCategoryId >= 0);

                // Check if the category is "Transfer" or "Breakdown of transactions"
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
                    transactionGrisbiCategory.setSubCategoryId(10000L);
                    transactionCategory = categories.get(transactionGrisbiCategory);
                    assert (transactionCategory != null);
                } else {
                    // Else, no category is defined
                    transactionCategory = Category.NO_CATEGORY;
                }
                assert (transactionCategory != null);

                // Get the comment of the transaction
                transactionComment = transactionNode.valueOf("@N");
                assert (transactionComment != null);

                // Get the payee of the transaction
                transactionPayeeIdValue = transactionNode.valueOf("@T"); // 'transactionPayeeIdValue' contains '0' if no payee is defined
                assert (transactionPayeeIdValue != null);
                transactionPayeeId = Long.parseLong(transactionPayeeIdValue);
                assert (transactionPayeeId >= 0);
                if (transactionPayeeId != 0) { // Get the payee if it has been defined
                    transactionPayee = payees.get(transactionPayeeId);
                    assert (transactionPayee != null);
                } else { // No payee has been defined
                    transactionPayee = Payee.NO_PAYEE;
                }

                // Get the parent transaction
                transactionParentIdValue = transactionNode.valueOf("@Va"); // '0' means that the transaction is a top-transaction ; not '0' means that the transaction is a sub-transaction
                assert (transactionParentIdValue != null);
                transactionParentId = Long.parseLong(transactionParentIdValue);
                assert (transactionParentId >= 0);
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
                transaction = new Transaction(transactionDate, account, transactionAmount, transactionCategory, transactionComment, transactionPayee, transactionParent);
                Datamodel.saveTransaction(transaction);
                transactions.put(transactionId, transaction); // Save the transaction in the map, so that parent transactions can be found

                numberOfImportedTransactions++;
            }

            // Make sure that the number of imported transactions and sub-transactions is the expected number
            if (!isImportCancelled() && numberOfImportedTransactions != expectedNumberOfTransactions) {
                throw new ParsingException("For the account '" + accountName + "', the number of imported transactions (" + numberOfImportedTransactions + ") is not equal to the expected number of transactions (" + expectedNumberOfTransactions + ") in the Grisbi file '" + pathToGrisbiFile + "'");
            }

            endImportingTransactionsTime = System.currentTimeMillis();
            log.info(numberOfImportedTransactions + " transactions have been successfully imported in the account '" + accountName + "' in " + (endImportingTransactionsTime - startImportingTransactionsTime) + " ms");
            totalNumberOfImportedTransactions += numberOfImportedTransactions;
        }

        endImportingTotalTransactionsTime = System.currentTimeMillis();
        log.info(totalNumberOfImportedTransactions + " transactions have been successfully imported in " + (endImportingTotalTransactionsTime - startImportingTotalTransactionsTime) + " ms");
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
        long startImportingFileTime;    // Time when the import of the file starts
        long endImportingFileTime;      // Time when the import of the file ends
        long importDuration;            // Time to import the Grisbi file

        importCancelled = false;
        ProgressHandle p = ProgressHandleFactory.createHandle("Import Grisbi file", new Cancellable() {

            @Override
            public boolean cancel() {
                importCancelled = true;
                log.info("Import of '" + pathToGrisbiFile + "' cancelled");
                return true;
            }
        });

        p.start();
        startImportingFileTime = System.currentTimeMillis();

        // Empty the embedded database
        p.progress("Emptying database");
        Datamodel.emptyDatabase();

        // Import the information from the Grisbi file into the embedded database
        if (!isImportCancelled()) {
            p.progress("Importing payees");
            importPayees();
        }

        if (!isImportCancelled()) {
            p.progress("Importing categories");
            importCategories();
        }

        if (!isImportCancelled()) {
            p.progress("Importing currencies");
            importCurrencies();
        }

        if (!isImportCancelled()) {
            p.progress("Importing accounts");
            importAccounts();
        }

        if (!isImportCancelled()) {
            p.progress("Importing transactions");
            importTransactions(p);
        }

        p.finish();
        endImportingFileTime = System.currentTimeMillis();
        importDuration = endImportingFileTime - startImportingFileTime;
        return importDuration;
    }

    @Override
    public boolean isImportCancelled() {
        return importCancelled;
    }
}
