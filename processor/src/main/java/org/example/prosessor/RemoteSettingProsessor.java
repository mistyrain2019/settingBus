package org.example.prosessor;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.example.bus.annotation.RemoteSetting;
import org.example.bus.annotation.SettingGetter;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



public class RemoteSettingProsessor extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        if (SourceVersion.latest().compareTo(SourceVersion.RELEASE_8) > 0) {
            return SourceVersion.latest();
        } else {
            return SourceVersion.RELEASE_8;
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> strSet = new HashSet<>();
        strSet.add(RemoteSetting.class.getCanonicalName());
        return strSet;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(RemoteSetting.class);
        List<Element> toDealWith = new ArrayList<>();
//        System.out.println("sssssssss");
        write(annotatedElements);
        return true;
    }

    private void write(Set<? extends Element> annotatedElements) {

        for (Element element : annotatedElements) {
            if (!(element instanceof TypeElement)) {
                continue;
            }

//            System.out.println("ttt:" + ((TypeElement) element).getQualifiedName());

            TypeElement typeElement = (TypeElement) element;

            typeElement.getAnnotationMirrors();

            String interfaceName = element.getSimpleName().toString();

            String fullName = typeElement.getQualifiedName().toString();

            int packageLastIndex = fullName.lastIndexOf(interfaceName) - 1;

            String packageName = fullName.substring(0, packageLastIndex);

            //获取源代码对象的成员
            List<? extends Element> enclosedElems = typeElement.getEnclosedElements();
            //留下方法成员,过滤掉其他成员
            List<? extends ExecutableElement> ees = ElementFilter.methodsIn(enclosedElems);

            List<MethodSpec> generatedMethods = new ArrayList<>();

            for (ExecutableElement executableElement: ees) {

                SettingGetter settingAnnotation = executableElement.getAnnotation(SettingGetter.class);

                if (settingAnnotation == null) {
                    throw new RuntimeException("抽象方法配置残缺 无法解析");
                }

                System.out.println("returns:  " + executableElement.getReturnType().getClass());
//                System.out.println("returns:  " + executableElement.getReturnType());

                MethodSpec methodSpec = MethodSpec.methodBuilder(executableElement.getSimpleName().toString())
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
//                        .returns(executableElement.getReturnType())
                        .returns(TypeName.VOID)
                        .build();
                generatedMethods.add(methodSpec);
            }


            // 构造一个类
            TypeSpec hello = TypeSpec.classBuilder(interfaceName + "__Impl")         //名称
                    .addModifiers(Modifier.PUBLIC)                         //修饰
                    .addSuperinterface(typeElement.asType())
                    .addMethods(generatedMethods)
//                    .addMethod(main)                                        //方法
                    .build();

            //生成一个Java文件
            JavaFile javaFile = JavaFile.builder(packageName, hello)
                    .build();

            //将java写到当前项目中
            try {
                javaFile.writeTo(System.out);    //打印到命令行中
                File file = new File("./demo/target/generated/");
                if (file.exists()) {
                    file.delete();
                }
                javaFile.writeTo(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
