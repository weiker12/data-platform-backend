package com.peilian.dataplatform.util;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetUtil {

    private final static String UNKNOWN = "unknown";

    public static boolean isInnerIp(String ip) {
        String reg = "^(127\\.0\\.0\\.1)$|^(localhost)$|^(10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})$|^(172\\.((1[6-9])|(2\\d)|(3[01]))\\.\\d{1,3}\\.\\d{1,3})$|^(192\\.168\\.\\d{1,3}\\.\\d{1,3})$|^(100\\.((6[4-9])|([7-9]\\d)|1[01]\\d|12[0-7])\\.\\d{1,3}\\.\\d{1,3})$";
        Pattern p = Pattern.compile(reg);
        Matcher matcher = p.matcher(ip);
        return matcher.find();
    }

    /**
     * 获取客户端IP地址
     *
     * @param request HttpServletRquest
     * @return String
     */
    public static String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    public static Integer getServerPort(HttpServletRequest request) {
        return request.getLocalPort();
    }


    /**
     * 判断是否为内部url,非内部域名的，即为外网url
     *
     * @param url                 来自客户端的url
     * @param innerTopLevelDomain 内部一级域名,如 peilian.work
     * @return 0：外网url 1：内部特定域名url 2：内部ip或本地访问 3：serviceName访问
     */
    public static int justUrlStatus(String url, String innerTopLevelDomain, String serviceName) {
        //K8s分配的Service域名
        String reg3 = "^http(s){0,1}://" + serviceName + "/";
        //访问Service域名判断
        if (Pattern.compile(reg3).matcher(url).find()) {
            return 3;
        }
        //带有内网一级域名后缀的域名
        String reg1 = "^http(s){0,1}://([\\.\\da-zA-Z_-]+\\.)" + innerTopLevelDomain;
        //访问内部特定域名判断
        if (Pattern.compile(reg1).matcher(url).find()) {
            return 1;
        }
        //访问本地或内网IP地址
        String reg2 = "^http(s){0,1}://(127\\.0\\.0\\.1)|(localhost)|(10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})|(172\\.((1[6-9])|(2\\d)|(3[01]))\\.\\d{1,3}\\.\\d{1,3})|(192\\.168\\.\\d{1,3}\\.\\d{1,3})|(100\\.((6[4-9])|([7-9]\\d)|1[01]\\d|12[0-7])\\.\\d{1,3}\\.\\d{1,3})";
        //访问本地或内部IP判断
        if (Pattern.compile(reg2).matcher(url).find()) {
            return 2;
        }
        return 0;
    }

}
