package org.smart4j.framework.helper;

import org.smart4j.framework.annotation.Inject;
import org.smart4j.framework.utils.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**实现依赖注入功能
 * 当IocHelper这个类被加载的时候就会加载他的静态块，完成IOC容器的初始化工作
 * IOC框架中所管理的对象都是单例的，IOC框架底层从IocHelper中获取BeanMap，而BeanMap中的对象是事先创建好并放入这个bean容器中，所以对象都是单例的。
 * @Author zzg
 * @Date 2018-05-03
 * @since 1.0.0
 */
public final class IocHelper {
    static{
        Map<Class<?>,Object> beanMap=BeanHelper.getBeanMap();
        if(!beanMap.isEmpty()){
            /**遍历beanMap*/
            beanMap.forEach((Class<?> beanClass, Object beanInstance) ->{
                Field[] beanFields=beanClass.getDeclaredFields();
                if(beanFields!=null&&beanFields.length>0){
                    for(Field beanField:beanFields){
                        if(beanField.isAnnotationPresent(Inject.class)){
                            Class<?> beanFieldClass=beanField.getType();
                            Object beanFieldInstance=beanMap.get(beanFieldClass);
                            if(beanFieldInstance!=null){
                                /**通过反射初始化BeanField的值*/
                                ReflectionUtil.setField(beanInstance,beanField,beanFieldInstance);
                            }
                        }
                    }
                }
            });
        }
    }
}
