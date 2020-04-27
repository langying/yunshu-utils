package com.yunshu.common;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ISMG {

    private static final String[] kEmpty = new String[0];

    private static final Map<Integer, IsmgDo> map = new HashMap<>();

    private static final IsmgDo kIsmgDo = IsmgDo.valueOf("", "", "");

    static {
        IO.forEach(new File(IO.getHomePath("data/ismg.csv")), (line, index, stop) -> {
            String[] list = line == null ? kEmpty : line.split(",");
            if (list.length == 7) {
                map.put(Integer.valueOf(list[1]), IsmgDo.valueOf(list[2], list[3], list[4]));
            }
        });
    }

    public static IsmgDo get(long tel) {
        int key = (int) (tel / 10000);
        IsmgDo val = map.get(key);
        if (val == null) {
            return kIsmgDo;
        }
        else {
            return val;
        }
    }

    public static IsmgDo get(String tel) {
        if (tel.length() > 7) {
            tel = tel.substring(0, 7);
        }
        IsmgDo val = map.get(FF.toInt(tel, 0));
        if (val == null) {
            return kIsmgDo;
        }
        else {
            return val;
        }
    }

    public static class IsmgDo {

        public int    ismg;
        public String prov;
        public String city;

        public static IsmgDo valueOf(String prov, String city, String ismg) {
            IsmgDo item = new IsmgDo();
            item.prov = prov;
            item.city = city;
            if ("移动".equals(ismg)) {
                item.ismg = 1;
            }
            else if ("联通".equals(ismg)) {
                item.ismg = 2;
            }
            else if ("电信".equals(ismg)) {
                item.ismg = 3;
            }
            else {
                item.ismg = 0;
            }
            return item;
        }

        @Override
        public String toString() {
            return FF.log("{}-{}-{}", prov, city, ismg);
        }
    }
}
