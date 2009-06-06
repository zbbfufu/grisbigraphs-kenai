/*
 * GrisbiCategory.java
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

/**
 * Grisbi category (contains the Grisbi's category ID and the Grisbi's sub-category ID)
 * @author Francois Duchemin
 */
public class GrisbiCategory implements Comparable<GrisbiCategory> {

    /** Grisbi category ID */
    private long categoryId;
    /** Grisbi sub-category ID */
    private long subCategoryId;

    /**
     * Creates a new instance of GrisbiCategory
     * @param categoryId ID of the category
     * @param subCategoryId ID of the sub-category
     */
    public GrisbiCategory(long categoryId, long subCategoryId) {
        setCategoryId(categoryId);
        setSubCategoryId(subCategoryId);
    }

    /**
     * Gets the Grisbi's ID of the category
     * @return Grisbi's category ID
     */
    public long getCategoryId() {
        return categoryId;
    }

    /**
     * Sets the Grisbi's ID of the category
     * @param categoryId Grisbi's category ID
     */
    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * Gets the Grisbi's ID of the sub-category
     * @return Grisbi's sub-category ID
     */
    public long getSubCategoryId() {
        return subCategoryId;
    }

    /**
     * Sets the Grisbi's ID of the sub-category
     * @param subCategoryId Grisbi's sub-category ID
     */
    public void setSubCategoryId(long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    /**
     * Compare Grisbi categories
     * @param grisbiCategory Grisbi category to compare (cannot be null)
     * @return 0 if the two Grisbi categories are identic, -1 otherwise
     */
    @Override
    public int compareTo(GrisbiCategory grisbiCategory) {
        if (grisbiCategory == null) {
            throw new IllegalArgumentException("The parameter 'aGrisbiCategory' is null");
        }

        if (categoryId == grisbiCategory.getCategoryId() &&
                subCategoryId == grisbiCategory.getSubCategoryId()) {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * Compare Grisbi categories
     * @param grisbiCategory Grisbi category to compare
     * @return true if both Grisbi categories are identic, false otherwise
     */
    @Override
    public boolean equals(Object grisbiCategory) {
        if (this == grisbiCategory) {
            return true;
        }

        if (!(grisbiCategory instanceof GrisbiCategory)) {
            return false;
        }

        return (categoryId == ((GrisbiCategory) grisbiCategory).getCategoryId() &&
                subCategoryId == ((GrisbiCategory) grisbiCategory).getSubCategoryId());
    }

    /**
     * Gets the hashCode of the Grisbi Category
     * @return Hashcode of the Grisbi category
     */
    @Override
    public int hashCode() {
        Long catId = categoryId;
        Long subCatId = subCategoryId;
        return ((catId.hashCode() * 3) + (subCatId.hashCode() * 2));
    }

    /**
     * Gets the ID of the category and the ID of the sub-category
     * @return ID of the category and ID of sub-category: for example (1, 0)
     */
    @Override
    public String toString() {
        return "(" + categoryId + ", " + subCategoryId + ")";
    }
}
