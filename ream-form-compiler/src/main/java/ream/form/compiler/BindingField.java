package ream.form.compiler;

import javax.lang.model.element.ElementKind;
import javax.lang.model.type.TypeMirror;

/**
 *
 */
public class BindingField {
    private final ElementKind elementKind;
    private final String filedName;
    private final String targetName;
    private final String setMethod;
    private final String getMethod;
    private final TypeMirror type;

    public BindingField(ElementKind elementKind, String filedName, String targetName, String setMethod, String getMethod, TypeMirror type) {
        this.elementKind = elementKind;
        this.filedName = filedName;
        this.targetName = targetName;
        this.setMethod = setMethod;
        this.getMethod = getMethod;
        this.type = type;
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

    public String getSetMethod() {
        return setMethod;
    }

    public String getGetMethod() {
        return getMethod;
    }

    public TypeMirror getType() {
        return type;
    }
}
