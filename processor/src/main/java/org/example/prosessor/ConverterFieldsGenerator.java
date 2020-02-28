package org.example.prosessor;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import org.example.bus.annotation.SettingGetter;
import org.example.bus.annotation.SettingSetter;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConverterFieldsGenerator {

    public static Iterable<FieldSpec> generateConverterFields(List<? extends ExecutableElement> methods) {
        Map<String, FieldSpec> res = new HashMap<>();

        for (ExecutableElement executableElement : methods) {
            SettingGetter settingGetter = executableElement.getAnnotation(SettingGetter.class);
            SettingSetter settingSetter = executableElement.getAnnotation(SettingSetter.class);
            if (settingGetter == null && settingSetter == null) {
                continue;
            }

            TypeKind typeKindToConvert = settingGetter != null ? executableElement.getReturnType().getKind()
                    : executableElement.getParameters().get(0).asType().getKind();

            if (typeKindToConvert.equals(TypeKind.DECLARED)) {
                List<? extends TypeMirror> tp = null;
                try {
                    if (settingGetter != null) {
                        settingGetter.converterClazz();
                    } else {
                        settingSetter.converterClazz();
                    }
                } catch (MirroredTypesException mte) {
                    tp = mte.getTypeMirrors();
                }
                if (tp == null || tp.isEmpty()) {
                    continue;
                }
                TypeMirror typeMirror = tp.get(0);
                String name = getGeneratedFieldName(typeMirror);
                if (res.containsKey(name)) {
                    continue;
                }
                FieldSpec fieldSpec = FieldSpec.builder(TypeName.get(typeMirror), name)
                        .addModifiers(Modifier.PRIVATE)
                        .initializer("new $T()", typeMirror)
                        .build();
                res.put(name, fieldSpec);
            }
        }
        return res.values();
    }

    public static String getGeneratedFieldName(TypeMirror typeMirror) {
        return typeMirror.toString().toLowerCase().replaceAll("\\.", "_");
    }
}
