/*
 * PayeeTest.java
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
 * Test of Payee
 * @author Francois Duchemin
 */
public class PayeeTest {

    /**
     * Test of setId method, of class Payee.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetIdNull() {
        Payee payee = new Payee();
        payee.setId(null);
    }

    /**
     * Test of setName method, of class Payee.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetNameNull() {
        Payee payee = new Payee();
        payee.setName(null);
    }

    /**
     * Test of compareTo method, of class Payee.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCompareToNull() {
        Payee payee = new Payee(1L, "McDonalds", false);
        int result = payee.compareTo(null);
    }

    /**
     * Test of compareTo method, of class Payee.
     */
    @Test
    public void testCompareTo() {
        try {
            // Compare payees that have different IDs
            Payee payeeA = new Payee(1L, "Name of the Payee A", false);
            Payee payeeB = new Payee(2L, "Name of the Payee A", false);
            assertFalse(payeeA.compareTo(payeeB) == 0);

            // Compare payees that have the same IDs
            Payee payeeC = new Payee(1L, "Name of the Payee C", false);
            Payee payeeD = new Payee(1L, "Name of the Payee D", false);
            assertTrue(payeeC.compareTo(payeeD) == 0);
        } catch (IllegalArgumentException ex) {
            fail("No IllegalArgumentException should be thrown" + ex);
        }
    }
}