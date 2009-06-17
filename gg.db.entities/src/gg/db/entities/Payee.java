/*
 * Payee.java
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
package gg.db.entities;

/**
 * <B>Payee</B>
 * <UL>
 * <LI>A payee is identified by its ID</LI>
 * <LI>A payee is a party that receives a payment (in a transaction)</LI>
 * </UL>
 * @author Francois Duchemin
 */
public class Payee {

    /** ID of the payee (generated by Grisbi) */
    private Long id;
    /** Name of the payee */
    private String name;
    /** Is this a system property? (i.e. NO_PAYEE) */
    private Boolean systemProperty;
    /** This constant permits to search for transactions, which have <B>no payee</B> */
    public static final Payee NO_PAYEE = new Payee(-1L, "NO_PAYEE", true);

    /** Creates a new instance of payee*/
    public Payee() {
    }

    /**
     * Creates a new instance of payee
     * @param id ID of the payee (generated by Grisbi)
     * @param name Name of the payee
     * @param systemProperty Is the payee to create a system property (i.e. NO_PAYEE)?
     */
    public Payee(Long id, String name, Boolean systemProperty) {
        setId(id);
        setName(name);
        setSystemProperty(systemProperty);
    }

    /**
     * Gets the ID of the payee
     * @return Id of the payee
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the payee
     * @param id ID of the payee
     */
    public void setId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("The parameter 'id' is null");
        }
        this.id = id;
    }

    /**
     * Gets the name of the payee
     * @return Name of the payee
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the payee
     * @param name Name of the payee
     */
    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("The parameter 'name' is null");
        }
        this.name = name;
    }

    /**
     * Is the payee a system property
     * @return true if the payee is a system property (i.e. NO_PAYEE), false otherwise
     */
    public Boolean getSystemProperty() {
        return systemProperty;
    }

    /**
     * Sets the system property
     * @param systemProperty true if the payee is a system property (i.e. NO_PAYEE)
     */
    public void setSystemProperty(Boolean systemProperty) {
        this.systemProperty = systemProperty;
    }

    /**
     * Compare payees
     * @param payee Payee to compare
     * @return 0 if the payees have the same IDs
     */
    public int compareTo(Payee payee) {
        if (payee == null) {
            throw new IllegalArgumentException("The parameter 'payee' is null");
        }

        assert (id != null && payee.getId() != null);

        // Same objects
        if (this == payee) {
            return 0;
        }

        return (payee.getId().compareTo(id));
    }

    /**
     * Gets the name of the payee
     * @return Name of the payee
     */
    @Override
    public String toString() {
        assert (name != null);
        return name;
    }
}
