package com.yunshu.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Java Language
 */
public class JM {

    public static Map<String, Object> asMap(Object obj, String... keys) {
        Map<String, Object> map = new HashMap<>();
        for (String key : keys) {
            Object val = get(obj, key);
            map.put(key, val);
        }
        return map;
    }

    public static Object get(Object map, String key) {
        Class<?> cls = map.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            try {
                String name = field.getName();
                if (key.equalsIgnoreCase(name)) {
                    field.setAccessible(true);
                    Object value = field.get(map);
                    return value;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void setIfNull(Object thiz, Object... vals) {
        Field[] fields = thiz.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object obj = field.get(thiz);
                if (obj != null) {
                    continue;
                }

                Class<?> clazz = field.getType();
                for (Object val : vals) {
                    if (clazz == val.getClass()) {
                        field.set(thiz, val);
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void makePublic(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }

    public static Class<?> clazz(int level) {
        return Thread.currentThread().getStackTrace()[2 - level].getClass();
    }

    public static String methodName(int level) {
        return Thread.currentThread().getStackTrace()[2 - level].getMethodName();
    }

    public static String clazzName(int level) {
        String clazz = Thread.currentThread().getStackTrace()[2 - level].getClassName();
        int idx = clazz.lastIndexOf('.');
        return clazz.substring(idx + 1);
    }

    public static String shell(String cmd, String dir) {
        StringBuilder msg = new StringBuilder();
        Process pid = null;
        BufferedReader ins = null;
        BufferedReader err = null;
        try {
            String[] command = { "/bin/sh", "-c", cmd };
            pid = Runtime.getRuntime().exec(command, null, new File(dir));
            ins = new BufferedReader(new InputStreamReader(pid.getInputStream(), "UTF-8"));
            err = new BufferedReader(new InputStreamReader(pid.getErrorStream(), "UTF-8"));
            String line;
            while ((line = ins.readLine()) != null) {
                msg.append(line).append('\n');
            }
            while ((line = err.readLine()) != null) {
                msg.append(line).append('\n');
            }

            if (pid.waitFor() != 0) {
                if (pid.exitValue() == 1) {
                    System.err.println("命令执行失败!");
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            IO.close(ins, err);
            if (pid != null) {
                pid.destroy();
            }
        }
        return msg.toString();
    }
}
