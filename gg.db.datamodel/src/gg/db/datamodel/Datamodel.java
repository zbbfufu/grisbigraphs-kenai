/*
 * Datamodel.java
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
package gg.db.datamodel;

import gg.db.entities.Account;
import gg.db.entities.Category;
import gg.db.entities.Currency;
import gg.db.entities.FileImport;
import gg.db.entities.GrisbiCategory;
import gg.db.entities.Payee;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Enable GrisbiGraphs to communicate with the embedded Derby database
 * @author Francois Duchemin
 */
public class Datamodel {

    /** Class name */
    private static final String CLASS_NAME = "gg.db.datamodel.Datamodel";
    /** Logger */
    private static Logger log = Logger.getLogger(CLASS_NAME);

    /**
     * Gets the file imports log
     * @return List of Grisbi files imported in the embedded database
     */
    public static List<FileImport> getFileImports() {
        log.entering(CLASS_NAME, "getFileImports");

        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        @SuppressWarnings("unchecked")
        List<FileImport> fileImports = (List<FileImport>) s.createQuery("from FileImport order by importedOn desc").list();
        t.commit();

        log.exiting(CLASS_NAME, "getFileImports", fileImports);
        return fileImports;
    }

    /**
     * Gets the currencies
     * @return List of currencies
     */
    public static List<Currency> getCurrencies() {
        log.entering(CLASS_NAME, "getCurrencies");

        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        @SuppressWarnings("unchecked")
        List<Currency> currencies = (List<Currency>) s.createQuery("from Currency order by name").list();
        t.commit();

        log.exiting(CLASS_NAME, "getCurrencies", currencies);
        return currencies;
    }

    /**
     * Gets the currencies with their IDs
     * @return Map of currencies: the key is the currency ID, the value is the currency itself
     */
    public static Map<Long, Currency> getCurrenciesWithId() {
        log.entering(CLASS_NAME, "getCurrenciesWithId");

        Map<Long, Currency> currenciesWithId = new HashMap<Long, Currency>();
        List<Currency> currencies = getCurrencies();

        for (Currency currency : currencies) {
            currenciesWithId.put(currency.getId(), currency);
        }

        log.exiting(CLASS_NAME, "getCurrencies", currenciesWithId);
        return currenciesWithId;
    }

    /**
     * Gets the active currencies
     * @return List of active currencies
     */
    public static List<Currency> getActiveCurrencies() {
        log.entering(CLASS_NAME, "getActiveCurrencies");

        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        @SuppressWarnings("unchecked")
        List<Currency> activeCurrencies = (List<Currency>) s.createQuery("from Currency where active=true order by name").list();
        t.commit();

        log.exiting(CLASS_NAME, "getCurrencies", activeCurrencies);
        return activeCurrencies;
    }

    /**
     * Gets the payees
     * @return List of payees
     */
    public static List<Payee> getPayees() {
        log.entering(CLASS_NAME, "getPayees");

        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        @SuppressWarnings("unchecked")
        List<Payee> payees = (List<Payee>) s.createQuery("from Payee order by name").list();
        t.commit();

        log.exiting(CLASS_NAME, "getPayees", payees);
        return payees;
    }

    /**
     * Gets the payees with their IDs
     * @return Map of payees: the key is the payee ID, the value is the payee itself
     */
    public static Map<Long, Payee> getPayeesWithId() {
        log.entering(CLASS_NAME, "getPayeesWithId");

        Map<Long, Payee> payeesWithId = new HashMap<Long, Payee>();
        List<Payee> payees = getPayees();

        for (Payee payee : payees) {
            payeesWithId.put(payee.getId(), payee);
        }

        log.exiting(CLASS_NAME, "getPayeesWithId", payeesWithId);
        return payeesWithId;
    }

    /**
     * Gets the accounts
     * @return List of accounts
     */
    public static List<Account> getAccounts() {
        log.entering(CLASS_NAME, "getAccounts");

        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        @SuppressWarnings("unchecked")
        List<Account> accounts = (List<Account>) s.createQuery("from Account order by name").list();
        t.commit();

        log.exiting(CLASS_NAME, "getAccounts", accounts);
        return accounts;
    }

    /**
     * Gets the active accounts
     * @return List of active accounts
     */
    public static List<Account> getActiveAccounts() {
        log.entering(CLASS_NAME, "getActiveAccounts");

        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        @SuppressWarnings("unchecked")
        List<Account> activeAccounts = (List<Account>) s.createQuery("from Account where active=true order by name").list();
        t.commit();

        log.exiting(CLASS_NAME, "getActiveAccounts", activeAccounts);
        return activeAccounts;
    }

    /**
     * Gets the accounts with their IDs
     * @return Map of accounts: the key is the account ID, the value is the account itself
     */
    public static Map<Long, Account> getAccountsWithId() {
        log.entering(CLASS_NAME, "getAccountsWithId");

        Map<Long, Account> accountsWithId = new HashMap<Long, Account>();
        List<Account> accounts = getAccounts();

        for (Account account : accounts) {
            accountsWithId.put(account.getId(), account);
        }

        log.exiting(CLASS_NAME, "getAccountsWithId", accountsWithId);
        return accountsWithId;
    }

    /**
     * Gets the categories
     * @return List of categories
     */
    public static List<Category> getCategories() {
        log.entering(CLASS_NAME, "getCategories");

        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        @SuppressWarnings("unchecked")
        List<Category> categories = (List<Category>) s.createQuery("from Category order by name").list();
        t.commit();

        log.exiting(CLASS_NAME, "getCategories", categories);
        return categories;
    }

    /**
     * Gets the sub-categories of a category
     * @param category Category for which the sub-categories are wanted
     * @return Sub-categories of the category
     */
    public static List<Category> getSubCategories(Category category) {
        log.entering(CLASS_NAME, "getSubCategories", category);
        if (category == null) {
            throw new IllegalArgumentException("The parameter 'category' is null");
        }

        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        @SuppressWarnings("unchecked")
        List<Category> subCategories = (List<Category>) s.createQuery("from Category where parentCategory=:category").
                setEntity("category", category).list();
        t.commit();

        log.exiting(CLASS_NAME, "getSubCategories", subCategories);
        return subCategories;
    }

    /**
     * Gets the active accounts of a currency
     * @param currency Currency for which the active accounts are wanted
     * @return Active accounts of the currency
     */
    public static List<Account> getActiveAccounts(Currency currency) {
        log.entering(CLASS_NAME, "getActiveAccounts", currency);
        if (currency == null) {
            throw new IllegalArgumentException("The parameter 'currency' is null");
        }

        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        @SuppressWarnings("unchecked")
        List<Account> activeAccounts = (List<Account>) s.createQuery("from Account where active=true and currency=:currency").
                setEntity("currency", currency).list();
        t.commit();

        log.exiting(CLASS_NAME, "getActiveAccounts", activeAccounts);
        return activeAccounts;
    }

    /**
     * Gets the available top-categories
     * @return List of top categories
     */
    public static List<Category> getTopCategories() {
        log.entering(CLASS_NAME, "getTopCategories");

        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        @SuppressWarnings("unchecked")
        List<Category> topCategories = (List<Category>) s.createQuery("from Category " +
                "where parentCategory is null " +
                "order by name").list();
        t.commit();

        log.exiting(CLASS_NAME, "getTopCategories", topCategories);
        return topCategories;
    }

    /**
     * Gets the categories with their Grisbi Category
     * @return Map of categories: the key is the Grisbi Category ID, the value is the category itself
     */
    public static Map<GrisbiCategory, Category> getCategoriesWithGrisbiCategory() {
        log.entering(CLASS_NAME, "getCategoriesWithGrisbiCategory");

        Map<GrisbiCategory, Category> categoriesWithGrisbiCategory = new HashMap<GrisbiCategory, Category>(); // Collection to return that contains the categories - the key is the Grisbi category
        List<Category> categories = getCategories();

        for (Category category : categories) {
            categoriesWithGrisbiCategory.put(new GrisbiCategory(category.getGrisbiCategoryId(), category.getGrisbiSubCategoryId()), category);
        }

        log.exiting(CLASS_NAME, "getCategoriesWithGrisbiCategory", categoriesWithGrisbiCategory);
        return categoriesWithGrisbiCategory;
    }

    /**
     * Gets the categories with their IDs
     * @return Map of categories: the key is the category ID, the value is the category itself
     */
    public static Map<Long, Category> getCategoriesWithId() {
        log.entering(CLASS_NAME, "getCategoriesWithId");

        Map<Long, Category> categoriesWithId = new HashMap<Long, Category>();
        List<Category> categories = getCategories();

        for (Category category : categories) {
            categoriesWithId.put(category.getId(), category);
        }

        log.exiting(CLASS_NAME, "getCategoriesWithId", categoriesWithId);
        return categoriesWithId;
    }

    /**
     * Gets the transactions that belong to an account<BR/>
     * The list contains only transactions (no sub-transaction)
     * @param account Account for which the transactions are wanted
     * @return List of transactions that belong to the account
     */
    public static List<gg.db.entities.Transaction> getAccountTransactions(Account account) {
        log.entering(CLASS_NAME, "getAccountTransactions", account);
        if (account == null) {
            throw new IllegalArgumentException("The parameter 'account' is null");
        }
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        @SuppressWarnings("unchecked")
        List<gg.db.entities.Transaction> transactions = (List<gg.db.entities.Transaction>) s.createQuery("from Transaction " +
                "where account=:account " +
                "and parentTransaction is null " +
                "order by date").setEntity("account", account).list();
        t.commit();

        log.exiting(CLASS_NAME, "getAccountTransactions", transactions);
        return transactions;
    }

    /**
     * Gets the total balance of an account<BR/>
     * The amounts of all transactions are summed<BR/>
     * To have the current balance of the account, the initial amount must be added to the total balance
     * @param account Account for which the total balance must be computed
     * @return Total balance in the currency of the account
     */
    public static BigDecimal getAccountTotalBalance(Account account) {
        log.entering(CLASS_NAME, "getAccountTotalBalance", account);
        if (account == null) {
            throw new IllegalArgumentException("The parameter 'account' is null");
        }
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        Query query = s.createQuery("select sum(amount) " +
                "from Transaction " +
                "where account=:account and parentTransaction is null").setEntity("account", account);
        BigDecimal totalBalance = (BigDecimal) query.uniqueResult();
        if (totalBalance == null) {
            totalBalance = new BigDecimal(0);
        }
        t.commit();

        log.exiting(CLASS_NAME, "getAccountTotalBalance", totalBalance);
        return totalBalance;
    }

    /**
     * Gets the list of transactions that belong to a currency<BR/>
     * The list contains only transactions (no sub-transaction)
     * @param currency Currency for which the transactions are wanted
     * @return List of transactions that belong to the currency
     */
    public static List<gg.db.entities.Transaction> getCurrencyTransactions(Currency currency) {
        log.entering(CLASS_NAME, "getCurrencyTransactions", currency);
        if (currency == null) {
            throw new IllegalArgumentException("The parameter 'currency' is null");
        }
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        Query query = s.createQuery("select t " +
                "from Transaction t inner join t.account as a " +
                "where t.parentTransaction is null and " +
                "a.currency=:currency and " +
                "a.active=true " +
                "order by t.date").setEntity("currency", currency);
        @SuppressWarnings("unchecked")
        List<gg.db.entities.Transaction> transactions = (List<gg.db.entities.Transaction>) query.list();
        t.commit();

        log.exiting(CLASS_NAME, "getCurrencyTransactions", transactions);
        return transactions;
    }

    /**
     * Gets the list of transactions that meet a search criteria
     * @param searchCriteria Search criteria
     * @return List of transactions
     */
    public static List<gg.db.entities.Transaction> getTransactions(SearchCriteria searchCriteria) {
        log.entering(CLASS_NAME, "getTransactions", searchCriteria);
        if (searchCriteria == null) {
            throw new IllegalArgumentException("The parameter 'searchCriteria' is null");
        }

        Session session = Installer.createSession();
        Transaction tx = session.beginTransaction();

        Query query = getQuery(session,
                searchCriteria,
                true, // Search from start
                true, // Search until end
                true, // Filter on categories
                true,
                true,
                "select t",
                null,
                ""); // No group by
        // Execute the query
        @SuppressWarnings("unchecked")
        List<gg.db.entities.Transaction> transactions = (List<gg.db.entities.Transaction>) query.list();

        session.flush();
        tx.commit();
        session.close();

        log.exiting(CLASS_NAME, "getTransactions", transactions);
        return transactions;
    }

    /**
     * Gets the total balance of a currency (sum of balances of the active accounts that belong to the currency)
     * @param currency Currency for which the total balance must be computed
     * @return Total balance
     */
    public static BigDecimal getCurrencyTotalBalance(Currency currency) {
        log.entering(CLASS_NAME, "getCurrencyTotalBalance", currency);
        if (currency == null) {
            throw new IllegalArgumentException("The parameter 'currency' is null");
        }
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        Query query = s.createQuery("select sum(balance) " +
                "from Account " +
                "where currency=:currency and " +
                "active=true").setEntity("currency", currency);
        BigDecimal totalBalance = (BigDecimal) query.uniqueResult();
        if (totalBalance == null) {
            totalBalance = new BigDecimal(0);
        }
        t.commit();

        log.exiting(CLASS_NAME, "getCurrencyTotalBalance", totalBalance);
        return totalBalance;
    }

    /**
     * Gets a category based on a Grisbi category ID and a grisbi sub-category ID
     * @param grisbiCategoryId Grisbi category ID
     * @param grisbiSubCategoryId Grisbi sub-category ID
     * @return Category
     */
    public static Category getCategory(Long grisbiCategoryId, Long grisbiSubCategoryId) {
        log.entering(CLASS_NAME, "getCategory", new Object[]{grisbiCategoryId, grisbiSubCategoryId});
        if (grisbiCategoryId == null) {
            throw new IllegalArgumentException("The parameter 'grisbiCategoryId' is null");
        }
        if (grisbiSubCategoryId == null) {
            throw new IllegalArgumentException("The parameter 'grisbiSubCategoryId' is null");
        }
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        Query query = s.createQuery("from Category " +
                "where grisbiCategoryId=:grisbiCategoryId and " +
                "grisbiSubCategoryId=:grisbiSubCategoryId").
                setParameter("grisbiCategoryId", grisbiCategoryId).
                setParameter("grisbiSubCategoryId", grisbiSubCategoryId);
        Category category = (Category) query.uniqueResult();
        t.commit();

        log.exiting(CLASS_NAME, "getCategory", category);
        return category;
    }

    /**
     * Gets a query object
     * @param session Database session
     * @param searchCriteria Search criteria
     * @param searchFromStartDate Search from start? (if false, no filter on start date)
     * @param searchUntilEndDate Search until end? (if false, no filter on end date)
     * @param filterOnCategories Filter on categories?
     * @param select Select statement
     * @param where Where statement
     * @param groupBy Group by statement
     * @return Query object
     */
    private static Query getQuery(Session session, SearchCriteria searchCriteria, boolean searchFromStartDate,
            boolean searchUntilEndDate, boolean filterOnCategories, boolean filterOnPayees,
            boolean filterOnKeywords, String select, String where, String groupBy) {
        log.entering(CLASS_NAME, "getQuery", new Object[]{searchCriteria, searchFromStartDate, searchUntilEndDate, filterOnCategories, filterOnPayees, filterOnKeywords, select, where, groupBy});
        if (searchCriteria == null) {
            throw new IllegalArgumentException("The parameter 'searchCriteria' is null");
        }

        Category transferCategory = getCategory(
                Category.TRANSFER.getGrisbiCategoryId(),
                Category.TRANSFER.getGrisbiSubCategoryId());
        String queryString = "";

        // SELECT clause
        if (select != null) {
            queryString = select + " ";
        }

        // FROM clause
        String fromClause = "from Transaction t inner join t.account as a";

        // WHERE clause
        List<String> whereClause = new ArrayList<String>();
        whereClause.add("a.active=true");
        if (searchCriteria.hasAccountsFilter()) {
            whereClause.add("t.account in (:accounts)");
        } else if (searchCriteria.hasCurrencyFilter()) {
            whereClause.add("a.currency=:currency");
        }

        whereClause.add("t.parentTransaction is null");
        if (where != null) {
            whereClause.add(where);
        }
        if (searchCriteria.hasPeriodFilter() && searchFromStartDate) {
            whereClause.add("t.date>=:start");
        }
        if (searchCriteria.hasPeriodFilter() && searchUntilEndDate) {
            whereClause.add("t.date<=:end");
        }
        if (searchCriteria.hasCategoriesFilter() && filterOnCategories) {
            whereClause.add("t.category in (:categories)");
        }
        if (searchCriteria.hasPayeesFilter() && filterOnPayees) {
            whereClause.add("t.payee in (:payees)");
        }
        if (!searchCriteria.isIncludeTransferTransactions()) {
            whereClause.add("t.category<>:categoryTransfer");
        }
        if (searchCriteria.hasKeywordsFilter() && filterOnKeywords) {
            whereClause.add("upper(t.comment) like :keyword");
        }

        // Compute the WHERE statement
        queryString += fromClause;
        if (whereClause.size() > 0) {
            queryString += " where ";
        }
        Iterator<String> it = whereClause.iterator();
        String whereItem;
        while (it.hasNext()) {
            whereItem = it.next();
            queryString += whereItem;

            if (it.hasNext()) {
                queryString += " and ";
            }
        }

        // Group by statement
        queryString += " " + groupBy;

        // Create query
        Query query = session.createQuery(queryString);

        // Add the entities to the query
        if (searchCriteria.hasAccountsFilter()) {
            query.setParameterList("accounts", searchCriteria.getAccounts());
        } else if (searchCriteria.hasCurrencyFilter()) {
            query.setParameter("currency", searchCriteria.getCurrency());
        }
        if (searchCriteria.hasPeriodFilter() && searchFromStartDate) {
            query.setParameter("start", searchCriteria.getPeriod().getStart());
        }
        if (searchCriteria.hasPeriodFilter() && searchUntilEndDate) {
            query.setParameter("end", searchCriteria.getPeriod().getEnd());
        }
        if (searchCriteria.hasCategoriesFilter() && filterOnCategories) {
            query.setParameterList("categories", searchCriteria.getCategories());
        }
        if (searchCriteria.hasPayeesFilter() && filterOnPayees) {
            query.setParameterList("payees", searchCriteria.getPayees());
        }
        if (!searchCriteria.isIncludeTransferTransactions()) {
            query.setParameter("categoryTransfer", transferCategory);
        }
        if (searchCriteria.hasKeywordsFilter() && filterOnKeywords) {
            query.setParameter("keyword", "%" + searchCriteria.getKeywords().toUpperCase() + "%");
        }

        log.exiting(CLASS_NAME, "getQuery", query.getQueryString());
        return query;
    }

    /**
     * Gets an income total based on a search criteria
     * @param searchCriteria Search criteria
     * @return Income
     */
    public static BigDecimal getIncome(SearchCriteria searchCriteria) {
        log.entering(CLASS_NAME, "getIncome", searchCriteria);
        if (searchCriteria == null) {
            throw new IllegalArgumentException("The parameter 'searchCriteria' is null");
        }

        Session session = Installer.createSession();
        Transaction tx = session.beginTransaction();

        Query query = getQuery(session,
                searchCriteria,
                true, // Search from start
                true, // Search until end
                false, // No filter on categories
                false, // No filter on payees
                false, // No filter on keywords
                "select sum(t.amount)",
                "t.amount>0", // Search only incomes
                ""); // No group by

        // Execute the query
        BigDecimal income = (BigDecimal) query.uniqueResult();
        if (income == null) {
            income = new BigDecimal(0);
        }

        session.flush();
        tx.commit();
        session.close();

        log.exiting(CLASS_NAME, "getIncome", income);
        return income;
    }

    /**
     * Gets the expenses total based on a search criteria
     * @param searchCriteria Search criteria
     * @return Expenses
     */
    public static BigDecimal getExpenses(SearchCriteria searchCriteria) {
        log.entering(CLASS_NAME, "getExpenses", searchCriteria);
        if (searchCriteria == null) {
            throw new IllegalArgumentException("The parameter 'searchCriteria' is null");
        }
        Session session = Installer.createSession();
        Transaction tx = session.beginTransaction();

        Query query = getQuery(session,
                searchCriteria,
                true, // Search from start
                true, // Search until end
                false, // No filter on categories
                false, // No filter on payees
                false, // No filter on keywords
                "select sum(t.amount)",
                "t.amount<0", // Search only expenses
                ""); // No group by

        // Execute the query
        BigDecimal expenses = (BigDecimal) query.uniqueResult();
        if (expenses == null) {
            expenses = new BigDecimal(0);
        }

        session.flush();
        tx.commit();
        session.close();

        log.exiting(CLASS_NAME, "getExpenses", expenses);
        return expenses;
    }

    /**
     * Gets the balance corresponding to a search criteria
     * @param searchCriteria Search criteria
     * @return Balance
     */
    public static BigDecimal getBalance(SearchCriteria searchCriteria) {
        log.entering(CLASS_NAME, "getBalance", searchCriteria);
        if (searchCriteria == null) {
            throw new IllegalArgumentException("The parameter 'searchCriteria' is null");
        }
        Session session = Installer.createSession();
        Transaction tx = session.beginTransaction();

        Query query = getQuery(session,
                searchCriteria,
                true, // Search from start
                true, // Search until end
                false, // No filter on categories
                false, // No filter on payees
                false, // No filter on keywords
                "select sum(t.amount)",
                null, // No specific where clause
                ""); // No group by

        // Execute the query
        BigDecimal balance = (BigDecimal) query.uniqueResult();
        if (balance == null) {
            balance = new BigDecimal(0);
        }
        session.flush();
        tx.commit();
        session.close();

        log.exiting(CLASS_NAME, "getBalance", balance);
        return balance;
    }

    /**
     * Gets the balance corresponding to a search criteria (until a certain date)
     * @param searchCriteria Search criteria
     * @return Balance
     */
    public static BigDecimal getBalanceUntil(SearchCriteria searchCriteria) {
        log.entering(CLASS_NAME, "getBalance", searchCriteria);
        if (searchCriteria == null) {
            throw new IllegalArgumentException("The parameter 'searchCriteria' is null");
        }
        Session session = Installer.createSession();
        Transaction tx = session.beginTransaction();

        Query query = getQuery(session,
                searchCriteria,
                false, // From date is not taken into account
                true, // Search until end
                false, // No filter on categories
                false, // No filter on payees
                false, // No filter on keywords
                "select sum(t.amount)",
                null, // No specific where clause
                ""); // No group by

        // Execute the query
        BigDecimal balance = (BigDecimal) query.uniqueResult();
        if (balance == null) {
            balance = new BigDecimal(0);
        }
        session.flush();
        tx.commit();
        session.close();

        log.exiting(CLASS_NAME, "getBalance", balance);
        return balance;
    }

    /**
     * Gets the balances for categories and sub-categories
     * @param searchCriteria Search criteria
     * @return List of category balances: each item of the list is an array.
     * The first element of the array is the category ID.
     * The second element of the array is the corresponding category balance.
     */
    public static List getCategoriesBalances(SearchCriteria searchCriteria) {
        log.entering(CLASS_NAME, "getCategoriesBalances", searchCriteria);
        if (searchCriteria == null) {
            throw new IllegalArgumentException("The parameter 'searchCriteria' is null");
        }
        Session session = Installer.createSession();
        Transaction tx = session.beginTransaction();

        Query query = getQuery(session,
                searchCriteria,
                true, // Search from start
                true, // Search until end
                false, // No filter on categories (all categories are expected)
                false, // No filter on payees
                false, // No filter on keywords
                "select t.category.id, sum(t.amount)",
                null, // No specific where clause
                "group by t.category.id");

        // Execute the query
        List categoriesBalances = query.list();

        session.flush();
        tx.commit();
        session.close();

        log.exiting(CLASS_NAME, "getCategoriesBalances", categoriesBalances);
        return categoriesBalances;
    }

    /**
     * Saves a file import log
     * @param fileImport New log to insert
     */
    public static void saveFileImport(FileImport fileImport) {
        log.entering(CLASS_NAME, "saveFileImport", fileImport);
        if (fileImport == null) {
            throw new IllegalArgumentException("The parameter 'fileImport' is null");
        }
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        s.save(fileImport);
        t.commit();

        log.exiting(CLASS_NAME, "saveFileImport");
    }

    /**
     * Saves a payee
     * @param payee Payee to save
     */
    public static void savePayee(Payee payee) {
        log.entering(CLASS_NAME, "savePayee", payee);
        if (payee == null) {
            throw new IllegalArgumentException("The parameter 'payee' is null");
        }
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        s.save(payee);
        t.commit();
        log.exiting(CLASS_NAME, "savePayee");
    }

    /**
     * Saves a category
     * @param category Category to save
     */
    public static void saveCategory(Category category) {
        log.entering(CLASS_NAME, "saveCategory", category);
        if (category == null) {
            throw new IllegalArgumentException("The parameter 'category' is null");
        }
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        s.save(category);
        t.commit();
        log.exiting(CLASS_NAME, "saveCategory");
    }

    /**
     * Saves a currency
     * @param currency Currency to save
     */
    public static void saveCurrency(Currency currency) {
        log.entering(CLASS_NAME, "saveCurrency", currency);
        if (currency == null) {
            throw new IllegalArgumentException("The parameter 'currency' is null");
        }
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        s.saveOrUpdate(currency);
        t.commit();
        log.exiting(CLASS_NAME, "saveCurrency");
    }

    /**
     * Saves an account
     * @param account Account to save
     */
    public static void saveAccount(Account account) {
        log.entering(CLASS_NAME, "saveAccount", account);
        if (account == null) {
            throw new IllegalArgumentException("The parameter 'account' is null");
        }
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        s.save(account);
        t.commit();
        log.exiting(CLASS_NAME, "saveAccount");
    }

    /**
     * Saves a transaction
     * @param transaction Transaction to save
     */
    public static void saveTransaction(gg.db.entities.Transaction transaction) {
        log.entering(CLASS_NAME, "saveTransaction", transaction);
        if (transaction == null) {
            throw new IllegalArgumentException("The parameter 'transaction' is null");
        }
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        s.save(transaction);
        t.commit();
        log.exiting(CLASS_NAME, "saveTransaction");
    }

    /** Empties the database */
    public static void emptyDatabase() {
        log.entering(CLASS_NAME, "emptyDatabase");
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        s.createQuery("delete Transaction").executeUpdate();
        s.createQuery("delete Category").executeUpdate();
        s.createQuery("delete Account").executeUpdate();
        s.createQuery("delete Currency").executeUpdate();
        s.createQuery("delete Payee").executeUpdate();
        t.commit();
        log.exiting(CLASS_NAME, "emptyDatabase");
    }
}
