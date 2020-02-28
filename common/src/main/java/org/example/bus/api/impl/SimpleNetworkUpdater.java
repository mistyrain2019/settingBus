package org.example.bus.api.impl;

import org.example.bus.api.RemoteSettingUpdater;
import org.example.bus.api.UpdateCallback;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 可进行简单 get 请求的基本 updater
 */
public class SimpleNetworkUpdater implements RemoteSettingUpdater {

    private String url;
    private int updateInterval;
    private boolean repeat;
    private Executor executor = Executors.newSingleThreadExecutor();

    public SimpleNetworkUpdater(String url, int updateInterval, boolean repeat) {
        this.repeat = repeat;
        this.url = url;
        this.updateInterval = updateInterval;
    }

    @Override
    public void update(UpdateCallback updateCallback) {
        executor.execute(() -> doUpdate(updateCallback));
    }

    @Override
    public int getUpdateInterval() {
        return updateInterval;
    }

    @Override
    public boolean repeat() {
        return repeat;
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
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateCallback.onFailure();
    }


    private String sendRequestToGetSetting() {
        try {
            URL getUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));// 设置编码,否则中文乱码
            String lines;
            String res = "";
            while ((lines = reader.readLine()) != null) {
                res += lines;
            }
            reader.close();
            connection.disconnect();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
