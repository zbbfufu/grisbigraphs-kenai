/*
 * FilterGSB.java
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

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * Custom file filter: display only GSB files
 * @author Francois Duchemin
 */
public class FilterGSB extends FileFilter {
    /**
     * Displays a file or not
     * @param file File to test
     * @return true if the file extension is accepted
     */
    @Override
    public boolean accept(File file) {
        return file.isDirectory() || file.getName().toLowerCase().endsWith(".gsb");
    }

    /**
     * Get the <B>description</B> of the files
     * @return <B>Description</B> of the files
     */
    @Override
    public String getDescription() {
        return "GSB files (*.gsb)";
    }
}