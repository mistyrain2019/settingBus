package org.example.bus.api.impl;

import org.example.bus.api.LocalSettingStorage;
import org.example.bus.common.FileUtil;

import java.util.Map;

public class FileStorage implements LocalSettingStorage {

    private static final String FILE_PATH = "./";

    @Override
    public void save(Map<String, String> kvs) {
        FileUtil.ensureFileExists(FILE_PATH);
        FileUtil.writeToFile(kvs, FILE_PATH);
    }

    @Override
    public Map<String, String> read() {
        FileUtil.ensureFileExists(FILE_PATH);
        return FileUtil.readFromFile(FILE_PATH);
    }
}
