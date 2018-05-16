package org.smart4j.framework.helper;

import org.smart4j.framework.annotation.Controller;
import org.smart4j.framework.annotation.Service;
import org.smart4j.framework.utils.ClassUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedArrayType;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author zzg
 * @Date 2018-05-03
 * @since 1.0.0
 */
public final class ClassHelper {
    private static final Set<Class<?>> CLASS_SET;

    static {
        String basePackage=ConfigHelper.getAppBasePackage();
        CLASS_SET= ClassUtil.getClassSet(basePackage);
    }

    /**获取应用包名下面的所有的类*/
    public  static Set<Class<?>> getClassSet(){
        return CLASS_SET;
    }

    /**获取应用包名下所有的Service类*/
    public static Set<Class<?>> getServiceSet(){
        return CLASS_SET.stream().filter(cls->cls.isAnnotationPresent(Service.class))
                .collect(Collectors.toSet());
    }

    /**获取应用包名下所有Controller类*/
    public  static Set<Class<?>> getControllerSet(){
        return CLASS_SET.stream().filter(cls->cls.isAnnotationPresent(Controller.class))
                .collect(Collectors.toSet());
    }

    /**获取应用包名下所有Bean类*/
    public static Set<Class<?>> getBeanClassSet(){
        Set<Class<?>> beanClassSet=new HashSet<>();
        beanClassSet.addAll(getServiceSet());
        beanClassSet.addAll(getControllerSet());
        return beanClassSet;
    }

    /**获取应用包名下某父类（或接口）的所有子类（或实现类）*/
    public static Set<Class<?>> getClassSetBySuper(Class<?> superClass){
       return  CLASS_SET.stream().filter(
               cls->superClass.isAssignableFrom(cls) &&
               !superClass.equals(cls)).collect(Collectors.toSet());
    }

    /**获取应用包名下带有某注解的所有类*/
    public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> annotationClass){
        return CLASS_SET.stream().filter(cls->cls.isAnnotationPresent(annotationClass))
                .collect(Collectors.toSet());
    }

}
