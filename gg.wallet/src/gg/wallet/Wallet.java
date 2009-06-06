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

import gg.db.entities.Transaction;
import java.math.BigDecimal;
import java.util.List;

/**
 * Static classes to do some operations on the transactions
 * @author Francois Duchemin
 */
public class Wallet {

    /**
     * Gets the balance of a list of transactions
     * @param transactions List of transactions
     * @return Total balance (0 if the list of transactions is empty)
     */
    public static BigDecimal getBalance(List<Transaction> transactions) {
        BigDecimal totalBalance = new BigDecimal(0); // Sum of the balances of each transaction

        if (transactions == null) {
            throw new IllegalArgumentException("The parameter 'transactions' is null");
        }

        // Compute the total balance of the list of transactions
        for (Transaction transaction : transactions) {
            assert (transaction != null);

            totalBalance = totalBalance.add(transaction.getAmount());
        }

        // Return the total balance
        return totalBalance;
    }

    /**
     * Gets the total income of a list of transactions
     * @param transactions List of transactions
     * @return Total income
     */
    public static BigDecimal getIncome(List<Transaction> transactions) {
        BigDecimal totalIncome = new BigDecimal(0); // Total income

        if (transactions == null) {
            throw new IllegalArgumentException("The parameter 'transactions' is null");
        }

        // Compute the total income of the list of transactions
        for (Transaction transaction : transactions) {
            assert (transaction != null);

            if (transaction.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                totalIncome = totalIncome.add(transaction.getAmount());
            }
        }

        // Return the total income
        return totalIncome;
    }

    /**
     * Gets the total expenses of a list of transactions
     * @param transactions List of transactions
     * @return Total expenses
     */
    public static BigDecimal getExpenses(List<Transaction> transactions) {
        BigDecimal totalExpenses = new BigDecimal(0); // Total expenses

        if (transactions == null) {
            throw new IllegalArgumentException("The parameter 'transactions' is null");
        }

        // Compute the total expenses of the list of transactions
        for (Transaction transaction : transactions) {
            assert (transaction != null);

            if (transaction.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                totalExpenses = totalExpenses.add(transaction.getAmount());
            }
        }

        // Return the total expenses
        return totalExpenses;
    }
}
