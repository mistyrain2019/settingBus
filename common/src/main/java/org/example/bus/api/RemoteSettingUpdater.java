package org.example.bus.api;


/**
 * 用户可自行实现 RemoteSetting 的更新方式
 */
public interface RemoteSettingUpdater {

    void update(UpdateCallback updateCallback);

    // 以秒为单位的更新频率
    int getUpdateInterval();

    default boolean repeat() {
        return false;
    }
}
