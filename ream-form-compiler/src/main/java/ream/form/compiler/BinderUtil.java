package ream.form.compiler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ream.form.binder.IReamForm;

/**
 * 2017/1/13 0013.
 */
public class BinderUtil {

    private static <T> T getField(Object target, String fieldName) throws IllegalAccessException {
        Field field = getDeclaredField(target, fieldName);
        Object obj = field == null ? null : field.get(target);

        return null;
    }

    private static Field getDeclaredField(Object target, String fieldName) {
        Field field;
        try {
            field = target.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            field = null;
        }
        return field;
    }

    private static Method getMethod(Object target, String methodName) {
        Method method;
        try {
            method = target.getClass().getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            method = null;
        }
        return method;
    }


    private static Map<String, List<IReamForm>> mariaCash = new ConcurrentHashMap<>();

    public static <T> void register(IReamForm<T> binder) {
        register(String.valueOf(binder.getClass().getName().hashCode()), binder);
    }

    public static <T> void register(String key, IReamForm<T> binder) {

    }
}
