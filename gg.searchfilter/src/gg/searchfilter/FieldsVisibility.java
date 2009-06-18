/*
 * FieldsVisibility.java
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

/**
 * Define which fields must be visible on the Search Filter window
 * @author Francois Duchemin
 */
public class FieldsVisibility {

    /** Field 'From' visible? */
    private boolean fromVisible;
    /** Field 'To' visible? */
    private boolean toVisible;
    /** Field 'By' visible? */
    private boolean byVisible;
    /** Field 'Currency' visible? */
    private boolean currencyVisible;
    /** Field 'Accounts' visible? */
    private boolean accountsVisible;
    /** Field 'Categories' visible? */
    private boolean categoriesVisible;
    /** Field 'Payees' visible? */
    private boolean payeesVisible;
    /** Field 'Keywords' visible? */
    private boolean keywordsVisible;

    /** Creates a new instance of FieldsVisibility: no field is visible by default */
    public FieldsVisibility() {
        setFromVisible(false);
        setToVisible(false);
        setByVisible(false);
        setCurrencyVisible(false);
        setAccountsVisible(false);
        setCategoriesVisible(false);
        setPayeesVisible(false);
        setKeywordsVisible(false);
    }

    /**
     * Is the 'from' field visible?
     * @return true if the 'from' field is visible
     */
    public boolean isFromVisible() {
        return fromVisible;
    }

    /**
     * Sets the 'from' field visibility
     * @param fromVisible Should the 'from' field be visible?
     */
    public void setFromVisible(boolean fromVisible) {
        this.fromVisible = fromVisible;
    }

    /**
     * Is the 'to' field visible?
     * @return true if the 'to' field is visible
     */
    public boolean isToVisible() {
        return toVisible;
    }

    /**
     * Sets the 'to' field visibillity
     * @param toVisible Should the 'to' field be visible?
     */
    public void setToVisible(boolean toVisible) {
        this.toVisible = toVisible;
    }

    /**
     * Is the 'by' field visible?
     * @return true if the 'by' field is visible
     */
    public boolean isByVisible() {
        return byVisible;
    }

    /**
     * Sets the 'by' field visibility
     * @param byVisible Should the 'by' field be visible?
     */
    public void setByVisible(boolean byVisible) {
        this.byVisible = byVisible;
    }

    /**
     * Is the 'currency' field visible?
     * @return true if the 'currency' field is visible
     */
    public boolean isCurrencyVisible() {
        return currencyVisible;
    }

    /**
     * Sets the 'currency' field visibility
     * @param currencyVisible Should the 'currency' field be visible?
     */
    public void setCurrencyVisible(boolean currencyVisible) {
        this.currencyVisible = currencyVisible;
    }

    /**
     * Is the 'accounts' field visible?
     * @return true if the 'accounts' field is visible
     */
    public boolean isAccountsVisible() {
        return accountsVisible;
    }

    /**
     * Sets the accounts 'field' visibility
     * @param accountsVisible Should the 'accounts' field be visible?
     */
    public void setAccountsVisible(boolean accountsVisible) {
        this.accountsVisible = accountsVisible;
    }

    /**
     * Is the 'categories' field visible?
     * @return true if the 'categories' field is visible
     */
    public boolean isCategoriesVisible() {
        return categoriesVisible;
    }

    /**
     * Sets the 'categories' field visibility
     * @param categoriesVisible Should the 'categories' field be visible
     */
    public void setCategoriesVisible(boolean categoriesVisible) {
        this.categoriesVisible = categoriesVisible;
    }

    /**
     * Is the 'payees' field visible?
     * @return true if the 'payees' field is visible
     */
    public boolean isPayeesVisible() {
        return payeesVisible;
    }

    /**
     * Sets the 'payees' field visibility
     * @param payeesVisible Should the 'payees' field be visible?
     */
    public void setPayeesVisible(boolean payeesVisible) {
        this.payeesVisible = payeesVisible;
    }

    /**
     * Is the 'keywords' field visible?
     * @return true if the 'keywords' field is visible
     */
    public boolean isKeywordsVisible() {
        return keywordsVisible;
    }

    /**
     * Sets the 'keywords' field visibility
     * @param keywordsVisible Should the 'keywords' field be visible?
     */
    public void setKeywordsVisible(boolean keywordsVisible) {
        this.keywordsVisible = keywordsVisible;
    }
}
