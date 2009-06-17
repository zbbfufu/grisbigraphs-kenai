/*
 * Importer.java
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

import gg.db.datamodel.DateFormatException;

/**
 * <B>Importer</B>
 * <UL>
 * <LI>An 'Importer' object is aimed at importing a Grisbi file into the embedded database</LI>
 * <LI>There should be one 'Importer' for each version of the Grisbi files (i.e. 0.5.0, 0.6.0...)</LI>
 * <LI>The currencies/accounts/transactions & sub-transactions/categories/payees are imported into the embedded database</LI>
 * </UL>
 * @author Francois Duchemin
 */
public interface Importer {

    /**
     * Imports the Grisbi file into the embedded database
     * @return Number of miliseconds needed to import the Grisbi file into the embedded database
     * @throws ParsingException If there is a problem finding a node
     * @throws NumberFormatException If a string is read when a number is expected
     * @throws DateFormatException If the format of a date is invalid
     */
    public long importFile() throws ParsingException, NumberFormatException, DateFormatException;

    /**
     * Is the import task cancelled by the user?
     * @return true if the import task is cancelled
     */
    public boolean isImportCancelled();
}
