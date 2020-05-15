package com.yunshu.common;

import java.math.BigInteger;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import com.alibaba.fastjson.JSON;
import com.yunshu.common.IO.CollectionAction;

public class FF {

    public static final String yyyyMMdd0       = "yyyyMMdd";
    public static final String yyyyMMdd1       = "yyyy-MM-dd";
    public static final String yyyyMMddHHmmss0 = "yyyyMMddHHmmss";
    public static final String yyyyMMddHHmmss1 = "yyyy-MM-dd HH:mm:ss";
    public static final String yyyyMMddHHmmssE = "yyyy-MM-dd HH:mm:ss E";

    public static int len(String str) {
        return str == null ? 0 : str.length();
    }

    public static boolean isLen(String str, int len) {
        return str == null ? false : str.length() == len;
    }

    public static boolean isEmpty(String txt) {
        return txt == null ? true : txt.length() <= 0;
    }

    public static boolean isSeparate(char c) {
        return c == ' ' || c == '\t' || c == '\r' || c == '\n';
    }

    public static void println(String txt, Object... args) {
        System.out.println(fmt(txt, "{}", args));
    }

    public static void cookie(String cookies, IO.MapAction<String, String> action) throws Exception {
        if (cookies == null || cookies.length() <= 0) {
            return;
        }

        String[] items = cookies.split(";");
        AtomicBoolean stop = new AtomicBoolean(false);
        for (int i = 0, l = items.length; i < l; i++) {
            String[] kv = FF.cut(items[i], "=");
            String k = kv[0].trim();
            String v = URLDecoder.decode(kv[1].trim(), "UTF-8");
            action.run(k, v, i, stop);
            if (stop.get()) {
                break;
            }
        }
    }

    public static int toInt(String txt, int val) {
        try {
            return Integer.parseInt(txt);
        }
        catch (Exception e) {
            return val;
        }
    }

    public static String toText(String txt, String val) {
        if (txt == null || txt.length() <= 0) {
            return val;
        }
        return txt;
    }

    public static long toLong(String txt, long val) {
        try {
            return Long.parseLong(txt);
        }
        catch (Exception e) {
            return val;
        }
    }

    public static float toFloat(String txt, float val) {
        try {
            return Float.parseFloat(txt);
        }
        catch (Exception e) {
            return val;
        }
    }

    public static double toDouble(String txt, double val) {
        try {
            return Double.parseDouble(txt);
        }
        catch (Exception e) {
            return val;
        }
    }

    public static Date toDate(String date, String fmt) {
        try {
            return new SimpleDateFormat(fmt, Locale.CHINESE).parse(date);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static Date toDate(String date, SimpleDateFormat fmt) {
        try {
            return fmt.parse(date);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static String toText(Date date, String fmt) {
        try {
            date = (date == null) ? new Date() : date;
            return new SimpleDateFormat(fmt, Locale.CHINESE).format(date);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static String toJson(Object... args) {
        Map<Object, Object> map = new HashMap<>();
        for (int i = 0, l = args.length / 2; i < l; i++) {
            map.put(args[i * 2 + 0], args[i * 2 + 1]);
        }
        return JSON.toJSONString(map);
    }

    public static String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static String trim(String src) {
        return src == null ? "" : src.trim();
    }

    public static String trim(String src, String val) {
        return src == null ? val : src.trim();
    }

    public static String log(String txt, Object... args) {
        return fmt(txt, "{}", args);
    }

    public static String fmt(String txt, String tag, Object... args) {
        int size = txt == null ? 0 : txt.length();
        if (size <= 0) {
            return txt;
        }

        StringBuilder ret = new StringBuilder(size * 2);
        for (int i = 0, idx = 0, pos = 0, len = (args == null ? 0 : args.length); i <= len; i++) {
            pos = txt.indexOf(tag, idx);
            if (i == len || pos == -1) {
                ret.append(txt.substring(idx));
                break;
            }
            else {
                String str = txt.substring(idx, pos);
                ret.append(str).append(args[i]);
                idx = pos + tag.length();
            }
        }
        return ret.toString();
    }

    public static String[] cut(String str, String tag) {
        int idx = str.indexOf(tag);
        if (idx == -1) {
            return new String[] { str, "" };
        }
        else {
            return new String[] { str.substring(0, idx), str.substring(idx + tag.length()) };
        }
    }

    public static int cut(String line, IO.CollectionAction<String> action) {
        int idx = 0;
        if (line == null || line.length() <= 0) {
            return idx;
        }

        StringBuilder text = new StringBuilder();
        AtomicBoolean stop = new AtomicBoolean(false);
        for (int i = 0, l = line.length(); i <= l; i++) {
            char c = i == l ? ' ' : line.charAt(i);
            if (isSeparate(c)) {
                if (text.length() > 0) {
                    action.run(text.toString(), idx++, stop);
                    if (stop.get()) {
                        break;
                    }
                    text.setLength(0);
                }
            }
            else {
                text.append(c);
            }
        }
        return idx;
    }

    public static String[] split(String str, String tag, int size) {
        String[] vals = str.split(tag);
        String[] list = new String[size];
        int len = vals.length < size ? vals.length : size;
        for (int idx = 0; idx < len; idx++) {
            list[idx] = vals[idx];
        }
        for (int idx = len; idx < size; idx++) {
            list[idx] = "";
        }
        return list;
    }

    public static int split(String str, String tag, CollectionAction<String> action) {
        int num = 0;
        if (tag == null || str == null || str.length() <= 0) {
            return num;
        }

        String[] list = str.split(tag);
        AtomicBoolean stop = new AtomicBoolean(false);
        for (String item : list) {
            action.run(item, num++, stop);
            if (stop.get()) {
                break;
            }
        }
        return num;
    }

    public static String replace(String src, String old, String New) {
        if (src == null) {
            return src;
        }
        return src.replaceAll(old, New);
    }

    public static <E> String concat(Collection<E> c, String tag) {
        return concat(c, tag, null, null);
    }

    public static <E> String concat(Collection<E> c, String tag, String prefix, String suffix) {
        if (c == null || c.size() <= 0) {
            return null;
        }
        if (tag == null) {
            tag = ",";
        }
        StringBuilder ret = new StringBuilder();
        for (E e : c) {
            if (prefix != null) {
                ret.append(prefix);
            }
            ret.append(e);
            if (suffix != null) {
                ret.append(suffix);
            }
            ret.append(tag);
        }
        ret.delete(ret.length() - tag.length(), ret.length());
        return ret.toString();
    }

    /** List: Arrays.asList<T>(); */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> asMap(Object... args) {
        Map<K, V> map = new HashMap<>();
        for (int idx = 0, len = args.length / 2; idx < len; idx++) {
            K k = (K) args[idx * 2 + 0];
            V v = (V) args[idx * 2 + 1];
            map.put(k, v);
        }
        return map;
    }
}
