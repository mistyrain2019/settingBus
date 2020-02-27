package org.example.bus;

import static org.example.bus.Constants.IMPL_SUFFIX;

public class SettingBus {

    public static <T> T obtainSetting(Class<T> clazz) {
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
