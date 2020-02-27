package org.example.bus;

public class SettingBus {

    public static <T> T obtainSetting(Class<T> clazz) {
        try {
            String interfaceName = clazz.getCanonicalName();
            System.out.println(interfaceName);
            Class<?> impl = Class.forName(interfaceName + "__Impl");
            return (T) impl.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
