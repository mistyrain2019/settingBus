package org.example.demo.setting;

import org.example.bus.annotation.LocalSetting;
import org.example.bus.annotation.SettingGetter;

@LocalSetting
public interface TestLocalSetting {

    @SettingGetter(key = "test_local_int", defaultValue = "9", explanation = "测试int类型")
    int testLocalInt();
}
