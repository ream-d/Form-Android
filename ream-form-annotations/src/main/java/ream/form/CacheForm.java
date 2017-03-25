package ream.form;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by win on 2017/3/23.
 */
public class CacheForm {
    private static Map<String, Map<String, CharSequence>> cache = new ConcurrentHashMap<>();

    public static Map<String, CharSequence> getCache(String formName) {
        Map<String, CharSequence> map = cache.get(formName);
        if (map == null) {
            map = new ConcurrentHashMap<>();
            cache.put(formName, map);
        }
        return map;
    }

    public static void putCache(String formName, String fieldName, String value) {
        Map<String, CharSequence> map = cache.get(formName);
        if (map == null) {
            map = new ConcurrentHashMap<>();
            cache.put(formName, map);
        }
        map.put(fieldName, value);
    }

    public static void removeMap(String formName) {
        cache.remove(formName);
    }
}
