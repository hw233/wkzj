package com.jtang.core.mina.router.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 命令注解，用于标识接收消息的方法
 * {@see ModuleHandler}
 * @author 0x737263
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Cmd {
	
	/**
	 * 命令id(该模块内唯一)
	 * @return
	 */
	public byte Id();
	
	/**
	 * 验证角色是否登陆.默认true
	 * @return
	 */
	public boolean CheckActorLogin() default true;
	
}
