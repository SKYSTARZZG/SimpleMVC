package org.smart4j.framework.helper;

import org.smart4j.framework.utils.ReflectionUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 获取所有被smart框架管理的Bean实例，需要调用ClassHealper 的getBeanClassSet方法，随后循环调用ReflectionUtil类的newInstance方法，根据类来实例化对象，最后将每次创建的对象放在一个静态的Map<Class<?>,Object> 中，
 * BeanHelper就是一个Bean容器，我们只需要调用getBean()方法，传入Bean类名，就能获取Bean实例
 *
 * @Author zzg
 * @Date 2018-05-03
 * @since 1.0.0
 */
public final class BeanHelper {
    /**Bean映射，用来存放Bean类与Bean实例的映射关系*/
    private static final Map<Class<?>,Object> BEAN_MAP=new HashMap<>();

    static {
        Set<Class<?>> beanClassSet=ClassHelper.getBeanClassSet();
        beanClassSet.forEach(beanClass->{
            Object obj= ReflectionUtil.newInstance(beanClass);
            BEAN_MAP.put(beanClass,obj);
        });
    }

    /**获取bean映射*/
    public static Map<Class<?>,Object> getBeanMap(){
        return BEAN_MAP;
    }

    /**获取Bean实例*/
    public static <T> T getBean(Class<T> cls){
        if(!BEAN_MAP.containsKey(cls)){
            throw new RuntimeException(("can not get bean by class:")+cls);
        }
        return (T)BEAN_MAP.get(cls);
    }

    public  static  void setBean(Class<?> cls,Object obj){
        BEAN_MAP.put(cls,obj);
    }


}
