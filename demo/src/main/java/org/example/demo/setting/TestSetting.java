package org.example.demo.setting;

import org.example.bus.annotation.RemoteSetting;
import org.example.bus.annotation.SettingGetter;

@RemoteSetting
public interface TestSetting {

    @SettingGetter(key = "test", defaultValue = "3", explanation = "测试")
    void test();

//    int getMaxLogTime();
}
