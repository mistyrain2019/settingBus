package org.example.bus.api;

import java.util.Map;

public interface UpdateCallback {

    void onSuccess(Map<String, String> mp);

    void onFailure();
}
