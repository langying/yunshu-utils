package com.yunshu.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Java Language
 */
public class JM {

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
