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
package gg.searchfilter;

import gg.db.datamodel.PeriodType;
import gg.db.entities.Account;
import gg.db.entities.Category;
import gg.db.entities.Currency;
import gg.db.entities.Payee;
import java.util.List;
import org.joda.time.LocalDate;

/**
 * Search filter (object that contains the information entered by the user in the Search Filter tc)
 * @author Francois Duchemin
 */
public class SearchFilter {

    /** From */
    private LocalDate from;
    /** To */
    private LocalDate to;
    /** Period type */
    private PeriodType periodType;
    /** Currency (null if there is no filter on the currency) */
    private Currency currency;
    /** List of accounts (empty if there is no filter on the accounts) */
    private List<Account> accounts;
    /** List of categories (empty if there is no filter on the categories) */
    private List<Category> categories;
    /** List of payees (empty if there is no filter on the payees) */
    private List<Payee> payees;
    /** Keywords (null if there is no filter on the keywords) */
    private String keywords;

    /**
     * Creates a new instance of SearchFilter
     * @param from Entered from date
     * @param to Entered to date
     * @param periodType Selected type of period
     * @param currency Selected currency
     * @param accounts Selected accounts
     * @param categories Selected categories
     * @param payees Selected payees
     * @param keywords Entered keywords
     */
    public SearchFilter(LocalDate from, LocalDate to, PeriodType periodType, Currency currency, List<Account> accounts,
            List<Category> categories, List<Payee> payees, String keywords) {
        this.setFrom(from);
        this.setTo(to);
        this.setPeriodType(periodType);
        this.setCurrency(currency);
        this.setAccounts(accounts);
        this.setCategories(categories);
        this.setPayees(payees);
        this.setKeywords(keywords);
    }

    /**
     * Gets the selected from Date
     * @return From date
     */
    public LocalDate getFrom() {
        assert (from != null);
        return from;
    }

    /**
     * Sets the selected from date
     * @param from From date
     */
    public void setFrom(LocalDate from) {
        if (from == null) {
            throw new IllegalArgumentException("The parameter 'from' is null");
        }
        this.from = from;
    }

    /**
     * Gets the selected to date
     * @return To date
     */
    public LocalDate getTo() {
        assert (to != null);
        return to;
    }

    /**
     * Sets the selected to date
     * @param to To date
     */
    public void setTo(LocalDate to) {
        if (to == null) {
            throw new IllegalArgumentException("The parameter 'to' is null");
        }
        this.to = to;
    }

    /**
     * Gets the selected type of period
     * @return Type of period
     */
    public PeriodType getPeriodType() {
        assert (periodType != null);
        return periodType;
    }

    /**
     * Sets the selected type of period
     * @param periodType Type of period
     */
    public void setPeriodType(PeriodType periodType) {
        if (periodType == null) {
            throw new IllegalArgumentException("The parameter 'periodType' is null");
        }
        this.periodType = periodType;
    }

    /**
     * Gets the selected currency
     * @return Selected currency (can be null if no filter)
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * Sets the selected the currency
     * @param currency Selected currency
     */
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    /**
     * Gets the selected accounts
     * @return Selected accounts
     */
    public List<Account> getAccounts() {
        return accounts;
    }

    /**
     * Sets the selected accounts
     * @param accounts Selected accounts
     */
    public void setAccounts(List<Account> accounts) {
        if (accounts == null) {
            throw new IllegalArgumentException("The parameter 'accounts' is null");
        }
        this.accounts = accounts;
    }

    /**
     * Gets the selected categories
     * @return Selected categories
     */
    public List<Category> getCategories() {
        return categories;
    }

    /**
     * Sets the selected categories
     * @param categories Selected categories
     */
    public void setCategories(List<Category> categories) {
        if (categories == null) {
            throw new IllegalArgumentException("The parameter 'categories' is null");
        }
        this.categories = categories;
    }

    /**
     * Gets the selected payees
     * @return Selected payees
     */
    public List<Payee> getPayees() {
        return payees;
    }

    /**
     * Sets the selected payees
     * @param payees Selected payees
     */
    public void setPayees(List<Payee> payees) {
        if (payees == null) {
            throw new IllegalArgumentException("The parameter 'payees' is null");
        }
        this.payees = payees;
    }

    /**
     * Gets the entered keywords
     * @return Entered keywords
     */
    public String getKeywords() {
        return keywords;
    }

    /**
     * Sets the entered keywords
     * @param keywords Entered keywords
     */
    public void setKeywords(String keywords) {
        if (keywords == null) {
            throw new IllegalArgumentException("The parameter 'keywords' is null");
        }
        this.keywords = keywords;
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
        return (keywords.length() > 0);
    }
}
