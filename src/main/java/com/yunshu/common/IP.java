package com.yunshu.common;

import java.io.File;

import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;

public class IP {

    private static final DbSearcher db;

    static {
        DbSearcher tmp = null;
        String file = OS.isWin() ? "D:/data/ip2region.db" : "/data/ip2region.db";
        FF.println("IP db file is:{}, exist:{}", file, new File(file).exists());
        try {
            tmp = new DbSearcher(new DbConfig(), file);
            tmp.memorySearch("127.0.0.1");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        db = tmp;
    }

    public static void find(String ip, IpAction action) {
        try {
            if (Util.isIpAddress(ip)) {
                DataBlock data = db.memorySearch(ip);
                if (data == null || data.getRegion() == null) {
                    FF.println("IP::find({}) is null.", ip);
                    action.run("-1", "-1", "-1", "-1", "-1");
                }
                else {
                    String[] info = data.getRegion().split("\\|");
                    String prov = info[2].replaceAll("省", "");
                    String city = info[3].replaceAll("市", "");
                    if ("0".equals(city)) {
                        city = prov;
                    }
                    action.run(info[0], prov, city, info[1], info[4]);
                }
            }
            else {
                FF.println("IP::find({}) is error.", ip);
                action.run("0", "0", "0", "0", "0");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            FF.println("IP::find({}) is exception.", ip);
            action.run("-2", "-2", "-2", "-2", "-2");
        }
    }

    @FunctionalInterface
    public interface IpAction {
        void run(String state, String prov, String city, String code, String ismg);
    }

    public static void main(String[] args) {
        IP.find("127.0.0.1", (state, prov, city, code, ismg) -> {
            FF.println("{},{},{},{},{}", state, prov, city, code, ismg);
        });
    }
}
