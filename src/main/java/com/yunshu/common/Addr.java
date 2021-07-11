package com.yunshu.common;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class Addr {

    private String     tag1;
    private String     tag2;
    private String     name;
    private List<Addr> list = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        Addr china = new Addr("citylist");
        china.tag2 = "citylist";
        IO.forEach(new File("/Users/hanqiong/dalu.csv"), (str, idx, stop) -> {
            String[] tag = str.trim().split(",");
            if (tag == null || tag.length < 5) {
                return;
            }
            String p = tag[1];
            String c = tag[2];
            String a = tag[3];
            china.add(p, c, a);
        });
        Map<String, Object> map = china.toMap();
        System.out.println(JSON.toJSONString(map));
        Thread.sleep(1000);
    }

    public Addr() {
    }

    public Addr(String name) {
        this.name = name;
    }

    public Addr get(String tag1, String tag2, String name) {
        name = FF.trim(name, null);
        if (name == null) {
            return new Addr();
        }
        for (Addr one : list) {
            if (name.equals(one.name)) {
                return one;
            }
        }
        Addr addr = new Addr(name);
        addr.tag1 = tag1;
        addr.tag2 = tag2;
        list.add(addr);
        return addr;
    }

    public void add(String prov, String city, String area) {
        if (IO.in(prov, "北京", "上海", "天津", "重庆")) {
            get("p", "c", city).get("n", null, area);
        }
        else {
            get("p", "c", prov).get("n", "a", city).get("s", null, area);
        }
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (tag1 != null) {
            map.put(tag1, name);
        }
        if (list.size() > 0) {
            List<Map<String, Object>> arr = new ArrayList<>();
            for (Addr one : list) {
                Map<String, Object> m = one.toMap();
                arr.add(m);
            }
            map.put(tag2, arr);
        }
        return map;
    }
}
