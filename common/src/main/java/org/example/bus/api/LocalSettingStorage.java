package org.example.bus.api;

import java.util.Map;

/**
 * 用户可自行实现 LocalSetting的 本地读取
 */
public interface LocalSettingStorage {

    void save(Map<String, String> kvs);

    void read(ReadStorageCallback readStorageCallback);
}
