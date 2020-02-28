package org.example.bus.api;

public interface Converter<T> {

    T deserialization(String s);

    String serialization(T obj);

    T defaultObj();
}
