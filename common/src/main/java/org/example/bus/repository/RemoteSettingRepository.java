package org.example.bus.repository;


import org.example.bus.api.OnUpdateObserver;
import org.example.bus.api.RemoteSettingUpdater;
import org.example.bus.api.UpdateCallback;
import org.example.bus.api.impl.DefaultUpdaterImpl;

import java.util.*;
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

    private Timer updateTimer = null;

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
        if (updateTimer != null) {
            updateTimer.cancel();
        }
        updateTimer = new Timer();
        TimerTask tTask = new TimerTask() {
            @Override
            public void run() {
                updater.update(updateCallback);
            }
        }; //在里面实现你的操作
        updateTimer.schedule(tTask, 0, Math.max(3000, updater.getUpdateInterval() * 1000));
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
