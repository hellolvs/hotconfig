package com.qunar.hotconfig.util.jsonUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shuai.lv
 * @date 2017/4/13.
 */
public final class JsonUtils {

    private static final Logger LOG = LoggerFactory.getLogger(JsonUtils.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonUtils() {

    }

    public static ObjectMapper getInstance() {
        return OBJECT_MAPPER;
    }

    /**
     * javaBean,list,array convert to json string
     */
    public static String obj2json(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOG.error("javaBean convert to json error：{}", e.getMessage());
        }
        return null;
    }

    /**
     * json string convert to javaBean
     */
    public static <T> T json2pojo(String jsonStr, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(jsonStr, clazz);
        } catch (IOException e) {
            LOG.error("json string convert to javaBean error：{}", e.getMessage());
        }
        return null;
    }

    /**
     * json string convert to map
     */
    public static Map<String, Object> json2map(String jsonStr) {
        try {
            return OBJECT_MAPPER.readValue(jsonStr, Map.class);
        } catch (IOException e) {
            LOG.error("json string convert to map error：{}", e.getMessage());
        }
        return null;
    }

    /**
     * json string convert to map with javaBean
     */
    public static <T> Map<String, T> json2map(String jsonStr, Class<T> clazz) {
        Map<String, Map<String, Object>> map = null;
        try {
            map = OBJECT_MAPPER.readValue(jsonStr, new TypeReference<Map<String, T>>() {
            });
        } catch (IOException e) {
            LOG.error("json string convert to map with javaBean error：{}", e.getMessage());
        }
        Map<String, T> result = new HashMap<String, T>();
        if (map != null) {
            for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
                result.put(entry.getKey(), map2pojo(entry.getValue(), clazz));
            }
        }
        return result;
    }

    /**
     * json array string convert to list with javaBean
     */
    public static <T> List<T> json2list(String jsonArrayStr, Class<T> clazz) {
        List<Map<String, Object>> list = null;
        try {
            list = OBJECT_MAPPER.readValue(jsonArrayStr, new TypeReference<List<T>>() {
            });
        } catch (IOException e) {
            LOG.error("json array string convert to list with javaBean error：{}", e.getMessage());
        }
        List<T> result = new ArrayList<T>();
        if (list != null) {
            for (Map<String, Object> map : list) {
                result.add(map2pojo(map, clazz));
            }
        }
        return result;
    }

    /**
     * map convert to javaBean
     */
    public static <T> T map2pojo(Map map, Class<T> clazz) {
        return OBJECT_MAPPER.convertValue(map, clazz);
    }
}
