package org.example.bus.api.impl;

import org.example.bus.api.LocalSettingStorage;
import org.example.bus.common.FileUtil;

import java.util.Map;

public class FileStorage implements LocalSettingStorage {

    private String filePath;

    public FileStorage() {
        filePath = "./local_setting_storage.txt";
    }

    public FileStorage(String path) {
        filePath = path;
    }

    @Override
    public void save(Map<String, String> kvs) {
        FileUtil.ensureFileExists(filePath);
        FileUtil.writeToFile(kvs, filePath);
    }

    @Override
    public Map<String, String> read() {
        FileUtil.ensureFileExists(filePath);
        return FileUtil.readFromFile(filePath);
    }
}
