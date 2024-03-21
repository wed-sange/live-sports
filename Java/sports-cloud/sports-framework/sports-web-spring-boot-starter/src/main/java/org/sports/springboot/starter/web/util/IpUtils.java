package org.sports.springboot.starter.web.util;

import java.net.InetAddress;

/**
 * IP地址管理
 */
public class IpUtils {

    /**
     * 获取用户真实地址
     * @return
     */
    public static String getIpAddress(){
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostAddress();
        }catch (Exception e){
            return "127.0.0.1";
        }
    }

}
