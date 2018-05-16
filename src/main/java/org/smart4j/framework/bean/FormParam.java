package org.smart4j.framework.bean;

/**
 * @Author Administrator
 * @Date 2018-05-06
 * @since 1.0.0
 */
public class FormParam {
    private String fieldParam;
    private Object fieldValue;

    public FormParam(String fieldParam, Object fieldValue) {
        this.fieldParam = fieldParam;
        this.fieldValue = fieldValue;
    }

    public String getFieldParam() {
        return fieldParam;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}

