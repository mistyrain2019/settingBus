package org.example.bus.annotation;

import org.example.bus.Converter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 仅支持 int long boolean double
 * 和 自定义类 如果是自定义类 需要 实现一个
 * @see Converter 接口 并把其类对象 传给 converterClazz
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface SettingGetter {

    // 为了拿到这个设置需要的键
    String key();

    // 自定义类 的 converter
    Class<? extends Converter>[] converterClazz() default {};

    // 关于这个设置的注释
    String explanation() default "";

    // 如果不存在键为 key 的设置 返回的默认值
    String defaultValue() default "";
}
