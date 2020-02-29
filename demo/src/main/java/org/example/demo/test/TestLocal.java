package org.example.demo.test;

import org.example.bus.SettingBus;
import org.example.bus.api.impl.FileStorage;
import org.example.bus.repository.LocalSettingRepository;
import org.example.demo.bean.Point;
import org.example.demo.setting.TestLocalSetting;

public class TestLocal {

    public static void testLocal() {

        LocalSettingRepository.getInstance().setStorage(new FileStorage());

        System.out.println("\nLocalBefore:\n");

        LocalSettingRepository.getInstance().readFromStorage();

        try {
            Thread.sleep(500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(SettingBus.obtainSetting(TestLocalSetting.class).getTestLocalInt());
        System.out.println(SettingBus.obtainSetting(TestLocalSetting.class).getLocalTestDouble());
        System.out.println(SettingBus.obtainSetting(TestLocalSetting.class).getLocalTestLong());
        System.out.println(SettingBus.obtainSetting(TestLocalSetting.class).getTestLocalStr());
        System.out.println(SettingBus.obtainSetting(TestLocalSetting.class).getLocalTestBoolean());
        System.out.println(SettingBus.obtainSetting(TestLocalSetting.class).getPointLocal());

        SettingBus.obtainSetting(TestLocalSetting.class).setLocalTestInt(11);
        SettingBus.obtainSetting(TestLocalSetting.class).setLocalTestDouble(945.54);
        SettingBus.obtainSetting(TestLocalSetting.class).setLocalTestLong(4567);
        SettingBus.obtainSetting(TestLocalSetting.class).setTestLocalStr("updated");
        SettingBus.obtainSetting(TestLocalSetting.class).setLocalTestBoolean(true);
        SettingBus.obtainSetting(TestLocalSetting.class).setPointLocal(new Point(9, 8));

        System.out.println("\nLocalAfter:\n");

        System.out.println(SettingBus.obtainSetting(TestLocalSetting.class).getTestLocalInt());
        System.out.println(SettingBus.obtainSetting(TestLocalSetting.class).getLocalTestDouble());
        System.out.println(SettingBus.obtainSetting(TestLocalSetting.class).getLocalTestLong());
        System.out.println(SettingBus.obtainSetting(TestLocalSetting.class).getTestLocalStr());
        System.out.println(SettingBus.obtainSetting(TestLocalSetting.class).getLocalTestBoolean());
        System.out.println(SettingBus.obtainSetting(TestLocalSetting.class).getPointLocal());

        LocalSettingRepository.getInstance().saveToStorage();

        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LocalSettingRepository.getInstance().readFromStorage();
    }
}
