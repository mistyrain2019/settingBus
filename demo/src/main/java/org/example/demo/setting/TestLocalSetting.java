package org.example.demo.setting;

import org.example.bus.annotation.LocalSetting;
import org.example.bus.annotation.SettingGetter;
import org.example.bus.annotation.SettingSetter;
import org.example.demo.bean.Point;
import org.example.demo.converter.PointConverter;

@LocalSetting
public interface TestLocalSetting {

    @SettingGetter(key = "test_local_int", defaultValue = "9", explanation = "测试int类型")
    int testLocalInt();

    @SettingSetter(key = "test_local_int")
    void setLocalTestInt(int tttt);

//    @SettingSetter(key = "test_local_int")
//    void setTestLocalInt();

//    @SettingSetter(key = "test_local_point", converterClazz = PointConverter.class)
//    void setPointLocal(Point point);

}
