package org.example.bus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 可从本地读取和修改的设置
 * <p>
 * <p>
 * 使用时把该注解加在一个接口上
 * 接口的每个方法都应该是被
 *
 * @see SettingGetter  或者
 * @see SettingSetter  所注解的
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface LocalSetting {
}
