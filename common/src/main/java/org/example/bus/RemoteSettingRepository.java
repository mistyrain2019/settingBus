package org.example.bus;


import java.util.HashMap;
import java.util.Map;

public class RemoteSettingRepository {

    private Map<String, String> settingKV = new HashMap<String, String>();

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
}
