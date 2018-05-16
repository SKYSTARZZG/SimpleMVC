package org.smart4j.framework.annotation;

import java.lang.annotation.*;

/**
 * @Author Administrator
 * @Date 2018-05-04
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {
    /**注解*/
    Class<? extends Annotation> value();
}
