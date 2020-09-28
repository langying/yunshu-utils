package com.yunshu.common;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

public class IE {

    public static final String kUA = "Mozilla/5.0 (Linux; U; Android 8.1.0; zh-cn; OPPO R11s Build/OPM1.171019.011) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/70.0.3538.80 Mobile Safari/537.36 HeyTapBrowser/10.7.4.2";

    public static final String ADR = "Mozilla/5.0 (Linux; Android 10; HLK-AL10; HMSCore 4.0.4.301) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108";
    public static final String IOS = "Mozilla/5.0 (iPhone; CPU iPhone OS 11_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E216 MicroMessenger/7.0.12(0x17000c30) NetType/WIFI Language/zh_CN ";
    public static final String MAC = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_5) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.1.1 Safari/605.1.15";
    public static final String WIN = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";

    public static final int TIME_OUT = 5000;

    public static final int GET  = 0;
    public static final int POST = 1;

    public static final String HOST = "Host";
    public static final String COOK = "Cookie";
    public static final String REFE = "Referer";
    public static final String USER = "User-Agent";
    public static final String ADDR = "x-forwarded-for";

    public static final String kHTML = "text/html;charset=UTF-8";
    public static final String kTEXT = "text/plain;charset=UTF-8";
    public static final String kJSON = "application/json;charset=UTF-8";
    public static final String kFORM = "application/x-www-form-urlencoded;charset=UTF-8";

    public static final String devADR = "adr";
    public static final String devAPP = "app";
    public static final String devIOS = "ios";
    public static final String devMAC = "mac";
    public static final String devWIN = "win";

    private static final CloseableHttpClient client = makeClient(null, null, 0);

    /** 不用设置ssl也可以成功 */
    public static String get(String url) {
        InputStream insm = null;
        HttpsURLConnection conn = null;
        try {
            conn = (HttpsURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(ID.kTimeOut);
            conn.setConnectTimeout(ID.kTimeOut);
            conn.connect();
            if (conn.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                System.out.println(FF.log("{} : {} : {}", url, conn.getResponseCode(), conn.getResponseMessage()));
            }
            insm = conn.getInputStream();
            String html = IO.read(insm);
            return html;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            IO.close(insm);
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    public static String get(String url, Map<String, Object> head, Map<String, Object> data) {
        String html = null;
        CloseableHttpResponse resp = null;
        try {
            URI uri = new URIBuilder(url).setParameters(makeNvps(data)).build();
            HttpGet http = new HttpGet(uri);
            IO.forEach(head, (key, val, idx, stop) -> {
                http.addHeader(key, val == null ? "" : val.toString());
            });

            resp = client.execute(http);
            html = EntityUtils.toString(resp.getEntity());
            if (HttpStatus.SC_OK != resp.getStatusLine().getStatusCode()) {
                StatusLine err = resp.getStatusLine();
                System.out.println(FF.log("uri:{}, code:{}, msg:{}", http.getURI(), err.getStatusCode(), err.getReasonPhrase()));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            IO.close(resp);
        }
        return html;
    }

    public static void get(String url, Map<String, Object> head, Map<String, Object> data, Success succ, Failure fail) {
        CloseableHttpResponse resp = null;
        try {
            URI uri = new URIBuilder(url).setParameters(makeNvps(data)).build();
            HttpGet http = new HttpGet(uri);
            IO.forEach(head, (key, val, idx, stop) -> {
                http.addHeader(key, val == null ? "" : val.toString());
            });

            resp = client.execute(http);
            if (succ != null) {
                succ.call(resp.getStatusLine().getStatusCode(), EntityUtils.toString(resp.getEntity()));
            }
        }
        catch (Exception e) {
            if (fail == null) {
                e.printStackTrace();
            }
            else {
                fail.call(e);
            }
        }
        finally {
            IO.close(resp);
        }
    }

    public static String get(String url, Map<String, Object> head, Map<String, Object> data, String host, int port) {
        String html = null;
        CloseableHttpResponse resp = null;
        CloseableHttpClient client = null;
        try {
            String scheme = url.substring(0, url.indexOf(':'));
            URI uri = new URIBuilder(url).setParameters(makeNvps(data)).build();
            HttpGet http = new HttpGet(uri);
            IO.forEach(head, (key, val, idx, stop) -> {
                http.addHeader(key, val == null ? "" : val.toString());
            });

            client = makeClient(scheme, host, port);
            resp = client.execute(http);
            html = EntityUtils.toString(resp.getEntity());
            if (HttpStatus.SC_OK != resp.getStatusLine().getStatusCode()) {
                StatusLine err = resp.getStatusLine();
                System.out.println(FF.log("uri:{}, code:{}, msg:{}", http.getURI(), err.getStatusCode(), err.getReasonPhrase()));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            IO.close(resp, client);
        }
        return html;
    }

    public static String post(String url) {
        InputStream inStream = null;
        HttpsURLConnection conn = null;
        try {
            conn = (HttpsURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(ID.kTimeOut);
            conn.setConnectTimeout(ID.kTimeOut);
            conn.connect();
            if (conn.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                System.out.println(FF.log("{} : {} : {}", url, conn.getResponseCode(), conn.getResponseMessage()));
            }
            inStream = conn.getInputStream();
            String html = IO.read(inStream);
            return html;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            IO.close(inStream);
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    public static String post(String uri, Map<String, String> head, String data) {
        String html = null;
        CloseableHttpResponse resp = null;
        try {
            HttpPost http = new HttpPost(uri);
            http.setEntity(new StringEntity(data, ID.kUTF8));
            IO.forEach(head, (key, val, idx, stop) -> {
                http.setHeader(key, val);
            });

            resp = client.execute(http);
            html = EntityUtils.toString(resp.getEntity());
            if (HttpStatus.SC_OK != resp.getStatusLine().getStatusCode()) {
                StatusLine err = resp.getStatusLine();
                System.out.println(FF.log("uri:{}, code:{}, msg:{}", http.getURI(), err.getStatusCode(), err.getReasonPhrase()));
            }
        }
        catch (Exception e) {
            System.out.println(FF.log("[{}] exception:{}", uri, e.getMessage()));
        }
        return html;
    }

    public static void post(String uri, Map<String, String> head, String data, Success succ, Failure fail) {
        CloseableHttpResponse resp = null;
        try {
            HttpPost http = new HttpPost(uri);
            http.setEntity(new StringEntity(data, ID.kUTF8));
            IO.forEach(head, (key, val, idx, stop) -> {
                http.setHeader(key, val);
            });

            resp = client.execute(http);
            if (succ != null) {
                succ.call(resp.getStatusLine().getStatusCode(), EntityUtils.toString(resp.getEntity()));
            }
        }
        catch (Exception e) {
            if (fail == null) {
                e.printStackTrace();
            }
            else {
                fail.call(e);
            }
        }
        finally {
            IO.close(resp);
        }
    }

    public static String post(String uri, Map<String, String> head, Map<String, Object> data) {
        String html = null;
        CloseableHttpResponse resp = null;
        try {
            HttpPost http = new HttpPost(uri);
            http.setEntity(new UrlEncodedFormEntity(makeNvps(data), ID.kUTF8));
            IO.forEach(head, (key, val, idx, stop) -> {
                http.addHeader(key, val == null ? "" : val.toString());
            });
            resp = client.execute(http);
            html = EntityUtils.toString(resp.getEntity());
            if (HttpStatus.SC_OK != resp.getStatusLine().getStatusCode()) {
                StatusLine err = resp.getStatusLine();
                System.out.println(FF.log("uri:{}, code:{}, msg:{}", http.getURI(), err.getStatusCode(), err.getReasonPhrase()));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            IO.close(resp);
        }
        return html;
    }

    public static void post(String uri, Map<String, String> head, Map<String, Object> data, Success succ, Failure fail) {
        CloseableHttpResponse resp = null;
        try {
            HttpPost http = new HttpPost(uri);
            http.setEntity(new UrlEncodedFormEntity(makeNvps(data), ID.kUTF8));
            IO.forEach(head, (key, val, idx, stop) -> {
                http.addHeader(key, val == null ? "" : val.toString());
            });
            resp = client.execute(http);
            if (succ != null) {
                succ.call(resp.getStatusLine().getStatusCode(), EntityUtils.toString(resp.getEntity()));
            }
        }
        catch (Exception e) {
            if (fail == null) {
                e.printStackTrace();
            }
            else {
                fail.call(e);
            }
        }
        finally {
            IO.close(resp);
        }
    }

    public static String device(String ua) {
        if (ua == null || ua.length() <= 0) {
            return "api";
        }
        ua = ua.toLowerCase();
        if (ua.contains("iphone") || ua.contains("ipad") || ua.contains("ipod")) {
            return "ios";
        }
        else if (ua.contains("linux") || ua.contains("android") || ua.contains("adr") || ua.contains("okhttp")) {
            return "adr";
        }
        else if (ua.contains("mac") || ua.contains("unix")) {
            return "mac";
        }
        else if (ua.contains("window")) {
            return "win";
        }
        else {
            return "api";
        }
    }

    public static boolean isADR(String ua) {
        if (ua == null) {
            return false;
        }
        ua = ua.toLowerCase();
        return ua.contains("linux") || ua.contains("android") || ua.contains("adr") || ua.contains("okhttp");
    }

    public static boolean isIOS(String ua) {
        if (ua == null) {
            return false;
        }
        ua = ua.toLowerCase();
        return ua.contains("iphone") || ua.contains("ipad") || ua.contains("ipod");
    }

    @FunctionalInterface
    public interface Success {
        void call(int code, String html);
    }

    @FunctionalInterface
    public interface Failure {
        void call(Exception e);
    }

    private static List<NameValuePair> makeNvps(Map<String, Object> data) {
        List<NameValuePair> args = new ArrayList<>();
        IO.forEach(data, (key, val, idx, stop) -> {
            args.add(new BasicNameValuePair(key, val.toString()));
        });
        return args;
    }

    public static CloseableHttpClient makeClient(String proxyScheme, String proxyHost, int proxyPort) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            TrustManager csr = new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] cer, String arg1) throws CertificateException {
                }

                @Override
                public void checkClientTrusted(X509Certificate[] cer, String arg1) throws CertificateException {
                }
            };
            ctx.init(null, new TrustManager[] { csr }, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((String name, SSLSession session) -> {
                return true;
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Builder cfg = RequestConfig.custom();
            cfg.setSocketTimeout(IE.TIME_OUT);
            cfg.setConnectTimeout(IE.TIME_OUT);
            cfg.setCookieSpec(CookieSpecs.STANDARD);
            cfg.setConnectionRequestTimeout(IE.TIME_OUT);
            if (proxyScheme != null) {
                cfg.setProxy(new HttpHost(proxyHost, proxyPort, proxyScheme));
            }
            SSLContextBuilder ctx = SSLContexts.custom();
            ctx.loadTrustMaterial(null, (X509Certificate[] chain, String authType) -> {
                return true;
            });
            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(ctx.build());
            return HttpClients.custom().setSSLSocketFactory(csf).setDefaultRequestConfig(cfg.build()).build();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
