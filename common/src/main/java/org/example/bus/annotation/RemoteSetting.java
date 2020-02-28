package org.example.bus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 把该注解加在一个接口上
 * 接口的每个方法都应该是 被
 * @see SettingGetter  所注解的
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface RemoteSetting {
}
