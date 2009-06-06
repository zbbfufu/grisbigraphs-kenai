/*
 * CategoryTest.java
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
 * Test of Category
 * @author Francois Duchemin
 */
public class CategoryTest {

    /**
     * Test of setId method, of class Category.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetIdNull() {
        Category category = new Category();
        category.setId(null);
    }

    /**
     * Test of setGrisbiCategoryId method, of class Category.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetGrisbiCategoryIdNull() {
        Category category = new Category();
        category.setGrisbiCategoryId(null);
    }

    /**
     * Test of setGrisbiSubCategoryId method, of class Category.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetGrisbiSubCategoryIdNull() {
        Category category = new Category();
        category.setGrisbiSubCategoryId(null);
    }

    /**
     * Test of setName method, of class Category.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetNameNull() {
        Category category = new Category();
        category.setName(null);
    }

    /**
     * Test of setParentCategory method, of class Category.
     */
    @Test
    public void testSetParentCategoryNull() {
        try {
            Category category = new Category();
            category.setParentCategory(null);
        } catch (IllegalArgumentException ex) {
            fail("No IllegalArgumentException should be thrown here");
        }
    }

    /**
     * Test of setSubCategories method, of class Category.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetSubCategoriesNull() {
        Category category = new Category();
        category.setSubCategories(null);
    }

    /** Test of isTopCategory and of isSubCategory method */
    @Test
    public void testIsTopCategory() {
        try {
            //   Food
            //    |
            // +--+-----+------------+
            // Meat   Fruit      Vegetables

            Category food = new Category(1L, 1L, "Food", null, false);
            Category fruit = new Category(1L, 2L, "Fruit", food, false);
            Category meat = new Category(1L, 3L, "Meat", food, false);
            Category vegetables = new Category(1L, 4L, "Vegetables", food, false);

            assertTrue(food.isTopCategory());
            assertFalse(fruit.isTopCategory());
            assertFalse(meat.isTopCategory());
            assertFalse(vegetables.isTopCategory());
        } catch(IllegalArgumentException ex) {
            fail("No IllegalArgumentException should be thrown");
        }
    }

    /** Test of compareTo method with null --> an exception is thrown */
    @Test(expected = IllegalArgumentException.class)
    public void compareToNull() {
        Category category = new Category(1L, 1L, "test", null, false);
        int result = category.compareTo(null);
    }

    /** Test of compareTo method */
    @Test
    public void compareTo() {
        // Compare two identic categories
        Category c1 = new Category(1L, 1L, "Alimentation", null, false);
        c1.setId(1L);
        Category c2 = new Category(1L, 1L, "Soins", null, false);
        c2.setId(1L);
        assertTrue(c1.compareTo(c2) == 0);

        // Compare two categories, which have different IDs
        Category c3 = new Category(1L, 1L, "Alimentation", null, false);
        c3.setId(1L);
        Category c4 = new Category(1L, 1L, "Alimentation", null, false);
        c4.setId(2L);
        assertFalse(c3.compareTo(c4) == 0);
    }
}