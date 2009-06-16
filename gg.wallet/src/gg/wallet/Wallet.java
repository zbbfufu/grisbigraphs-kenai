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
import gg.db.entities.GrisbiCategory;
import gg.db.entities.Payee;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Francois Duchemin
 */
public class Wallet {

    /** Singleton instance */
    private static Wallet wallet = null;
    private List<FileImport> fileImports;
    private Map<Long, Currency> currenciesWithId;
    private List<Currency> activeCurrencies;
    private Map<Long, Account> accountsWithId;
    private Map<Long, Payee> payeesWithId;
    private List<Payee> payees;
    private Map<Long, Category> categoriesWithId;
    private Map<GrisbiCategory, Category> categoriesWithGrisbiCategory;
    private List<Category> topCategories;
    private Map<Category, List<Category>> subCategoriesWithParentCategory;
    private Map<Currency, List<Account>> activeAccountsWithCurrency;

    private Wallet() {
        updateContent();
    }

    public void updateContent() {
        this.fileImports = Datamodel.getFileImports();
        this.currenciesWithId = Datamodel.getCurrenciesWithId();
        this.activeCurrencies = Datamodel.getActiveCurrencies();
        this.accountsWithId = Datamodel.getAccountsWithId();
        this.payeesWithId = Datamodel.getPayeesWithId();
        this.payees = Datamodel.getPayees();
        this.categoriesWithId = Datamodel.getCategoriesWithId();
        this.categoriesWithGrisbiCategory = Datamodel.getCategoriesWithGrisbiCategory();
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
    }

    public static Wallet getInstance() {
        if (wallet == null) {
            wallet = new Wallet();
        }
        return wallet;
    }

    /**
     * @return the fileImports
     */
    public List<FileImport> getFileImports() {
        return fileImports;
    }

    public FileImport getCurrentFileImport() {
        FileImport currentFileImport = null;
        if (fileImports != null && !fileImports.isEmpty()) {
            currentFileImport = fileImports.get(0);
        }

        return currentFileImport;
    }

    /**
     * @return the currenciesWithId
     */
    public Map<Long, Currency> getCurrenciesWithId() {
        return currenciesWithId;
    }

    /**
     * @return the activeCurrencies
     */
    public List<Currency> getActiveCurrencies() {
        return activeCurrencies;
    }

    /**
     * @return the accountsWithId
     */
    public Map<Long, Account> getAccountsWithId() {
        return accountsWithId;
    }

    /**
     * @return the payeesWithId
     */
    public Map<Long, Payee> getPayeesWithId() {
        return payeesWithId;
    }

    /**
     * @return the payees
     */
    public List<Payee> getPayees() {
        return payees;
    }

    /**
     * @return the categoriesWithGrisbiCategory
     */
    public Map<GrisbiCategory, Category> getCategoriesWithGrisbiCategory() {
        return categoriesWithGrisbiCategory;
    }

    /**
     * @return the topCategories
     */
    public List<Category> getTopCategories() {
        return topCategories;
    }

    /**
     * @return the subCategoriesWithParentCategory
     */
    public Map<Category, List<Category>> getSubCategoriesWithParentCategory() {
        return subCategoriesWithParentCategory;
    }

    /**
     * @return the activeAccountsWithCurrency
     */
    public Map<Currency, List<Account>> getActiveAccountsWithCurrency() {
        return activeAccountsWithCurrency;
    }

    /**
     * @return the categoriesWithId
     */
    public Map<Long, Category> getCategoriesWithId() {
        return categoriesWithId;
    }
}
