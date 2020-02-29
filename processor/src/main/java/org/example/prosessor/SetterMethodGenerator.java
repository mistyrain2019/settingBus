package org.example.prosessor;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import org.example.bus.annotation.SettingSetter;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;

import static org.example.bus.common.Constants.STRING_QUALIFIED_NAME;

public class SetterMethodGenerator {

    public static Iterable<MethodSpec> generateSetterMethods(List<? extends ExecutableElement> ees) {
        List<MethodSpec> generatedSetterMethods = new ArrayList<>();

        for (ExecutableElement executableElement : ees) {

            SettingSetter settingSetter = executableElement.getAnnotation(SettingSetter.class);
            List<? extends VariableElement> parameters = executableElement.getParameters();
            if (parameters == null || parameters.isEmpty()) {
                throw new RuntimeException("无参方法无法生成setter");
            }

            TypeKind setTypeKind = parameters.get(0).asType().getKind();
            MethodSpec method = null;

            switch (setTypeKind) {
                case INT:
                case LONG:
                case DOUBLE:
                case BOOLEAN:
                    method = getBasicMethod(executableElement, settingSetter);
                    break;
                case DECLARED:
                    method = getDeclaredMethod(executableElement, settingSetter);
                    break;
                default:
            }

            generatedSetterMethods.add(method);
        }
        return generatedSetterMethods;
    }

    private static MethodSpec getBasicMethod(ExecutableElement executableElement, SettingSetter settingSetter) {
        String key = settingSetter.key();
        VariableElement variable = executableElement.getParameters().get(0);
        String toSetValueName = variable.toString();
        return MethodSpec.methodBuilder(executableElement.getSimpleName().toString())
                .addParameter(TypeName.get(variable.asType()), toSetValueName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addStatement("centreRepository.set($S, $T.valueOf($L))", key, String.class, toSetValueName)
                .returns(void.class)
                .build();
    }

    private static MethodSpec getDeclaredMethod(ExecutableElement executableElement, SettingSetter settingSetter) {
        VariableElement variable = executableElement.getParameters().get(0);
        TypeMirror typeMirror = variable.asType();
        if (STRING_QUALIFIED_NAME.equals(typeMirror.toString())) {
            return getBasicMethod(executableElement, settingSetter);
        }
        List<? extends TypeMirror> tp = null;
        try {
            settingSetter.converterClazz();
        } catch (MirroredTypesException mte) {
            tp = mte.getTypeMirrors();
        }
        String key = settingSetter.key();
        if (tp == null || tp.size() < 1) {
            throw new RuntimeException("不提供转换器 无法转换");
        }
        String toSetValueName = variable.toString();
        String fieldName = ConverterFieldsGenerator.getGeneratedFieldName(tp.get(0));
        return MethodSpec.methodBuilder(executableElement.getSimpleName().toString())
                .addParameter(TypeName.get(typeMirror), toSetValueName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addStatement("String convertedStr = $L.serialization($L)", fieldName, toSetValueName)
                .addStatement("centreRepository.set($S, convertedStr)", key)
                .returns(void.class)
                .build();
    }
}
