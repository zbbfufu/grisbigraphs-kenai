/*
 * Transaction.java
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
import org.joda.time.LocalDate;

/**
 * <B>Transaction</B>
 * <UL>
 * <LI>A transaction is an exchange of money between two parties</LI>
 * <LI>A transaction is identified by its ID</LI>
 * <LI>A transaction can have the category <I>Breakdown of transactions</I><BR/>
 * In this case, the transaction contains sub-transactions.</LI>
 * <LI>A sub-transaction cannot contain sub-transactions</LI>
 * <LI>A parent transaction cannot have a parent transaction</LI>
 * <LI>A transaction is identified by its ID ; each transaction (and sub-transaction) has a unique ID</LI>
 * <LI>An account contains transactions</LI>
 * </UL>
 * @author Francois Duchemin
 */
public class Transaction {

    /** Transaction's ID (generated by Derby) */
    private Long id;
    /** Transaction's date */
    private LocalDate date;
    /** Account of the transaction */
    private Account account;
    /** Transaction's amount */
    private BigDecimal amount;
    /** Transaction's category */
    private Category category;
    /** Transaction's comment */
    private String comment;
    /** Transaction's payee*/
    private Payee payee;
    /** Parent transaction */
    private Transaction parentTransaction;
    /** Sub-transactions */
    private Set<Transaction> subTransactions;

    /** Creates a new instance of Transaction */
    public Transaction() {
    }

    /**
     * Creates a new instance of Transaction
     * @param date Transaction's date
     * @param account Account of the transaction
     * @param amount Amount of the transaction
     * @param category Tansaction's category (use <CODE>Category.NO_CATEGORY</CODE> to specify that no category is defined)
     * @param comment Transaction comment
     * @param payee Payee (use <CODE>Payee.NO_PAYEE</CODE> to specify that no payee is defined)
     * @param parentTransaction Parent transaction (for breakdowns of transactions) (can be null)
     */
    public Transaction(LocalDate date, Account account, BigDecimal amount, Category category, String comment, Payee payee, Transaction parentTransaction) {
        setDate(date);
        setAccount(account);
        setAmount(amount);
        setCategory(category);
        setComment(comment);
        setPayee(payee);
        setParentTransaction(parentTransaction);
    }

    /**
     * Gets the transaction's ID
     * @return Transaction's ID (generated by Derby)
     */
    public Long getId() {
        return id;
    }

    /** Sets the transaction's ID
     * @param id Transaction's ID (generated by Derby)
     */
    public void setId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("The parameter 'id' is null");
        }
        this.id = id;
    }

    /**
     * Gets the transaction's date
     * @return Transaction's date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the transaction's date
     * @param date Transaction's date
     */
    public void setDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("The parameter 'date' is null");
        }
        this.date = date;
    }

    /**
     * Gets the transaction's account
     * @return Transaction's account
     */
    public Account getAccount() {
        return account;
    }

    /**
     * Sets the transaction's account
     * @param account Transaction's account
     */
    public void setAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("The parameter 'account' is null");
        }
        this.account = account;
    }

    /**
     * Gets the amount of the transaction
     * @return Transaction's amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the transaction
     * @param amount Transaction's amount
     */
    public void setAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("The parameter 'amount' is null");
        }
        this.amount = amount;
    }

    /**
     * Gets the category of the transaction
     * @return Transaction's category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Sets the category of the transaction
     * @param category Transaction's category
     */
    public void setCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("The parameter 'category' is null");
        }
        this.category = category;
    }

    /**
     * Gets the comment of the transaction
     * @return Transaction's comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the comment of the transaction
     * @param comment Transaction's comment
     */
    public void setComment(String comment) {
        if (comment == null) {
            throw new IllegalArgumentException("The parameter 'comment' is null");
        }
        this.comment = comment;
    }

    /**
     * Gets the payee of the transaction
     * @return Transaction's payee
     */
    public Payee getPayee() {
        return payee;
    }

    /**
     * Sets the payee of the transaction
     * @param payee Transaction's payee
     */
    public void setPayee(Payee payee) {
        if (payee == null) {
            throw new IllegalArgumentException("The parameter 'payee' is null");
        }
        this.payee = payee;
    }

    /**
     * Gets the parent transaction (for breakdowns of transactions)
     * @return Parent transaction (null if top transaction)
     */
    public Transaction getParentTransaction() {
        return parentTransaction;
    }

    /**
     * Sets the parent transaction (for breakdowns of transactions)
     * @param parentTransaction Parent transaction (null if top transaction)
     */
    public void setParentTransaction(Transaction parentTransaction) {
        this.parentTransaction = parentTransaction;
    }

    /**
     * Gets the sub-transactions (for breakdowns of transactions)
     * @return List of sub-transactions
     */
    public Set<Transaction> getSubTransactions() {
        return subTransactions;
    }

    /**
     * Sets the sub-transactions (for breakdowns of transactions)
     * @param subTransactions List of sub-transactions
     */
    public void setSubTransactions(Set<Transaction> subTransactions) {
        this.subTransactions = subTransactions;
    }

    /**
     * Compare transactions
     * @param transaction Transaction to compare (cannot be null)
     * @return 0 if the two transactions have the same ID
     */
    public int compareTo(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("The parameter 'transaction' is null");
        }

        assert (transaction.getId() != null && id != null);

        // Same objects
        if (this == transaction) {
            return 0;
        }

        // Compare IDs
        return (transaction.getId().compareTo(id));
    }

    /**
     * Gets the ID of the transaction
     * @return ID of the transaction
     */
    @Override
    public String toString() {
        return date + " " + amount;
    }
}