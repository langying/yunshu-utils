package com.yunshu.common.ext;

import java.util.Calendar;

import com.yunshu.common.FF;
import com.yunshu.common.ID;

public class CMPP {

    public static final byte[] kBody = new byte[0];

    public static String getSign(String content) {
        return getSignText(content)[0];
    }

    public static String[] getSignText(String content) {
        content = content == null ? ID.kEmpty : content.trim();
        int idx1 = content.indexOf('【');
        int idx2 = content.indexOf('】');
        if (idx1 < 0 || idx2 < 0 || idx1 > idx2) {
            return new String[] { ID.kEmpty, content };
        }
        else if (idx1 == 0) {
            return new String[] { content.substring(idx1 + 1, idx2), content.substring(idx2 + 1) };
        }
        else if (idx2 == content.length() - 1) {
            return new String[] { content.substring(idx1 + 1, idx2), content.substring(0, idx1) };
        }
        else {
            return new String[] { ID.kEmpty, content.substring(0, idx1) };
        }
    }

    public static int size(String text) {
        if (text == null || text.length() <= 70) {
            return 1;
        }
        else {
            return text.length() / 68 + 1;
        }
    }

    public static long msgId(long gateId, long seqId) {
        Calendar cc = Calendar.getInstance();
        int MM = cc.get(Calendar.MONTH);
        int dd = cc.get(Calendar.DAY_OF_MONTH);
        int HH = cc.get(Calendar.HOUR_OF_DAY);
        int mm = cc.get(Calendar.MINUTE);
        int ss = cc.get(Calendar.SECOND);
        return msgId(MM, dd, HH, mm, ss, gateId, seqId);
    }

    /**
     * <pre>
     * gg gateId
     * sq sequeueId
     * </pre>
     */
    public static long msgId(long MM, long dd, long HH, long mm, long ss, long gg, long sq) {
        gg &= 0x3FFFFFL; // 22 bit gateId
        sq &= 0x00FFFFL; // 16 bit sequeueId

        long value = 0;
        value |= MM << 60; // 4 bit 15月
        value |= dd << 55; // 5 bit 31日
        value |= HH << 50; // 5 bit 31时
        value |= mm << 44; // 6 bit 63分
        value |= ss << 38; // 6 bit 63秒
        value |= gg << 16; // 22bit gateId
        value |= sq; // 16bit uuid
        return value;
    }

    public static void msgId(long msgId, MsgIdAction action) {
        long MM = (msgId >>> 60) & 0x00000FL; // 4 bit 15月
        long dd = (msgId >>> 55) & 0x00001FL; // 5 bit 31日
        long HH = (msgId >>> 50) & 0x00001FL; // 5 bit 31时
        long mm = (msgId >>> 44) & 0x00003FL; // 6 bit 63分
        long ss = (msgId >>> 38) & 0x00003FL; // 6 bit 63秒
        long gg = (msgId >>> 16) & 0x3FFFFFL; // 22bit gateId
        long sq = msgId & 0xFFFF; // 16bit sequeueId
        action.run(MM, dd, HH, mm, ss, gg, sq);
    }

    public static String msgId2String(long msgId) {
        long MM = (msgId >>> 60) & 0x00000FL; // 4 bit 15月
        long dd = (msgId >>> 55) & 0x00001FL; // 5 bit 31日
        long HH = (msgId >>> 50) & 0x00001FL; // 5 bit 31时
        long mm = (msgId >>> 44) & 0x00003FL; // 6 bit 63分
        long ss = (msgId >>> 38) & 0x00003FL; // 6 bit 63秒
        long gg = (msgId >>> 16) & 0x3FFFFFL; // 22bit gateId
        long sq = msgId & 0xFFFF; // 16bit sequeueId
        return FF.log("[{}\n{}\n2018-{}-{} {}:{}:{} ({}, {})]", msgId, String.format("%08X", msgId), MM, dd, HH, mm, ss, gg, sq);
    }

    @FunctionalInterface
    public interface MsgIdAction {
        void run(long MM, long dd, long HH, long mm, long ss, long gateId, long seqId);
    }
}
