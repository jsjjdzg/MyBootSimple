package com.bocloud.emaas.database.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface Column {
	// 数据库字段名
	public String value() default "";

	// 字段是否可以为空
	public boolean nullable() default true;
}
