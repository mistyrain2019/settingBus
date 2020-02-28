package org.example.bus.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalSettingRepository {

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

    public String getVal(String key) {
        return settingKV.get(key);
    }

    public String getOrDefault(String key, String defaultVal) {
        return settingKV.getOrDefault(key, defaultVal);
    }

    public String set(String key, String val) {
        return settingKV.put(key, val);
    }

    private Map<String, String> settingKV = new ConcurrentHashMap<>();
}
