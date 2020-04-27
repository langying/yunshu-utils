package com.yunshu.common;

public class CN {

    /** ASCII表中可见字符从!开始，偏移位值为33(Decimal) */
    private static final char DBC_CHAR_START = 33; // 半角!

    /** ASCII表中可见字符到~结束，偏移位值为126(Decimal) */
    private static final char DBC_CHAR_END = 126; // 半角~

    /** 全角对应于ASCII表的可见字符从！开始，偏移值为65281 */
    private static final char SBC_CHAR_START = 65281; // 全角！

    /** 全角对应于ASCII表的可见字符到～结束，偏移值为65374 */
    private static final char SBC_CHAR_END = 65374; // 全角～

    /** ASCII表中除空格外的可见字符与对应的全角字符的相对偏移 */
    private static final int CONVERT_STEP = 65248; // 全角半角转换间隔

    /** 全角空格的值，它没有遵从与ASCII的相对偏移，必须单独处理 */
    private static final char SBC_SPACE = 12288; // 全角空格 12288

    /** 半角空格的值，在ASCII中为32(Decimal) */
    private static final char DBC_SPACE = ' '; // 半角空格

    /**
     * 半角字符->全角字符转换,只处理空格，!到˜之间的字符，忽略其他
     */
    public static String bj2qj(String src) {
        if (src == null) {
            return null;
        }

        char[] list = src.toCharArray();
        StringBuilder dst = new StringBuilder(src.length());
        for (int i = 0, l = list.length; i < l; i++) {
            if (list[i] == DBC_SPACE) { // 如果是半角空格，直接用全角空格替代
                dst.append(SBC_SPACE);
            }
            else if ((list[i] >= DBC_CHAR_START) && (list[i] <= DBC_CHAR_END)) { // 字符是!到~之间的可见字符
                dst.append((char) (list[i] + CONVERT_STEP));
            }
            else { // 不对空格以及ascii表中其他可见字符之外的字符做任何处理
                dst.append(list[i]);
            }
        }
        return dst.toString();
    }

    /**
     * 全角字符->半角字符转换.只处理全角的空格，全角！到全角～之间的字符，忽略其他
     */
    public static String qj2bj(String src) {
        if (src == null) {
            return null;
        }

        char[] list = src.toCharArray();
        StringBuilder dst = new StringBuilder(src.length());
        for (int i = 0; i < src.length(); i++) {
            if (list[i] >= SBC_CHAR_START && list[i] <= SBC_CHAR_END) { // 如果位于全角！到全角～区间内
                dst.append((char) (list[i] - CONVERT_STEP));
            }
            else if (list[i] == SBC_SPACE) { // 如果是全角空格
                dst.append(DBC_SPACE);
            }
            else { // 不处理全角空格，全角！到全角～区间外的字符
                dst.append(list[i]);
            }
        }
        return dst.toString();
    }
}
