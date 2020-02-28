package org.example.demo.converter;

import org.example.bus.api.Converter;
import org.example.demo.bean.Point;
import org.json.JSONObject;

public class PointConverter implements Converter<Point> {
    @Override
    public Point deserialization(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            int x = jsonObject.optInt("x");
            int y = jsonObject.optInt("y");
            return new Point(x, y);
        } catch (Exception e) {
        }
        return defaultObj();
    }

    @Override
    public String serialization(Point point) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("x", point.x);
            jsonObject.put("y", point.y);
            return jsonObject.toString();
        } catch (Exception e) {
        }
        return "";
    }

    @Override
    public Point defaultObj() {
        return new Point();
    }
}
