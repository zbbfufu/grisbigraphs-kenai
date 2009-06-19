/*
 * TransactionTest.java
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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test of Transaction
 * @author Francois Duchemin
 */
public class TransactionTest {

    /**
     * Test of setId method, of class Transaction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetId() {
        Transaction transaction = new Transaction();
        transaction.setId(null);
    }

    /**
     * Test of setDate method, of class Transaction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetDate() {
        Transaction transaction = new Transaction();
        transaction.setDate(null);
    }

    /**
     * Test of setAccount method, of class Transaction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetAccount() {
        Transaction transaction = new Transaction();
        transaction.setAccount(null);
    }

    /**
     * Test of setAmount method, of class Transaction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetAmount() {
        Transaction transaction = new Transaction();
        transaction.setAmount(null);
    }

    /**
     * Test of setCategory method, of class Transaction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetCategory() {
        Transaction transaction = new Transaction();
        transaction.setCategory(null);
    }

    /**
     * Test of setComment method, of class Transaction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetComment() {
        Transaction transaction = new Transaction();
        transaction.setComment(null);
    }

    /**
     * Test of setPayee method, of class Transaction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetPayee() {
        Transaction transaction = new Transaction();
        transaction.setPayee(null);
    }

    /**
     * Test of setParentTransaction method, of class Transaction.
     */
    @Test
    public void testSetParentTransaction() {
        try {
            Transaction transaction = new Transaction();
            transaction.setParentTransaction(null);
        } catch (IllegalArgumentException ex) {
            fail("No IllegalArgumentException should be thrown");
        }
    }

    /**
     * Test of setSubTransactions method, of class Transaction.
     */
    @Test
    public void testSetSubTransactions() {
        try {
            Transaction transaction = new Transaction();
            transaction.setSubTransactions(null);
        } catch (IllegalArgumentException ex) {
            fail("No IllegalArgumentException should be thrown");
        }
    }

    /**
     * Test of compareTo method, of class Transaction.
     */
    @Test
    public void testCompareTo() {
        // Compare two transactions, which have different IDs
        Transaction t1 = new Transaction();
        t1.setId(1L);
        Transaction t2 = new Transaction();
        t2.setId(2L);
        assertFalse(t1.compareTo(t2) == 0);

        // Compare two transactions, which have the same IDs
        Transaction t3 = new Transaction();
        t3.setId(1L);
        Transaction t4 = new Transaction();
        t4.setId(1L);
        assertTrue(t3.compareTo(t4) == 0);
    }
}