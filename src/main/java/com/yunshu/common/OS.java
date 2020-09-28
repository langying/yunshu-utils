package com.yunshu.common;

public class OS {

    public static final String kName = System.getProperty("os.name").toLowerCase();

    public static boolean isLinux() {
        return kName.indexOf("linux") >= 0;
    }

    public static boolean isMacOS() {
        return kName.indexOf("mac") >= 0 && kName.indexOf("os") > 0 && kName.indexOf("x") < 0;
    }

    public static boolean isMacOSX() {
        return kName.indexOf("mac") >= 0 && kName.indexOf("os") > 0 && kName.indexOf("x") > 0;
    }

    public static boolean isWin() {
        return kName.indexOf("windows") >= 0;
    }

    public static boolean isOS2() {
        return kName.indexOf("os/2") >= 0;
    }

    public static boolean isSolaris() {
        return kName.indexOf("solaris") >= 0;
    }

    public static boolean isSunOS() {
        return kName.indexOf("sunos") >= 0;
    }

    public static boolean isMPEiX() {
        return kName.indexOf("mpe/ix") >= 0;
    }

    public static boolean isHPUX() {
        return kName.indexOf("hp-ux") >= 0;
    }

    public static boolean isAix() {
        return kName.indexOf("aix") >= 0;
    }

    public static boolean isOS390() {
        return kName.indexOf("os/390") >= 0;
    }

    public static boolean isFreeBSD() {
        return kName.indexOf("freebsd") >= 0;
    }

    public static boolean isIrix() {
        return kName.indexOf("irix") >= 0;
    }

    public static boolean isDigitalUnix() {
        return kName.indexOf("digital") >= 0 && kName.indexOf("unix") > 0;
    }

    public static boolean isNetWare() {
        return kName.indexOf("netware") >= 0;
    }

    public static boolean isOSF1() {
        return kName.indexOf("osf1") >= 0;
    }

    public static boolean isOpenVMS() {
        return kName.indexOf("openvms") >= 0;
    }

}
