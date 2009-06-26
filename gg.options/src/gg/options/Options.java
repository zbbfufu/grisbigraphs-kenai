/*
 * Options.java
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
package gg.options;

import org.openide.util.NbPreferences;

/**
 * Static methods to get the options' values
 * @author Francois Duchemin
 */
public final class Options {

    /** Zeros are displayed by default on the UI */
    private static final boolean DEFAULT_DISPLAY_ZERO = true;
    /** Sums are displayed by default on the UI */
    private static final boolean DEFAULT_CALCULATE_SUMS = true;
    /** Default maximum number of periods that can be displayed on the UI */
    private static final int DEFAULT_MAX_NUMBER_PERIODS = 10;

    /**
     * Should zeros be displayed in the tables
     * @return true if zeros should be displayed in the tables on the UI
     */
    public static boolean displayZero() {
        return NbPreferences.forModule(GeneralPanel.class).getBoolean(
                GeneralPanel.DISPLAY_ZEROS_KEY,
                DEFAULT_DISPLAY_ZERO);
    }

    /**
     * Should sums be displayed in the tables
     * @return true if the sums should be displayed in the tables on the UI
     */
    public static boolean calculateSums() {
        return NbPreferences.forModule(GeneralPanel.class).getBoolean(
                GeneralPanel.CALCULATE_SUMS_KEY,
                DEFAULT_CALCULATE_SUMS);
    }

    /**
     * Gets the maximum number of periods that can be displayed on the UI
     * @return Maximum number of periods
     */
    public static int getMaxPeriods() {
        return NbPreferences.forModule(GeneralPanel.class).getInt(
                GeneralPanel.MAX_PERIODS_KEY,
                DEFAULT_MAX_NUMBER_PERIODS);
    }
}
