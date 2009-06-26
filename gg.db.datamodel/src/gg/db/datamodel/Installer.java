/*
 * Installer.java
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

import gg.application.Constants;
import java.io.File;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.openide.modules.ModuleInstall;

/**
 * Initializes the DB connection when the module is installed
 * @author Francois Duchemin
 */
public class Installer extends ModuleInstall {

    /** Class name */
    private static final String CLASS_NAME = "gg.db.datamodel.Installer";
    /** Logger */
    private static Logger log = Logger.getLogger(CLASS_NAME);

    /** Session factory */
    private static final SessionFactory sessionFactory;

    /** Set the database folder and initializes the connection with the DB */
    static {
        try {
            log.entering(CLASS_NAME, "restored");

            // Set the DB system directory
            System.setProperty("derby.system.home", Constants.DATABASE_FOLDER_PATH);

            // Create the DB directory if it does not already exist
            File fileSystemDir = new File(Constants.DATABASE_FOLDER_PATH);
            if (!fileSystemDir.exists()) {
                fileSystemDir.mkdir();
            }

            // Create the SessionFactory from hibernate.cfg.xml (create the DB is it does not already exist)
            sessionFactory = new Configuration().configure().buildSessionFactory();

            log.exiting(CLASS_NAME, "restored");
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Creates a session
     * @return Session
     */
    public static Session createSession() {
        return sessionFactory.openSession();
    }

    /**
     * Gets the current session
     * @return Current session
     */
    public static Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    /** Closes the session */
    @Override
    public void close() {
        log.entering(CLASS_NAME, "close");

        sessionFactory.close();

        log.exiting(CLASS_NAME, "close");
    }
}
