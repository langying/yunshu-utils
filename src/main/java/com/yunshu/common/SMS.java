package com.yunshu.common;

public class SMS {

    public static String getSign(String content) {
        return toSignText(content)[0];
    }

    public static String getText(String content) {
        return toSignText(content)[1];
    }

    public static String toContent(String sign, String text) {
        if (sign == null || sign.length() <= 0) {
            return text;
        }
        else {
            return '【' + sign + '】' + text;
        }
    }

    public static String[] toSignText(String content) {
        String[] arr = new String[2];
        content = FF.trim(content);
        int idx1 = content.indexOf('【');
        int idx2 = content.indexOf('】');
        if (idx1 < 0 || idx2 < 0 || idx1 > idx2) {
            arr[0] = ID.kEmpty;
            arr[1] = content;
        }
        else if (idx1 == 0) {
            arr[0] = content.substring(idx1 + 1, idx2);
            arr[1] = content.substring(idx2 + 1);
        }
        else if (idx2 == content.length() - 1) {
            arr[0] = content.substring(idx1 + 1, idx2);
            arr[1] = content.substring(0, idx1);
        }
        else {
            arr[0] = ID.kEmpty;
            arr[1] = content;
        }
        return arr;
    }

}
