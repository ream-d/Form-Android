package ream.form.compiler;

import javax.lang.model.element.ElementKind;
import javax.lang.model.type.TypeMirror;

/**
 * 2017/1/12 0012.
 */
class BindingValue {

    private final String packageName;
    private final String className;
    private final ElementKind elementKind;
    private final String filedName;
    private final String targetName;
    private final String methodName;
    private final TypeMirror type;

    public BindingValue(String packageName, String className, ElementKind elementKind, String filedName, String targetName, String methodName, TypeMirror type) {
        this.packageName = packageName;
        this.className = className;
        this.elementKind = elementKind;
        this.filedName = filedName;
        this.targetName = targetName;
        this.methodName = methodName;
        this.type = type;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public ElementKind getElementKind() {
        return elementKind;
    }

    public String getFiledName() {
        return filedName;
    }

    public String getTargetName() {
        return targetName;
    }

    public String getMethodName() {
        return methodName;
    }

    public TypeMirror getType() {
        return type;
    }
}
