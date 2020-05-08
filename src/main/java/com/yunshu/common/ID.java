package com.yunshu.common;

import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

public class ID {

    public static final char kEOF = 0;

    public static final String RF      = "referer";
    public static final String UA      = "user-agent";
    public static final String IP      = "ip";
    public static final String API     = "api";
    public static final String TTID    = "ttid";
    public static final String UDID    = "udid";
    public static final String UUID    = "uuid";
    public static final String HOST    = "host";
    public static final String SHOP    = "shop";
    public static final String SIGN    = "sign";
    public static final String STAT    = "stat";
    public static final String USER    = "user";
    public static final String TOKEN   = "token";
    public static final String KAPTCHA = "kaptcha";

    private static AtomicInteger mIntID = new AtomicInteger(0);

    public static final String kEmpty = "";

    public static final int kSQLSize = 4000;
    public static final int kTimeOut = 3000;

    public static final Charset kGBK   = Charset.forName("GBK");
    public static final Charset kUTF8  = Charset.forName("UTF-8");
    public static final Charset kASCII = Charset.forName("ASCII");
    public static final Charset kUCS2  = Charset.forName("ISO-10646-UCS-2");

    public static int next() {
        return mIntID.getAndIncrement();
    }

    public static boolean isAlpha(char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == '_' || ch == '*' || ch == '%' || ch > 255;
    }

    public static boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    public static boolean isWhite(char ch) {
        return ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r';
    }

    public static boolean equals(char ch, char... args) {
        for (int idx = 0, len = (args == null ? 0 : args.length); idx < len; idx++) {
            if (ch == args[idx]) {
                return true;
            }
        }
        return false;
    }

    public static boolean unequals(char ch, char... args) {
        for (int idx = 0, len = (args == null ? 0 : args.length); idx < len; idx++) {
            if (ch == args[idx]) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Calendar c = Calendar.getInstance();
        long t = c.getTimeInMillis();
        System.out.println(TM.toText(t));
    }
}
