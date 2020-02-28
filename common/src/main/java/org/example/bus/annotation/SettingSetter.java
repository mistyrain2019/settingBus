package org.example.bus.annotation;

import org.example.bus.api.Converter;

/**
 * 仅支持 int long boolean double String
 * 和 自定义类 如果是自定义类 需要 实现一个
 * @see Converter 接口 并把实现的Converter类的class 传给 converterClazz
 */
public interface SettingSetter {
}
