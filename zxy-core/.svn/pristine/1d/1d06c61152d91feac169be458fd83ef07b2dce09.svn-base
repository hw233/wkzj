package com.jtang.core.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jtang.core.db.DBQueueType;

/**
 * db表名标注
 * @author 0x737263
 *
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface TableName {

	/**
	 * 表名？？是否要的存在。在反射中直接用.Entity的类名即可。
	 * @return
	 */
	String name();
	/**
	 * 存储级别 {@code DBQueueType}
	 * @return
	 */
	DBQueueType type();
}
