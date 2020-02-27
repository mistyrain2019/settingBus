package org.example.bus.annotation;

public interface Convertible<T> {

    T deserialization(String s);

    String serialization(T obj);

    T defaultObj();
}
