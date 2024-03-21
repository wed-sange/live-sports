package org.sports.app.service.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 博彩公司处理工具类
 *

 */
public class OddsCompanyUtil {
    private static final Map<String, String> companyMap = new HashMap() {
        {
            put("2", "BET365");
            put("3", "皇冠");
            put("4", "10BET");
            put("5", "立博");
            put("6", "明陞");
            put("7", "澳彩");
            put("8", "SNAI");
            put("9", "威廉希尔");
            put("10", "易胜博");
            put("11", "韦德");
            put("12", "EuroBet");
            put("13", "Inter wetten");
            put("14", "12bet");
            put("15", "利记");
            put("16", "盈禾");
            put("17", "18Bet");
            put("18", "Fun88");
            put("19", "竞彩官方");
            put("20", "onex");
            put("21", "188");
            put("22", "平博");
        }
    };


    /**
     * 获取博彩公司名称
     *
     * @param id
     * @return
     */
    public static String getNameById(String id) {
        return companyMap.get(id);
    }
}
