package org.example.bus.api;

/**
 * 实现实体类和 String 之间的映射
 */
public interface Converter<T> {

    T deserialization(String s);

    String serialization(T obj);

    T defaultObj();
}
