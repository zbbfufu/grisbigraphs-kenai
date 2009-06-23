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
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Enable GrisbiGraphs to communicate with the embedded Derby database
 * @author Francois Duchemin
 */
public class Datamodel {

    private Datamodel() {
    }

    /**
     * Gets the file imports log
     * @return List of Grisbi files imported in the embedded database
     */
    public static List<FileImport> getFileImports() {
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        @SuppressWarnings("unchecked")
        List<FileImport> fileImports = (List<FileImport>) s.createQuery("from FileImport order by importedOn desc").list();
        t.commit();

        return fileImports;
    }

    /**
     * Gets the currencies
     * @return List of currencies
     */
    public static List<Currency> getCurrencies() {
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        @SuppressWarnings("unchecked")
        List<Currency> currencies = (List<Currency>) s.createQuery("from Currency order by name").list();
        t.commit();

        return currencies;
    }

    /**
     * Gets the currencies with their IDs
     * @return Map of currencies: the key is the currency ID, the value is the currency itself
     */
    public static Map<Long, Currency> getCurrenciesWithId() {
        Map<Long, Currency> currenciesWithId = new HashMap<Long, Currency>();
        List<Currency> currencies = getCurrencies();

        for (Currency currency : currencies) {
            currenciesWithId.put(currency.getId(), currency);
        }

        return currenciesWithId;
    }

    /**
     * Gets the active currencies
     * @return List of active currencies
     */
    public static List<Currency> getActiveCurrencies() {
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        @SuppressWarnings("unchecked")
        List<Currency> activeCurrencies = (List<Currency>) s.createQuery("from Currency where active=true order by name").list();
        t.commit();

        return activeCurrencies;
    }

    /**
     * Gets the payees
     * @return List of payees
     */
    public static List<Payee> getPayees() {
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        @SuppressWarnings("unchecked")
        List<Payee> payees = (List<Payee>) s.createQuery("from Payee order by name").list();
        t.commit();

        return payees;
    }

    /**
     * Gets the payees with their IDs
     * @return Map of payees: the key is the payee ID, the value is the payee itself
     */
    public static Map<Long, Payee> getPayeesWithId() {
        Map<Long, Payee> payeesWithId = new HashMap<Long, Payee>();
        List<Payee> payees = getPayees();

        for (Payee payee : payees) {
            payeesWithId.put(payee.getId(), payee);
        }

        return payeesWithId;
    }

    /**
     * Gets the accounts
     * @return List of accounts
     */
    public static List<Account> getAccounts() {
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        @SuppressWarnings("unchecked")
        List<Account> accounts = (List<Account>) s.createQuery("from Account order by name").list();
        t.commit();

        return accounts;
    }

    /**
     * Gets the active accounts
     * @return List of active accounts
     */
    public static List<Account> getActiveAccounts() {
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        @SuppressWarnings("unchecked")
        List<Account> list = (List<Account>) s.createQuery("from Account where active=true order by name").list();
        t.commit();

        return list;
    }

    /**
     * Gets the accounts with their IDs
     * @return Map of accounts: the key is the account ID, the value is the account itself
     */
    public static Map<Long, Account> getAccountsWithId() {
        Map<Long, Account> accountsWithId = new HashMap<Long, Account>();
        List<Account> accounts = getAccounts();

        for (Account account : accounts) {
            accountsWithId.put(account.getId(), account);
        }

        return accountsWithId;
    }

    /**
     * Gets the categories
     * @return List of categories
     */
    public static List<Category> getCategories() {
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        @SuppressWarnings("unchecked")
        List<Category> categories = (List<Category>) s.createQuery("from Category order by name").list();
        t.commit();

        return categories;
    }

    /**
     * Gets the sub-categories of a category
     * @param category Category for which the sub-categories are wanted
     * @return Sub-categories of the category
     */
    public static List<Category> getSubCategories(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("The parameter 'category' is null");
        }

        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        @SuppressWarnings("unchecked")
        List<Category> subCategories = (List<Category>) s.createQuery("from Category where parentCategory=:category").
                setEntity("category", category).list();
        t.commit();

        return subCategories;
    }

    /**
     * Gets the active accounts of a currency
     * @param currency Currency for which the active accounts are wanted
     * @return Active accounts of the currency
     */
    public static List<Account> getActiveAccounts(Currency currency) {
        if (currency == null) {
            throw new IllegalArgumentException("The parameter 'currency' is null");
        }

        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        @SuppressWarnings("unchecked")
        List<Account> activeAccounts = (List<Account>) s.createQuery("from Account where active=true and currency=:currency").
                setEntity("currency", currency).list();
        t.commit();

        return activeAccounts;
    }

    /**
     * Gets the available top-categories
     * @return List of top categories
     */
    public static List<Category> getTopCategories() {
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        @SuppressWarnings("unchecked")
        List<Category> topCategories = (List<Category>) s.createQuery("from Category " +
                "where parentCategory is null " +
                "order by name").list();
        t.commit();

        return topCategories;
    }

    /**
     * Gets the categories with their Grisbi Category
     * @return Map of categories: the key is the Grisbi Category ID, the value is the category itself
     */
    public static Map<GrisbiCategory, Category> getCategoriesWithGrisbiCategory() {
        Map<GrisbiCategory, Category> categoriesWithGrisbiCategory = new HashMap<GrisbiCategory, Category>(); // Collection to return that contains the categories - the key is the Grisbi category
        List<Category> categories = getCategories();

        for (Category category : categories) {
            categoriesWithGrisbiCategory.put(new GrisbiCategory(category.getGrisbiCategoryId(), category.getGrisbiSubCategoryId()), category);
        }

        return categoriesWithGrisbiCategory;
    }

    /**
     * Gets the categories with their IDs
     * @return Map of categories: the key is the category ID, the value is the category itself
     */
    public static Map<Long, Category> getCategoriesWithId() {
        Map<Long, Category> categoriesWithId = new HashMap<Long, Category>();
        List<Category> categories = getCategories();

        for (Category category : categories) {
            categoriesWithId.put(category.getId(), category);
        }

        return categoriesWithId;
    }

    /**
     * Gets the transactions that belong to an account<BR/>
     * The list contains only transactions (no sub-transaction)
     * @param account Account for which the transactions are wanted
     * @return List of transactions that belong to the account
     */
    public static List<gg.db.entities.Transaction> getAccountTransactions(Account account) {
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

        return totalBalance;
    }

    /**
     * Gets the list of transactions that belong to a currency<BR/>
     * The list contains only transactions (no sub-transaction)
     * @param currency Currency for which the transactions are wanted
     * @return List of transactions that belong to the currency
     */
    public static List<gg.db.entities.Transaction> getCurrencyTransactions(Currency currency) {
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

        return transactions;
    }

    /**
     * Gets the list of transactions that meet a search filter
     * @param searchFilter Search filter
     * @return List of transactions
     */
    public static List<gg.db.entities.Transaction> getTransactions(SearchFilter searchFilter) {
        if (searchFilter == null) {
            throw new IllegalArgumentException("The parameter 'searchFilter' is null");
        }

        Session session = Installer.createSession();
        Transaction tx = session.beginTransaction();

        Query query = getQuery(session,
                searchFilter,
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

        return transactions;
    }

    /**
     * Gets the total balance of a currency (sum of balances of the active accounts that belong to the currency)
     * @param currency Currency for which the total balance must be computed
     * @return Total balance
     */
    public static BigDecimal getCurrencyTotalBalance(Currency currency) {
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

        return totalBalance;
    }

    /**
     * Gets a category based on a Grisbi category ID and a grisbi sub-category ID
     * @param grisbiCategoryId Grisbi category ID
     * @param grisbiSubCategoryId Grisbi sub-category ID
     * @return Category
     */
    public static Category getCategory(Long grisbiCategoryId, Long grisbiSubCategoryId) {
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

        return category;
    }

    /**
     * Gets a query object
     * @param session Database session
     * @param searchFilter Search filter
     * @param searchFromStartDate Search from start? (if false, no filter on start date)
     * @param searchUntilEndDate Search until end? (if false, no filter on end date)
     * @param filterOnCategories Filter on categories?
     * @param select Select statement
     * @param where Where statement
     * @param groupBy Group by statement
     * @return Query object
     */
    private static Query getQuery(Session session, SearchFilter searchFilter, boolean searchFromStartDate,
            boolean searchUntilEndDate, boolean filterOnCategories, boolean filterOnPayees,
            boolean filterOnKeywords, String select, String where, String groupBy) {
        if (searchFilter == null) {
            throw new IllegalArgumentException("The parameter 'searchFilter' is null");
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
        if (searchFilter.hasAccountsFilter()) {
            whereClause.add("t.account in (:accounts)");
        } else if (searchFilter.hasCurrencyFilter()) {
            whereClause.add("a.currency=:currency");
        }

        whereClause.add("t.parentTransaction is null");
        if (where != null) {
            whereClause.add(where);
        }
        if (searchFilter.hasPeriodFilter() && searchFromStartDate) {
            whereClause.add("t.date>=:start");
        }
        if (searchFilter.hasPeriodFilter() && searchUntilEndDate) {
            whereClause.add("t.date<=:end");
        }
        if (searchFilter.hasCategoriesFilter() && filterOnCategories) {
            whereClause.add("t.category in (:categories)");
        }
        if (searchFilter.hasPayeesFilter() && filterOnPayees) {
            whereClause.add("t.payee in (:payees)");
        }
        if (!searchFilter.isIncludeTransferTransactions()) {
            whereClause.add("t.category<>:categoryTransfer");
        }
        if (searchFilter.hasKeywordsFilter() && filterOnKeywords) {
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
        if (searchFilter.hasAccountsFilter()) {
            query.setParameterList("accounts", searchFilter.getAccounts());
        } else if (searchFilter.hasCurrencyFilter()) {
            query.setParameter("currency", searchFilter.getCurrency());
        }
        if (searchFilter.hasPeriodFilter() && searchFromStartDate) {
            query.setParameter("start", searchFilter.getPeriod().getStart());
        }
        if (searchFilter.hasPeriodFilter() && searchUntilEndDate) {
            query.setParameter("end", searchFilter.getPeriod().getEnd());
        }
        if (searchFilter.hasCategoriesFilter() && filterOnCategories) {
            query.setParameterList("categories", searchFilter.getCategories());
        }
        if (searchFilter.hasPayeesFilter() && filterOnPayees) {
            query.setParameterList("payees", searchFilter.getPayees());
        }
        if (!searchFilter.isIncludeTransferTransactions()) {
            query.setParameter("categoryTransfer", transferCategory);
        }
        if (searchFilter.hasKeywordsFilter() && filterOnKeywords) {
            query.setParameter("keyword", "%" + searchFilter.getKeywords().toUpperCase() + "%");
        }

        return query;
    }

    /**
     * Gets an income total based on a search filter
     * @param searchFilter Search filter
     * @return Income
     */
    public static BigDecimal getIncome(SearchFilter searchFilter) {
        if (searchFilter == null) {
            throw new IllegalArgumentException("The parameter 'searchFilter' is null");
        }

        Session session = Installer.createSession();
        Transaction tx = session.beginTransaction();

        Query query = getQuery(session,
                searchFilter,
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

        return income;
    }

    /**
     * Gets the expenses total based on a search filter
     * @param searchFilter Search filter
     * @return Expenses
     */
    public static BigDecimal getExpenses(SearchFilter searchFilter) {
        if (searchFilter == null) {
            throw new IllegalArgumentException("The parameter 'searchFilter' is null");
        }
        Session session = Installer.createSession();
        Transaction tx = session.beginTransaction();

        Query query = getQuery(session,
                searchFilter,
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

        return expenses;
    }

    /**
     * Gets the balance corresponding to a search filter
     * @param searchFilter Search filter
     * @return Balance
     */
    public static BigDecimal getBalance(SearchFilter searchFilter) {
        if (searchFilter == null) {
            throw new IllegalArgumentException("The parameter 'searchFilter' is null");
        }
        Session session = Installer.createSession();
        Transaction tx = session.beginTransaction();

        Query query = getQuery(session,
                searchFilter,
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

        return balance;
    }

    /**
     * Gets the balances for categories and sub-categories
     * @param searchFilter Search filter
     * @return List of category balances: each item of the list is an array.
     * The first element of the array is the category ID.
     * The second element of the array is the corresponding category balance.
     */
    public static List getCategoriesBalances(SearchFilter searchFilter) {
        if (searchFilter == null) {
            throw new IllegalArgumentException("The parameter 'searchFilter' is null");
        }
        Session session = Installer.createSession();
        Transaction tx = session.beginTransaction();

        Query query = getQuery(session,
                searchFilter,
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

        return categoriesBalances;
    }

    /**
     * Gets the balances for accounts until a date (from is not taken into account)
     * @param searchFilter Search filter
     * @return List of account balances: each item of the list is an array.
     * The first element of the array is the account ID.
     * The second element of the array is the corresponding account balance.
     */
    public static List getAccountsBalancesUntil(SearchFilter searchFilter) {
        if (searchFilter == null) {
            throw new IllegalArgumentException("The parameter 'searchFilter' is null");
        }
        Session session = Installer.createSession();
        Transaction tx = session.beginTransaction();

        Query query = getQuery(session,
                searchFilter,
                false, // from is not taken into account
                true, // Search until end
                false, // No filter on categories
                false, // No filter on payees
                false, // No filter on keywords
                "select t.account.id, sum(t.amount)",
                null, // No specific where clause
                "group by t.account.id");

        // Execute the query
        List accountsBalances = query.list();

        session.flush();
        tx.commit();
        session.close();

        return accountsBalances;
    }

    /**
     * Saves a file import log
     * @param fileImport New log to insert
     */
    public static void saveFileImport(FileImport fileImport) {
        if (fileImport == null) {
            throw new IllegalArgumentException("The parameter 'fileImport' is null");
        }
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        s.save(fileImport);
        t.commit();
    }

    /**
     * Saves a payee
     * @param payee Payee to save
     */
    public static void savePayee(Payee payee) {
        if (payee == null) {
            throw new IllegalArgumentException("The parameter 'payee' is null");
        }
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        s.save(payee);
        t.commit();
    }

    /**
     * Saves a category
     * @param category Category to save
     */
    public static void saveCategory(Category category) {
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        s.save(category);
        t.commit();
    }

    /**
     * Saves a currency
     * @param currency Currency to save
     */
    public static void saveCurrency(Currency currency) {
        if (currency == null) {
            throw new IllegalArgumentException("The parameter 'currency' is null");
        }
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        s.saveOrUpdate(currency);
        t.commit();
    }

    /**
     * Saves an account
     * @param account Account to save
     */
    public static void saveAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("The parameter 'account' is null");
        }
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        s.save(account);
        t.commit();
    }

    /**
     * Saves a transaction
     * @param transaction Transaction to save
     */
    public static void saveTransaction(gg.db.entities.Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("The parameter 'transaction' is null");
        }
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        s.save(transaction);
        t.commit();
    }

    /** Empties the database */
    public static void emptyDatabase() {
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        s.createQuery("delete Transaction").executeUpdate();
        s.createQuery("delete Category").executeUpdate();
        s.createQuery("delete Account").executeUpdate();
        s.createQuery("delete Currency").executeUpdate();
        s.createQuery("delete Payee").executeUpdate();
        t.commit();
    }
}
