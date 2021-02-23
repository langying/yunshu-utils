package com.yunshu.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class IO {

    public static void close(Closeable... handles) {
        for (Closeable handle : handles) {
            try {
                if (handle != null) {
                    handle.close();
                }
            }
            catch (Exception e) {
            }
        }
    }

    public static void sleep(long millis) {
        if (millis <= 0) {
            return;
        }
        try {
            Thread.sleep(millis);
        }
        catch (Exception e) {
        }
    }

    public static void sleep(long millis, CallAction action) {
        try {
            long start = System.currentTimeMillis();
            action.run();
            long dtime = millis - (System.currentTimeMillis() - start);
            if (dtime > 0) {
                Thread.sleep(dtime);
            }
        }
        catch (InterruptedException e) {
        }
    }

    public static String read(File file) {
        return read(file, null);
    }

    public static String read(File file, String charset) {
        try {
            return read(new FileInputStream(file), charset);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String read(InputStream inStream) {
        return read(inStream, null);
    }

    public static String read(InputStream inStream, String charset) {
        ByteArrayOutputStream otStream = null;
        try {
            byte[] buffer = new byte[1024];
            otStream = new ByteArrayOutputStream();
            for (int len = 0; (len = inStream.read(buffer)) != -1;) {
                otStream.write(buffer, 0, len);
            }
            return otStream.toString(charset == null ? "UTF-8" : charset);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
            IO.close(inStream, otStream);
        }
    }

    public static boolean in(Integer value, Integer... args) {
        if (args == null || args.length <= 0) {
            return false;
        }
        for (Integer one : args) {
            if (one == value) {
                return true;
            }
        }
        return false;
    }

    public static boolean in(Long value, Long... args) {
        if (args == null || args.length <= 0) {
            return false;
        }
        for (Long one : args) {
            if (one == value) {
                return true;
            }
        }
        return false;
    }

    public static <T> String join(T[] arr, String tag) {
        if (arr == null || arr.length <= 0) {
            return "";
        }
        StringBuilder txt = new StringBuilder();
        for (int i = 0, l = arr.length - 1; i <= l; i++) {
            if (i == l) {
                txt.append(arr[i]);
            }
            else {
                txt.append(arr[i]).append(tag);
            }

        }
        return txt.toString();
    }

    public static <T> String join(List<T> arr, String tag) {
        if (arr == null || arr.size() <= 0) {
            return "";
        }
        StringBuilder txt = new StringBuilder();
        for (int i = 0, l = arr.size() - 1; i <= l; i++) {
            if (i == l) {
                txt.append(arr.get(i));
            }
            else {
                txt.append(arr.get(i)).append(tag);
            }
        }
        return txt.toString();
    }

    public static <K, V> int forEach(Map<K, V> map, MapAction<K, V> action) {
        int idx = 0;
        if (map == null || map.size() <= 0) {
            return idx;
        }
        AtomicBoolean stop = new AtomicBoolean(false);
        for (Map.Entry<K, V> e : map.entrySet()) {
            action.run(e.getKey(), e.getValue(), idx++, stop);
            if (stop.get()) {
                break;
            }
        }
        return idx;
    }

    public static <E> int forEach(E[] list, CollectionAction<E> action) {
        int idx = 0;
        if (list == null || list.length <= 0) {
            return idx;
        }

        AtomicBoolean stop = new AtomicBoolean(false);
        for (E item : list) {
            action.run(item, idx++, stop);
            if (stop.get()) {
                break;
            }
        }
        return idx;
    }

    public static <E> int forEach(Collection<E> collection, CollectionAction<E> action) {
        int idx = 0;
        if (collection == null || collection.size() <= 0) {
            return idx;
        }

        AtomicBoolean stop = new AtomicBoolean(false);
        for (E item : collection) {
            action.run(item, idx++, stop);
            if (stop.get()) {
                break;
            }
        }
        return idx;
    }

    public static <E> int forEach(Enumeration<E> enumeration, CollectionAction<E> action) {
        int idx = 0;
        if (enumeration == null) {
            return idx;
        }

        AtomicBoolean stop = new AtomicBoolean(false);
        while (enumeration.hasMoreElements()) {
            action.run(enumeration.nextElement(), idx++, stop);
            if (stop.get()) {
                break;
            }
        }
        return idx;
    }

    public static int forEach(File file, CollectionAction<String> action) {
        int idx = 0;
        InputStream inStream = null;
        InputStreamReader inReader = null;
        BufferedReader inBuffer = null;

        try {
            inStream = new FileInputStream(file);
            inReader = new InputStreamReader(inStream, ID.kUTF8);
            inBuffer = new BufferedReader(inReader);

            AtomicBoolean stop = new AtomicBoolean(false);
            for (String text = null; (text = inBuffer.readLine()) != null;) {
                action.run(text, idx++, stop);
                if (stop.get()) {
                    break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            IO.close(inBuffer, inReader, inStream);
        }
        return idx;
    }

    public static int forEach(File file, MapAction<String, String> action) {
        int idx = 0;
        InputStream inStream = null;
        try {
            inStream = new FileInputStream(file);
            Properties props = new Properties();
            props.load(inStream);
            AtomicBoolean stop = new AtomicBoolean(false);
            for (Map.Entry<Object, Object> e : props.entrySet()) {
                String k = e.getKey().toString();
                Object v = e.getValue();
                action.run(k, v == null ? "" : v.toString(), idx++, stop);
            }
        }
        catch (Exception e) {
        }
        finally {
            IO.close(inStream);
        }
        return idx;
    }

    public static int forEach(String text, String tag, CollectionAction<String> action) {
        if (text == null) {
            return 0;
        }

        int idx = 0;
        int len = tag.length();
        AtomicBoolean stop = new AtomicBoolean(false);
        for (int p1 = 0, p2 = 0; p2 != -1;) {
            p2 = text.indexOf(tag, p1);
            String line;
            if (p2 == -1) {
                line = text.substring(p1);
            }
            else {
                line = text.substring(p1, p2);
                p1 = p2 + len;
            }
            action.run(line, idx++, stop);
            if (stop.get()) {
                break;
            }
        }
        return idx;
    }

    public static int forEach(InputStream inStream, MobileAction action) throws Exception {
        int idx = 0;
        StringBuilder tel = new StringBuilder();
        AtomicBoolean stop = new AtomicBoolean(false);
        for (int ch = -1; (ch = inStream.read()) != -1;) {
            if ('0' <= ch && ch <= '9') {
                tel.append((char) ch);
                continue;
            }
            if (tel.length() == 11) {
                action.run(tel.toString(), idx++, stop);
                if (stop.get()) {
                    break;
                }
            }
            tel.setLength(0);
        }
        if (tel.length() == 11 && !stop.get()) {
            action.run(tel.toString(), idx++, stop);
        }
        return idx;
    }

    public static void write(String file, byte[] data) {
        FileOutputStream ots = null;
        try {
            ots = new FileOutputStream(file);
            ots.write(data);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            IO.close(ots);
        }
    }

    public static int write(String file, WriteAction action) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            int idx = 0;
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            for (idx = 0; idx < Integer.MAX_VALUE; idx++) {
                String val = action.run(idx);
                if (val == null) {
                    break;
                }
                bw.write(val);
            }
            return idx;
        }
        catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        finally {
            IO.close(bw, fw);
        }
    }

    public static <E> int write(String file, Collection<E> c, WriteCollectionAction<E> action) {
        if (c == null || c.size() <= 0) {
            return 0;
        }

        int size = 0;
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            for (E e : c) {
                String val = action.run(e, size++);
                if (val == null) {
                    break;
                }
                bw.write(val);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        finally {
            IO.close(bw, fw);
        }
        return size;
    }

    public static <K, V> int write(String file, Map<K, V> map, WriteMapAction<K, V> action) {
        if (map == null || map.size() <= 0) {
            return 0;
        }

        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            int size = 0;
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            for (Map.Entry<K, V> entry : map.entrySet()) {
                String val = action.run(entry.getKey(), entry.getValue(), size++);
                if (val == null) {
                    break;
                }
                bw.write(val);
            }
            return size;
        }
        catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        finally {
            IO.close(bw, fw);
        }
    }

    public static String getHomePath(String name) {
        String path = System.getenv("HOME");
        if (name == null) {
            return path;
        }
        else {
            return path + "/" + name;
        }
    }

    // /Users/hanqiong/Documents/WorkSpaces/MyEclipse/schedule/schedule-service/target/classes/
    public static String getClassPath(String name) {
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        if (url == null) {
            url = IO.class.getClassLoader().getResource("");
        }
        if (name == null) {
            return url.getPath();
        }
        else {
            return url.getPath() + name;
        }
    }

    public static byte[] copy(byte[] src, int len) {
        if (src.length == len) {
            return src;
        }
        byte[] dst = new byte[len];
        System.arraycopy(src, 0, dst, 0, Math.min(src.length, len));
        return dst;
    }

    public static int copy(File src, File dst, boolean override) throws IOException {
        InputStream in = null;
        OutputStream ot = null;
        try {
            File path = dst.getParentFile();
            if (!path.exists()) {
                path.mkdirs();
            }

            if (override) {
                dst.delete();
            }

            in = new FileInputStream(src);
            ot = new FileOutputStream(dst);
            return copy(in, ot);
        }
        finally {
            IO.close(in, ot);
        }
    }

    public static int copy(InputStream src, OutputStream dst) throws IOException {
        int num = 0;
        byte[] buffer = new byte[4096];
        for (int len = 0; (len = src.read(buffer)) != -1;) {
            num += len;
            dst.write(buffer, 0, len);
        }
        dst.flush();
        return num;
    }

    public static <E> void sub(Collection<E> set, int size, SubAction<E> action) {
        if (set == null || set.isEmpty()) {
            return;
        }
        if (set.size() <= size) {
            action.run(set);
            return;
        }

        List<E> sub = new ArrayList<>();
        for (E val : set) {
            sub.add(val);
            if (sub.size() >= size) {
                action.run(sub);
                sub.clear();
            }
        }
        if (sub.size() > 0) {
            action.run(sub);
            sub.clear();
        }
    }

    public static <K, V> void submap(Map<K, V> map, int size, SubMapAction<K, V> action) {
        if (map == null || map.size() <= 0) {
            return;
        }
        if (map.size() <= size) {
            action.run(map);
            return;
        }

        Map<K, V> sub = new HashMap<>();
        for (Map.Entry<K, V> e : map.entrySet()) {
            sub.put(e.getKey(), e.getValue());
            if (sub.size() >= size) {
                action.run(sub);
                sub.clear();
            }
        }
        if (sub.size() > 0) {
            action.run(sub);
            sub.clear();
        }
    }

    public static <E> void subset(Set<E> set, int size, SubsetAction<E> action) {
        if (set == null || set.size() <= 0) {
            return;
        }
        if (set.size() <= size) {
            action.run(set);
            return;
        }

        Set<E> sub = new HashSet<>();
        for (E val : set) {
            sub.add(val);
            if (sub.size() >= size) {
                action.run(sub);
                sub.clear();
            }
        }
        if (sub.size() > 0) {
            action.run(sub);
            sub.clear();
        }
    }

    public static <E> void sublist(List<E> list, int size, SublistAction<E> action) {
        if (list == null || list.size() <= 0) {
            return;
        }
        for (int idx = 0, len = list.size(); idx < len; idx++) {
            int stt = (idx + 0) * size;
            int end = (idx + 1) * size;
            if (end < len) {
                action.run(list.subList(stt, end), idx, stt, end);
            }
            else {
                action.run(list.subList(stt, len), idx, stt, len);
                break;
            }
        }
    }

    public static void subtime(Date sttTime, Date endTime, long dtime, SubTimeAction action) {
        long stt = sttTime.getTime();
        long end = endTime.getTime();
        for (long tm1 = stt; tm1 < end; tm1 += dtime) {
            long tm2 = tm1 + dtime;
            if (tm2 < end) {
                action.run(new Date(tm1), new Date(tm2));
            }
            else {
                action.run(new Date(tm1), new Date(end));
            }
        }
    }

    public static <K, V> void stream(Collection<K> list, int size, StreamAction<K, V> mid, StreamListAction<V> ret) {
        if (list == null || list.size() <= 0) {
            return;
        }

        List<V> set = new ArrayList<>();
        for (K val : list) {
            set.add(mid.run(val));
            if (set.size() >= size) {
                ret.run(set);
                set.clear();
            }
        }
        if (set.size() > 0) {
            ret.run(set);
            set.clear();
        }
    }

    public static boolean in(String src, Object... dst) {
        for (Object one : dst) {
            if (src.equals(one.toString())) {
                return true;
            }
        }
        return false;
    }

    public static boolean contains(String src, Object... dst) {
        for (Object one : dst) {
            if (src.contains(one.toString())) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsIgnoreCase(String src, Object... dst) {
        src = src == null ? "" : src.toLowerCase();
        for (Object one : dst) {
            if (src.contains(one.toString().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    @FunctionalInterface
    public interface CallAction {
        void run();
    }

    @FunctionalInterface
    public interface MobileAction {
        void run(String tel, int idx, AtomicBoolean stop);
    }

    @FunctionalInterface
    public interface MapAction<K, V> {
        void run(K k, V v, int idx, AtomicBoolean stop);
    }

    @FunctionalInterface
    public interface CollectionAction<T> {
        void run(T t, int idx, AtomicBoolean stop);
    }

    @FunctionalInterface
    public interface WriteAction {
        String run(int idx);
    }

    @FunctionalInterface
    public interface WriteMapAction<K, V> {
        String run(K k, V v, int idx);
    }

    @FunctionalInterface
    public interface WriteCollectionAction<E> {
        String run(E e, int idx);
    }

    @FunctionalInterface
    public interface SubAction<E> {
        void run(Collection<E> set);
    }

    @FunctionalInterface
    public interface SubMapAction<K, V> {
        void run(Map<K, V> map);
    }

    @FunctionalInterface
    public interface SubsetAction<E> {
        void run(Set<E> set);
    }

    @FunctionalInterface
    public interface SublistAction<E> {
        void run(List<E> sub, int idx, int stt, int end);
    }

    @FunctionalInterface
    public interface SubTimeAction {
        void run(Date sttTime, Date endTime);
    }

    @FunctionalInterface
    public interface StreamAction<K, V> {
        V run(K key);
    }

    @FunctionalInterface
    public interface StreamListAction<E> {
        void run(List<E> key);
    }
}
