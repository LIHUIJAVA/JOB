package com.px.mis.util;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonUtil {

    /**
     * 字符串转list
     */
    public static List<String> strToList(String param) {

        List<String> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(param)) {
            if (param.contains(",")) {
                String[] str = param.split(",");
                for (int i = 0; i < str.length; i++) {

                    list.add(str[i]);

                }
            } else {
                if (StringUtils.isNotEmpty(param)) {
                    list.add(param);
                }
            }
        }

        return list;

    }

    public static String getLoginTime(String local) {
        if (local == null || local.length() < 10) {
            throw new RuntimeException("时间格式不正确，请检查格式");
        }
        LocalDate localDate = LocalDate.parse(local.substring(0, 10));
        LocalTime localTime = LocalTime.now();
        return LocalDateTime.of(localDate, localTime).toString();
    }

    /**
     * map去前后空串
     */
    @SuppressWarnings("unchecked")
	public static void mapTrimToNull(Map map) {
        map.forEach((k, y) -> {
            if (y instanceof String) {
                map.put(k, StringUtils.trimToEmpty((String) map.get(k)));
            }
        });
    }
    
  
}
