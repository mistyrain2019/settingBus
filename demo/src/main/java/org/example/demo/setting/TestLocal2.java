package org.example.demo.setting;

import org.example.bus.annotation.LocalSetting;
import org.example.bus.annotation.SettingGetter;
import org.example.bus.annotation.SettingSetter;

@LocalSetting
public interface TestLocal2 {

    @SettingGetter(key = "ssss", defaultValue = "987654")
    int getSSSS();

    @SettingSetter(key = "ssss")
    void segSSSS(int s);
}
