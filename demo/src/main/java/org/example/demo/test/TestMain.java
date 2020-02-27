package org.example.demo.test;

import org.example.bus.SettingBus;
import org.example.demo.setting.TestSetting;

public class TestMain {

    public static void main(String[] args) {
        System.out.println(SettingBus.obtainSetting(TestSetting.class).testInt());

    }
}
