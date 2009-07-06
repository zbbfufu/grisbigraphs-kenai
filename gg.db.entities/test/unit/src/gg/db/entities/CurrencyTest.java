/*
 * CurrencyTest.java
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
 * Test of Currency
 * @author Francois Duchemin
 */
public class CurrencyTest {

    /**
     * Test of setId method, of class Currency.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetIdNull() {
        Currency currency = new Currency();
        currency.setId(null);
    }

    /**
     * Test of setName method, of class Currency.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetNameNull() {
        Currency currency = new Currency();
        currency.setName(null);
    }

    /**
     * Test of setCode method, of class Currency.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetCodeNull() {
        Currency currency = new Currency();
        currency.setCode(null);
    }

    /**
     * Test of setIsoCode method, of class Currency.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetIsoCodeNull() {
        Currency currency = new Currency();
        currency.setIsoCode(null);
    }

    /**
     * Test of setAccounts method, of class Currency.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetAccountsNull() {
        Currency currency = new Currency();
        currency.setAccounts(null);
    }

    /**
     * Test of compareTo method, of class Currency.
     */
    @Test
    public void testCompareTo() {
        try {
            // Compare two currencies, which have different IDs
            Currency c1 = new Currency(1L, "US Dollar", "$", "USD", new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), false, false, true);
            Currency c2 = new Currency(2L, "US Dollar", "$", "USD", new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), false, false, true);
            assertFalse(c1.compareTo(c2) == 0);

            // Compare two currencies, which have the same IDs
            Currency c3 = new Currency(1L, "US Dollar", "$", "USD", new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), false, false, true);
            Currency c4 = new Currency(1L, "Euro", "€", "EUR", new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), false, false, true);
            assertTrue(c3.compareTo(c4) == 0);
        } catch (IllegalArgumentException ex) {
            fail("No exception should be thrown here");
        }
    }
}