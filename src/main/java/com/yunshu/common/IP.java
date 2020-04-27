package com.yunshu.common;

import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;

public class IP {

    private static final DbSearcher db;

    static {
        DbSearcher tmp = null;
        try {
            tmp = new DbSearcher(new DbConfig(), IO.getHomePath("/data/ip2region.db"));
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
                String[] info = data.getRegion().split("\\|");
                String prov = info[2].replaceAll("省", "");
                String city = info[3].replaceAll("市", "");
                if ("0".equals(city)) {
                    city = prov;
                }
                action.run(info[0], prov, city, info[1], info[4]);
            }
            else {
                action.run("0", "0", "0", "0", "0");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            action.run("-1", "-1", "-1", "-1", "-1");
        }
    }

    @FunctionalInterface
    public interface IpAction {
        void run(String state, String prov, String city, String code, String ismg);
    }

    public static void main(String[] args) {
        IP.find("13.65.201.223", (state, prov, city, code, ismg) -> {
            FF.println("{},{},{},{},{}", state, prov, city, code, ismg);
        });
    }
}
