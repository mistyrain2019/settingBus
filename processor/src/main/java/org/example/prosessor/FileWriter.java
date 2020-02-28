package org.example.prosessor;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Filer;
import java.io.IOException;

import static org.example.bus.common.Constants.DEBUG;

public class FileWriter {

    public static void write(String packageName, TypeSpec typeSpec, Filer filer) {
        //生成一个Java文件
        JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                .addFileComment("Generated code should not be modified! ")
                .build();

        //将java写到当前项目中
        try {
            if (DEBUG) {
                javaFile.writeTo(System.out);    //打印到命令行中
            }
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
