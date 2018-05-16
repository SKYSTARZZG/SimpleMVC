package org.smart4j.framework.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author Administrator
 * @Date 2018-05-03
 * @since 1.0.0
 */
public class JsonUtil {

    private static final Logger LOGGER= LoggerFactory.getLogger(JsonUtil.class);
    private static final ObjectMapper OBJECT_MAPPER=new ObjectMapper();

    /**将POJO转化成JSON*/
    public static <T> String toJson(T obj) {
        String json;
        try{
            json=OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOGGER.error("convert pojo to json failture",e);
            throw new RuntimeException(e);
        }
        return json;
    }

    /**将JSON转换成POJO*/

    public  static <T> T fromJson(String json,Class<T> type){
        T pojo;
        try{
            pojo=OBJECT_MAPPER.readValue(json,type);
        }catch (Exception e){
            LOGGER.error("convert json to pojo failture",e);
            throw new RuntimeException(e);
        }
        return pojo;
    }
}
