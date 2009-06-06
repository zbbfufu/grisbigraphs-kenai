/*
 * GrisbiCategoryTest.java
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
 * Test of GrisbiCategory
 * @author Francois Duchemin
 */
public class GrisbiCategoryTest {
    /**
     * Test of compareTo and equals methods, of class GrisbiCategory.
     */
    @Test
    public void testCompareToAndEquals() {
        // Compare different Grisbi categories
        GrisbiCategory category1 = new GrisbiCategory(2, 3);
        GrisbiCategory category2 = new GrisbiCategory(3, 3);
        assertFalse (category1.equals(category2));
        assertFalse (category1.compareTo(category2) == 0);

        GrisbiCategory category3 = new GrisbiCategory(2, 3);
        GrisbiCategory category4 = new GrisbiCategory(2, 2);
        assertFalse (category3.equals(category4));
        assertFalse (category3.compareTo(category4) == 0);

        // Compare identic Grisbi categories
        GrisbiCategory category5 = new GrisbiCategory(2, 3);
        GrisbiCategory category6 = new GrisbiCategory(2, 3);
        assertTrue (category5.equals(category6));
        assertTrue (category5.compareTo(category6) == 0);
    }

}