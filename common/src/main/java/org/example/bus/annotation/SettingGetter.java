package org.example.bus.annotation;

import org.example.bus.Converter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SettingGetter {

    String key();

    Class<? extends Converter>[] converterClazz() default {};

    String explanation() default "";

    String defaultValue() default "";
}
