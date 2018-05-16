package org.smart4j.framework.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.annotation.Transaction;
import org.smart4j.framework.helper.DatabaseHelper;

import java.lang.reflect.Method;

/**
 * FlAG_HOLDER 本地线程变量，是一个标志，可以保证同一线程中事务控制相关逻辑只会执行一次。
 * 通过ProxyChain对象可以获取目标方法，进而判断该方法是否有Transaction注解。
 * @Author zzg
 * @Date 2018-05-06
 * @since 1.0.0
 */
public class TransactionProxy implements Proxy{

    private static  final Logger LOGGER= LoggerFactory.getLogger(TransactionProxy.class);

    private static final ThreadLocal<Boolean> FLAG_HOLDER= ThreadLocal.withInitial(() -> false);

    /**DatabaseHelper.beginTransaction（）开启事务
     * 调用ProxyChain的doProxyChain方法执行目标方法
     * 接着调用DatabaseHelper.commitTransaction()提交事务
     * 在异常处理当中调用DatabaseHelper.rollbackTransaction()回滚事务*/
    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result;
        boolean flag=FLAG_HOLDER.get();
        Method method=proxyChain.getTargetMethod();
        if(!flag&&method.isAnnotationPresent(Transaction.class)){
            FLAG_HOLDER.set(true);
            try{
                DatabaseHelper.beginTransaction();
                LOGGER.debug("begin transaction");
                result=proxyChain.doProxyChain();
                DatabaseHelper.commitTransaction();
                LOGGER.debug("commit transaction");
            }catch(Exception e){
                DatabaseHelper.rollbackTransaction();
                LOGGER.debug("rollback transaction");
                throw e;
            }finally {
                FLAG_HOLDER.remove();
            }
        }else{
            result=proxyChain.doProxyChain();
        }
        return result;
    }
}
