package com.qunar.hotconfig.util.dateFormatUtil;

import org.joda.time.DateTime;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by shadandan on 17/4/10.
 */
public class MapDateFormatUtil {
    public static List<Map> formatDateTimeToString(List<Map> list) {
        for (Map map : list) {
            formatDateTimeToString(map);
        }
        return list;
    }

    public static Map formatDateTimeToString(Map map) {
        if (map != null) {
            for (Object key : map.keySet()) {
                if (map.get(key) instanceof Date || map.get(key) instanceof Time || map.get(key) instanceof Timestamp) {
                    map.put(key, new DateTime(map.get(key)).toString("yyyy-MM-dd HH:mm:ss"));
                }
            }
        }
        return map;
    }
}
