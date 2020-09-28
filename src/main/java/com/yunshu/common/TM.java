package com.yunshu.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.yunshu.common.ext.CMPP;

public class TM {

    public static final long SECOND = 1000L;
    public static final long MINUTE = 1000L * 60;
    public static final long HOUR   = 1000L * 3600;
    public static final long DAY    = 1000L * 3600 * 24;
    public static final long WEEK   = 1000L * 3600 * 24 * 7;
    public static final long MONTH  = 1000L * 3600 * 24 * 30;
    public static final long YEAR   = 1000L * 3600 * 24 * 365;

    public static final SimpleDateFormat FMT1 = new SimpleDateFormat("yyyyMMdd", Locale.CHINESE);
    public static final SimpleDateFormat FMT2 = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINESE);
    public static final SimpleDateFormat FMT3 = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.CHINESE);
    public static final SimpleDateFormat FMT4 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
    public static final SimpleDateFormat FMT5 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
    public static final SimpleDateFormat FMT6 = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINESE);
    public static final SimpleDateFormat FMT7 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINESE);

    private static final AtomicLong mSMID = new AtomicLong(0);
    private static final AtomicLong mUUID = new AtomicLong(0);
    private static final AtomicLong mTime = new AtomicLong(0);

    private static final ScheduledExecutorService mTask;

    static {
        update();
        mTask = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable, TM.class.getName());
                thread.setDaemon(true);
                return thread;
            }
        });
        mTask.scheduleAtFixedRate(() -> {
            update();
        }, 1000, 1000, TimeUnit.MILLISECONDS);
    }

    public static Date toDate(String txt) {
        return toDate(txt, null);
    }

    public static Date toDate(String txt, Date val) {
        try {
            int len = txt == null ? 0 : txt.length();
            switch (len) {
            case 8: {
                return FMT1.parse(txt); // yyyyMMdd
            }
            case 10: {
                if (txt.indexOf('-') > 0) {
                    return FMT4.parse(txt); // yyyy-MM-dd
                }
                else {
                    return FMT6.parse(txt); // yyyy/MM/dd
                }
            }
            case 14: {
                return FMT2.parse(txt); // yyyyMMddHHmmss
            }
            case 17: {
                return FMT3.parse(txt); // yyyyMMddHHmmssSSS
            }
            case 19: {
                if (txt.indexOf('-') > 0) {
                    return FMT5.parse(txt); // yyyy-MM-dd HH:mm:ss
                }
                else {
                    return FMT7.parse(txt); // yyyy/MM/dd HH:mm:ss
                }
            }
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return val;
    }

    public static Date toDate(int time) {
        try {
            return FMT1.parse(String.valueOf(time));
        }
        catch (ParseException e) {
        }
        return null;
    }

    public static Date toDate(long time) {
        try {
            return FMT2.parse(String.valueOf(time));
        }
        catch (ParseException e) {
        }
        return null;
    }

    public static int toInt(Date date) {
        String time = FMT1.format(date == null ? time() : date);
        return Integer.parseInt(time);
    }

    public static int toInt(long millis) {
        String time = FMT1.format(new Date(millis));
        return Integer.parseInt(time);
    }

    private static void update() {
        Calendar c = Calendar.getInstance();
        int yy = c.get(Calendar.YEAR) % 100;
        int MM = c.get(Calendar.MONTH) + 1;
        int dd = c.get(Calendar.DAY_OF_MONTH);
        int HH = c.get(Calendar.HOUR_OF_DAY);
        int mm = c.get(Calendar.MINUTE);
        int ss = c.get(Calendar.SECOND);

        long dt = yy * 10000000000L + MM * 100000000 + dd * 1000000 + HH * 10000 + mm * 100 + ss;
        mTime.set(c.getTimeInMillis());
        mSMID.set(CMPP.msgId(MM, dd, HH, mm, ss, 0, 0));
        mUUID.set(dt * 10000000);
    }

    public static long toLong(Date date) {
        String time = FMT2.format(date == null ? time() : date);
        return Long.parseLong(time);
    }

    public static String toText(long millis) {
        return FMT2.format(new Date(millis));
    }

    public static String toText(Date date) {
        return FMT2.format(date == null ? time() : date);
    }

    public static long toLong(long millis) {
        String time = FMT2.format(new Date(millis));
        return Long.parseLong(time);
    }

    /**
     * 返回当天的起始时间和结束时间
     *
     * @param 20170730
     * @return [2017-07-30 00:00:00, 2017-07-31 00:00:00]
     */
    public static Date[] toOneDay(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long time = c.getTimeInMillis();
        Date d1 = new Date(time);
        Date d2 = new Date(time + 24 * 3600 * 1000);
        return new Date[] { d1, d2 };
    }

    public static Date[] day(Date date, int before, int after) {
        if (date == null) {
            date = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long time = c.getTimeInMillis();
        Date d1 = new Date(time + DAY * before);
        Date d2 = new Date(time + DAY * after);
        return new Date[] { d1, d2 };
    }

    public static long now() {
        return mTime.get();
    }

    public static Date time() {
        return new Date(mTime.get());
    }

    public static long uuid() {
        return mUUID.getAndIncrement();
    }

    public static long msgId(long gateId) {
        gateId &= 0x3FFFFF;
        long id = mSMID.getAndIncrement();
        id |= (gateId << 16);
        return id;
    }

    /** Calendar.MONDAY */
    public static Date week(int which) {
        Calendar rili = Calendar.getInstance();
        rili.set(Calendar.DAY_OF_WEEK, which);
        return rili.getTime();
    }

    public static Date getDate(int num) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(now());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return new Date(c.getTimeInMillis() + num * DAY);
    }

    public static Date getDay(Date date, int num) {
        if (date == null) {
            date = new Date();
        }

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return new Date(c.getTimeInMillis() + num * DAY);
    }

    public static Date getMonth(Date date, int num) {
        if (date == null) {
            date = new Date();
        }

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) + num);
        return new Date(c.getTimeInMillis());
    }
}
