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
 * <b>Datamodel</b>
 * <ul>
 * <li>Enable GrisbiGraphs to communicate with the embedded Derby database</li>
 * </ul>
 * @author Francois Duchemin
 */
public class Datamodel {

    /**
     * Gets the file imports log
     * @return List of Grisbi file imports
     */
    @SuppressWarnings("unchecked")
    public static List<FileImport> getFileImports() {
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        List<FileImport> list = (List<FileImport>) s.createQuery("from FileImport f order by f.importedOn desc").list();
        t.commit();

        return list;
    }

    /**
     * Gets the available currencies
     * @return List of currencies
     */
    @SuppressWarnings("unchecked")
    public static List<Currency> getCurrencies() {
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        List<Currency> list = (List<Currency>) s.createQuery("from Currency order by name").list();
        t.commit();

        return list;
    }

    /**
     * Gets the available currencies together with their IDs
     * @return List of currencies with their IDs
     */
    public static Map<Long, Currency> getCurrenciesWithId() {
        Map<Long, Currency> currencies = new HashMap<Long, Currency>(); // Collection to return that contains the currencies - the key is the Currency ID
        List<Currency> list = getCurrencies();

        for (Currency currency : list) {
            currencies.put(currency.getId(), currency);
        }

        return currencies;
    }

    /**
     * Gets the active currencies
     * @return List of active currencies
     */
    @SuppressWarnings("unchecked")
    public static List<Currency> getActiveCurrencies() {
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        List<Currency> list = (List<Currency>) s.createQuery("from Currency c where c.active=true order by name").list();
        t.commit();

        return list;
    }

    /**
     * Gets the available payees
     * @return List of payees
     */
    @SuppressWarnings("unchecked")
    public static List<Payee> getPayees() {
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        List<Payee> list = (List<Payee>) s.createQuery("from Payee order by name").list();
        t.commit();

        return list;
    }

    /**
     * Gets the available payees togetheir with their IDs
     * @return List of payees with their IDs
     */
    public static Map<Long, Payee> getPayeesWithId() {
        Map<Long, Payee> payees = new HashMap<Long, Payee>(); // Collection to return that contains the payees - the key is the Payee ID
        List<Payee> list = getPayees();

        for (Payee payee : list) {
            payees.put(payee.getId(), payee);
        }

        return payees;
    }

    /**
     * Gets the available accounts
     * @return List of accounts
     */
    @SuppressWarnings("unchecked")
    public static List<Account> getAccounts() {
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        List<Account> list = (List<Account>) s.createQuery("from Account a order by name").list();
        t.commit();

        return list;
    }

    /**
     * Gets the active accounts
     * @return List of active accounts
     */
    @SuppressWarnings("unchecked")
    public static List<Account> getActiveAccounts() {
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        List<Account> list = (List<Account>) s.createQuery("from Account a where a.active=true order by name").list();
        t.commit();

        return list;
    }

    /**
     * Gets the available accounts together with their IDs
     * @return List of accounts with their IDs
     */
    public static Map<Long, Account> getAccountsWithId() {
        Map<Long, Account> accounts = new HashMap<Long, Account>(); // Collection to return that contains the accounts - the key is the Account ID
        List<Account> list = getAccounts();

        for (Account account : list) {
            accounts.put(account.getId(), account);
        }

        return accounts;
    }

    /**
     * Gets the available categories
     * @return List of categories
     */
    @SuppressWarnings("unchecked")
    public static List<Category> getCategories() {
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        List<Category> list = (List<Category>) s.createQuery("from Category c " +
                "order by c.name").list();
        t.commit();

        return list;
    }

    /**
     * Gets the sub-categories of a category
     * @param category Category for which the sub-categories are wanted
     * @return Sub-categories of the category
     */
    @SuppressWarnings("unchecked")
    public static List<Category> getSubCategories(Category category) {
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        Query query = s.createQuery("from Category c " +
                "where c.parentCategory=:category").setEntity("category", category);
        List<Category> list = (List<Category>) query.list();
        t.commit();

        return list;
    }

    @SuppressWarnings("unchecked")
    public static List<Account> getAccounts(Currency currency) {
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        Query query = s.createQuery("from Account a " +
                "where a.currency=:currency").setEntity("currency", currency);
        List<Account> list = (List<Account>) query.list();
        t.commit();

        return list;
    }

    @SuppressWarnings("unchecked")
    public static List<Account> getActiveAccounts(Currency currency) {
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        Query query = s.createQuery("from Account a " +
                "where a.active=true and a.currency=:currency").setEntity("currency", currency);
        List<Account> list = (List<Account>) query.list();
        t.commit();

        return list;
    }

    /**
     * Gets the available top-categories
     * @return List of top categories
     */
    @SuppressWarnings("unchecked")
    public static List<Category> getTopCategories() {
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        List<Category> list = (List<Category>) s.createQuery("from Category c " +
                "where c.parentCategory is null " +
                "order by c.name").list();
        t.commit();

        return list;
    }

    /**
     * Gets the available categories together with their IDs<BR/>
     * A category ID is a GrisbiCategory object<BR/>
     * It's an object that contains the Grisbi category ID and Grisbi sub-category ID
     * @return List of categories with their IDs
     */
    public static Map<GrisbiCategory, Category> getCategoriesWithGrisbiCategory() {
        Map<GrisbiCategory, Category> categories = new HashMap<GrisbiCategory, Category>(); // Collection to return that contains the categories - the key is the Grisbi category
        List<Category> list = getCategories();

        for (Category category : list) {
            categories.put(new GrisbiCategory(category.getGrisbiCategoryId(), category.getGrisbiSubCategoryId()), category);
        }

        return categories;
    }

    public static Map<Long, Category> getCategoriesWithId() {
        Map<Long, Category> categories = new HashMap<Long, Category>();
        List<Category> list = getCategories();

        for (Category category : list) {
            categories.put(category.getId(), category);
        }

        return categories;
    }

    /**
     * Gets the list of transactions that belong to an account<BR/>
     * The list contains only transactions (no sub-transaction)
     * @param account Account for which the transactions must be retrieved
     * @return List of transactions that belong to the account
     */
    @SuppressWarnings("unchecked")
    public static List<gg.db.entities.Transaction> getAccountTransactions(Account account) {
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        List<gg.db.entities.Transaction> list = (List<gg.db.entities.Transaction>) s.createQuery("from Transaction t " +
                "where t.account=:account " +
                "and t.parentTransaction is null " +
                "order by t.date").setEntity("account", account).list();
        t.commit();

        return list;
    }

    /**
     * Gets the total balance of an account<BR/>
     * The amounts of all transactions are summed<BR/>
     * To have the current balance of the account, the initial amount must be added to the total balance
     * @param account Account for which the total balance must be computed
     * @return Total balance in the currency of the account
     */
    public static BigDecimal getAccountTotalBalance(Account account) {
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        Query query = s.createQuery("select sum(amount) " +
                "from Transaction t " +
                "where t.account=:account and t.parentTransaction is null").setEntity("account", account);
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
     * @param currency Currency for which the transactions must be retrieved
     * @return List of transactions that belong to the currency
     */
    @SuppressWarnings("unchecked")
    public static List<gg.db.entities.Transaction> getCurrencyTransactions(Currency currency) {
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        Query query = s.createQuery("select t " +
                "from Transaction t inner join t.account as a " +
                "where t.parentTransaction is null and " +
                "a.currency=:currency and " +
                "a.active=true " +
                "order by t.date").setEntity("currency", currency);
        List<gg.db.entities.Transaction> list = (List<gg.db.entities.Transaction>) query.list();
        t.commit();

        return list;
    }

    /**
     * Gets the sub-transactions of a transaction
     * @param transaction Transaction for which the sub-transactions are wanted
     * @return Sub-transactions of the transaction
     */
    @SuppressWarnings("unchecked")
    public static List<gg.db.entities.Transaction> getSubTransactions(gg.db.entities.Transaction transaction) {
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        Query query = s.createQuery("from Transaction t " +
                "where t.parentTransaction=:transaction").setEntity("transaction", transaction);
        List<gg.db.entities.Transaction> list = (List<gg.db.entities.Transaction>) query.list();
        t.commit();

        return list;
    }

    /**
     * Gets the total balance of a currency
     * @param currency Currency for which the total balance must be computed
     * @return Total balance
     */
    public static BigDecimal getCurrencyTotalBalance(Currency currency) {
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        Query query = s.createQuery("select sum(balance) " +
                "from Account a " +
                "where a.currency=:currency and " +
                "a.active=true").setEntity("currency", currency);
        BigDecimal totalBalance = (BigDecimal) query.uniqueResult();
        if (totalBalance == null) {
            totalBalance = new BigDecimal(0);
        }
        t.commit();

        return totalBalance;
    }

    /**
     * Gets a category
     * @param grisbiCategoryId Grisbi category ID
     * @param grisbiSubCategoryId Grisbi sub-category ID
     * @return Category
     */
    public static Category getCategory(Long grisbiCategoryId, Long grisbiSubCategoryId) {
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        Query query = s.createQuery("from Category c " +
                "where c.grisbiCategoryId=:grisbiCategoryId and " +
                "c.grisbiSubCategoryId=:grisbiSubCategoryId").setParameter("grisbiCategoryId", grisbiCategoryId).setParameter("grisbiSubCategoryId", grisbiSubCategoryId);
        Category category = (Category) query.uniqueResult();
        t.commit();

        return category;
    }

    private static Query getQuery(SearchFilter searchFilter, boolean searchFromStartDate,
            boolean searchUntilEndDate, String select, String where,
            String groupBy,
            boolean isIncludeSubCategories) {
        Category transferCategory = getCategory(Category.TRANSFER.getGrisbiCategoryId(), Category.TRANSFER.getGrisbiSubCategoryId());
        String queryString = "";
        String fromClause;
        List<String> whereClause = new ArrayList<String>();
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();

        whereClause.clear();

        // SELECT clause
        if (select != null) {
            queryString = select + " ";
        }

        // FROM clause
        fromClause = "from Transaction t inner join t.account as a";

        // WHERE clause
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
        if (searchFilter.hasCategoriesFilter()) {
            //whereClause.add("t.category in (:categories)");
        }
        if (searchFilter.hasPayeesFilter()) {
            whereClause.add("t.payee in (:payees)");
        }
        if (!searchFilter.isIncludeTransferTransactions()) {
            whereClause.add("t.category<>:categoryTransfer");
        }
        if (searchFilter.hasKeywordsFilter()) {
            // TODO: Add keyword filter
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
        queryString += groupBy;

        Query query = s.createQuery(queryString);

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
        if (searchFilter.hasCategoriesFilter()) {
            /*List<Category> categories = searchFilter.getCategories();
            if (isIncludeSubCategories) {
            List<Category> subCategories = new ArrayList<Category>();
            for (Category category : categories) {
            subCategories.addAll(category.getSubCategories());
            }
            categories.addAll(subCategories);
            }*/
            //query.setParameterList("categories", categories);
        }
        if (searchFilter.hasPayeesFilter()) {
            query.setParameterList("payees", searchFilter.getPayees());
        }
        if (!searchFilter.isIncludeTransferTransactions()) {
            query.setParameter("categoryTransfer", transferCategory);
        }
        if (searchFilter.hasKeywordsFilter()) {
            // TODO: Add keyword filter
        }

        return query;
    }

    /**
     * Gets an income
     * @param searchFilter Search filter
     * @return Income
     */
    public static BigDecimal getIncome(SearchFilter searchFilter) {
        Query query = getQuery(searchFilter, true, true, "select sum(t.amount)", "t.amount>0", "", false);
        //Session s = Installer.currentSession();
        //Transaction t = s.beginTransaction();

        // Execute the query
        BigDecimal income = (BigDecimal) query.uniqueResult();
        if (income == null) {
            income = new BigDecimal(0);
        }
        //t.commit();

        return income;
    }

    /**
     * Gets the expenses
     * @param searchFilter Search filter
     * @return Expenses
     */
    public static BigDecimal getExpenses(SearchFilter searchFilter) {
        Query query = getQuery(searchFilter, true, true, "select sum(t.amount)", "t.amount<0", "", false);
        //Session s = Installer.currentSession();
        //Transaction t = s.beginTransaction();

        // Execute the query
        BigDecimal expenses = (BigDecimal) query.uniqueResult();
        if (expenses == null) {
            expenses = new BigDecimal(0);
        }
        //t.commit();

        return expenses;
    }

    /**
     * Gets the balance
     * @param searchFilter Search filter
     * @return Balance
     */
    public static BigDecimal getBalance(SearchFilter searchFilter) {
        Query query = getQuery(searchFilter, true, true, "select sum(t.amount)", null, "", false);
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();

        // Execute the query
        BigDecimal balance = (BigDecimal) query.uniqueResult();
        if (balance == null) {
            balance = new BigDecimal(0);
        }
        t.commit();

        return balance;
    }

    /**
     * Gets the balance
     * @param searchFilter Search filter
     * @return Balance
     */
    public static BigDecimal getBalanceWithSubCategories(SearchFilter searchFilter) {
        Query query = getQuery(searchFilter, true, true, "select sum(t.amount)", null, "", true);
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();

        // Execute the query
        BigDecimal balance = (BigDecimal) query.uniqueResult();
        if (balance == null) {
            balance = new BigDecimal(0);
        }
        t.commit();

        return balance;
    }

    public static List getCategoriesBalances(SearchFilter searchFilter) {
        Query query = getQuery(searchFilter, true, true, "select t.category.id, sum(t.amount)", null, " group by t.category.id", false);
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();

        // Execute the query
        List list = query.list();
        t.commit();

        return list;
    }

    public static List getAccountsBalancesUntil(SearchFilter searchFilter) {
        Query query = getQuery(searchFilter, false, true, "select t.account.id, sum(t.amount)", null, " group by t.account.id", false);
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();

        // Execute the query
        List list = query.list();
        t.commit();

        return list;
    }

    /**
     * Gets the balance
     * @param searchFilter Search filter
     * @return Balance
     */
    public static BigDecimal getBalanceUntil(SearchFilter searchFilter) {
        Query query = getQuery(searchFilter, false, true, "select sum(t.amount)", null, "", false);
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();

        // Execute the query
        BigDecimal balance = (BigDecimal) query.uniqueResult();
        if (balance == null) {
            balance = new BigDecimal(0);
        }
        t.commit();

        return balance;
    }

    /**
     * Gets the transactions
     * @param searchFilter Search filter
     * @return Transactions
     */
    @SuppressWarnings("unchecked")
    public static List<gg.db.entities.Transaction> getTransactions(SearchFilter searchFilter) {
        Query query = getQuery(searchFilter, true, true, null, null, "", false);
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();

        // Execute the query
        List<gg.db.entities.Transaction> list = (List<gg.db.entities.Transaction>) query.list();
        t.commit();

        return list;
    }

    /**
     * Saves a file import log<BR/>
     * When a Grisbi file is successfuly imported into the embedded DB, a new FileImport entry is created
     * @param fileImport New log to insert
     */
    public static void saveFileImport(FileImport fileImport) {
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
        Session s = Installer.currentSession();
        Transaction t = s.beginTransaction();
        if (transaction.getCategory() == null) {
            System.out.println("transaction id=" + transaction.getId());
        }
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
