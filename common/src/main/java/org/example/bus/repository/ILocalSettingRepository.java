package org.example.bus.repository;

public interface ILocalSettingRepository {
    String getVal(String key);

    String getOrDefault(String key, String defaultVal);

    void set(String key, String val);
}
