package org.example.bus.api;

import java.util.Map;

public interface RemoteSettingUpdater {

    Map<String, String> update();

    // 以秒为单位的更新频率
    int getUpdateFrequency();
}
