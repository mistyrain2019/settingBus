package org.example.prosessor;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import java.util.List;

/**
 * 接口信息
 */
public class InterfaceInfo {

    public String interfaceName;
    public String fullName;
    public String packageName;
    public List<? extends Element> enclosedElements;
    public List<? extends ExecutableElement> executableElements;

    public InterfaceInfo(TypeElement typeElement, Elements elementUtil) {
        interfaceName = typeElement.getSimpleName().toString();
        fullName = typeElement.getQualifiedName().toString();
        packageName = elementUtil.getPackageOf(typeElement).getQualifiedName().toString();
        //获取源代码对象的成员
        enclosedElements = typeElement.getEnclosedElements();
        //留下方法成员,过滤掉其他成员
        executableElements = ElementFilter.methodsIn(enclosedElements);
    }
}