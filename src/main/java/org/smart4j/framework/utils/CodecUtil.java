package org.smart4j.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @Author Administrator
 * @Date 2018-05-03
 * @since 1.0.0
 */
public class CodecUtil {

    private static final Logger LOGGER= LoggerFactory.getLogger(CodecUtil.class);

    /**将url编码*/
    public static String ecodeURl(String source) {
        String target;
        try{
            target= URLEncoder.encode(source,"UTF-8");

        }catch (Exception e){
            LOGGER.error("encode url failture",e);
            throw new RuntimeException(e);
        }
        return target;
    }

    /**将url解码*/
    public static String decodeURL(String source){
        String target;
        try{
            target= URLDecoder.decode(source,"UTF-8");
        }catch (Exception e){
            LOGGER.error("decode url failture",e);
            throw new RuntimeException(e);
        }
        return target;
    }
}
