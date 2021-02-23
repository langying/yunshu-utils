package com.yunshu.common;

import java.security.MessageDigest;
import java.util.Base64;

public class MD {

    public static final char[] CA;
    public static final long[] AC;
    public static final char[] HEX;
    static {
        HEX = "0123456789ABCDEF".toCharArray();
        CA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_".toCharArray();
        AC = new long[256];
        for (int i = 0, l = CA.length; i < l; i++) {
            AC[CA[i]] = i;
        }
    }

    public static String encodeBase64(String src) {
        return Base64.getEncoder().encodeToString(src.getBytes());
    }

    public static String decodeBase64(String src) {
        byte[] dst = Base64.getDecoder().decode(src.getBytes());
        return new String(dst, ID.kUTF8);
    }

    public static String md5(String src) {
        String ret = MD5(src);
        return ret == null ? null : ret.toLowerCase();
    }

    public static String MD5(String src) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            // 分步1: digest.update(str.getBytes());
            // 分步2: byte[] dist = digest.digest();
            byte[] dst = digest.digest(src.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0, l = dst.length; i < l; i++) {
                sb.append(HEX[dst[i] >>> 4 & 0x0F]);
                sb.append(HEX[dst[i] & 0x0F]);
            }
            return sb.toString();
        }
        catch (Exception e) {
            return null;
        }
    }

    public static String encodeInt(int numb) {
        StringBuilder sb = new StringBuilder();
        sb.append(CA[numb & 0x3F]);
        sb.append(CA[(numb >> 6) & 0x3F]);
        sb.append(CA[(numb >> 12) & 0x3F]);
        sb.append(CA[(numb >> 18) & 0x3F]);
        sb.append(CA[(numb >> 24) & 0x3F]);
        sb.append(CA[(numb >> 30) & 0x3F]);
        return sb.toString();
    }

    public static int decodeInt(String code) {
        if (!FF.isLen(code, 6)) {
            return 0;
        }
        int num = 0;
        num |= AC[code.charAt(0)];
        num |= AC[code.charAt(1)] << 6;
        num |= AC[code.charAt(2)] << 12;
        num |= AC[code.charAt(3)] << 18;
        num |= AC[code.charAt(4)] << 24;
        num |= AC[code.charAt(5)] << 30;
        return num;
    }

    public static String encodeLong(long numb) {
        StringBuilder sb = new StringBuilder();
        sb.append(CA[(int) (numb & 0x3F)]);
        sb.append(CA[(int) ((numb >> 6) & 0x3F)]);
        sb.append(CA[(int) ((numb >> 12) & 0x3F)]);
        sb.append(CA[(int) ((numb >> 18) & 0x3F)]);
        sb.append(CA[(int) ((numb >> 24) & 0x3F)]);
        sb.append(CA[(int) ((numb >> 30) & 0x3F)]);
        sb.append(CA[(int) ((numb >> 36) & 0x3F)]);
        sb.append(CA[(int) ((numb >> 42) & 0x3F)]);
        sb.append(CA[(int) ((numb >> 48) & 0x3F)]);
        sb.append(CA[(int) ((numb >> 54) & 0x3F)]);
        sb.append(CA[(int) ((numb >> 60) & 0x3F)]);
        return sb.toString();
    }

    public static long decodeLong(String code) {
        if (code == null || code.length() != 11) {
            return 0;
        }
        long num = 0;
        num |= AC[code.charAt(0)];
        num |= AC[code.charAt(1)] << 6;
        num |= AC[code.charAt(2)] << 12;
        num |= AC[code.charAt(3)] << 18;
        num |= AC[code.charAt(4)] << 24;
        num |= AC[code.charAt(5)] << 30;
        num |= AC[code.charAt(6)] << 36;
        num |= AC[code.charAt(7)] << 42;
        num |= AC[code.charAt(8)] << 48;
        num |= AC[code.charAt(9)] << 54;
        num |= AC[code.charAt(10)] << 60;
        return num;
    }

    public static String encodeMobile(long num) {
        num -= 10000000000L;
        StringBuilder sb = new StringBuilder();
        sb.append(CA[(int) (num & 0x3F)]);
        sb.append(CA[(int) ((num >> 6) & 0x3F)]);
        sb.append(CA[(int) ((num >> 12) & 0x3F)]);
        sb.append(CA[(int) ((num >> 18) & 0x3F)]);
        sb.append(CA[(int) ((num >> 24) & 0x3F)]);
        sb.append(CA[(int) ((num >> 30) & 0x3F)]);
        return sb.toString();
    }

    public static long decodeMobile(String code) {
        if (code == null || code.length() != 6) {
            return 0;
        }
        long num = 0;
        num |= AC[code.charAt(0)];
        num |= AC[code.charAt(1)] << 6;
        num |= AC[code.charAt(2)] << 12;
        num |= AC[code.charAt(3)] << 18;
        num |= AC[code.charAt(4)] << 24;
        num |= AC[code.charAt(5)] << 30;
        return num + 10000000000L;
    }

    public static String encodeNumb(long numb) {
        StringBuilder sb = new StringBuilder();
        sb.append(CA[(int) (numb & 0x3F)]);
        sb.append(CA[(int) ((numb >> 6) & 0x3F)]);
        sb.append(CA[(int) ((numb >> 12) & 0x3F)]);
        sb.append(CA[(int) ((numb >> 18) & 0x3F)]);
        sb.append(CA[(int) ((numb >> 24) & 0x3F)]);
        sb.append(CA[(int) ((numb >> 30) & 0x3F)]);
        sb.append(CA[(int) ((numb >> 36) & 0x3F)]);
        sb.append(CA[(int) ((numb >> 42) & 0x3F)]);
        sb.append(CA[(int) ((numb >> 48) & 0x3F)]);
        sb.append(CA[(int) ((numb >> 54) & 0x3F)]);
        sb.append(CA[(int) ((numb >> 60) & 0x3F)]);

        for (int i = sb.length() - 1; i >= 0; i--) {
            if (sb.charAt(i) == CA[0]) {
                sb.deleteCharAt(i);
            }
            else {
                break;
            }
        }

        return sb.toString();
    }

    public static long decodeNumb(String code) {
        if (code == null || code.length() <= 0) {
            return 0;
        }
        if (code.length() < 11) {
            int l = 11 - code.length();
            for (int i = 0; i < l; i++) {
                code += CA[0];
            }
        }

        long num = 0;
        num |= AC[code.charAt(0)];
        num |= AC[code.charAt(1)] << 6;
        num |= AC[code.charAt(2)] << 12;
        num |= AC[code.charAt(3)] << 18;
        num |= AC[code.charAt(4)] << 24;
        num |= AC[code.charAt(5)] << 30;
        num |= AC[code.charAt(6)] << 36;
        num |= AC[code.charAt(7)] << 42;
        num |= AC[code.charAt(8)] << 48;
        num |= AC[code.charAt(9)] << 54;
        num |= AC[code.charAt(10)] << 60;
        return num;
    }

    public static void main(String[] args) {
        System.out.println(MD.encodeInt(10000));

        Long aaa = 9999999999L;
        System.out.println(Long.toHexString(aaa));
        System.out.println(0x2540be3ffL);

        int num1 = Integer.MAX_VALUE;
        System.out.println(num1);
        String code = encodeInt(num1);
        System.out.println(code);
        int num2 = decodeInt(code);
        System.out.println(num2);

        System.out.println("===============LONG================");

        long lon1 = Long.MIN_VALUE;
        System.out.println(lon1);
        String lons = encodeLong(lon1);
        System.out.println(lons);
        long lon2 = decodeLong(lons);
        System.out.println(lon2);

        System.out.println("================TELS===============");

        long n = 18094714299L;
        System.out.println(n);
        String s = encodeMobile(n);
        System.out.println(s);
        long m = decodeMobile(s);
        System.out.println(m);

        System.out.println(encodeInt(10000));

        System.out.println("================NUMB===============");

        long numb1 = 18094714299L;
        System.out.println(numb1);
        String numb2 = encodeNumb(numb1);
        System.out.println(numb2);
        long numb3 = decodeNumb("722h2Q");
        System.out.println(numb3);
    }
}
