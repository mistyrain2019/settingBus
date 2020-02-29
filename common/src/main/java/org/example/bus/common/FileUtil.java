package org.example.bus.common;


import java.io.*;

public class FileUtil {

    public static <T> T readFromFile(String path) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);
            return (T) ois.readObject();
        } catch (Exception ignored) {
        }
        return null;
    }

    public static <T> void writeToFile(T data, String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            oos.close();
        } catch (Exception ignored) {
        }
    }

    public static boolean ensureFileExists(String path) {
        File file = new File(path);
        boolean success = false;
        if (!file.exists()) {
            try {
                success = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            success = true;
        }
        return success;
    }

    public static boolean deleteFile(String path) {
        File file = new File(path);
        return file.exists() && file.delete();
    }
}
