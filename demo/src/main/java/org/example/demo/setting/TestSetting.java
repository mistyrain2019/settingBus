package org.example.demo.setting;

import org.example.bus.annotation.RemoteSetting;
import org.example.bus.annotation.SettingGetter;

@RemoteSetting
public interface TestSetting {

    @SettingGetter
    void test();

//    int getMaxLogTime();
}
