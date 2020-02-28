package org.example.bus.annotation;

import org.example.bus.api.Converter;

/**
 * 仅支持 int long boolean double String
 * 和 自定义类 如果是自定义类 需要 实现一个
 *
 * @see Converter 接口
 * 并把实现的Converter类的class 传给 converterClazz
 */
public @interface SettingSetter {

    // 为了拿到这个设置需要的键
    String key();

    // 自定义类 的 converter
    Class<? extends Converter>[] converterClazz() default {};

    // 关于这个设置的注释
    String explanation() default "";

    // 如果不存在键为 key 的设置 返回的默认值
    String defaultValue() default "";
}
