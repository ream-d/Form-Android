package com.util.form;

import com.util.form.annotation.FormField;
import com.util.form.annotation.FormMap;
import com.util.form.annotation.FormMethod;
import com.util.form.annotation.FormView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 2017/1/12 0012.
 */
public class FormUtil {

    private static Map<String, Map<String, ViewBean>> formMaps = new ConcurrentHashMap<>();

    public static void unbind(Object obj) {
        FormMap mapKey = getAnnotation(obj);
        if (mapKey == null) return;
        formMaps.remove(mapKey.name());
    }

    public static void bind(Object obj) {

        FormMap mapKey = getAnnotation(obj);
        if (mapKey == null) return;
        Map<String, ViewBean> formMap = formMaps.get(mapKey.name());
        if (formMap == null) formMap = new HashMap<>();
        bindField(obj, formMap);
        bindMethod(obj, formMap);
        formMaps.put(mapKey.name(), formMap);
    }

    private static void bindMethod(Object obj, Map<String, ViewBean> formMap) {
        final Method[] fields = obj.getClass().getDeclaredMethods();
        for (Method method : fields) {
            FormMethod formItem = method.getAnnotation(FormMethod.class);
            if (formItem == null) continue;
            try {
                method.setAccessible(true);
                formMap.put(formItem.beanFiled(), new ViewBean(formItem.beanFiled(), method, obj));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void bindField(Object obj, Map<String, ViewBean> formMap) {
        final Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {

            if (field.isAnnotationPresent(FormView.class)) {
                try {
                    FormView formItem = field.getAnnotation(FormView.class);
                    field.setAccessible(true);
                    Object view = field.get(obj);
                    Method textMethod;

                    try {
                        textMethod = view.getClass().getDeclaredMethod(formItem.valueMethod());
                    } catch (NoSuchMethodException e) {
                        textMethod = null;
                    }
                    if (textMethod == null) {
                        textMethod = getSuperclassMethod(view.getClass(), formItem.valueMethod());
                    }

                    //
                    if (isNotEmpty(formItem.beanFiled()))
                        formMap.put(formItem.beanFiled(), new ViewBean(formItem.beanFiled(), textMethod, view));
                    else
                        formMap.put(field.getName(), new ViewBean(field.getName(), textMethod, view));


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (field.isAnnotationPresent(FormField.class)) {
                try {
                    FormField formItem = field.getAnnotation(FormField.class);
                    field.setAccessible(true);
                    Object view = field.get(obj);

                    if (isNotEmpty(formItem.beanFiled()))
                        formMap.put(formItem.beanFiled(), new ViewBean(formItem.beanFiled(), null, view));
                    else
                        formMap.put(field.getName(), new ViewBean(field.getName(), null, view));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static Object getValue(String fromName, String beanName) {
        try {
            final ViewBean viewBean = formMaps.get(fromName).get(beanName);
            if (viewBean.getMethod() != null)
                return viewBean.getMethod().invoke(viewBean.getView());
            else
                return viewBean.getView();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object formBean(Object obj) {
        FormMap mapKey = getAnnotation(obj);
        if (mapKey == null) return null;
        Object fromValue;
        try {
            fromValue = mapKey.bean().newInstance();
            final Field[] fields = mapKey.bean().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                field.set(fromValue, getValue(mapKey.name(), field.getName()));
            }
        } catch (Exception e) {
            fromValue = null;
        }
        return fromValue;
    }

    private static FormMap getAnnotation(Object obj) {
        if (obj == null) return null;
        return obj.getClass().getAnnotation(FormMap.class);
    }

    private static Method getSuperclassMethod(Class<?> clz, String name) {
        Method method;
        try {
            method = clz.getSuperclass().getDeclaredMethod(name);
        } catch (NoSuchMethodException e) {
            method = null;
        }
        if (method == null) {
            method = getSuperclassMethod(clz.getSuperclass(), name);
        }
        return method;
    }

    private static boolean isNotEmpty(String value) {
        return value != null && value.length() >= 1;
    }

}
