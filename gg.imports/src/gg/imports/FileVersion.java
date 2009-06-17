/*
 * FileVersion.java
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
package gg.imports;

import java.util.logging.Logger;

/**
 * Version of the Grisbi file
 * <UL>
 * <LI>Each grisbi file (*.gsb) contains a file version</LI>
 * <LI>Thanks to this file version, it is possible to know with which Grisbi version the GSB file is compatible</LI>
 * </UL>
 * @author Francois Duchemin
 */
public enum FileVersion {

    /** Grisbi File - Version 0.5.0 */
    VERSION_0_5_0,
    /** Grisbi File - Version 0.6.0 */
    VERSION_0_6_0,
    /** Unsupported format */
    UNSUPPORTED_VERSION;
    /** Logger */
    private static Logger log = Logger.getLogger(FileVersion.class.getName());

    // Returns a string description of the Grisbi version
    @Override
    public String toString() {
        String description = ""; // The string description of the version of Grisbi
        switch (this) {
            case VERSION_0_5_0:
                description = "0.5.0";
                break;
            case VERSION_0_6_0:
                description = "0.6.0";
                break;
            case UNSUPPORTED_VERSION:
                description = "Unsupported";
                break;
            default:
                // Should never happen
                log.severe("The Grisbi version is unknown");
                throw new AssertionError("The Grisbi version is unknown - Method 'toString' from class 'FileVersion'");
        }

        return description;
    }
}
