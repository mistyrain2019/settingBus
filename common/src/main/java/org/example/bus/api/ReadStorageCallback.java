package org.example.bus.api;

import java.util.Map;

public interface ReadStorageCallback {

    void onRead(Map<String, String> kvs);
}
