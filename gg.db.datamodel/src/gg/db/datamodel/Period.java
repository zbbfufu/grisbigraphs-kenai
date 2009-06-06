/*
 * Period.java
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

import java.util.Locale;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

/**
 * <B>Period</B>
 * <UL>
 * <LI>Period of time between 2 dates</LI>
 * <LI>The type of the period can be <B>FREE</B>: From 05/01/2006 to 06/22/2006 (no constraint on the period of time)</LI>
 * <LI>The type of the period can also be: DAY, WEEK, MONTH or YEAR.<BR/>
 * In this case, the period has to be a "full" period.<BR/>
 * Example of valid periods:
 *      <UL>
 *      <LI>Valid <B>DAY</B>: Start date=12/30/2006, End date=12/30/2006 (Full day)</LI>
 *      <LI>Valid <B>WEEK</B>: Start date=12/11/2006, End date=12/17/2006 (Full week beginning on MONDAY)</LI>
 *      <LI>Valid <B>MONTH</B>: Start date=01/01/2006, End date=01/31/2006 (Full month)</LI>
 *      <LI>Valid <B>YEAR</B>: Start date=01/01/2006, End date=12/31/2006 (Full year)</LI>
 *      </UL>
 * </LI>
 * </UL>
 * @author Francois Duchemin
 */
public class Period implements Comparable<Period> {

    /** Start date of the period */
    private LocalDate start;

    /** End date of the period */
    private LocalDate end;

    /** Type of the period (DAY, WEEK...) */
    private PeriodType periodType;

    /**
     * Creates a new instance of Period
     * @param start Start date of the period
     * @param end End date of the period
     * @param periodType Type of the period (day, week, month, year)
     */
    public Period(LocalDate start, LocalDate end, PeriodType periodType) {
        if (start == null || end == null || periodType == null) {
            throw new IllegalArgumentException("One of the following parameters is null: 'start', 'end', 'periodType'");
        }

        // Try to create the period
        setStart(start);
        setEnd(end);
        setPeriodType(periodType);

        // The parameters of the Period have been set
        assert (start != null && end != null && periodType != null);

        // The created Period is valid
        assert (isPeriodValid(start, end, periodType));
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
    public void setStart(LocalDate start) {
        if (start == null) {
            throw new IllegalArgumentException("The parameter 'start' is null");
        }

        // Check if the period is still valid with 'start'
        if (!isPeriodValid(start, end, periodType)) {
            throw new IllegalArgumentException("The period is no longer valid: start=" + start + ", end=" + end + ", periodType=" + periodType);
        }

        // Set the new start date of the Period
        this.start = start;
    }

    /**
     * Sets the start date of the period to the minimum possible date
     */
    public void setStartToMinimum() {
        DateTime dateTime = new DateTime();
        LocalDate newStart = new LocalDate(dateTime.year().withMinimumValue().
                monthOfYear().withMinimumValue().
                dayOfMonth().withMinimumValue()); // Minimum start date
        assert (newStart != null);

        setPeriodType(PeriodType.FREE); // Free type of period
        setStart(newStart); // Throws an exception if the Period is no longer valid

        assert (isPeriodValid(start, end, periodType)); // The period is still valid
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
    public void setEnd(LocalDate end) {
        if (end == null) {
            throw new IllegalArgumentException("The parameter 'end' is null");
        }

        // Check if the period is still valid with 'end'
        if (!isPeriodValid(start, end, periodType)) {
            throw new IllegalArgumentException("The period is no longer valid: start=" + start + ", end=" + end + ", periodType=" + periodType);
        }

        // Set the new end date of the Period
        this.end = end;
    }

    /**
     * Sets the end date of the period to the maximum possible date
     */
    public void setEndToMaximum() {
        DateTime dateTime = new DateTime();
        LocalDate newEnd = new LocalDate(dateTime.year().withMaximumValue().
                monthOfYear().withMaximumValue().
                dayOfMonth().withMaximumValue()); // Maximum end date
        assert (newEnd != null);

        setPeriodType(PeriodType.FREE); // Free type of period
        setEnd(newEnd); // Throws an exception if the Period is not valid anymore

        assert (isPeriodValid(start, end, periodType)); // The period is still valid
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
    public void setPeriodType(PeriodType periodType) {
        if (periodType == null) {
            throw new IllegalArgumentException("The parameter 'periodType' is null");
        }

        // Check if the period is still valid with 'periodType'
        if (!isPeriodValid(start, end, periodType)) { // The Period is not valid
            throw new IllegalArgumentException("The period is no longer valid: start=" + start + ", end=" + end + ", periodType=" + periodType);
        }

        // Set the new type of period
        this.periodType = periodType;
    }

    /**
     * Gets details of the Period
     * @return Details of the period <BR/><BR/>
     * <B>Example: </B>
     * <UL>
     * <LI><B>DAY</B>: "04/20/2006"</LI>
     * <LI><B>WEEK</B>: "From 01/01/2005 to 01/07/2005"</LI>
     * <LI><B>MONTH</B>: "From 01/01/2005 to 01/31/2005"</LI>
     * <LI><B>YEAR</B>: "From 01/01/2005 to 12/31/2005"</LI>
     * </UL>
     */
    public String getDetails() {
        String strFrom = ""; // Translation of "From"
        String strTo = ""; // Translation of "To"
        String details = ""; // Details about the period

        assert (start != null && end != null && periodType != null);

        // Gets the translations of "from" and "to"
        strFrom = "From";
        strTo = "To";
        assert (strFrom != null && strTo != null);

        if (periodType == PeriodType.DAY) {
            details = toString(); // April 14, 2006 (Depending on what is defined in Configuration.dateFormat)
        } else { // From 11/20/2005 to 11/27/2005
            details =
                    strFrom + " " + DateTimeFormat.longDate().withLocale(Locale.US).print(start.toDateMidnight()) + " " +
                    strTo + " " + DateTimeFormat.longDate().withLocale(Locale.US).print(end.toDateMidnight());
        }

        // Returns the string details of the period
        return details;
    }

    /**
     * Gets the number of days in the period
     * @return Number of days in the period<BR/>
     * <I>There are 2 days between 22.03.2006 and 24.03.2006 (24 - 22 = 2)</I>
     */
    public int getNumberOfDays() {
        assert (start != null && end != null);
        org.joda.time.Period period = new org.joda.time.Period(start, end, org.joda.time.PeriodType.days());
        assert (period != null);
        int numberOfDays = period.getDays();

        return numberOfDays;
    }

    /**
     * Gets the number of weeks in the period
     * @return Number of weeks in the period<BR/>
     * <I>There is only 1 week between 22.03.2006 and 30.05.2008 (30-22=8)</I>
     */
    public int getNumberOfWeeks() {
        assert (start != null && end != null);
        org.joda.time.Period period = new org.joda.time.Period(start, end, org.joda.time.PeriodType.weeks());
        assert (period != null);
        int numberOfWeeks = period.getWeeks();

        return numberOfWeeks;
    }

    /**
     * Gets the number of months in the period
     * @return Number of months in the period<BR/>
     * <I>There are 2 months between 22.03.2006 and 24.05.2006 (5 - 3 = 2)</I>
     */
    public int getNumberOfMonths() {
        assert (start != null && end != null);
        org.joda.time.Period period = new org.joda.time.Period(start, end, org.joda.time.PeriodType.months());
        assert (period != null);
        int numberOfMonths = period.getMonths();

        return numberOfMonths;
    }

    /**
     * Gets the number of years in the period
     * @return Number of years in the period<BR/>
     * <I>There are 2 years between 22.03.2006 and 24.05.2008 (2008-2006=2)</I>
     */
    public int getNumberOfYears() {
        assert (start != null && end != null);
        org.joda.time.Period period = new org.joda.time.Period(start, end, org.joda.time.PeriodType.years());
        assert (period != null);
        int numberOfYears = period.getYears();

        return numberOfYears;
    }

    /**
     * Compares the current period with another period
     * @param period Period to compare
     * @return 0 if the two periods are identic, -1 otherwise
     */
    @Override
    public int compareTo(Period period) {
        boolean periodsIdentic = false; // true if the two periods are identic

        // Make sure that the parameter is not null
        if (period == null) {
            throw new IllegalArgumentException("The parameter 'period' is null");
        }

        assert (start != null && end != null && periodType != null);
        assert (period.getStart() != null && period.getEnd() != null && period.getPeriodType() != null);

        // Same objects
        if (this == period) {
            return 0;
        }

        periodsIdentic =
                (start.compareTo(period.getStart()) == 0) &&
                (end.compareTo(period.getEnd()) == 0) &&
                (periodType.compareTo(period.getPeriodType()) == 0);

        // Returns 0 if the periods are identic, -1 if the periods are different
        if (periodsIdentic) {
            return 0;
        } else {
            return -1;
        }
    }


    /**
     * Is the specified period valid?
     * @param startDate Start date of the period
     * @param endDate End date of the period
     * @param periodType Type of the period
     * @return <B>true</B> if the period is valid (or if startDate=enDate=periodType=null), <B>false</B> otherwise<BR/>
     * To be valid, a period has to be a "full" period:
     * <UL>
     * <LI> For each type of period: Start date > End date</LI>
     * <LI> For DAY period: number of days between Start date and End date = 0<BR/>
     * Example: <I>From 12/30/2005 to 12/30/2005</I></LI>
     * <LI> For WEEK period: number of days between Start date and End date = 6<BR/>
     * The period has to start on SUNDAY or MONDAY (depending on what is defined in Configuration.weekStartingOn)<BR/>
     * The period has to end on SATURDAY or SUNDAY (depending on what is defined in Configuration.weekStartingOn)<BR/>
     * Example: <I>From 12/01/2005 to 12/18/2005</I></LI>
     * <LI> For MONTH period: number of month between Start date and End date = 0<BR/>
     * The period has to start on the first day of the month (05/01/2006)<BR/>
     * The period has to end on the last day of the month (05/31/2006)<BR/>
     * Number of months between Start date - 1 day and End date = 1<BR/>
     * Number of months between Start date and End date + 1 day = 1<BR/>
     * Example: <I>From 03/01/2005 to 03/31/2005</I></LI>
     * <LI> For YEAR period: number of years between Start date and End date = 0<BR/>
     * The period has to start on the first day of the first month (01/01/2006)<BR/>
     * The period has to end in December<BR/>
     * The period has to end on the last day of the last month (12/31/2006)<BR/>
     * Number of years between Start date - 1 day and End date = 1<BR/>
     * Number of years between Start date and End date + 1 day = 1<BR/>
     * Example: <I>From 01/01/2006 to 12/31/2006</I></LI>
     * </UL>
     */
    private boolean isPeriodValid(LocalDate startDate, LocalDate endDate, PeriodType periodType) {
        boolean periodValid = true; // Is the period valid

        // 1/ Check if Start date < End date
        if (startDate != null && endDate != null && startDate.compareTo(endDate) > 0) {
            periodValid = false;
        }

        // 2/ Check if the period is a "full" period
        if (periodValid && startDate != null && endDate != null && periodType != null) {
            switch (periodType) {
                case DAY:
                    // i.e. a DAY period: from 05/12/2006 to 05/12/2006
                    if ( (getNumberOfDays(startDate.minusDays(1), endDate) != 1) || // 12-11 = 1  (Nb of days = 1)
                            (getNumberOfDays(startDate, endDate) != 0) || // // 12-12 = 0 (Nb of days = 0)
                            (getNumberOfDays(startDate, endDate.plusDays(1)) != 1) ) { // 13-12 = 1 (Nb of days = 1)
                        periodValid = false;
                    }
                    break;
                case WEEK:
                    // i.e. a WEEK period: from 05/12/2006 to 05/18/2006
                    if ( (startDate.toDateTimeAtStartOfDay().getDayOfWeek() != DateTimeConstants.MONDAY) || // the week has to begin on MONDAY
                            (getNumberOfWeeks(startDate.minusDays(1), endDate) != 1) || // 18-11 = 7 (Nb of weeks = 1)
                            (getNumberOfWeeks(startDate, endDate) != 0) || // 18-12 = 6 (Nb of weeks = 0)
                            (getNumberOfWeeks(startDate, endDate.plusDays(1)) != 1) ) { // 19-12 = 7 (Nb of weeks = 1)
                        periodValid = false;
                    }
                    break;
                case MONTH:
                    // i.e. a MONTH period: from 05/01/2006 to 05/30/2006
                    if ( (startDate.getDayOfMonth() != 1) || // the first day of the period has to be the first day of the month
                            (getNumberOfMonths(startDate.minusDays(1), endDate) != 1) || // 5-4 = 1 (Nb of months = 1)
                            (getNumberOfMonths(startDate, endDate) != 0) || // 5-5 = 0 (Nb of months = 0)
                            (getNumberOfMonths(startDate, endDate.plusDays(1)) != 1) ) { // 6-5 = 1 (Nb of months = 1)
                        periodValid = false;
                    }
                    break;
                case YEAR:
                    // i.e. a YEAR period: from 01/01/2006 to 12/31/2006
                    if ( (startDate.getMonthOfYear() != 1 || startDate.getDayOfMonth() != 1) || // the first day of the period has to be the first day of the first month
                            (endDate.getMonthOfYear() != 12) || // the month of the end date of the period has to be December
                            (getNumberOfYears(startDate.minusDays(1), endDate) != 1) || // 2006-2005 = 1 (Nb of years = 1)
                            (getNumberOfYears(startDate, endDate) != 0) || // 2006-2006 = 0 (Nb of years = 0)
                            (getNumberOfYears(startDate, endDate.plusDays(1)) != 1) ) { // 2007-2006 = 1 (Nb of years = 1)
                        periodValid = false;
                    }
                    break;
                case FREE:
                    // There is no constraint on the number of units for FREE type of period
                    break;
                default:
                    // Should never happen
                    throw new AssertionError("The PeriodType is unknown");
            }
        }

        return periodValid;
    }

    /**
     * Gets the number of days between two dates
     * @param startDate Start date of the period
     * @param endDate End date of the period
     * @return Number of days between two dates
     */
    private static int getNumberOfDays(LocalDate startDate, LocalDate endDate) {
        assert (startDate != null && endDate != null);
        org.joda.time.Period period = new org.joda.time.Period(startDate, endDate, org.joda.time.PeriodType.days());
        assert (period != null);
        int numberOfDays = period.getDays();

        return numberOfDays;
    }

    /**
     * Gets the number of weeks between two dates
     * @param startDate Start date of the period
     * @param endDate End date of the period
     * @return Number of weeks between two dates
     */
    private static int getNumberOfWeeks(LocalDate startDate, LocalDate endDate) {
        assert (startDate != null && endDate != null);
        org.joda.time.Period period = new org.joda.time.Period(startDate, endDate, org.joda.time.PeriodType.weeks());
        assert (period != null);
        int numberOfWeeks = period.getWeeks();

        return numberOfWeeks;
    }

    /**
     * Gets the number of months between two dates
     * @param startDate Start date of the period
     * @param endDate End date of the period
     * @return Number of months between two dates
     */
    private static int getNumberOfMonths(LocalDate startDate, LocalDate endDate) {
        assert (startDate != null && endDate != null);
        org.joda.time.Period period = new org.joda.time.Period(startDate, endDate, org.joda.time.PeriodType.months());
        assert (period != null);
        int numberOfMonths = period.getMonths();

        return numberOfMonths;
    }

    /**
     * Gets the number of years between two dates
     * @param startDate Start date of the period
     * @param endDate End date of the period
     * @return Number of years between two dates
     */
    private static int getNumberOfYears(LocalDate startDate, LocalDate endDate) {
        assert (startDate != null && endDate != null);
        org.joda.time.Period period = new org.joda.time.Period(startDate, endDate, org.joda.time.PeriodType.years());
        assert (period != null);
        int numberOfYears = period.getYears();

        return numberOfYears;
    }

    /**
     * Does the current period contain a date?<BR/>
     * For example: If the start date of the period is 08/07/2006 and the end date is 08/14/2006<BR/>
     * Then:
     * <UL>
     * <LI>08/07/2006 is contained in the Period</LI>
     * <LI>08/14/2006 is contained in the Period</LI>
     * <LI>08/06/2006 is not contained in the Period</LI>
     * <LI>08/15/2006 is not contained in the Period</LI>
     * </UL>
     * @param date date to test
     * @return true if the current period contains the date, false otherwise
     */
    public boolean contains(LocalDate date) {
        boolean isDateContained = false;

        if (date == null) {
            throw new IllegalArgumentException("The parameter 'date' is null");
        }

        assert (start != null && end != null);

        // Check if the tested date is contained in the current period
        if (date.compareTo(start) >= 0 && date.compareTo(end) <= 0) {
            isDateContained = true;
        }

        return isDateContained;
    }

    /**
     * Is the day valid<BR/>
     * The constants of the class DateTimeConstants should be used
     * @param day Day to test
     * @return true if the day is valid (between Monday and Sunday), false otherwise
     */
    public static boolean isValidDay(int day) {
        return (day == DateTimeConstants.MONDAY || day == DateTimeConstants.TUESDAY ||
                day == DateTimeConstants.WEDNESDAY || day == DateTimeConstants.THURSDAY ||
                day == DateTimeConstants.FRIDAY || day == DateTimeConstants.SATURDAY ||
                day == DateTimeConstants.SUNDAY);
    }

    /**
     * Gets a date from a string, which has the following format: "Day/Month/Year"<BR/>
     * Example: getDate("20/1/2006");
     * @param date Date to convert
     * @return Converted date
     * @throws DateFormatException If the format of 'date' is not valid
     */
    public static LocalDate getDate(String date) throws DateFormatException {
        LocalDate convertedDate = null;   // Converted date
        String[] dateSplit;         // Table, which contains the 3 fields: day, month, year
        //                             - dateSplit[0] is supposed to contain the day
        //                             - dateSplit[1] is supposed to contain the month
        //                             - dateSplit[2] is supposed to contain the year
        int day;   // Day of the converted date
        int month; // Month of the converted date
        int year;  // Year of the converted date

        // Make sure that the parameter (date) is not null
        if (date == null) {
            throw new IllegalArgumentException("The parameter 'date' is null");
        }

        // Try to split the date into 3 strings: day, month, year
        dateSplit = date.split("/");

        // Make sure that the date string has been split into 3 elements
        if (dateSplit.length != 3) {
            throw new DateFormatException("The format of the date is incorrect");
        }

        // Try to get the day, the month and the year, and then try to create a date
        try {
            day = Integer.parseInt(dateSplit[0]);
            month = Integer.parseInt(dateSplit[1]);
            year = Integer.parseInt(dateSplit[2]);

            convertedDate = new LocalDate(year, month, day);
        } catch (NumberFormatException ex) {
            throw new DateFormatException("The format of the date is incorrect");
        } catch (IllegalFieldValueException ex) {
            throw new DateFormatException(ex.toString());
        }

        return convertedDate;
    }

    /**
     * Returns a string description of the Period
     * @return Description of the period <BR/><BR/>
     * <B>Example: </B>
     * <UL>
     * <LI><B>DAY</B>: "11/20/2005" (Depending on the format of the date defined in Configuration.dateFormat)</LI>
     * <LI><B>WEEK</B>: "43 (2005)"</LI>
     * <LI><B>MONTH</B>: "Jully 2005"</LI>
     * <LI><B>YEAR</B>: "2005"</LI>
     * <LI><B>FREE</B>: "From 11/20/2005 to 11/27/2005"</LI>
     * </UL>
     */
    @Override
    public String toString() {
        String strFrom = ""; // Translation of "From"
        String strTo = ""; // Translation of "To"
        String description = ""; // The string description of the period

        assert (start != null && end != null && periodType != null);

        // Gets the translations of "from" and "to"
        strFrom = "From";
        strTo = "To";
        assert (strFrom != null && strTo != null);

        // Creates the string description depending on the type of the period
        switch (periodType) {
            case DAY: // April 14, 2006 (depending on what is defined in Configuration.dateFormat)
                description = DateTimeFormat.longDate().withLocale(Locale.US).print(start.toDateMidnight());
                break;
            case WEEK: // 43 (2005)
                description = start.toDateTimeAtCurrentTime().getWeekOfWeekyear() + " (" + start.getYear() + ")";
                break;
            case MONTH: // Jully 2005
                description = start.monthOfYear().getAsShortText(Locale.US) + " " + start.getYear();
                break;
            case YEAR: // 2001
                description = String.valueOf(start.getYear());
                break;
            case FREE: // From 20/11/2005 to 27/11/2005 (Depending on what is defined in Configuration)
                description =
                        strFrom + " " + DateTimeFormat.longDate().withLocale(Locale.US).print(start.toDateMidnight()) + " " +
                        strTo + " " + DateTimeFormat.longDate().withLocale(Locale.US).print(end.toDateMidnight());
                break;
            default: // Should never happen
                throw new AssertionError("The PeriodType is unkwown");
        }

        // Returns the string description of the period
        assert (description != null);
        return description;
    }
}