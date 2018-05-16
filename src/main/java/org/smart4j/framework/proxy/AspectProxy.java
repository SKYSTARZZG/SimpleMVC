package org.smart4j.framework.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.soap.MTOM;
import java.lang.reflect.Method;

/**
 * doProxy方法是关键，先从proxyChain中获取目标类，目标方法，和方法参数，随后通过try...catch...finally 代码
 * 来实现调用框架，从框架中抽象出一系列的“钩子方法”，这些抽象方法可以在Aspect子类中有选择的进行实现
 * @Author zzg
 * @Date 2018-05-05
 * @since 1.0.0
 */
public abstract class AspectProxy implements Proxy {

    private static final Logger logger= LoggerFactory.getLogger(AspectProxy.class);
    @Override
    public final Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result;
        Class<?> cls=proxyChain.getTargetClass();
        Method method=proxyChain.getTargetMethod();
        Object[] params=proxyChain.getMethodParams();
        begin();
        try{
            if(intercept(cls,method,params)){
                before(cls,method,params);
                result =proxyChain.doProxyChain();
                after(cls,method,params,result);
            }else{
                result=proxyChain.doProxyChain();
            }
        }catch (Exception e){
            logger.error("proxy failture",e);
            error(cls,method,params,e);
            throw e;
        }finally {
            end();
        }
        return result;
    }
    public void begin(){
    }

    public boolean intercept(Class<?> cls, Method method,Object[] aprams) throws Throwable{
        return true;
    }

    public void before(Class<?> cls,Method method,Object[] params) throws Throwable{
    }

    public void after(Class<?> cls,Method method,Object[] params,Object result) throws Throwable{

    }

    public void error(Class<?> cls,Method method,Object[] parmas,Throwable e){

    }
    public void end(){

    }
}
