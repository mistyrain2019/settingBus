package org.example.prosessor;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.example.bus.annotation.SettingGetter;
import org.example.prosessor.util.FileUtil;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.example.bus.Constants.DEBUG;
import static org.example.bus.Constants.IMPL_SUFFIX;

public class RemoteSettingImplGenerator {

    void generateRemote(Set<? extends Element> annotatedElements, Filer filer) {

        for (Element element : annotatedElements) {
            if (!(element instanceof TypeElement)) {
                continue;
            }

            TypeElement typeElement = (TypeElement) element;
            InterfaceInfo info = new InterfaceInfo(typeElement);


            //获取源代码对象的成员
            List<? extends Element> enclosedElements = typeElement.getEnclosedElements();
            //留下方法成员,过滤掉其他成员
            List<? extends ExecutableElement> executableElements = ElementFilter.methodsIn(enclosedElements);

            List<MethodSpec> generatedMethods = generatedMethods(executableElements);

            // 构造一个实现类
            TypeSpec hello = TypeSpec.classBuilder(info.interfaceName + IMPL_SUFFIX)         //名称
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(typeElement.asType())
                    .addMethods(generatedMethods)
                    .build();

            //生成一个Java文件
            JavaFile javaFile = JavaFile.builder(info.packageName, hello)
                    .build();

//            FileUtil.write(javaFile, "./demo/target/generated/");

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

    private List<MethodSpec> generatedMethods(List<? extends ExecutableElement> ees) {
        List<MethodSpec> generatedMethods = new ArrayList<>();

        for (ExecutableElement executableElement : ees) {

            SettingGetter settingGetter = executableElement.getAnnotation(SettingGetter.class);

            if (settingGetter == null) {
                throw new RuntimeException("抽象方法配置残缺 无法解析");
            }

//            System.out.println("returns:  " + executableElement.getReturnType().getClass());
            System.out.println("returns:  " + executableElement.getReturnType());

            String returnType = executableElement.getReturnType().toString();
            MethodSpec method = null;

            switch (returnType) {
                case "int":
                    method = getIntMethod(executableElement, settingGetter);
                    break;
                case "void":
                    method = getVoidMethod(executableElement);
                    break;
                default:
                    method = getDefaultMethod(executableElement);

            }

            generatedMethods.add(method);
        }
        return generatedMethods;
    }

    private MethodSpec getVoidMethod(ExecutableElement executableElement) {
        MethodSpec methodSpec = MethodSpec.methodBuilder(executableElement.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(void.class)
                .build();
        return methodSpec;
    }

    private MethodSpec getDefaultMethod(ExecutableElement executableElement) {
        TypeMirror tm = executableElement.getReturnType();
        MethodSpec methodSpec = MethodSpec.methodBuilder(executableElement.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
//                .returns())
//                .addStatement("return null;")
                .build();
        return methodSpec;
    }

    private MethodSpec getIntMethod(ExecutableElement executableElement, SettingGetter settingGetter) {
        String key = settingGetter.key();
        int defaultIntVal = 0;
        try {
            defaultIntVal = Integer.parseInt(settingGetter.defaultValue());
        } catch (Exception ignored) {
        }
        MethodSpec methodSpec = MethodSpec.methodBuilder(executableElement.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addStatement("return $L", defaultIntVal)
                .returns(int.class)
                .build();
        return methodSpec;
    }


}
