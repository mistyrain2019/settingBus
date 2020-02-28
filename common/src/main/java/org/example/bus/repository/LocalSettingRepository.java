package org.example.bus.repository;

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

}
