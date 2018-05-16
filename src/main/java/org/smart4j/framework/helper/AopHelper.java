package org.smart4j.framework.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.annotation.Aspect;
import org.smart4j.framework.annotation.Service;
import org.smart4j.framework.proxy.AspectProxy;
import org.smart4j.framework.proxy.Proxy;
import org.smart4j.framework.proxy.ProxyManager;
import org.smart4j.framework.proxy.TransactionProxy;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @Author Administrator
 * @Date 2018-05-05
 * @since 1.0.0
 */
public final class AopHelper {
    private static final Logger LOGGER= LoggerFactory.getLogger(AopHelper.class);

    static{
        try{
            Map<Class<?>,Set<Class<?>>> proxyMap=ceateProxyMap();
            Map<Class<?>,List<Proxy>> targetMap=createTargetMap(proxyMap);
            targetMap.forEach((targetClass,proxyList)->{
                Object proxy= ProxyManager.createProxy(targetClass,proxyList);
                BeanHelper.setBean(targetClass,proxy);
            });
        } catch (Exception e) {
            LOGGER.error("aop failture",e);
        }
    }

    private static Set<Class<?>> createTargetClassSet(Aspect aspect)throws Exception{
        Set<Class<?>> targetClassSet=new HashSet<>();
        Class<? extends Annotation> annotation=aspect.value();
        if(annotation!=null&&!annotation.equals(Aspect.class)){
            targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));
        }
        return targetClassSet;
    }

    /**获取代理类及其目标类集合之间的映射关系，一个代理类可以对应一个或多个目标类，这里所说的代理类是切面类*/
    private static Map<Class<?>,Set<Class<?>>> ceateProxyMap() throws Exception{
        Map<Class<?>,Set<Class<?>>> proxyMap=new HashMap<>();
        addAspectProxy(proxyMap);
        addTransactionProxy(proxyMap);
        return proxyMap;
    }
    private static void addAspectProxy(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception{
        Set<Class<?>> proxyClassSet=ClassHelper.getClassSetBySuper(AspectProxy.class);
        for (Class<?> proxyClass : proxyClassSet) {
            if (proxyClass.isAnnotationPresent(Aspect.class)) {
                Aspect aspect = proxyClass.getAnnotation(Aspect.class);
                Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
                proxyMap.put(proxyClass, targetClassSet);
            }
        }
    }
    private static void addTransactionProxy(Map<Class<?>, Set<Class<?>>> proxyMap) {
        Set<Class<?>> serviceClassSet=ClassHelper.getClassSetByAnnotation(Service.class);
        proxyMap.put(TransactionProxy.class,serviceClassSet);
    }

    private static Map<Class<?>,List<Proxy>> createTargetMap(Map<Class<?>,Set<Class<?>>> proxyMap)throws Exception{
        Map<Class<?>,List<Proxy>> targetMap=new HashMap<>();
        proxyMap.forEach((Class<?> proxyClass, Set<Class<?>> targetClassSet) ->{
            for (Class<?> targetClass : targetClassSet) {
                try {
                    Proxy proxy = (Proxy) proxyClass.newInstance();
                    if(targetMap.containsKey(targetClass)){
                        targetMap.get(targetClass).add(proxy);
                    }else{
                        List<Proxy> proxyList=new ArrayList<>();
                        proxyList.add(proxy);
                        targetMap.put(targetClass,proxyList);
                    }
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        return targetMap;
    }
}
