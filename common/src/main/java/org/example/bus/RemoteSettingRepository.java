package org.example.bus;


import org.example.bus.api.OnUpdateObserver;
import org.example.bus.api.RemoteSettingUpdater;
import org.example.bus.api.impl.DefaultUpdaterImpl;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class RemoteSettingRepository {

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

    private RemoteSettingUpdater updater = new DefaultUpdaterImpl();

    private List<OnUpdateObserver> onUpdateObservers = new Vector<>();

    public String getVal(String key) {
        return settingKV.get(key);
    }

    public String getOrDefault(String key, String defaultVal) {
        return settingKV.getOrDefault(key, defaultVal);
    }

    public void setUpdater(RemoteSettingUpdater updater) {
        this.updater = updater;
    }

    private void update() {
        Map<String, String> allSetting = updater.update();
        settingKV.putAll(allSetting);
        for (OnUpdateObserver observer: onUpdateObservers) {
            observer.onUpdate();
        }
    }
}
