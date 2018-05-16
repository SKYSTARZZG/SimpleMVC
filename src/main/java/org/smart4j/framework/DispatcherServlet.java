package org.smart4j.framework;

import com.sun.deploy.util.StringUtils;
import org.smart4j.framework.bean.Data;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.bean.View;
import org.smart4j.framework.helper.BeanHelper;
import org.smart4j.framework.helper.ConfigHelper;
import org.smart4j.framework.helper.ControllerHelper;
import org.smart4j.framework.helper.HelperLoader;
import org.smart4j.framework.utils.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**这个类处理所有的请求，从HttpServletRequest对象中获取请求方法和请求路径，
 * 通过ControllerHelper#getHandler()方法获取Handler对象
 * 通过BeanHelper.getBean方法获取Controller对象
 * 从HttpServletRequest对象中获取所有的请求参数，并将其初始化到一个名为Param的对象中
 *
 * @Author zzg
 * @Date 2018-05-03
 * @since 1.0.0
 */
public class DispatcherServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        HelperLoader.init();
        /**获取ServletContext对象，用来注册Servlet*/
        ServletContext servletContext=config.getServletContext();
        /**注册处理jsp的Servlet*/
        ServletRegistration jspServlet=servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppAssetPath()+"*");
        /**注册处理静态资源的默认的Servlet*/
        ServletRegistration defaultServlet=servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath()+"*");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /**获取 请求方法与路径*/
        String requestMethod=req.getMethod().toLowerCase();
        String requestPaht=req.getPathInfo();

        /**获取Action处理器*/
        Handler handler= ControllerHelper.getHandler(requestMethod,requestPaht);
        if(handler!=null){
            /**获取Controller类及其Bean实例*/
            Class<?> controllerClass=handler.getControllerClass();
            Object controllerBean= BeanHelper.getBean(controllerClass);

            /**创建请求参数对象*/
            Map<String,Object> paramMap=new HashMap<String,Object>();
            Enumeration<String> paramNames=req.getParameterNames();
            while(paramNames.hasMoreElements()){
                String paramName=paramNames.nextElement();
                String paramValue=req.getParameter(paramName);
                paramMap.put(paramName,paramValue);
            }
            String body= CodecUtil.decodeURL(StreamUtil.getString(req.getInputStream()));
            if(StringUtil.isNotEmpty(body)){
                String[] params= StringUtils.splitString(body,"&");
                if(params!=null&&params.length>0){
                    for(String param:params){
                        String[] array=StringUtils.splitString(param,"=");
                        if(array!=null&&array.length==2){
                            String paramName=array[0];
                            String paramValue=array[1];
                            paramMap.put(paramName,paramValue);
                        }
                    }
                }
            }

            Param param=new Param(paramMap);
            /**调用action方法*/
            Method actionMethod=handler.getActionMethod();
            Object result;
            if(param.isEmpty()){
                result= ReflectionUtil.invokeMethod(controllerBean,actionMethod);
            }else{
                result= ReflectionUtil.invokeMethod(controllerBean,actionMethod,param);
            }

            /**根据action方法返回值的类型进行不同的处理*/
            if(result instanceof View){
                View view=(View)result;
                String path=view.getPath();
                if(StringUtil.isNotEmpty(path)){
                    if(path.startsWith("/")){
                        resp.sendRedirect(req.getContextPath()+path);
                    }else{
                        Map<String,Object> model=view.getModel();
                        for(Map.Entry<String,Object> entry:model.entrySet()){
                            req.setAttribute(entry.getKey(),entry.getValue());
                        }
                        req.getRequestDispatcher(ConfigHelper.getAppAssetPath()+path).forward(req,resp);
                    }
                }
            }else if(result instanceof Data){
                Data data=(Data) result;
                Object model=data.getModel();
                if(model!=null){
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    PrintWriter writer=resp.getWriter();
                    String json= JsonUtil.toJson(model);
                    writer.write(json);
                    writer.flush();
                    writer.close();
                }
            }
        }
    }
}
