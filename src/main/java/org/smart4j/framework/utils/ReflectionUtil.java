package org.smart4j.framework.utils;

import com.sun.deploy.trace.LoggerTraceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

/**
 * 反射工具类，用来实例化对象。
 * @Author zzg
 * @Date 2018-05-03
 * @since 1.0.0
 */
public class ReflectionUtil {
    private static final Logger LOGGER= LoggerFactory.getLogger(ReflectionUtil.class);

    /**创造实例*/
    public static Object newInstance(Class<?> cls){
        Object instance;
        try{
            instance=cls.newInstance();
        }catch (Exception e){
            LOGGER.error("new instance failture",e);
            throw new RuntimeException(e);
        }
        return instance;
    }

    /**调用方法*/
    public static Object invokeMethod(Object obj, Method method,Object... args){
        Object result;
        try{
            method.setAccessible(true);
            result=method.invoke(obj,args);
        } catch (IllegalAccessException e) {
            LOGGER.error("invoke method failture",e);
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            LOGGER.error("invoke method failture",e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**设置成员变量的值*/
    public static void setField(Object obj, Field field,Object value){
        try{
            field.setAccessible(true);
            field.set(obj,value);
        }catch (Exception e){
            LOGGER.error("set field error",e);
            throw new RuntimeException(e);
        }
    }
}
