package org.example.demo.setting;

import org.example.bus.annotation.RemoteSetting;
import org.example.bus.annotation.SettingGetter;
import org.example.demo.bean.Point;
import org.example.demo.converter.PointConverter;


/**
 * 远端设置 demo
 * processor 会根据注解创建实现
 */
@RemoteSetting
public interface TestSetting {

    @SettingGetter(key = "test_int", defaultValue = "3", explanation = "测试int类型")
    int testInt();

    @SettingGetter(key = "")
    void testVoid();

    @SettingGetter(key = "test_other", converterClazz = {PointConverter.class})
    Point testOther();

    @SettingGetter(key = "test_other1", converterClazz = {PointConverter.class})
    Point testOther1();

    @SettingGetter(key = "test_long", defaultValue = "500000000000000000")
    long testLong();

    @SettingGetter(key = "test_double", defaultValue = "0.878")
    double testDouble();

    @SettingGetter(key = "test_boolean", defaultValue = "true")
    boolean testBoolean();

    @SettingGetter(key = "test_str", defaultValue = "ttttstr")
    String testString();
}