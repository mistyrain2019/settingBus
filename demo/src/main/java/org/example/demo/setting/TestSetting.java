package org.example.demo.setting;

import org.example.bus.annotation.RemoteSetting;
import org.example.bus.annotation.SettingGetter;
import org.example.demo.bean.Point;
import org.example.demo.converter.PointConverter;

@RemoteSetting
public interface TestSetting {

    @SettingGetter(key = "test_int", defaultValue = "3", explanation = "测试int类型")
    int testInt();

    @SettingGetter(key = "")
    void testVoid();

    @SettingGetter(key = "test_other", converterClazz = {PointConverter.class})
    Point testOther();
}