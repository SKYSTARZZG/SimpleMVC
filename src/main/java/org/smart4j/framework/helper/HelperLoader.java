package org.smart4j.framework.helper;

import org.smart4j.framework.annotation.Controller;
import org.smart4j.framework.utils.ClassUtil;

/**
 * 加载ClassHelper、BeanHelper、IocHelper、ControllerHelper，就是加载他们的静态代码块。
 * @Author zzg
 * @Date 2018-05-03
 * @since 1.0.0
 */
public class HelperLoader {
    public static void init() {
        Class<?>[] classList={
                ClassHelper.class,
                BeanHelper.class,
                AopHelper.class,
                IocHelper.class,
                ControllerHelper.class
        };
        for(Class<?> cls:classList){
             ClassUtil.loadClass(cls.getName(),true);
        }
    }
}
