package org.smart4j.framework.bean;


import com.sun.org.apache.xpath.internal.operations.Equals;

/**
 * @Author zzg
 * @Date 2018-05-03
 * @since 1.0.0
 */
public class Request {
    /**请求方法*/
    private String requestMethod;
    /**请求路径*/
    private String requestPath;

    public Request(String requestMethod, String requestPath) {
        this.requestMethod = requestMethod;
        this.requestPath = requestPath;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
