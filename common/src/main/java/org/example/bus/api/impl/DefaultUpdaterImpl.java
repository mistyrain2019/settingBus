package org.example.bus.api.impl;

import org.example.bus.api.RemoteSettingUpdater;

import java.util.HashMap;
import java.util.Map;

public class DefaultUpdaterImpl implements RemoteSettingUpdater {
    @Override
    public Map<String, String> update() {
        return new HashMap<>();
    }

    @Override
    public int getUpdateFrequency() {
        return Integer.MAX_VALUE;
    }
}
