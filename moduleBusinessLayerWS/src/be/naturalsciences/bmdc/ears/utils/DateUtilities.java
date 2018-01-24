/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.utils;

/**
 *
 * @author Thomas Vandenberghe
 */
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtilities {

    // format : YYYY-MM-DD hh:mm:ss
    public static final String DATETIME_FORMAT_AS_STRING = "yyyy-MM-dd'T'HH:mm:ss";
    public static final DateFormat DATETIME_FORMAT = new SimpleDateFormat(DATETIME_FORMAT_AS_STRING);

    public static final String DATE_FORMAT_AS_STRING = "yyyy-MM-dd";
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_AS_STRING);

    public static boolean isBetween(OffsetDateTime test, OffsetDateTime start, OffsetDateTime stop) {
        return !(test.isBefore(start) || test.isAfter(stop));
    }

    public static Date parseDate(String dateAsString) throws ParseException {
        return DATETIME_FORMAT.parse(dateAsString);
    }

    public static String formatDateTime(Date date) {
        return DATETIME_FORMAT.format(date);
    }

    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    public static OffsetDateTime getOffsetDateTime(Date date) {
        return OffsetDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static String extractSymFromDate(String sDate) {
        String utfDate;

        sDate = sDate.replace(" ", "");
        sDate = sDate.replace(":", "");
        sDate = sDate.replace("-", "");
        sDate = sDate.replace("T", "");

        //Remove "\0" for some errores when marshaling with JAX
        sDate = sDate.replace((char) 0, (char) 13);
        return sDate;
    }

    public TimeParts getTimeParts(Date date) {
        TimeParts tp = new TimeParts();
        tp.setYear(2015);
        tp.setMonth(10);
        tp.setDay(14);
        tp.setHour(12);
        tp.setMin(30);
        tp.setSecs(00);
        return tp;
    }

    public TimeParts setTimeParts() {
        return new TimeParts();
    }

    class TimeParts {

        private int year;
        private int month;
        private int day;
        private int hour;
        private int min;
        private int secs;

        public TimeParts() {
            super();
            // TODO Auto-generated constructor stub
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }

        public int getSecs() {
            return secs;
        }

        public void setSecs(int secs) {
            this.secs = secs;
        }

        public boolean canHaveAsPeriod(Date dStartDate, Date dEndDate) {
            if (dEndDate == null || dStartDate == null) {
                return false;
            } else {
                if (dEndDate.before(dStartDate) || dStartDate.after(dEndDate)) {
                    return false;
                }
                return true;
            }
        }

        public boolean canHaveAsPeriod(String startDate, String endDate) {
            Date dStartDate;
            Date dEndDate;
            try {
                dStartDate = parseDate(startDate);
                dEndDate = parseDate(endDate);
            } catch (ParseException ex) {
                return false;
            }

            if (dEndDate == null || dStartDate == null) {
                return false;
            } else {
                if (dEndDate.before(dStartDate) || dStartDate.after(dEndDate)) {
                    return false;
                }
                return true;
            }
        }

    }
}
