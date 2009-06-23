/*
 * SearchFilter.java
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
import gg.db.entities.Payee;
import java.util.ArrayList;
import java.util.List;

/**
 * <B>SearchFilter</B>
 * <UL>
 * <LI>Criterion (Period of time, keywords, categories...), that permit to look for transactions</LI>
 * <LI>The boolean operator is 'or': if the search filter contains a list of two keywords, and if a transaction contains one of them,
 * then the transaction matches the filter</LI>
 * </UL>
 * @author Francois Duchemin
 */
public class SearchFilter implements Comparable<SearchFilter> {

    /** Currency (null if there is no filter on the currency) */
    private Currency currency;
    /**  List of accounts (empty if there is no filter on the accounts) */
    private List<Account> accounts;
    /** Period of time between 2 dates (null if there is no filter on the period) */
    private Period period;
    /** List of categories (empty if there is no filter on the categories) */
    private List<Category> categories;
    /** List of payees (empty if there is no filter on the payees) */
    private List<Payee> payees;
    /** Keywords (null if there is no filter on the keywords) */
    private String keywords;
    /** Include transfer transactions in the search? */
    private boolean includeTransferTransactions;

    /** Creates a new instance of SearchFilter that doesn't filter on anything */
    public SearchFilter() {
        setCurrency(null);
        setAccounts(null);
        setPeriod(null);
        setCategories(null);
        setKeywords(null);
        setPayees(null);
        setIncludeTransferTransactions(true);

        assert (!hasCurrencyFilter() && !hasAccountsFilter() && !hasPeriodFilter() &&
                !hasCategoriesFilter() && !hasPayeesFilter() && !hasKeywordsFilter());
    }

    /**
     * Creates a new instance of SearchFilter
     * @param currency Currency (can be null if there is no filter on the currency)
     * @param accounts List of accounts (can be null if there is no filter on the accounts)
     * @param period Period (can be null if there is no filter on the period)
     * @param categories List of categoroes (can be null if there is no filter on the categories)
     * @param payees List of payees (can be null if there is no filter on the payees)
     * @param keywords Keywords (can be null if there is no filter on the comments)
     * @param includeTransferTransactions Should the transactions of type "Transfer" be included in the search?
     */
    public SearchFilter(Currency currency, List<Account> accounts, Period period, List<Category> categories, List<Payee> payees, String keywords, boolean includeTransferTransactions) {
        setCurrency(currency);
        setAccounts(accounts);
        setPeriod(period);
        setCategories(categories);
        setPayees(payees);
        setKeywords(keywords);
        setIncludeTransferTransactions(includeTransferTransactions);
    }

    /**
     * Gets the filtered currency
     * @return Currency on which the result will be filtered (null if there is no filter on the currency)
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * Sets a filter on the currency
     * @param currency Currency on which the result will be filtered (null if there shall be no filter on the currency)
     */
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    /**
     * Gets the filtered accounts
     * @return List of accounts on which the result will be filtered (empty if there is no filter on the accounts)
     */
    public List<Account> getAccounts() {
        return accounts;
    }

    /**
     * Sets a filter on the accounts
     * @param accounts List of accounts (null if there shall be no filter on the accounts)
     */
    public void setAccounts(List<Account> accounts) {
        if (accounts == null) {
            this.accounts = new ArrayList<Account>();
        } else {
            this.accounts = accounts;
        }
    }

    /**
     * Gets the filtered period
     * @return Period on which the result will be filtered (null if there shall be no filter on the period)
     */
    public Period getPeriod() {
        return period;
    }

    /**
     * Sets a filter on the period
     * @param period Period on which the result will be filtered (null if there shall be no filter on the period)
     */
    public void setPeriod(Period period) {
        this.period = period;
    }

    /**
     * Gets the filtered categories
     * @return List of categories on which the result will be filtered (empty if there is no filter on the categories)
     */
    public List<Category> getCategories() {
        return categories;
    }

    /**
     * Sets a filter on the categories
     * @param categories List of categories (null if there shall be no filter on the categories)
     */
    public void setCategories(List<Category> categories) {
        if (categories == null) {
            this.categories = new ArrayList<Category>();
        } else {
            this.categories = categories;
        }
    }

    /**
     * Gets the filtered payees
     * @return List of payees on which the result will be filtered (empty if there is no filter on the payees)
     */
    public List<Payee> getPayees() {
        return payees;
    }

    /**
     * Sets a filter on the payees
     * @param payees List of payees (null if there shall be no filter on the payees)
     */
    public void setPayees(List<Payee> payees) {
        if (payees == null) {
            this.payees = new ArrayList<Payee>();
        } else {
            this.payees = payees;
        }
    }

    /**
     * Gets the filtered keywords
     * @return Keywords on which the result will be filtered (null if there is no filter on the keywords)
     */
    public String getKeywords() {
        return keywords;
    }

    /**
     * Sets a filter on the keywords
     * @param keywords Keywords (null if there shall be no filter on the keywords)
     */
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    /**
     * Are the transfer transactions included in the search?
     * @return true if the transactions of category "transfer" must be included in the search
     */
    public boolean isIncludeTransferTransactions() {
        return includeTransferTransactions;
    }

    /**
     * Should the transfer transactions be included in the search?
     * @param includeTransferTransactions true if the transfer transactions must be included in the search
     */
    public void setIncludeTransferTransactions(boolean includeTransferTransactions) {
        this.includeTransferTransactions = includeTransferTransactions;
    }

    /**
     * Is there a filter on a currency
     * @return true if there is a filter on a currency
     */
    public boolean hasCurrencyFilter() {
        return (currency != null);
    }

    /**
     * Is there a filter on accounts
     * @return true if there is a filter on accounts
     */
    public boolean hasAccountsFilter() {
        return (accounts.size() > 0);
    }

    /**
     * Is there a filter on a period
     * @return true if there is a filter on a period
     */
    public boolean hasPeriodFilter() {
        return (period != null);
    }

    /**
     * Is there a filter on categories
     * @return true if there is a filter on categories
     */
    public boolean hasCategoriesFilter() {
        return (categories.size() > 0);
    }

    /**
     * Is there a filter on payees
     * @return true if there is a filter on payees
     */
    public boolean hasPayeesFilter() {
        return (payees.size() > 0);
    }

    /**
     * Is there a filter on keywords
     * @return true if there is a filter on keywords
     */
    public boolean hasKeywordsFilter() {
        return (keywords != null);
    }

    @Override
    public int compareTo(SearchFilter searchFilter) {
        if (searchFilter == null) {
            throw new IllegalArgumentException("The parameter 'searchFilter' is null");
        }

        return period.compareTo(searchFilter.getPeriod());
    }
}
