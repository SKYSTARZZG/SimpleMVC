package org.smart4j.framework.bean;

import org.smart4j.framework.utils.CastUtil;
import org.smart4j.framework.utils.CollectionUtil;

import java.util.Map;

/**
 * @Author Administrator
 * @Date 2018-05-03
 * @since 1.0.0
 */
public class Param {
    private Map<String,Object> paramMap;

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }
    /**根据参数名获取long型参数*/
    public long getLong(String name){
        return CastUtil.castLong(paramMap.get(name));
    }
    /**获取所有字段信息*/
    public  Map<String,Object> getParamMap(){
        return paramMap;
    }

    /**判断参数是否为空*/
    public boolean isEmpty(){
        return CollectionUtil.isEmpty(paramMap);
    }

}
