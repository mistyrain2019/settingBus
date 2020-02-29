package org.example.bus.api;

import org.example.bus.repository.RemoteSettingRepository;

/**
 * Remote Setting 更新后的观察者
 * 用户和自行实现并添加给
 *
 * @see RemoteSettingRepository
 */
public interface OnUpdateObserver {

    void onUpdate();
}
