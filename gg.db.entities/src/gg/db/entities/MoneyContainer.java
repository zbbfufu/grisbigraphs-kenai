/*
 * MoneyContainer.java
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

import java.math.BigDecimal;

/**
 * <B>MoneyContainer</B>
 * <UL>
 * <LI>A 'MoneyContainer' object contains transactions and thus, a balance</LI>
 * </UL>
 * @author Francois Duchemin
 */
public interface MoneyContainer {

    /**
     * Gets the balance of the container
     * @return Balance of the container
     */
    public BigDecimal getBalance();
}
