/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tec02.Time;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author Administrator
 */
public class TimeBase {

    public static final String DD__MM__YYYY = "dd-MM-yyyy";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String HH_MM_SS = "HH : mm : ss";
    public static final String SIMPLE_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String MM_DD_YYYY_HH_MM_SS = "MM/dd/yyyy HH:mm:ss";
    public static final String DATE_TIME_MS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final TimeZone UTC = TimeZone.getTimeZone("UTC");
    public static final TimeZone UTC7 = TimeZone.getTimeZone("Asia/Saigon");
    private static final String CUSTOMENT_SERVER = "http://10.90.0.15:8000/auth/login//";
    private static final String WEB_SERVER = "http://time.windows.com";
    private final TimeZone defaultTimeZone;

    public String conVertToFormat(String timeString, String format, String formatNew) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            Date date =  dateFormat.parse(timeString);
            return simpleDateTimeFormat(date, formatNew);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public TimeBase() {
        this(UTC);
    }
    public TimeBase(TimeZone defaultTimeZone) {
        this.defaultTimeZone = defaultTimeZone;
    }

    public String simpleDateTimeFormat(Date time, String format) {
        return simpleDateTimeFormat(defaultTimeZone, time, format);
    }

    public String simpleDateTimeFormat(TimeZone timeZone, Date time, String format) {
        try {
            SimpleDateFormat simFormat = new SimpleDateFormat(format);
            simFormat.setTimeZone(timeZone);
            return simFormat.format(time);
        } catch (Exception e) {
            return null;
        }
    }

    public String getTimeZone() {
        TimeZone tz = Calendar.getInstance().getTimeZone();
        return tz.getID();
    }

    public String getDateTime(TimeZone timeZone, String fomat) {
        try {
            return simpleDateTimeFormat(timeZone, Calendar.getInstance().getTime(), fomat);
        } catch (Exception ex) {
            return null;
        }
    }

    public String getDateTime(String fomat) {
        try {
            return TimeBase.this.simpleDateTimeFormat(Calendar.getInstance().getTime(), fomat);
        } catch (Exception ex) {
            return null;
        }
    }

    public Date getWebsiteDatetime(String URL) {
        try {
            URL url = new URL(URL);
            URLConnection uc = url.openConnection();
            uc.connect();
            return new Date(uc.getDate());
        } catch (IOException ex) {
            return null;
        }
    }

    public long getCurrentMillis() {
        return System.currentTimeMillis();
    }

    public String getSimpleDateTime() {
        return getDateTime(TimeBase.SIMPLE_DATE_TIME);
    }

    public String getDate() {
        return getDateTime(TimeBase.YYYY_MM_DD);
    }

    public String getSimpleDateTime(TimeZone timeZone) {
        return getDateTime(timeZone, TimeBase.SIMPLE_DATE_TIME);
    }

    public String getDate(TimeZone timeZone) {
        return getDateTime(timeZone, TimeBase.YYYY_MM_DD);
    }
}
