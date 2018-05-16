package org.smart4j.framework.bean;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Administrator
 * @Date 2018-05-06
 * @since 1.0.0
 */
public class TotalParam {
    private List<FileParam> fileParams;
    private List<FormParam> formParams;

    public TotalParam(List<FormParam> formParams) {
        this.formParams = formParams;
    }

    public TotalParam(List<FileParam> fileParams, List<FormParam> formParams) {
        this.fileParams = fileParams;
        this.formParams = formParams;
    }

    /**获取请求参数映射*/
    public Map<String,Object> getFieldMap(){
        Map<String,Object> fieldMap=new HashMap<>();
        if(formParams!=null&&formParams.size()>0){
            formParams.forEach(formParam->{
                String fieldName=formParam.getFieldParam();
                Object fieldValue=formParam.getFieldValue();
                if(fieldMap.containsKey(fieldName)){
                    fieldValue=fieldMap.get(fieldName)+File.separator+fieldValue;
                }
                fieldMap.put(fieldName,fieldValue);
            });
        }
        return fieldMap;
    }

    public Map<String,List<FileParam>> getFileMap(){
        return null;
    }
}
