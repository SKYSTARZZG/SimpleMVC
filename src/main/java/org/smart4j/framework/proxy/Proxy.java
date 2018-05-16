package org.smart4j.framework.proxy;

/**
 * @Author zzg
 * @Date 2018-05-04
 * @since 1.0.0
 */
public interface Proxy {
    Object doProxy(ProxyChain proxyChain) throws Throwable;
}
