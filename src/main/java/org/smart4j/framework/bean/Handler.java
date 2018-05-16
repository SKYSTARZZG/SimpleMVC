package org.smart4j.framework.bean;

import java.lang.reflect.Method;

/**
 * @Author zzg
 * @Date 2018-05-03
 * @since 1.0.0
 */
public class Handler {
    /**Controller类*/
    private Class<?> controllerClass;
    /**Action类*/
    private Method actionMethod;

    public Handler(Class<?> controllerClass, Method actionMethod) {
        this.controllerClass = controllerClass;
        this.actionMethod = actionMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }
}
