/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gg.options;

import org.openide.util.NbPreferences;

/**
 *
 * @author franswa
 */
public class Options {

    private static boolean defaultDisplayZero = true;
    private static boolean defaultCalculateSums = true;
    private static int defaultMaxNumberPeriods = 10;

    public static boolean displayZero() {
        return NbPreferences.forModule(GeneralPanel.class).getBoolean(
                GeneralPanel.DISPLAY_ZEROS_KEY,
                defaultDisplayZero);
    }

    public static boolean calculateSums() {
        return NbPreferences.forModule(GeneralPanel.class).getBoolean(
                GeneralPanel.CALCULATE_SUMS_KEY,
                defaultCalculateSums);
    }

    public static int getMaxPeriods() {
        return NbPreferences.forModule(GeneralPanel.class).getInt(
                GeneralPanel.MAX_PERIODS_KEY,
                defaultMaxNumberPeriods);
    }
}
