package org.example.prosessor;

import com.squareup.javapoet.*;
import org.example.bus.repository.RemoteSettingRepository;
import org.example.bus.annotation.SettingGetter;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.example.bus.common.Constants.*;

public class RemoteSettingImplGenerator {

    private Filer filer;
    private Elements elementUtil;
    private Types typeUtil;

    public RemoteSettingImplGenerator(Filer filer, Elements elementUtil, Types typeUtil) {
        this.filer = filer;
        this.elementUtil = elementUtil;
        this.typeUtil = typeUtil;
    }

    void generateRemote(Iterable<? extends Element> annotatedElements) {

        for (Element element : annotatedElements) {
            if (!(element instanceof TypeElement)) {
                continue;
            }

            TypeElement typeElement = (TypeElement) element;
            InterfaceInfo info = new InterfaceInfo(typeElement, elementUtil);

            Iterable<FieldSpec> converterFields = ConverterFieldsGenerator.generateConverterFields(info.executableElements);

            List<MethodSpec> generatedMethods = generatedMethods(info.executableElements);

            MethodSpec constructor = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("this.centreRepository = $T.getInstance()", RemoteSettingRepository.class)
                    .build();

            // 构造一个实现类
            TypeSpec impl = TypeSpec.classBuilder(info.interfaceName + IMPL_SUFFIX)
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(typeElement.asType())
                    .addFields(converterFields)
                    .addField(RemoteSettingRepository.class, "centreRepository", Modifier.PRIVATE, Modifier.FINAL)
                    .addMethod(constructor)
                    .addMethods(generatedMethods)
                    .build();

            //生成一个Java文件
            JavaFile javaFile = JavaFile.builder(info.packageName, impl)
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

    private List<MethodSpec> generatedMethods(List<? extends ExecutableElement> ees) {
        List<MethodSpec> generatedMethods = new ArrayList<>();

        for (ExecutableElement executableElement : ees) {

            SettingGetter settingGetter = executableElement.getAnnotation(SettingGetter.class);

            if (settingGetter == null) {
                throw new RuntimeException("抽象方法配置残缺 无法解析");
            }

            TypeKind returnTypeKind = executableElement.getReturnType().getKind();
            MethodSpec method = null;

            switch (returnTypeKind) {
                case INT:
                    method = getIntMethod(executableElement, settingGetter);
                    break;
                case LONG:
                    method = getLongMethod(executableElement, settingGetter);
                    break;
                case DOUBLE:
                    method = getDoubleMethod(executableElement, settingGetter);
                    break;
                case BOOLEAN:
                    method = getBooleanMethod(executableElement, settingGetter);
                    break;
                case DECLARED:
                    method = getDeclaredMethod(executableElement, settingGetter);
                    break;
                case VOID:
                    method = getVoidMethod(executableElement);
                    break;
                default:
            }

            generatedMethods.add(method);
        }
        return generatedMethods;
    }

    private MethodSpec getVoidMethod(ExecutableElement executableElement) {
        return MethodSpec.methodBuilder(executableElement.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(void.class)
                .build();
    }

    private MethodSpec getDeclaredMethod(ExecutableElement executableElement, SettingGetter settingGetter) {
        TypeMirror tm = executableElement.getReturnType();
        if (STRING_QUALIFIED_NAME.equals(tm.toString())) {
            return getStringMethod(executableElement, settingGetter);
        }
        List<? extends TypeMirror> tp = null;
        try {
            settingGetter.converterClazz();
        } catch (MirroredTypesException mte) {
            tp = mte.getTypeMirrors();
        }
        String key = settingGetter.key();
        String defaultStr = settingGetter.defaultValue();
        if (tp == null || tp.size() < 1) {
            return MethodSpec.methodBuilder(executableElement.getSimpleName().toString())
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .returns(TypeName.get(tm))
                    .addStatement("return null")
                    .build();
        }
        String fieldName = ConverterFieldsGenerator.getGeneratedFieldName(tp.get(0));
        return MethodSpec.methodBuilder(executableElement.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .beginControlFlow("try")
                .addStatement("String val = centreRepository.getOrDefault($S, $S)", key, defaultStr)
                .addStatement("return $L.deserialization(val)", fieldName)
                .endControlFlow("catch (Exception ignored) {}")
                .addStatement("return $L.defaultObj()", fieldName)
                .returns(TypeName.get(tm))
                .build();
    }

    private MethodSpec getStringMethod(ExecutableElement executableElement, SettingGetter settingGetter) {
        String key = settingGetter.key();
        String defaultStr = settingGetter.defaultValue();
        return MethodSpec.methodBuilder(executableElement.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addStatement("return centreRepository.getOrDefault($S, $S)", key, defaultStr)
                .returns(String.class)
                .build();
    }

    private MethodSpec getIntMethod(ExecutableElement executableElement, SettingGetter settingGetter) {
        String key = settingGetter.key();
        String defaultIntVal = settingGetter.defaultValue();
        return MethodSpec.methodBuilder(executableElement.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addStatement("String val = centreRepository.getOrDefault($S, $S)", key, defaultIntVal)
                .addStatement("int intVal = 0")
                .beginControlFlow("try")
                .addStatement("intVal = $T.parseInt(val)", Integer.class)
                .endControlFlow("catch (Exception ignored) {}")
                .addStatement("return intVal")
                .returns(int.class)
                .build();
    }

    private MethodSpec getLongMethod(ExecutableElement executableElement, SettingGetter settingGetter) {
        String key = settingGetter.key();
        String defaultLongVal = settingGetter.defaultValue();
        return MethodSpec.methodBuilder(executableElement.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addStatement("String val = centreRepository.getOrDefault($S, $S)", key, defaultLongVal)
                .addStatement("long longVal = 0L")
                .beginControlFlow("try")
                .addStatement("longVal = $T.parseLong(val)", Long.class)
                .endControlFlow("catch (Exception ignored) {}")
                .addStatement("return longVal")
                .returns(long.class)
                .build();
    }

    private MethodSpec getDoubleMethod(ExecutableElement executableElement, SettingGetter settingGetter) {
        String key = settingGetter.key();
        String defaultDoubleVal = settingGetter.defaultValue();
        return MethodSpec.methodBuilder(executableElement.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addStatement("String val = centreRepository.getOrDefault($S, $S)", key, defaultDoubleVal)
                .addStatement("double doubleVal = 0.0")
                .beginControlFlow("try")
                .addStatement("doubleVal = $T.parseDouble(val)", Double.class)
                .endControlFlow("catch (Exception ignored) {}")
                .addStatement("return doubleVal")
                .returns(double.class)
                .build();
    }

    private MethodSpec getBooleanMethod(ExecutableElement executableElement, SettingGetter settingGetter) {
        String key = settingGetter.key();
        String defaultBooleanVal = settingGetter.defaultValue();
        return MethodSpec.methodBuilder(executableElement.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addStatement("String val = centreRepository.getOrDefault($S, $S)", key, defaultBooleanVal)
                .addStatement("boolean booleanVal = false")
                .beginControlFlow("try")
                .addStatement("booleanVal = $T.parseBoolean(val)", Boolean.class)
                .endControlFlow("catch (Exception ignored) {}")
                .addStatement("return booleanVal")
                .returns(boolean.class)
                .build();
    }
}
