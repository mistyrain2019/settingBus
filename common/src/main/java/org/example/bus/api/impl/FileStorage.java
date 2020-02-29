package org.example.bus.api.impl;

import org.example.bus.api.LocalSettingStorage;
import org.example.bus.api.ReadStorageCallback;
import org.example.bus.common.FileUtil;

import java.util.Map;
import java.util.concurrent.*;

public class FileStorage implements LocalSettingStorage {

    private String filePath;
    private ExecutorService fileIO = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
            60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>());

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
//        fileIO.shutdownNow();
    }

    @Override
    public void read(ReadStorageCallback readStorageCallback) {
        fileIO.execute(() -> {
            FileUtil.ensureFileExists(filePath);
            Map<String, String> mp = FileUtil.readFromFile(filePath);
            readStorageCallback.onRead(mp);
        });
    }
}
