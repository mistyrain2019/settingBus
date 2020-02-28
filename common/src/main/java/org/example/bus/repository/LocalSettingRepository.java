package org.example.bus.repository;

import org.example.bus.api.LocalSettingStorage;
import org.example.bus.api.ReadStorageCallback;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalSettingRepository implements ILocalSettingRepository {

    private static volatile LocalSettingRepository remoteSettingRepository;

    public static LocalSettingRepository getInstance() {
        if (remoteSettingRepository == null) {
            synchronized (LocalSettingRepository.class) {
                if (remoteSettingRepository == null) {
                    remoteSettingRepository = new LocalSettingRepository();
                }
            }
        }
        return remoteSettingRepository;
    }

    private Map<String, String> settingKV = new ConcurrentHashMap<>();

    private LocalSettingReadCallbackImpl callback = new LocalSettingReadCallbackImpl();

    private LocalSettingStorage storage;

    @Override
    public String getVal(String key) {
        return settingKV.get(key);
    }

    @Override
    public String getOrDefault(String key, String defaultVal) {
        return settingKV.getOrDefault(key, defaultVal);
    }

    @Override
    public void set(String key, String val) {
        settingKV.put(key, val);
    }

    @Override
    public void setStorage(LocalSettingStorage storage) {
        this.storage = storage;
    }

    @Override
    public void save() {
        if (storage != null) {
            storage.save(settingKV);
        }
    }

    @Override
    public void readFromStorage() {
        if (storage != null) {
            storage.read(callback);
        }
    }

    private class LocalSettingReadCallbackImpl implements ReadStorageCallback {

        @Override
        public void onRead(Map<String, String> kvs) {
            settingKV.putAll(kvs);
        }
    }
}
