package org.example.demo.test;

import org.example.bus.SettingBus;
import org.example.bus.api.RemoteSettingUpdater;
import org.example.bus.api.UpdateCallback;
import org.example.demo.setting.TestSetting;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class TestUpdater implements RemoteSettingUpdater {

    public static final String GET_URL = "http://localhost:2012/settings";

    @Override
    public void update(UpdateCallback updateCallback) {
        new Thread() {
            @Override
            public void run() {
                doUpdate(updateCallback);

            }
        }.start();
    }

    private void doUpdate(UpdateCallback updateCallback) {
        try {
            String res = sendRequestToGetSetting();
            Map<String, String> settingMap = new HashMap<>();
            JSONObject jo = new JSONObject(res);
            for (String key : jo.keySet()) {
                String val = jo.optString(key);
                if (!val.isEmpty()) {
                    settingMap.put(key, val);
                }
            }
            updateCallback.onSuccess(settingMap);
            System.out.println("更新后: " + SettingBus.obtainSetting(TestSetting.class).testInt());

        } catch (Exception e) {
        }
        updateCallback.onFailure();
    }

    private String sendRequestToGetSetting() {
        try {
            URL getUrl = new URL(GET_URL);
            // 根据拼凑的URL，打开连接，URL.openConnection函数会根据URL的类型，
            // 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
            HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
            // 进行连接，但是实际上get request要在下一句的connection.getInputStream()函数中才会真正发到服务器
            connection.connect();
            // 取得输入流，并使用Reader读取
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));// 设置编码,否则中文乱码
            System.out.println("=============================");
            System.out.println("Contents of get request");
            System.out.println("=============================");
            String lines;
            String res = "";
            while ((lines = reader.readLine()) != null) {
                res += lines;
                System.out.println(lines);
            }
            reader.close();
            // 断开连接
            connection.disconnect();
            System.out.println("=============================");
            System.out.println("Contents of get request ends");
            System.out.println("=============================");
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    @Override
    public int getUpdateInterval() {
        return 2;
    }

    @Override
    public boolean repeat() {
        return true;
    }
}
