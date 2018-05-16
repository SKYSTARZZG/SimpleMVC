package org.smart4j.framework.proxy;

import com.fasterxml.jackson.databind.deser.impl.MethodProperty;
import net.sf.cglib.proxy.MethodProxy;
import org.smart4j.framework.utils.ClassUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 链式代理，将多个代理通过一条链子串起来，一个个去执行，执行顺序决定于添加到链子上的先后顺序
 * @Author zzg
 * @Date 2018-05-04
 * @since 1.0.0
 */
public class ProxyChain {
    private  final Class<?> targetClass;
    private final Object targetObject;
    private final Method targetMethod;
    private final MethodProxy methodProxy;
    private final Object[] methodParams;

    private List<Proxy> proxyList=new ArrayList<Proxy>();
    private int proxyIndex=0;

    public ProxyChain(Class<?> targetClass, Object targetObject, Method targetMethod, MethodProxy methodProxy, Object[] methodParams, List<Proxy> proxyList) {
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.methodProxy = methodProxy;
        this.methodParams = methodParams;
        this.proxyList = proxyList;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Object[] getMethodParams() {
        return methodParams;
    }

    /**proxyIndex充当代理对象的计数器，如果没有达到proxyList的上限，则从proxyList中取出相应打的Proxy对象，
     * 并调用其doProxy方法，在Proxy接口的实现中会提供相应的横切逻辑，并调用doProxyChain方法，随后将再次调用
     * 当前ProxyChain对象的doProxyChain方法，知道ProxyIndex达到proxyList的上限为止，最后调用methodProxy
     * 的invokeSuper方法，执行目标对象的业务逻辑*/
    public Object doProxyChain() throws Throwable{
        Object methodResult;
        if(proxyIndex<proxyList.size()){
            methodResult=proxyList.get(proxyIndex++).doProxy(this);
        }else {
            methodResult=methodProxy.invokeSuper(targetObject,methodParams);
        }
        return methodResult;
    }
}
