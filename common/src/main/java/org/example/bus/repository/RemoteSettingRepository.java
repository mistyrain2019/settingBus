package org.example.bus.repository;


import org.example.bus.api.OnUpdateObserver;
import org.example.bus.api.RemoteSettingUpdater;
import org.example.bus.api.UpdateCallback;
import org.example.bus.api.impl.DefaultUpdaterImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RemoteSettingRepository implements IRemoteSettingRepository {

    private static volatile RemoteSettingRepository remoteSettingRepository;

    public static RemoteSettingRepository getInstance() {
        if (remoteSettingRepository == null) {
            synchronized (RemoteSettingRepository.class) {
                if (remoteSettingRepository == null) {
                    remoteSettingRepository = new RemoteSettingRepository();
                }
            }
        }
        return remoteSettingRepository;
    }

    private Map<String, String> settingKV = new ConcurrentHashMap<>();

    private volatile List<OnUpdateObserver> onUpdateObservers = new ArrayList<>();

    private final Object OBSERVER_LOCK = new Object();

    private volatile RemoteSettingUpdater updater = new DefaultUpdaterImpl();

    private UpdateCallback updateCallback = new RemoteSettingUpdateCallBack();

    @Override
    public String getVal(String key) {
        return settingKV.get(key);
    }

    @Override
    public String getOrDefault(String key, String defaultVal) {
        return settingKV.getOrDefault(key, defaultVal);
    }

    @Override
    public void setUpdater(RemoteSettingUpdater newUpdater) {
        this.updater = newUpdater;
    }

    @Override
    public void update() {
        updater.update(updateCallback);
    }

    @Override
    public void registerOnUpdateObserver(OnUpdateObserver observer) {
        synchronized (OBSERVER_LOCK) {
            onUpdateObservers.add(observer);
        }
    }

    @Override
    public void unRegisterOnUpdateObserver(OnUpdateObserver observer) {
        synchronized (OBSERVER_LOCK) {
            onUpdateObservers.removeIf(visiting -> visiting == observer);
        }
    }

    private class RemoteSettingUpdateCallBack implements UpdateCallback {

        @Override
        public void onSuccess(Map<String, String> newSetting) {
            settingKV.putAll(newSetting);
            for (OnUpdateObserver observer : onUpdateObservers) {
                observer.onUpdate();
            }
        }

        @Override
        public void onFailure() {
        }
    }
}
