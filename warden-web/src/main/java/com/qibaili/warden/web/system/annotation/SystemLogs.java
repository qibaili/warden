package com.qibaili.warden.web.system.annotation;

import java.lang.annotation.*;

/**
 * @author qibaili
 * @date 2018-08-14
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemLogs {

    String value();

}
