package org.smart4j.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author Administrator
 * @Date 2018-05-01
 * @since 1.0.0
 */
public class PropsUtil {
    private static Logger LOGGER= LoggerFactory.getLogger(PropsUtil.class);
    public static Properties loadProps(String fileName){
        Properties props=null;
        InputStream is=null;
        try{
            is=Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(fileName);
            if(is==null){
                throw new FileNotFoundException(fileName+"file is not found");
            }
            props=new Properties();
            props.load(is);
        }catch (Exception e){
            LOGGER.error("load propeties file falure",e);
        }finally {
            if(is!=null){
                try {
                    is.close();
                }catch (IOException e){
                    LOGGER.error("close input stream failture",e);
                }
            }
        }
        return props;
    }

    /**获取字符型属性（默认为空字符串）*/
    public  static String getString(Properties props,String key){
        return getString(props,key,"");
    }
    /**获取字符型属性（可指定默认值）*/
    public static String getString(Properties props,String key,String defaultValue){
        String value=defaultValue;
        if(props.containsKey(key)){
            value=props.getProperty(key);
        }
        return value;
    }
    /**获取数值型属性（默认0）*/
    public static int getInt(Properties props,String key){
        return  getInt(props,key,0);
    }
    /**获取数值型属性（可指定默认值）*/
    public static int getInt(Properties props,String key,int defaultValue){
        int value=defaultValue;
        if(props.containsKey(key)){
            value=Integer.parseInt(props.getProperty(key));
        }
        return value;
    }
    /**获取布尔型属性（可指定默认值）*/
    public static boolean getBoolean(Properties props,String key,boolean defaultValue){
        boolean value=defaultValue;
        if(props.containsKey(key)){
            value=Boolean.parseBoolean(props.getProperty(key));
        }
        return value;
    }
    /**获取布尔型属性（默认false）*/
    public static boolean getBoolean(Properties props,String key){
        return getBoolean(props,key,false);
    }
}
