package org.smart4j.framework.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**createProxyY用来创建代理对象，输入目标类和一组Proxy接口实现，输出一个代理对象
 * 使用CGLib提供的Enhancer#create方法来创建代理对象，将intercept的参数传入ProxyChain的构造器中即可
 * @Author zzg
 * @Date 2018-05-04
 * @since 1.0.0
 */
public class ProxyManager {
    public static <T> T createProxy(final Class<?> targetClass,final List<Proxy> proxyList){
        return (T) Enhancer.create(targetClass, new MethodInterceptor() {
            @Override
            public Object intercept(Object targetObject, Method targetMethod, Object[] methodParams,
                                    MethodProxy methodProxy) throws Throwable {
                return new ProxyChain(targetClass,targetObject,targetMethod,
                        methodProxy,methodParams,proxyList).doProxyChain();
            }
        });
    }
}
