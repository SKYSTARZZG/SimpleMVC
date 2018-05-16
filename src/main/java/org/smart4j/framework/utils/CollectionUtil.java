package org.smart4j.framework.utils;

import java.util.Map;

/**
 * @Author Administrator
 * @Date 2018-05-06
 * @since 1.0.0
 */
public class CollectionUtil {

    public static boolean isEmpty(Map map){
        if(map==null||map.size()>0) {
            return false;
        }else{
            return true;
        }


    }

}
