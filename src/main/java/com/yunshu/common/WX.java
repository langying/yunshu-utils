package com.yunshu.common;

import java.util.Map;

import com.alibaba.fastjson.JSON;

public class WX {

    private static final String kTicket      = "ticket";
    private static final String kErrmsg      = "errmsg";
    private static final String kErrcode     = "errcode";
    private static final String kExpiresIn   = "expires_in";
    private static final String kAccessToken = "access_token";

    private static final String appId  = "wx241efebe5b164cfe";
    private static final String secret = "a4bdcd5eaa1a2f1c11ca944c1d2db3bf";

    private static final String kToken = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={}&secret={}";
    private static final String TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token={}&type=wx_card";
    private static final String QRCODE = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket={}";

    public static Map<String, Object> getToken(String appId, String secret) {
        String url = FF.log(kToken, appId, secret);
        String txt = IE.get(url);
        return JSON.parseObject(txt);
    }

    public static Map<String, Object> getTicket(String token) {
        String url = FF.log(TICKET, token);
        String txt = IE.get(url);
        return JSON.parseObject(txt);
    }

    public static Map<String, Object> getQRCode(String ticket) {
        String url = FF.log(QRCODE, ticket);
        String txt = IE.get(url);
        return JSON.parseObject(txt);
    }

    public static void main(String[] args) {
        Map<String, Object> token = getToken(appId, secret);
        System.out.println(token);

        Map<String, Object> ticket = getTicket(token.get(kAccessToken).toString());
        System.out.println(ticket);

        //        Map<String, Object> qrcode = getQRCode(ticket.get(kTicket).toString());
        //        System.out.println(qrcode);
    }
}
