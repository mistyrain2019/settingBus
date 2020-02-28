package org.example.bus.api;

public interface RemoteSettingUpdater {

    void update(UpdateCallback updateCallback);

    // 以秒为单位的更新频率
    int getUpdateFrequency();
}
