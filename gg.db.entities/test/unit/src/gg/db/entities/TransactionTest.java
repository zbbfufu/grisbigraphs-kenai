/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gg.db.entities;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author DuchemF
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