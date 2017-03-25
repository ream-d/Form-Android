package ream.form.compiler;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.type.TypeMirror;

/**
 * 2017/2/28.
 */
public class BindingClass {
    private final String formName;
    private final String packageName;
    private final String className;
    private final TypeMirror type;
    private final List<BindingField> fields;

    public BindingClass(String formName, String packageName, String className, TypeMirror type) {
        this.formName = formName;
        this.packageName = packageName;
        this.className = className;
        this.type = type;
        fields = new ArrayList<>();
    }

    public String getFormName() {
        return formName;
    }

    public List<BindingField> getFields() {
        return fields;
    }

    public void addField(BindingField field) {
        fields.add(field);
    }

    public int getFieldCount() {
        return fields.size();
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public TypeMirror getType() {
        return type;
    }


}
