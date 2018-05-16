package org.smart4j.framework.helper;

import com.sun.deploy.util.ArrayUtil;
import org.smart4j.framework.annotation.Action;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Request;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 加载Controller
 * 通过ClassHelper,获取定义了Controller注解的类
 * 通过反射获取Controller类中带有Action注解的方法，获取Action注解中的请求表达式
 * 获取请求方法与请求路径，封装一个请求对象（Request)与处理对象（Handler）
 * 将Request与handler建立一个映射关系，放入ActionMap中，
 * 提供一个可以根据请求方法和请求路径获取处理对象的方法
 * @Author zzg
 * @Date 2018-05-03
 * @since 1.0.0
 */
public class ControllerHelper {
    private static final Map<Request,Handler> ACTION_MAP=new HashMap<>();

    static {
        Set<Class<?>> controllerClassSet=ClassHelper.getControllerSet();
        if(controllerClassSet!=null&&controllerClassSet.size()>0){
            controllerClassSet.forEach(controllerClass->{
                Method[] methods=controllerClass.getDeclaredMethods();
                if(methods!=null&&methods.length>0){
                    for(Method method:methods){
                        if(method.isAnnotationPresent(Action.class)){
                            Action action=method.getAnnotation(Action.class);
                            String mapping=action.value();
                            if(mapping.matches("\\w+:\\w*")){
                                String[] array=mapping.split(":");
                                if(array!=null&&array.length>0){
                                    String requestMethod=array[0];
                                    String requestPath=array[1];
                                    Request request=new Request(requestMethod,requestPath);
                                    Handler handler=new Handler(controllerClass,method);
                                    ACTION_MAP.put(request,handler);
                                }
                            }
                        }

                    }
                }
            });
        }
    }

    public static Handler getHandler(String requestMethod,String requestPath){
        Request request=new Request(requestMethod,requestPath);
        return ACTION_MAP.get(request);
    }

}
