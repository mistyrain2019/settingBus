package org.example.prosessor;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import org.example.bus.annotation.SettingGetter;

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

            if (settingGetter == null) {
                continue;
            }
            TypeKind returnTypeKind = executableElement.getReturnType().getKind();

            if (returnTypeKind.equals(TypeKind.DECLARED)) {
                List<? extends TypeMirror> tp = null;
                try {
                    settingGetter.converterClazz();
                } catch (MirroredTypesException mte) {
                    tp = mte.getTypeMirrors();
                }
                if (tp == null || tp.isEmpty()) {
                    continue;
                }
                TypeMirror tm = tp.get(0);
                String name = getGeneratedFieldName(tm);
                if (res.containsKey(name)) {
                    continue;
                }
                FieldSpec fieldSpec = FieldSpec.builder(TypeName.get(tm), name)
                        .addModifiers(Modifier.PRIVATE)
                        .initializer("new $T()", tm)
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
