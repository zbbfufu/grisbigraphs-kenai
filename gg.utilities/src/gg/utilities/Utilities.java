/*
 * Utilities.java
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
package gg.utilities;

import java.awt.Component;
import java.awt.Cursor;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.JFrame;
import org.openide.util.Mutex;
import org.openide.windows.WindowManager;

/**
 * Static utility methods
 * @author Francois Duchemin
 */
public class Utilities {

    /**
     * Utility method to add a sign to a balance<BR/>
     * getSignedBalance(100) returns "+100"<BR/>
     * getSignedBalance(-100) returns "-100"<BR/>
     * getSignedBalance(0) returns "0"
     * @param balance Balance for which a sign must be added
     * @return Signed balance as string
     */
    public static String getSignedBalance(BigDecimal balance) {
        String signedBalance = "";

        if (balance.compareTo(BigDecimal.ZERO) > 0) {
            signedBalance += "+";
        }
        signedBalance += balance.setScale(2, RoundingMode.HALF_EVEN).toString();

        return signedBalance;
    }

    /**
     * Rounds a balance with 2 digits
     * @param balance Balance to round
     * @return Rounded balance
     */
    public static String getBalance(BigDecimal balance) {
        return balance.setScale(2, RoundingMode.HALF_EVEN).toString();
    }

    /**
     * Displays or hides the wait status cursor
     * @param isWaiting Should the hourglass cursor be displayed?
     */
    public static void changeCursorWaitStatus(final boolean isWaiting) {
        Mutex.EVENT.writeAccess(new Runnable() {

            @Override
            public void run() {
                try {
                    JFrame mainFrame =
                            (JFrame) WindowManager.getDefault().getMainWindow();
                    Component glassPane = mainFrame.getGlassPane();
                    if (isWaiting) {
                        glassPane.setVisible(true);
                        glassPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                    } else {
                        glassPane.setVisible(false);
                        glassPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }
                } catch (Exception e) {
                    // probably not worth handling
                }
            }
        });
    }
}
