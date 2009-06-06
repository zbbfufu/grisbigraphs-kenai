/*
 * EntitiesTestSuite.java
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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test suite for entities
 * @author Francois Duchemin
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({gg.db.entities.CategoryTest.class, gg.db.entities.AccountTest.class,
                    gg.db.entities.CurrencyTest.class, gg.db.entities.FileImportTest.class,
                    gg.db.entities.GrisbiCategoryTest.class, gg.db.entities.PayeeTest.class,
                    gg.db.entities.TransactionTest.class})
public class EntitiesTestSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

}