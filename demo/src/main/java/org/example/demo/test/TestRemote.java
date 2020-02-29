package org.example.demo.test;

import org.example.bus.SettingBus;
import org.example.bus.api.impl.SimpleNetworkUpdater;
import org.example.bus.repository.RemoteSettingRepository;
import org.example.demo.setting.TestSetting;

public class TestRemote {

    public static final String GET_URL = "http://localhost:2012/settings";

    public static void testRemote() {

        System.out.println("\nRemoteBefore: \n");

        RemoteSettingRepository.getInstance().setUpdater(new SimpleNetworkUpdater(GET_URL, 2, true));

        System.out.println(SettingBus.obtainSetting(TestSetting.class).testInt());
        System.out.println(SettingBus.obtainSetting(TestSetting.class).testOther());
        System.out.println(SettingBus.obtainSetting(TestSetting.class).testLong());
        System.out.println(SettingBus.obtainSetting(TestSetting.class).testDouble());
        System.out.println(SettingBus.obtainSetting(TestSetting.class).testBoolean());
        try {
            Thread.sleep(4000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nRemoteAfter: \n");

        System.out.println(SettingBus.obtainSetting(TestSetting.class).testInt());
        System.out.println(SettingBus.obtainSetting(TestSetting.class).testOther());
        System.out.println(SettingBus.obtainSetting(TestSetting.class).testLong());
        System.out.println(SettingBus.obtainSetting(TestSetting.class).testDouble());
        System.out.println(SettingBus.obtainSetting(TestSetting.class).testBoolean());
        System.exit(0);
    }
}
