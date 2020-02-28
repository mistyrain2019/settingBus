package org.example.bus.repository;

import org.example.bus.api.OnUpdateObserver;
import org.example.bus.api.RemoteSettingUpdater;

public interface IRemoteSettingRepository {

    String getVal(String key);

    String getOrDefault(String key, String defaultVal);

    void setUpdater(RemoteSettingUpdater newUpdater);

    void update();

    void registerOnUpdateObserver(OnUpdateObserver observer);

    void unRegisterOnUpdateObserver(OnUpdateObserver observer);
}
