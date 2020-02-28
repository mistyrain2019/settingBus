package org.example.prosessor;

import org.example.bus.annotation.LocalSetting;
import org.example.bus.annotation.RemoteSetting;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.HashSet;
import java.util.Set;


public class SettingProsessor extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;
    private RemoteSettingImplGenerator remoteGenerator;
    private LocalSettingImplGenerator localGenerator;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        remoteGenerator = new RemoteSettingImplGenerator(filer, elementUtils, typeUtils);
        localGenerator = new LocalSettingImplGenerator(filer, elementUtils, typeUtils);
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
        Set<String> supportedSet = new HashSet<>();
        supportedSet.add(RemoteSetting.class.getCanonicalName());
        supportedSet.add(LocalSetting.class.getCanonicalName());
        return supportedSet;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> annotatedRemoteElements = roundEnv.getElementsAnnotatedWith(RemoteSetting.class);
        remoteGenerator.generateRemoteSettingImpl(annotatedRemoteElements);

        Set<? extends Element> annotatedLocalElements = roundEnv.getElementsAnnotatedWith(LocalSetting.class);
        localGenerator.generateLocalSettingImpl(annotatedLocalElements);
        return true;
    }
}
