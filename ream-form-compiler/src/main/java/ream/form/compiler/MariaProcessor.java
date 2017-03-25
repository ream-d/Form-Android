package ream.form.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import ream.form.CacheForm;
import ream.form.annoation.FormField;
import ream.form.annoation.Form;
import ream.form.annoation.FormMethod;
import ream.form.annoation.FormView;
import ream.form.binder.IReamForm;

/**
 * Created by win on 2017/2/16.
 */
@AutoService(Processor.class)
public class MariaProcessor extends AbstractProcessor {
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(FormView.class.getCanonicalName());
//        types.add(FormField.class.getCanonicalName());
        types.add(Form.class.getCanonicalName());
//        types.add(FormMethod.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private Elements elementUtils; //元素相关的辅助类


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
    }

    void logInfo(String info) {
        log(Diagnostic.Kind.NOTE, info);
    }

    void log(Diagnostic.Kind kind, String info) {
        if (this.processingEnv == null) return;
        final Messager messager = this.processingEnv.getMessager();
        if (messager != null) {
            messager.printMessage(kind, "Maria Form Building : " + info);
        }
    }

    void logError(String info) {
        log(Diagnostic.Kind.ERROR, info);
    }

    private Filer getFiler() {
        return this.processingEnv == null ? null : this.processingEnv.getFiler();
    }

    private String getPackageName(TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }

    private static final String BINDING_CLASS_SUFFIX = "ReamForm";//生成类的后缀 以后会用反射去取


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Map<String, BindingClass> classMap = new LinkedHashMap<>();

        for (Element element : roundEnv.getElementsAnnotatedWith(Form.class)) {
            if (element.getKind() == ElementKind.CLASS) {
                TypeElement typeElement = (TypeElement) element;
                String packageName = getPackageName(typeElement);
                String className = typeElement.getSimpleName().toString();
                Form mariaMap = element.getAnnotation(Form.class);
                String formName = isEmpty(mariaMap.name()) ? typeElement.getQualifiedName().toString() : mariaMap.name();
                BindingClass bindingClass = new BindingClass(formName, packageName, className, typeElement.asType());
                classMap.put(typeElement.getQualifiedName().toString(), bindingClass);
            }
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(FormView.class)) {
            if (element.getKind() == ElementKind.FIELD) {
                //
                TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

                //
                VariableElement elementVariable = (VariableElement) element;
                FormView mariaView = element.getAnnotation(FormView.class);
                BindingField bindingField = new BindingField(element.getKind()
                        , element.getSimpleName().toString()
                        , mariaView.bean()
                        , mariaView.set()
                        , mariaView.get()
                        , elementVariable.asType());
                BindingClass bindingClass = classMap.get(enclosingElement.getQualifiedName().toString());
                if (bindingClass != null) {
                    bindingClass.addField(bindingField);
                }
            }

        }
        Iterator<Map.Entry<String, BindingClass>> iterator = classMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, BindingClass> entry = iterator.next();
            BindingClass val = entry.getValue();
            if (val != null && val.getFieldCount() >= 1) {
                try {
                    final MethodSpec.Builder methodBind = MethodSpec.methodBuilder("bind")
                            .addModifiers(Modifier.PUBLIC)
                            .addAnnotation(Override.class)
                            .addParameter(TypeName.get(val.getType()), "target", Modifier.FINAL);
                    final MethodSpec.Builder methodResetForm = MethodSpec.methodBuilder("resetForm")
                            .addModifiers(Modifier.PUBLIC)
                            .addAnnotation(Override.class);
                    final MethodSpec.Builder methodStoreForm = MethodSpec.methodBuilder("storeForm")
                            .addModifiers(Modifier.PUBLIC)
                            .addAnnotation(Override.class);
                    final MethodSpec.Builder methodFormName = MethodSpec.methodBuilder("formName")
                            .addModifiers(Modifier.PUBLIC)
                            .addAnnotation(Override.class)
                            .returns(String.class)
                            .addStatement("return \"$L\"", val.getFormName());

                    final TypeSpec.Builder classBuilder = TypeSpec.classBuilder(val.getClassName() + BINDING_CLASS_SUFFIX)
                            .addModifiers(Modifier.PUBLIC)
                            .addSuperinterface(ParameterizedTypeName.get(ClassName.get(IReamForm.class.getPackage().getName(), IReamForm.class.getSimpleName())
                                    , TypeName.get(val.getType())));

                    methodResetForm.addStatement("final $T<String, CharSequence> form = $T.getCache(\"$L\")"
                            , MapClass, CacheClass, val.getFormName());
                    methodStoreForm.addStatement("final $T<String, CharSequence> form = $T.getCache(\"$L\")"
                            , MapClass, CacheClass, val.getFormName());

                    for (BindingField field : val.getFields()) {
                        ElementKind kind = field.getElementKind();
                        if (kind.isField()) {
                            String fieldName = isEmpty(field.getTargetName()) ? field.getFiledName() : field.getTargetName();

                            classBuilder.addField(TypeName.get(field.getType()), fieldName);
                            methodBind.addStatement("this.$L = target.$L", fieldName, field.getFiledName());

                            //
                            methodResetForm.addStatement("this.$L.$L(form.get(\"$L\"))", fieldName, field.getSetMethod(), fieldName);
                            methodStoreForm.addStatement("form.put(\"$L\", this.$L.$L())", fieldName, fieldName, field.getGetMethod());
                        }
                    }

                    classBuilder.addMethod(methodBind.build());
                    classBuilder.addMethod(methodResetForm.build());
                    classBuilder.addMethod(methodStoreForm.build());
                    classBuilder.addMethod(methodFormName.build());

                    JavaFile.builder(val.getPackageName()
                            , classBuilder.build())
                            .build()
                            .writeTo(getFiler());
                } catch (IOException e) {
                    logError(e.getMessage());
                }
            }
        }
        return true;
    }

    final ClassName MapClass = ClassName.get(Map.class);
    final ClassName CacheClass = ClassName.get(CacheForm.class);

    private boolean isEmpty(String value) {
        return !(value != null && value.length() >= 1);
    }
}
