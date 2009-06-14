/*
 * Account.java
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
package gg.db.entities;

import java.math.BigDecimal;
import java.util.Set;

/**
 * <B>Account</B>
 * <UL>
 * <LI>An account contains transactions</LI>
 * <LI>An account is identified by its ID</LI>
 * <LI>An account has a currency</LI>
 * </UL>
 * @author Francois Duchemin
 */
public class Account implements MoneyContainer {

    /** Account's ID (generated by Grisbi) */
    private Long id;
    /** Name of the account */
    private String name;
    /** Currency of the account */
    private Currency currency;
    /** Initial amount on the account */
    private BigDecimal initialAmount;
    /** Current balance (computed by Grisbi and stored in the Grisbi file) of the account */
    private BigDecimal balance;
    /** Is the current account active? (not closed) */
    private Boolean active;
    /** Transactions */
    private Set<Transaction> transactions;


    /** Creates a new instance of Account */
    public Account() {
    }

    /**
     * Creates a new instance of Account
     * @param id ID of the account (generated by Grisbi)
     * @param name Name of the account
     * @param currency  Currency of the account
     * @param initialAmount Initial amount of the account
     * @param balance Current balance of the account (computed by Grisbi and stored in the Grisbi file)
     * @param active Is the current account active? (not closed)
     */
    public Account(Long id, String name, Currency currency, BigDecimal initialAmount, BigDecimal balance, Boolean active) {
        setId(id);
        setName(name);
        setCurrency(currency);
        setInitialAmount(initialAmount);
        setBalance(balance);
        setActive(active);
    }

    /**
     * Gets the ID of the account
     * @return ID of the account (generated by Grisbi)
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the account
     * @param id ID of the account (generated by Grisbi)
     */
    public void setId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("The parameter 'id' is null");
        }
        this.id = id;
    }

    /**
     * Gets the name of the account
     * @return Name of the account
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the account
     * @param name Name of the account
     */
    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("The parameter 'name' is null");
        }
        this.name = name;
    }

    /**
     * Gets the currency of the account
     * @return Currency of the account
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * Sets the currency of the account
     * @param currency Currency of the account
     */
    public void setCurrency(Currency currency) {
        if (currency == null) {
            throw new IllegalArgumentException("The parameter 'currency' is null");
        }
        this.currency = currency;
    }

    /**
     * Gets the initial amount of the account
     * @return Initial amount of the account
     */
    public BigDecimal getInitialAmount() {
        return initialAmount;
    }

    /**
     * Sets the initial amount of the account
     * @param initialAmount Initial amount of the account
     */
    public void setInitialAmount(BigDecimal initialAmount) {
        if (initialAmount == null) {
            throw new IllegalArgumentException("The parameter 'initialAmount' is null");
        }
        this.initialAmount = initialAmount;
    }

    /**
     * Gets the balance of the account
     * @return Balance of the account (computed by Grisbi and stored in the Grisbi file)
     */
    @Override
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Sets the balance of the account
     * @param balance Balance of the account (computed by Grisbi and stored in the Grisbi file)
     */
    public void setBalance(BigDecimal balance) {
        if (balance == null) {
            throw new IllegalArgumentException("The parameter 'balance' is null");
        }
        this.balance = balance;
    }

    /**
     * Is the current account active ?
     * @return true if the account is active (not closed), false otherwise
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * Sets the active status of the account
     * @param active Is the account active (not closed)
     */
    public void setActive(Boolean active) {
        if (active == null) {
            throw new IllegalArgumentException("The parameter 'active' is null");
        }
        this.active = active;
    }

    /**
     * Compares accounts
     * @param account Account to compare
     * @return 0 if the two accounts have the same IDs
     */
    public int compareTo(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("The parameter 'account' is null");
        }

        assert (account.getId() != null && id != null);

        // Same objects
        if (this == account) {
            return 0;
        }

        // Compare the IDs of both accounts
        return (account.getId().compareTo(id));
    }

    /**
     * Gets the transactions of the account
     * @return Account's transactions
     */
    public Set<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Set the transactions of the account
     * @param transactions Account's transactions
     */
    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    /**
     * Compares accounts
     * @param account Account to compare
     * @return true if both accounts are identic, false otherwise
     */
    @Override
    public boolean equals(Object account) {
        if (this == account) {
            return true;
        }

        if (!(account instanceof Account)) {
            return false;
        }

        return (id.compareTo(((Account) account).getId()) == 0 &&
                name.compareToIgnoreCase(((Account) account).getName()) == 0 &&
                initialAmount.compareTo(((Account) account).getInitialAmount()) == 0 &&
                balance.compareTo(((Account) account).getBalance()) == 0 &&
                currency.getId().compareTo(((Account) account).getCurrency().getId()) == 0 &&
                active.compareTo(((Account) account).getActive()) == 0);
    }

    /**
     * Returns a hash code for the account
     * @return A hash code value for the account
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    /**
     * Gets the name of the account
     * @return Name of the account
     */
    @Override
    public String toString() {
        assert (name != null);
        return name;
    }
}