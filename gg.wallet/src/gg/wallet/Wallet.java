/*
 * Wallet.java
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
package gg.wallet;

import gg.db.datamodel.Datamodel;
import gg.db.entities.Account;
import gg.db.entities.Category;
import gg.db.entities.Currency;
import gg.db.entities.FileImport;
import gg.db.entities.Payee;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * The singleton wallet contains hard references to entities from the database such as currencies,
 * accounts, categories, payees...
 * It permits to use these entities in the program without having to query the database every time they are needed
 * @author Francois Duchemin
 */
public class Wallet {

    /** Singleton instance */
    private static Wallet wallet = null;
    /** File imports */
    private List<FileImport> fileImports;
    /** Map of currencies: the key is the currency ID, the value is the currency itself */
    private Map<Long, Currency> currenciesWithId;
    /** Active currencies */
    private List<Currency> activeCurrencies;
    /** Map of accounts: the key is the account ID, the value is the account itself */
    private Map<Long, Account> accountsWithId;
    /** Map of payees: the key is the payee ID, the value is the payee itself */
    private Map<Long, Payee> payeesWithId;
    /** Payees */
    private List<Payee> payees;
    /** Map of categories: the key is the category ID, the value is the category itself */
    private Map<Long, Category> categoriesWithId;
    /** Top categories */
    private List<Category> topCategories;
    /** Map of sub-categories: the key is the parent category, the value is the list of sub-categories that belong to the parent category */
    private Map<Category, List<Category>> subCategoriesWithParentCategory;
    /** Map of active accounts: the key is the currency, the value is the list of accounts that belong to the currency */
    private Map<Currency, List<Account>> activeAccountsWithCurrency;
    /** Logger */
    private Logger log = Logger.getLogger(this.getClass().getName());

    /** Creates a new instance of Wallet */
    private Wallet() {
        // All lists and maps are updated with the current values from the DB
        updateContent();
    }

    /** Updates all lists and maps with the current values from the DB */
    public void updateContent() {
        log.entering(this.getClass().getName(), "updateContent");

        this.fileImports = Datamodel.getFileImports();
        this.currenciesWithId = Datamodel.getCurrenciesWithId();
        this.activeCurrencies = Datamodel.getActiveCurrencies();
        this.accountsWithId = Datamodel.getAccountsWithId();
        this.payeesWithId = Datamodel.getPayeesWithId();
        this.payees = Datamodel.getPayees();
        this.categoriesWithId = Datamodel.getCategoriesWithId();
        this.topCategories = Datamodel.getTopCategories();

        this.subCategoriesWithParentCategory = new HashMap<Category, List<Category>>();
        for (Category topCategory : this.getTopCategories()) {
            List<Category> subCategories = Datamodel.getSubCategories(topCategory);
            this.subCategoriesWithParentCategory.put(topCategory, subCategories);
        }

        this.activeAccountsWithCurrency = new HashMap<Currency, List<Account>>();
        for (Currency currency : this.getActiveCurrencies()) {
            List<Account> accounts = Datamodel.getActiveAccounts(currency);
            this.activeAccountsWithCurrency.put(currency, accounts);
        }

        log.exiting(this.getClass().getName(), "updateContent");
    }

    /**
     * Gets the singleton instance of the wallet
     * @return Instance of the wallet
     */
    public static Wallet getInstance() {
        if (wallet == null) {
            wallet = new Wallet();
        }
        return wallet;
    }

    /**
     * Gets the file imports
     * @return File imports
     */
    public List<FileImport> getFileImports() {
        return fileImports;
    }

    /**
     * Gets the last file import
     * @return Last file import (null if there is no file import in the DB)
     */
    public FileImport getCurrentFileImport() {
        FileImport currentFileImport = null;
        if (fileImports != null && !fileImports.isEmpty()) {
            currentFileImport = fileImports.get(0);
        }

        return currentFileImport;
    }

    /**
     * Gets a map of currencies: the key is the currency ID, the value is the currency itself
     * @return Map of currencies
     */
    public Map<Long, Currency> getCurrenciesWithId() {
        return currenciesWithId;
    }

    /**
     * Gets the active currencies (currencies that contain active accounts - not closed)
     * @return Active currencies
     */
    public List<Currency> getActiveCurrencies() {
        return activeCurrencies;
    }

    /**
     * Gets a map of accounts: the key is the account ID, the value is the account itself
     * @return Map of accounts
     */
    public Map<Long, Account> getAccountsWithId() {
        return accountsWithId;
    }

    /**
     * Gets a map of payees: the key is the payee ID, the value is the payee itself
     * @return Map of payees
     */
    public Map<Long, Payee> getPayeesWithId() {
        return payeesWithId;
    }

    /**
     * Gets the payees
     * @return Payees
     */
    public List<Payee> getPayees() {
        return payees;
    }

    /**
     * Gets the top categories
     * @return Top categories
     */
    public List<Category> getTopCategories() {
        return topCategories;
    }

    /**
     * Gets a map of sub-categories: the key is the parent category, the value is the list of sub-categories that belong to the parent category
     * @return Map of sub-categories
     */
    public Map<Category, List<Category>> getSubCategoriesWithParentCategory() {
        return subCategoriesWithParentCategory;
    }

    /**
     * Gets a map of active accounts: the key is the currency, the value is the list of accounts that belong to the currency
     * @return Map of active accounts
     */
    public Map<Currency, List<Account>> getActiveAccountsWithCurrency() {
        return activeAccountsWithCurrency;
    }

    /**
     * Gets a map of categories: the key is the category ID, the value is the category itself
     * @return Map of categories
     */
    public Map<Long, Category> getCategoriesWithId() {
        return categoriesWithId;
    }
}
