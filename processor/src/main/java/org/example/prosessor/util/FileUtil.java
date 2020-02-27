package org.example.prosessor.util;

import com.squareup.javapoet.JavaFile;
import static org.example.bus.Constants.DEBUG;

import java.io.File;
import java.io.IOException;

public class FileUtil {

    public static void write(JavaFile javaFile, String path) {

        //将java写到当前项目中
        try {
            if (DEBUG) {
                javaFile.writeTo(System.out);    //打印到命令行中
            }
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            javaFile.writeTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
