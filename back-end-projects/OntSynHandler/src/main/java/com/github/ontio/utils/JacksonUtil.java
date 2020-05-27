package com.github.ontio.utils;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The class JacksonUtil
 *
 * json字符与对像转换
 *
 * @version: $Revision$ $Date$ $LastChangedBy$
 *
 */
public final class JacksonUtil {

    public static ObjectMapper objectMapper;

    /**
     * jsonstring to JavaBean
     *
     * @param jsonStr
     * @param valueType
     * @return
     */
    public static <T> T jsonStrToBean(String jsonStr, Class<T> valueType) {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }

        try {
            return objectMapper.readValue(jsonStr, valueType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * jsonarray to List
     * @param jsonStr
     * @param valueTypeRef
     * @return
     */
    public static <T> T jsonStrToBean(String jsonStr, TypeReference<T> valueTypeRef){
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }

        try {
            return objectMapper.readValue(jsonStr, valueTypeRef);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * JavaBean to jsonstring
     *
     * @param object
     * @return
     */
    public static String beanToJSonStr(Object object) {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }

        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}