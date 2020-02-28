package org.example.bus;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RemoteSettingRepository {

    private Map<String, String> settingKV = new ConcurrentHashMap<>();

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

    public String getVal(String key) {
        return settingKV.get(key);
    }

    public String getOrDefault(String key, String defaultVal) {
        return settingKV.getOrDefault(key, defaultVal);
    }
}
