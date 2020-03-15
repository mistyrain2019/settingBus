package org.example.demo.setting;

import org.example.bus.annotation.LocalSetting;
import org.example.bus.annotation.SettingGetter;
import org.example.bus.annotation.SettingSetter;
import org.example.demo.bean.Point;
import org.example.demo.converter.PointConverter;


/**
 * 本地设置 demo
 * processor 会根据注解创建实现
 */
@LocalSetting
public interface TestLocalSetting {

    @SettingSetter(key = "test_local_int")
    void setLocalTestInt(int tttt);

    @SettingSetter(key = "test_local_double")
    void setLocalTestDouble(double dddd);

    @SettingSetter(key = "test_local_long")
    void setLocalTestLong(long longVal);

    @SettingSetter(key = "test_local_boolean")
    void setLocalTestBoolean(boolean bbbb);

    @SettingSetter(key = "test_local_str")
    void setTestLocalStr(String s);

    @SettingSetter(key = "test_local_point", converterClazz = PointConverter.class)
    void setPointLocal(Point point);

    @SettingGetter(key = "test_local_int", defaultValue = "9", explanation = "测试int类型")
    int getTestLocalInt();

    @SettingGetter(key = "test_local_double", defaultValue = "0.01010101")
    double getLocalTestDouble();

    @SettingGetter(key = "test_local_long", defaultValue = "-14")
    long getLocalTestLong();

    @SettingGetter(key = "test_local_boolean", defaultValue = "false")
    boolean getLocalTestBoolean();

    @SettingGetter(key = "test_local_str", defaultValue = "defaultStr")
    String getTestLocalStr();

    @SettingGetter(key = "test_local_point", converterClazz = PointConverter.class)
    Point getPointLocal();

    @SettingGetter(key = "test_local_int2", defaultValue = "22")
    int getInt2Val();

    @SettingGetter(key = "test_local_int3", defaultValue = "33")
    int getInt3Val();
}
