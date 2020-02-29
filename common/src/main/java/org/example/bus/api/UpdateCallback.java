package org.example.bus.api;

import java.util.Map;

/**
 * 为支持异步读取
 * RemoteSetting 的更新回调
 */
public interface UpdateCallback {

    void onSuccess(Map<String, String> mp);

    void onFailure();
}
