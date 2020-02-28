package org.example.bus.api;

import java.util.Map;

public interface LocalSettingStorage {

    void save(Map<String, String> kvs);

    Map<String, String> read();
}
