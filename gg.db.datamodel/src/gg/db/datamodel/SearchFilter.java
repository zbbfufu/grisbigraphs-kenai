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
 * <LI>The list of categories does never contain a null category</LI>
 * <LI>The list of categories does never contain two categories having the same ID</LI>
 * <LI>The list of keywords does never contain a null keyword</LI>
 * <LI>The list of keywords does never contain two identic keywords</LI>
 * </UL>
 * @author Francois Duchemin
 */
public class SearchFilter implements Comparable<SearchFilter>{

    /** Currency (null if there is no currency filter) */
    private Currency currency;
    /** 
     * List of accounts (never null)
     * There is an "Account" filter if the size of 'accounts' is greater than 0 (the list of keywords always exists: it is never null)
     */
    private List<Account> accounts;
    /** Period of time between 2 dates (null if there is no period filter) */
    private Period period;
    /** 
     * List of categories (never null)
     * There is a "Category" filter if the size of 'categories' is greater than 0 (the list of categories always exists: it is never null)
     */
    private List<Category> categories;
    /**
     * List of keywords (never null)<BR/>
     * There is a "Keyword" filter if the size of 'keywords' is greater than 0 (the list of keywords always exists: it is never null)
     */
    private List<String> keywords;
    /** 
     * List of payees (never null)
     * There is a "Payee" filter if the size of 'payees' is greater than 0 (the list of payees always exists: it is never null)
     */
    private List<Payee> payees;
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
                !hasCategoriesFilter() && !hasKeywordsFilter() && !hasPayeesFilter());
    }

    /**
     * Creates a new instance of SearchFilter
     * @param currency Currency (can be null if there is no filter on the currency)
     * @param accounts List of accounts (can be null if there is no filter on the accounts)
     * @param period Period (can be null if there is no filter on the period)
     * @param categories List of categoroes (can be null if there is no filter on the categories)
     * @param keywords List of keywords (can be null if there is no filter on the comments: in this case, an empty list of keywords is created)
     * @param payees List of payees (can be null if there is no filter on the payees (use <CODE>Payee.NO_PAYEE</CODE> to search transactions for which no payee is defined)
     * @param includeTransferTransactions Should the transactions of type "Transfer" be included in the search?
     */
    public SearchFilter(Currency currency, List<Account> accounts, Period period, List<Category> categories, List<String> keywords, List<Payee> payees, boolean includeTransferTransactions) {
        setCurrency(currency);
        setAccounts(accounts);
        setPeriod(period);
        setCategories(categories);
        setKeywords(keywords); // If 'keywords' is null, an empty list of keywords is created
        setPayees(payees);
        setIncludeTransferTransactions(includeTransferTransactions);
    }

    /**
     * Gets the currency (can be null if there is no filter on the currency)
     * @return Currency<BR/>
     * There is a "Currency" filter, if the currency is not null
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * Sets the currency
     * @param currency Currency (can be null if there is no filter on the currency)
     */
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    /**
     * Gets the list of accounts
     * @return List of accounts
     */
    public List<Account> getAccounts() {
        return accounts;
    }

    /**
     * Sets the list of accounts
     * @param accounts List of accounts
     */
    public void setAccounts(List<Account> accounts) {
        if (accounts == null) {
            this.accounts = new ArrayList<Account>();
        } else {
            this.accounts = accounts;
        }
    }

    /**
     * Gets the period (can be null if there is no filter on the period)
     * @return Period<BR/>
     * There is a "Period" filter, if the period is not null
     */
    public Period getPeriod() {
        return period;
    }

    /**
     * Sets the period
     * @param period Period (can be null if there is no filter on the period)
     */
    public void setPeriod(Period period) {
        this.period = period;
    }

    /**
     * Gets the list of categories
     * @return Categories
     */
    public List<Category> getCategories() {
        return categories;
    }

    /**
     * Sets the list of categories
     * @param categories List of categories
     */
    public void setCategories(List<Category> categories) {
        if (categories == null) {
            this.categories = new ArrayList<Category>();
        } else {
            this.categories = categories;
        }
    }

    /**
     * Gets the list of keywords
     * @return List of keywords (never null)<BR/>
     * There is a "keyword" filter, if the size of this list is greater than 0
     */
    public List<String> getKeywords() {
        assert (keywords != null); // The list of keywords is never null
        return keywords;
    }

    /**
     * Sets the list of keywords
     * @param keywords List of keywords (can be null)<BR/>
     * If keywords is null, an empty list of keywords is created
     */
    public void setKeywords(List<String> keywords) {
        if (keywords == null) { // If the list of keywords to set is null, create an empty list
            this.keywords = new ArrayList<String>();
        } else {
            // Make sure that 'keywords' does not contain two identic keywords
            for (int i = 0; i < keywords.size(); i++) {
                for (int j = i + 1; j < keywords.size(); j++) {
                    // Make sure that each keyword is not null
                    if (keywords.get(i) == null || keywords.get(j) == null) {
                        throw new IllegalArgumentException("One of the keywords of 'keywords' is null");
                    }

                    // Make sure that each keyword is unique
                    if (keywords.get(i).compareToIgnoreCase(keywords.get(j)) == 0) {
                        throw new IllegalArgumentException("Two keywords are identic: " + keywords.get(i));
                    }
                }
            }

            this.keywords = keywords;
        }
    }

    /**
     * Gets the list of payees
     * @return List of Payees
     */
    public List<Payee> getPayees() {
        return payees;
    }

    /**
     * Sets the list of payees
     * @param payees List of payees
     */
    public void setPayees(List<Payee> payees) {
        if (payees == null) {
            this.payees = new ArrayList<Payee>();
        } else {
            this.payees = payees;
        }
    }

    /**
     * Are the transfer transactions included in the search?
     * @return true if the transactions of category "transfer" must be included in the search
     */
    public boolean isIncludeTransferTransactions() {
        return includeTransferTransactions;
    }

    /**
     * Sets the transaction transfer inclusion flag
     * @param includeTransferTransactions true if the transfer transactions must be included in the search
     */
    public void setIncludeTransferTransactions(boolean includeTransferTransactions) {
        this.includeTransferTransactions = includeTransferTransactions;
    }

    /**
     * Is there a "currency" filter
     * @return true if there is a "currency" filter on the transactions, false otherwise
     */
    public boolean hasCurrencyFilter() {
        return (currency != null);
    }

    /**
     * Is there an "account" filter
     * @return true if there is an "account" filter on the transactions, false otherwise
     */
    public boolean hasAccountsFilter() {
        return (accounts.size() > 0);
    }

    /**
     * Is there a "period" filter (if GrisbiGraphs should look for transactions, which are in a certain period)
     * @return true if there is a "period" filter on the transactions, false otherwise
     */
    public boolean hasPeriodFilter() {
        return (period != null);
    }

    /**
     * Is there a "category" filter (if GrisbiGraphs should look for transactions, which belongs to certain categories)
     * @return true if there is a "category" filter, false otherwise
     */
    public boolean hasCategoriesFilter() {
        return (categories.size() > 0);
    }

    /**
     * Is there a "keyword" filter (if GrisbiGraphs should look for transactions, which have certain keywords)
     * @return true if there is a "keyword" filter, false otherwise
     */
    public boolean hasKeywordsFilter() {
        assert (keywords != null); // The list of keywords always exists
        return (keywords.size() > 0);
    }

    /**
     * Is there a "Payee" filter (if GrisbiGraphs should look for transactions, which have a certain Payee)
     * @return true if there is a "Payee" filter, false otherwise
     */
    public boolean hasPayeesFilter() {
        return (payees.size() > 0);
    }

    @Override
    public int compareTo(SearchFilter searchFilter) {
        // Make sure that the parameter is not null
        if (searchFilter == null) {
            throw new IllegalArgumentException("The parameter 'searchFilter' is null");
        }

        return period.compareTo(searchFilter.getPeriod());
    }
}
