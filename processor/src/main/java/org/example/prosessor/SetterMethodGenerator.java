package org.example.prosessor;

import com.squareup.javapoet.MethodSpec;
import org.example.bus.annotation.SettingGetter;
import org.example.bus.annotation.SettingSetter;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;
import java.util.ArrayList;
import java.util.List;

public class SetterMethodGenerator {

    public static Iterable<MethodSpec> generateSetterMethods(List<? extends ExecutableElement> ees) {
        List<MethodSpec> generatedSetterMethods = new ArrayList<>();

        for (ExecutableElement executableElement : ees) {

            SettingSetter settingGetter = executableElement.getAnnotation(SettingSetter.class);

            TypeKind setTypeKind = executableElement.getParameters().get(0).asType().getKind();
            MethodSpec method = null;

            switch (setTypeKind) {
                case INT:
                    method = getIntMethod(executableElement, settingGetter);
                    break;
//                case LONG:
//                    method = getLongMethod(executableElement, settingGetter);
//                    break;
//                case DOUBLE:
//                    method = getDoubleMethod(executableElement, settingGetter);
//                    break;
//                case BOOLEAN:
//                    method = getBooleanMethod(executableElement, settingGetter);
//                    break;
//                case DECLARED:
//                    method = getDeclaredMethod(executableElement, settingGetter);
//                    break;
//                case VOID:
//                    method = getVoidMethod(executableElement);
//                    break;
//                default:
            }

            generatedSetterMethods.add(method);
        }
        return generatedSetterMethods;
    }

    private static MethodSpec getIntMethod(ExecutableElement executableElement, SettingSetter settingSetter) {
        String key = settingSetter.key();
        String toSetVal = executableElement.getParameters().get(0).toString();
//        executableElement.getParameters();
        System.out.println(executableElement.getParameters().get(0));
        return MethodSpec.methodBuilder(executableElement.getSimpleName().toString())
                .addParameter(int.class, toSetVal)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addStatement("centreRepository.set($S, $T.valueOf($L))", key, String.class, toSetVal)
                .returns(void.class)
                .build();
    }
}
