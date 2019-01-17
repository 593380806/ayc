package com.ayc.framework.datasource.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据源注解<br>
 * DataSource Annotation
 * @author ysj
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.PARAMETER, java.lang.annotation.ElementType.METHOD})
@Inherited
public @interface DataSource
{
  public abstract String field();

  public abstract String name() default "";

  public abstract boolean readonly() default false;
}

