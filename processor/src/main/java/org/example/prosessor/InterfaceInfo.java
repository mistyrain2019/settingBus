package org.example.prosessor;

import javax.lang.model.element.TypeElement;

public class InterfaceInfo {

    String interfaceName;

    String fullName;

    int packageLastIndex;

    String packageName;

    public InterfaceInfo(TypeElement typeElement) {
        interfaceName = typeElement.getSimpleName().toString();
        fullName = typeElement.getQualifiedName().toString();
        int packageLastIndex = fullName.lastIndexOf(interfaceName) - 1;
        packageName = fullName.substring(0, packageLastIndex);
    }
}