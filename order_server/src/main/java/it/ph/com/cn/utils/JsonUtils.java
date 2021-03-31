package it.ph.com.cn.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Json解析工具类
 */
public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Json字符串转JsonNode对象方法
     */
    public static JsonNode str2JsonNode(String str) {
        try {
            return objectMapper.readTree(str);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
