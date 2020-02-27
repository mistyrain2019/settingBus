package org.example.bus;

public class SettingBus {

    public static <T> T obtainSetting(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
