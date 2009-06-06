/*
 * PeriodType.java
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

package gg.db.datamodel;

/**
 * Type of period (Day, Week, Month, Year, Free)<BR/>
 * The financial reports are grouped by PeriodType.
 * @author Francois Duchemin
 */
public enum PeriodType {
    /** Day */
    DAY,

    /** Week */
    WEEK,

    /** Month */
    MONTH,

    /** Year */
    YEAR,

    /** Free */
    FREE;

    // Returns a string description of the type of period
    @Override
    public String toString() {
        String description = ""; // The string description of the type of the period

        switch (this) {
            case DAY:
                description = "Day";
                break;
            case WEEK:
                description = "Week";
                break;
            case MONTH:
                description = "Month";
                break;
            case YEAR:
                description = "Year";
                break;
            case FREE:
                description = "Free";
                break;
            default:
                // Should never happen
                throw new AssertionError("The PeriodType is unknown - Method 'toString' from class 'PeriodType'");
        }

        assert (description != null);
        return description;
    }
}