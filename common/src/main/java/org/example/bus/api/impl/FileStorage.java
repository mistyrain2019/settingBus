package org.example.bus.api.impl;

import org.example.bus.api.LocalSettingStorage;
import org.example.bus.api.ReadStorageCallback;
import org.example.bus.common.FileUtil;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FileStorage implements LocalSettingStorage {

    private String filePath;
    private Executor fileIO = Executors.newSingleThreadExecutor();

    public FileStorage() {
        filePath = "./local_setting_storage.txt";
    }

    public FileStorage(String path) {
        filePath = path;
    }

    @Override
    public void save(Map<String, String> kvs) {
        fileIO.execute(() -> {
            FileUtil.ensureFileExists(filePath);
            FileUtil.writeToFile(kvs, filePath);
        });
    }

    @Override
    public void read(ReadStorageCallback readStorageCallback) {
        fileIO.execute(() -> {
            FileUtil.ensureFileExists(filePath);
            Map<String, String> mp = FileUtil.readFromFile(filePath);
            readStorageCallback.onRead(readStorageCallback);
        });
    }
}
