package org.smart4j.framework.utils;

import com.sun.deploy.util.StringUtils;

/**
 * @Author zzg
 * @Date 2018-05-03
 * @since 1.0.0
 */
public class StringUtil {
    public static boolean isEmpty(String str){
        if(str!=null){
            str=str.trim();
        }
        return str.isEmpty();
    }

    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }
}
