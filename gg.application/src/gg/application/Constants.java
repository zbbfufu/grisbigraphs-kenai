/*
 * Constants.java
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
package gg.application;

/**
 * Application constants
 * @author Francois Duchemin
 */
public class Constants {

    /** Application name */
    public static final String APPLICATION_TITLE = "GrisbiGraphs";
    /** GrisbiGraphs version */
    public static final String VERSION = "0.1.0";
    /** Folder in which the database is located */
    public static final String DATABASE_FOLDER_PATH = System.getProperty("user.home", ".") + System.getProperty("file.separator", "/") + "." + "grisbigraphs";
}
