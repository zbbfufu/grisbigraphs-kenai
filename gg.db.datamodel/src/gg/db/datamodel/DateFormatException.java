/*
 * DateFormatException.java
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
 * Exception thrown when the format of the date is incorrect
 * @author Francois Duchemin
 */
public class DateFormatException extends java.lang.Exception {

    /** Creates a new instance of <code>DateFormatException</code> without detail message */
    public DateFormatException() {
    }

    /**
     * Constructs an instance of <code>DateFormatException</code> with the specified detail message
     * @param msg the detail message
     */
    public DateFormatException(String msg) {
        super(msg);
    }
}
