package org.example.bus;

import java.util.HashMap;
import java.util.Map;

import static org.example.bus.Constants.IMPL_SUFFIX;

public class SettingBus {

    private static final Map<String, Object> SETTING_CLASS_CACHE = new HashMap<String, Object>();

    public static <T> T obtainSetting(Class<T> clazz) {
        String clazzName = clazz.toString();
        if (!SETTING_CLASS_CACHE.containsKey(clazzName)) {
            SETTING_CLASS_CACHE.put(clazzName, findSettingsImpl(clazz));
        }
        return (T) SETTING_CLASS_CACHE.get(clazz.toString());
    }

    private static <T> T findSettingsImpl(Class<T> clazz) {
        try {
            String interfaceName = clazz.getCanonicalName();
            System.out.println(interfaceName);
            Class<?> impl = Class.forName(interfaceName + IMPL_SUFFIX);
            return (T) impl.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
