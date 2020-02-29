package org.example.bus.api;

import java.util.Map;

/**
 * 为支持异步读取
 * LocalSetting 的持久化回调
 */
public interface ReadStorageCallback {

    void onRead(Map<String, String> kvs);
}
