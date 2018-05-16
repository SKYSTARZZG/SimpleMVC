package org.smart4j.framework.utils;

/**
 * Created by Administrator on 2018-05-01.
 */
public class CastUtil {
    /**转为String(可指定默认值）*/
    public static String castString(Object obj,String defaultValue){
        return obj==null?defaultValue:String.valueOf(obj);
    }
    /**转为String(默认值是""）*/
    public static String castString(Object obj) {
        return castString(obj,"");
    }
    /**转为Double(可指定默认值）*/
    public static double castDouble(Object obj,double defaultValue){
        double doubleValue=defaultValue;
        if(obj!=null){
            String strValue=castString(obj);
            if(!StringUtil.isEmpty(strValue)){
                try{
                    doubleValue=Double.parseDouble(strValue);
                }catch (NumberFormatException e){
                    doubleValue=defaultValue;
                }
            }
        }
        return doubleValue;
    }
    /**转为Double(默认值0）*/
    public static double castDouble(Object obj){
        return castDouble(obj,0.0);
    }
    /**转为long(提供默认值）*/
    public static long castLong(Object obj,long defaultValue){
        long longValue=defaultValue;
        if(obj!=null){
            String strValue=castString(obj);
            if(!StringUtil.isEmpty(strValue)){
                try{
                    longValue=Long.parseLong(strValue);
                }catch (NumberFormatException e){
                    longValue=defaultValue;
                }
            }
        }
        return longValue;
    }
    /**转为long(默认值0）*/
    public static long castLong(Object obj){
        return castLong(obj,0);
    }

    /**转为int（提供默认值）*/
    public static  int castInt(Object obj,int defaultValue){
        int intValue=defaultValue;
        if(obj!=null){
            String strValue=castString(obj);
            if(!StringUtil.isEmpty(strValue)){

                try {
                    intValue=Integer.parseInt(strValue);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return intValue;
    }
    /**转为int（默认值0）*/
    public static  int castInt(Object obj){
        return castInt(obj,0);
    }
    /**转为oolean（提供默认值）*/
    public static boolean castBoolean(Object obj,boolean defaultValue){
        boolean booleanValue=defaultValue;
        if(obj!=null){
            String strValue=castString(obj);
            booleanValue=Boolean.parseBoolean(strValue);
        }
        return booleanValue;
    }
    /**转为oolean（默认值false）*/
    public static boolean castBoolean(Object obj){
        return castBoolean(obj,false);
    }
}
