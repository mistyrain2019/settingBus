package org.example.bus.repository;

import org.example.bus.api.LocalSettingStorage;

public interface ILocalSettingRepository {

    String getVal(String key);

    String getOrDefault(String key, String defaultVal);

    void set(String key, String val);

    void setStorage(LocalSettingStorage storage);

    void saveToStorage();

    void readFromStorage();
}
