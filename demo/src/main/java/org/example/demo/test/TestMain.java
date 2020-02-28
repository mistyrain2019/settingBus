package org.example.demo.test;

import org.example.bus.SettingBus;
import org.example.bus.repository.RemoteSettingRepository;
import org.example.demo.setting.TestSetting;

public class TestMain {

    public static void main(String[] args) {
        RemoteSettingRepository.getInstance().setUpdater(new TestUpdater());


        System.out.println(SettingBus.obtainSetting(TestSetting.class).testInt());
        System.out.println(SettingBus.obtainSetting(TestSetting.class).testOther());
        System.out.println(SettingBus.obtainSetting(TestSetting.class).testLong());
        System.out.println(SettingBus.obtainSetting(TestSetting.class).testDouble());
        System.out.println(SettingBus.obtainSetting(TestSetting.class).testBoolean());
        while (true) {

        }
    }
}
