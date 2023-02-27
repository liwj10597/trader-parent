package com.mfml.trader.common.core.utils;

import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author caozhou
 * @date 2023-01-11 09:34
 */
@Slf4j
public class NetUtil {
    /**
     * 获取本机公网ip
     */
    public static String getIpv4(){
        String ipv4 = "";
        try{
            String ipStr = HttpUtil.get("https://www.taobao.com/help/getip.php");
            ipv4 = ipStr.split("\"")[1];
            log.info("本机外网ip为{}", ipv4);
        }catch(Exception e){
            log.warn("getIpv4 warn", e);
        }
        return ipv4;
    }
}
