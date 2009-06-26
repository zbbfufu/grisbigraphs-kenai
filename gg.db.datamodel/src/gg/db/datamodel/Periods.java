/*
 * Periods.java
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

import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;

/**
 * <B>Periods</B> contains a list of Period<BR/>
 * Creating monthly Periods between the 05/02/2006 and the 07/24/2006 results in creating the following Period objects:
 * <UL>
 * <LI>From 05/01/2006 to 05/31/2006 (May)</LI>
 * <LI>From 06/01/2006 to 06/30/2006 (June)</LI>
 * <LI>From 07/01/2006 to 07/31/2006 (July)</LI>
 * </UL>
 * @author Francois Duchemin
 */
public class Periods {

    /** Start date of the period */
    private LocalDate start;
    /** End date of the period */
    private LocalDate end;
    /** Type of the period (DAY, WEEK...) */
    private PeriodType periodType;
    /** List of Period */
    private List<Period> periods;

    /**
     * Creates a new instance of Periods<BR/>
     * Creating a Periods from 05/20/2006 to 07/02/2006 (with type=MONTH) should result in creating the following Period objects:<BR/>
     * <UL>
     * <LI> First month: <B>May 2006</B> (from 05/01/2006 to 05/31/2006)</LI>
     * <LI> Second month: <B>June 2006</B> (from 06/01/2006 to 06/30/2006)</LI>
     * <LI> Third month: <B>July 2006</B> (from 07/01/2006 to 07/31/2006)</LI>
     * </UL>
     * @param start Start date of the period
     * @param end End date of the period
     * @param periodType Type of the period (day, week, month, year, free)
     */
    public Periods(LocalDate start, LocalDate end, PeriodType periodType) {
        setStart(start);
        setEnd(end);
        setPeriodType(periodType);

        // Create the list of Period objects
        List<Period> newPeriods = getListOfPeriod(start, end, periodType);
        setPeriods(newPeriods);
    }

    /**
     * Gets the list of Period between a start date and an end date<BR/><BR/>
     * Example:<BR/>
     * getListOfPeriod(new LocalDate(2006, 5, 20), new LocalDate(2006, 7, 2), PeriodType.MONTH) should create the following Period objects:
     * <UL>
     * <LI> From 05/01/2006 to 05/31/2006 (MONTH)</LI>
     * <LI> From 06/01/2006 to 06/30/2006 (MONTH)</LI>
     * <LI> From 07/01/2006 to 07/31/2006 (MONTH)</LI>
     * </UL>
     * @param start Start date of the whole period
     * @param end End date of the whole period
     * @param periodType Type of the Period to create
     * @return The list of computed Period
     */
    private List<Period> getListOfPeriod(LocalDate start, LocalDate end, PeriodType periodType) {
        if (start == null || end == null || periodType == null) {
            throw new IllegalArgumentException("One of the following parameters is null: 'start', 'end', 'periodType'");
        }

        // Adjust the start date to the start of the period's type (first day of the week, first day of the month, first day of the year depending on the type of the period to create)
        LocalDate startPeriods = getAdjustedStartDate(start, periodType);

        // Adjust the end date to the end of the period's type (last day of the week, last day of the month, last day of the year depending on the type of the period to create)
        LocalDate endPeriods = getAdjustedEndDate(end, periodType);

        assert (startPeriods != null && endPeriods != null);

        // Create the list of Period objects
        List<Period> listOfPeriod = new ArrayList<Period>(); // List of created Period (returned object)
        Period newPeriod;
        if (periodType == PeriodType.FREE) {
            // Create the Period object and add it to the list of Period
            newPeriod = new Period(start, end, PeriodType.FREE);
            listOfPeriod.add(newPeriod);
        } else {
            // Create all Period objects and add them to the list of Period
            while (startPeriods.compareTo(endPeriods) <= 0) {
                switch (periodType) {
                    case DAY:
                        // Create the DAY period
                        newPeriod = new Period(startPeriods, startPeriods, PeriodType.DAY); // i.e. Day from 05/02/2006 to 05/02/2006
                        startPeriods = startPeriods.plusDays(1); // Move to the next day
                        break;
                    case WEEK:
                        // Create the WEEK period
                        newPeriod = new Period(startPeriods, startPeriods.plusDays(6), PeriodType.WEEK); // i.e. Week from 05/01/2006 (MONDAY) to 05/07/2006 (SUNDAY)
                        startPeriods = startPeriods.plusDays(7); // Move to the next week
                        break;
                    case MONTH:
                        // Create the MONTH period
                        newPeriod = new Period(startPeriods, startPeriods.plusMonths(1).minusDays(1), PeriodType.MONTH); // i.e. Month from 05/01/2006 to 05/31/2006
                        startPeriods = startPeriods.plusMonths(1); // Move to the next month
                        break;
                    case YEAR:
                        // Create the YEAR period
                        newPeriod = new Period(startPeriods, startPeriods.plusYears(1).minusDays(1), PeriodType.YEAR); // i.e. Year from 01/01/2006 to 12/31/2006
                        startPeriods = startPeriods.plusYears(1); // Move to the next year
                        break;
                    case FREE: // should never happen
                        throw new AssertionError("The FREE PeriodType should not be handled in this switch statement");
                    default: // should never happen
                        throw new AssertionError("Unknown PeriodType");
                }
                assert (newPeriod != null);

                // Add the created period to the list of Period
                listOfPeriod.add(newPeriod);
            }
        }

        return listOfPeriod;
    }

    /**
     * Gets the adjusted start date: the date is adjusted to the first day of the period<BR/>
     * Depending on periodType, the date will be:
     * <UL>
     * <LI>First day of the week (monday)</LI>
     * <LI>First day of the month</LI>
     * <LI>First day of the year</LI>>
     * </UL>
     * Example:
     * <UL>
     * <LI>getAdjustedStartDate(new LocalDate(2006, 5, 20), PeriodType.DAY) should return: <B>05/20/2006</B></LI>
     * <LI>getAdjustedStartDate(new LocalDate(2006, 5, 20), PeriodType.WEEK) should return: <B>05/15/2006</B> (if Configuration.getWeekStartingOn() == MONDAY)</LI>
     * <LI>getAdjustedStartDate(new LocalDate(2006, 5, 20), PeriodType.MONTH) should return: <B>05/01/2006</B></LI>
     * <LI>getAdjustedStartDate(new LocalDate(2006, 5, 20), PeriodType.YEAR) should return: <B>01/01/2006</B></LI>
     * <LI>getAdjustedStartDate(new LocalDate(2006, 5, 20), PeriodType.FREE) should return: <B>05/20/2006</B></LI>
     * </UL>
     * @param date Date to adjust to the start of the period
     * @param periodType Type of the Period to adjust the period
     * @return Date adjusted to the start of the period
     */
    public static LocalDate getAdjustedStartDate(LocalDate date, PeriodType periodType) {
        if (date == null || periodType == null) {
            throw new IllegalArgumentException("One of the following parameters is null: 'date', 'periodType'");
        }

        LocalDate adjustedStartDate = null;
        switch (periodType) {
            case DAY: // No adjustement to do
                adjustedStartDate = date;
                break;
            case FREE: // No adjustement to do
                adjustedStartDate = date;
                break;
            case WEEK: // Adjust the date to the first day of the week (Monday)
                adjustedStartDate = date.toDateMidnight().withField(DateTimeFieldType.dayOfWeek(), DateTimeConstants.MONDAY).toLocalDate();
                break;
            case MONTH: // Adjust the date to the first day of the month (<MONTH>/01/<YEAR>)
                adjustedStartDate = date.withField(DateTimeFieldType.dayOfMonth(), 1);
                break;
            case YEAR: // Adjust the date to the first day of the year (01/01/<YEAR>)
                adjustedStartDate = date.withField(DateTimeFieldType.dayOfMonth(), 1).withField(DateTimeFieldType.monthOfYear(), 1);
                break;
            default:
                throw new AssertionError("Unknown PeriodType"); // should never happen
        }

        assert (adjustedStartDate != null);
        return adjustedStartDate;
    }

    /**
     * Gets the adjusted end date: the date is adjusted to the last day of the period<BR/>
     * Depending on periodType, the date will be:
     * <UL>
     * <LI>Last day of the week (sunday)</LI>
     * <LI>Last day of the month</LI>
     * <LI>Last day of the year</LI>>
     * </UL>
     * Example:
     * <UL>
     * <LI>getAdjustedEndDate(new LocalDate(2006, 5, 20), PeriodType.DAY) should return: <B>05/20/2006</B></LI>
     * <LI>getAdjustedEndDate(new LocalDate(2006, 5, 20), PeriodType.WEEK) should return: <B>05/21/2006</B> (if Configuration.getWeekStartingOn() == MONDAY)</LI>
     * <LI>getAdjustedEndDate(new LocalDate(2006, 5, 20), PeriodType.MONTH) should return: <B>05/31/2006</B></LI>
     * <LI>getAdjustedEndDate(new LocalDate(2006, 5, 20), PeriodType.YEAR) should return: <B>12/31/2006</B></LI>
     * <LI>getAdjustedEndDate(new LocalDate(2006, 5, 20), PeriodType.FREE) should return: <B>05/20/2006</B></LI>
     * </UL>
     * @param date Date to adjust to the end of the period
     * @param periodType Type of the Period to adjust the date
     * @return Date adjusted to the end of the period
     */
    public static LocalDate getAdjustedEndDate(LocalDate date, PeriodType periodType) {
        if (date == null || periodType == null) {
            throw new IllegalArgumentException("One of the following parameters is null: 'date', 'periodType'");
        }

        LocalDate adjustedEndDate = null;
        switch (periodType) {
            case DAY: // No adjustement to do
                adjustedEndDate = date;
                break;
            case FREE: // No adjustement to do
                adjustedEndDate = date;
                break;
            case WEEK: // Adjust the date to the last day of the week (sunday)
                adjustedEndDate = date.toDateMidnight().withField(DateTimeFieldType.dayOfWeek(), DateTimeConstants.MONDAY + 6).toLocalDate();
                break;
            case MONTH: // Adjust the date to the last day of the month
                adjustedEndDate = date.withField(DateTimeFieldType.dayOfMonth(), 1).plusMonths(1).minusDays(1);
                break;
            case YEAR: // Adjust the date to the last day of the year (12/31/<YEAR>)
                adjustedEndDate = date.withField(DateTimeFieldType.dayOfMonth(), 1).plusMonths(1).minusDays(1).withField(DateTimeFieldType.monthOfYear(), 12);
                break;
            default:
                throw new AssertionError("Unknown PeriodType"); // should never happen
        }

        assert (adjustedEndDate != null);
        return adjustedEndDate;
    }

    /**
     * Gets the start date of the period
     * @return Start date of the period
     */
    public LocalDate getStart() {
        assert (start != null);
        return start;
    }

    /**
     * Sets the start date of the period
     * @param start Start date of the period
     */
    private void setStart(LocalDate start) {
        if (start == null) {
            throw new IllegalArgumentException("The parameter 'start' is null");
        }

        // Make sure that the Periods is valid
        if (end != null && start.compareTo(end) > 0) { // The start date is after the end date --> the Periods object is not valid
            throw new IllegalArgumentException("The period is not valid: the start date (" + start + ") is after the end date (" + end + ")");
        }

        this.start = start;
    }

    /**
     * Gets the end date of the period
     * @return End date of the period
     */
    public LocalDate getEnd() {
        assert (end != null);
        return end;
    }

    /**
     * Sets the end date of the period
     * @param end End date of the period
     */
    private void setEnd(LocalDate end) {
        if (end == null) {
            throw new IllegalArgumentException("The parameter 'end' is null");
        }

        // Make sure that the Periods is valid
        if (start != null && end.compareTo(start) < 0) { // The start date is after the end date --> the Periods object is not valid
            throw new IllegalArgumentException("The period is not valid: the start date (" + start + ") is after the end date (" + end + ")");
        }

        this.end = end;
    }

    /**
     * Gets the type of the period
     * @return Type of the period (day, week, month, year)
     */
    public PeriodType getPeriodType() {
        assert (periodType != null);
        return periodType;
    }

    /**
     * Sets the type of the period
     * @param periodType Type of the period (free, day, week...)
     */
    private void setPeriodType(PeriodType periodType) {
        if (periodType == null) {
            throw new IllegalArgumentException("The parameter 'periodType' is null");
        }

        this.periodType = periodType;
    }

    /**
     * Gets the list of periods
     * @return List of Period
     */
    public List<Period> getPeriods() {
        assert (periods != null);
        return periods;
    }

    /**
     * Sets the list of periods
     * @param periods List of Period
     */
    private void setPeriods(List<Period> periods) {
        if (periods == null) {
            throw new IllegalArgumentException("The parameter 'periods' is null");
        }

        // Make sure that the list of Period is not empty
        if (periods.size() == 0) {
            throw new IllegalArgumentException("The list of periods is empty");
        }

        // Make sure that each period is not null
        for (Period p : periods) {
            if (p == null) {
                throw new IllegalArgumentException("One of the period is null");
            }
        }

        // Make sure that 'periods' does not contain two identic periods
        for (int i = 0; i < periods.size(); i++) {
            for (int j = i + 1; j < periods.size(); j++) {
                if (periods.get(i).compareTo(periods.get(j)) == 0) {
                    throw new IllegalArgumentException("periods contains two identic periods");
                }
            }
        }

        this.periods = periods;
    }

    @Override
    public String toString() {
        String description = "start=" + start + " - end=" + end + " - period type=" + periodType + "\n";

        for (Period period : periods) {
            description += period.toString() + "\n";
        }

        return description;
    }
}
