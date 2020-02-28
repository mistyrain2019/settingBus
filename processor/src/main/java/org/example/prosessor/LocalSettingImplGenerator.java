package org.example.prosessor;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.example.bus.annotation.SettingGetter;
import org.example.bus.annotation.SettingSetter;
import org.example.bus.repository.LocalSettingRepository;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.bus.common.Constants.IMPL_SUFFIX;

public class LocalSettingImplGenerator {

    private Filer filer;
    private Elements elementUtil;
    private Types typeUtil;

    public LocalSettingImplGenerator(Filer filer, Elements elementUtil, Types typeUtil) {
        this.filer = filer;
        this.elementUtil = elementUtil;
        this.typeUtil = typeUtil;
    }

    public void generateLocalSettingImpl(Iterable<? extends Element> annotatedElements) {

        for (Element element : annotatedElements) {
            if (!(element instanceof TypeElement)) {
                continue;
            }
            TypeElement typeElement = (TypeElement) element;

            // 得到接口信息
            InterfaceInfo info = new InterfaceInfo(typeElement, elementUtil);

            // 得到要处理的 setter 方法
            List<? extends ExecutableElement> setterMethods = info.executableElements
                    .stream()
                    .filter(executableElement -> executableElement.getAnnotation(SettingSetter.class) != null)
                    .collect(Collectors.toList());

            // 得到要处理的 getter 方法
            List<? extends ExecutableElement> getterMethods = info.executableElements
                    .stream()
                    .filter(executableElement -> executableElement.getAnnotation(SettingGetter.class) != null)
                    .collect(Collectors.toList());

            if (setterMethods.size() + getterMethods.size() < info.executableElements.size()) {
                throw new RuntimeException("抽象方法配置残缺 无法解析");
            }

            // 生成 converter 成员变量
            Iterable<FieldSpec> converterFields = ConverterFieldsGenerator.generateConverterFields(info.executableElements);

            // 生成 getter 方法
            Iterable<MethodSpec> generatedGetterMethods = GetterMethodGenerator.generateGetterMethods(getterMethods);

            // 生成构造方法
            MethodSpec constructor = generateConstructor();

            // 构造一个实现类
            TypeSpec impl = TypeSpec.classBuilder(info.interfaceName + IMPL_SUFFIX)
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(typeElement.asType())
                    .addFields(converterFields)
                    .addField(LocalSettingRepository.class, "centreRepository", Modifier.PRIVATE, Modifier.FINAL)
                    .addMethod(constructor)
                    .addMethods(generatedGetterMethods)
                    .build();

            // 写入文件
            FileWriter.write(info.packageName, impl, filer);
        }
    }

    private MethodSpec generateConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.centreRepository = $T.getInstance()", LocalSettingRepository.class)
                .build();
    }
}
