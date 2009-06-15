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

    public static boolean displayZero() {
        return NbPreferences.forModule(GeneralPanel.class).getBoolean(GeneralPanel.DISPLAY_ZEROS_KEY, true);
    }

    public static boolean calculateSums() {
        return NbPreferences.forModule(GeneralPanel.class).getBoolean(GeneralPanel.CALCULATE_SUMS_KEY, true);
    }
}
