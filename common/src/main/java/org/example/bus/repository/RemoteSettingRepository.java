package org.example.bus.repository;


import org.example.bus.api.OnUpdateObserver;
import org.example.bus.api.RemoteSettingUpdater;
import org.example.bus.api.UpdateCallback;
import org.example.bus.api.impl.DefaultUpdaterImpl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.example.bus.common.Constants.MIN_UPDATE_INTERVAL;


/**
 * RemoteSetting 即
 * 远端设置的中央仓库
 */
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
        if (updater.repeat()) {
            updateTimer = new Timer();
            TimerTask tTask = new TimerTask() {
                @Override
                public void run() {
                    updater.update(updateCallback);
                }
            };
            updateTimer.schedule(tTask, 0L,
                    Math.max(1000 * MIN_UPDATE_INTERVAL, updater.getUpdateInterval() * 1000));
        } else {
            updater.update(updateCallback);
        }
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
            System.out.println("更新失败 请检查 update 配置");
        }
    }
}
