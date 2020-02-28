package org.example.bus.api.impl;

import org.example.bus.api.RemoteSettingUpdater;
import org.example.bus.api.UpdateCallback;

public class DefaultUpdaterImpl implements RemoteSettingUpdater {
    @Override
    public void update(UpdateCallback updateCallback) {
    }

    @Override
    public int getUpdateFrequency() {
        return Integer.MAX_VALUE;
    }
}
