package com.ayc.framework.dao;

import java.lang.annotation.*;

/**
 * 分表数标签
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@Inherited
public @interface TableIndex {

	int value() default 1;

}
