package com.cxylk.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * JSON 工具类
 *
 * @author admin
 */
@Slf4j
public class JsonUtils {

    private JsonUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 将对象转换成JSON字符串
     *
     * @param obj 目标对象
     * @return 字符串，转换失败时返回null
     */
    public static String toJson(Object obj) {
        return JSON.toJSONString(obj, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * 将单个键值对转换成JSON字符串，用于返回只有一个键值对json时的便捷方法
     *
     * @param key   目标key
     * @param value 目标value
     * @return 字符串，转换失败时返回null
     */
    public static String toJson(Object key, Object value) {
        Map<Object, Object> map = Maps.newHashMap();
        map.put(key, value);
        return toJson(map);
    }

    /**
     * 反序列化POJO或简单Collection如List<String>.
     * <p>
     * 如果JSON字符串为Null或"null"字符串, 返回Null. 如果JSON字符串为"[]", 返回空集合.
     * <p>
     * 如需反序列化复杂Collection如List<MyBean>, 请使用fromJson(String, JavaType)
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    /**
     * 判断字符串是否是个json
     *
     * @param jsonInString
     * @return
     */
    public static boolean isJsonValid(String jsonInString) {
        if (StringUtils.isBlank(jsonInString)) {
            return false;
        }
        try {

            Object object = JSON.parse(jsonInString);
            if (object != null ) {
                if (object instanceof JSONObject || object instanceof JSONArray) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * @param jsonInString
     * @return
     */
    public static JSONObject stringToJsonObject(String jsonInString) {
        return JSON.parseObject(jsonInString);
    }

    /**
     * @param text
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> toList(String text, Class<T> clazz) {
        return JSON.parseArray(text, clazz);
    }

    /**
     * @param s
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> stringToMap(String s) {
       return (Map<K, V>) JSON.parseObject(s);
    }


}
