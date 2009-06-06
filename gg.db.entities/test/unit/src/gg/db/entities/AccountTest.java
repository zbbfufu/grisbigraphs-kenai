/*
 * AccountTest.java
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
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test of Account
 * @author Francois Duchemin
 */
public class AccountTest {

    /**
     * Test of setId method, of class Account.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetIdNull() {
        Account a = new Account();
        a.setId(null);
    }

    /**
     * Test of setName method, of class Account.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetNameNull() {
        Account a = new Account();
        a.setName(null);
    }

    /**
     * Test of setCurrency method, of class Account.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetCurrencyNull() {
        Account a = new Account();
        a.setCurrency(null);
    }

    /**
     * Test of setInitialAmount method, of class Account.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetInitialAmountNull() {
        Account a = new Account();
        a.setInitialAmount(null);
    }

    /**
     * Test of setBalance method, of class Account.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetBalanceNull() {
        Account a = new Account();
        a.setBalance(null);
    }

    /**
     * Test of setActive method, of class Account.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetActiveNull() {
        Account a = new Account();
        a.setActive(null);
    }

    /** 
     * Test of the compareTo method with null --> an exception is thrown
     */
    @Test(expected = IllegalArgumentException.class)
    public void compareToNull() {
        Account account = new Account(0L, "Savings", new Currency(1L, "US Dollar", "$", "USD", true), new BigDecimal(0), new BigDecimal(1000), true);
        int result = account.compareTo(null);
    }

    /**
     * Test of compareTo method, of class Account.
     */
    @Test
    public void testCompareToAndEquals() {
        // Compare two accounts, which have different IDs
        Account a1 = new Account(1L, "Savings", new Currency(1L, "US Dollar", "$", "USD", true), new BigDecimal(0), new BigDecimal(1000), true);
        Account a2 = new Account(2L, "Savings", new Currency(1L, "US Dollar", "$", "USD", true), new BigDecimal(0), new BigDecimal(1000), true);
        assertFalse(a1.compareTo(a2) == 0);
        assertFalse(a1.equals(a2));

        // Compare two accounts, which have different names but same IDs
        Account a3 = new Account(1L, "Savings 1", new Currency(1L, "US Dollar", "$", "USD", true), new BigDecimal(0), new BigDecimal(1000), true);
        Account a4 = new Account(1L, "Savings 2", new Currency(1L, "US Dollar", "$", "USD", true), new BigDecimal(0), new BigDecimal(1000), true);
        assertTrue(a3.compareTo(a4) == 0);
        assertFalse(a3.equals(a4));

        // Compare two accounts, which have different currencies but same IDs
        Account a5 = new Account(1L, "Savings", new Currency(1L, "US Dollar", "$", "USD", true), new BigDecimal(0), new BigDecimal(1000), true);
        Account a6 = new Account(1L, "Savings", new Currency(2L, "Euro", "€", "EUR", true), new BigDecimal(0), new BigDecimal(1000), true);
        assertTrue(a5.compareTo(a6) == 0);
        assertFalse(a5.equals(a6));

        // Compare two accounts, which have different initial amounts but same IDs
        Account a7 = new Account(1L, "Savings", new Currency(1L, "US Dollar", "$", "USD", true), new BigDecimal(1), new BigDecimal(1000), true);
        Account a8 = new Account(1L, "Savings", new Currency(1L, "US Dollar", "$", "USD", true), new BigDecimal(2), new BigDecimal(1000), true);
        assertTrue(a7.compareTo(a8) == 0);
        assertFalse(a7.equals(a8));

        // Compare two accounts, which have different balances but same IDs
        Account a9 = new Account(1L, "Savings", new Currency(1L, "US Dollar", "$", "USD", true), new BigDecimal(0), new BigDecimal(1000), true);
        Account a10 = new Account(1L, "Savings", new Currency(1L, "US Dollar", "$", "USD", true), new BigDecimal(0), new BigDecimal(2000), true);
        assertTrue(a9.compareTo(a10) == 0);
        assertFalse(a9.equals(a10));

        // Compare two accounts, which have not the same value for the property 'active' but same IDs
        Account a11 = new Account(1L, "Savings", new Currency(1L, "US Dollar", "$", "USD", true), new BigDecimal(0), new BigDecimal(1000), true);
        Account a12 = new Account(1L, "Savings", new Currency(1L, "US Dollar", "$", "USD", true), new BigDecimal(0), new BigDecimal(1000), false);
        assertTrue(a11.compareTo(a12) == 0);
        assertFalse(a11.equals(a12));

        // Compare two identic accounts
        Account a13 = new Account(1L, "Savings", new Currency(1L, "US Dollar", "$", "USD", true), new BigDecimal(0), new BigDecimal(1000), true);
        Account a14 = new Account(1L, "Savings", new Currency(1L, "US Dollar", "$", "USD", true), new BigDecimal(0), new BigDecimal(1000), true);
        assertTrue(a13.compareTo(a14) == 0);
        assertTrue(a13.equals(a14));
    }
}