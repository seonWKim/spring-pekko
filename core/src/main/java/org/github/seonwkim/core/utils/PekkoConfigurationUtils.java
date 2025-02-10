package org.github.seonwkim.core.utils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

import org.github.seonwkim.core.PekkoConfiguration;

public class PekkoConfigurationUtils {

    public static Map<String, String> toMap(PekkoConfiguration pekkoConfiguration) {
        Map<String, String> propertiesMap = new HashMap<>();
        populateMap(pekkoConfiguration, propertiesMap, "pekko");
        return propertiesMap;
    }

    private static void populateMap(Object obj, Map<String, String> map, String prefix) {
        if (obj == null) {
            return;
        }

        Method[] methods = obj.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("get") && method.getParameterCount() == 0) {
                try {
                    Object value = method.invoke(obj);
                    if (value != null) {
                        String propertyName = prefix + "." + toKebabCase(method.getName().substring(3));
                        if (value instanceof String || value instanceof Number || value instanceof Boolean) {
                            map.put(propertyName, value.toString());
                        } else if (value instanceof String[] || value instanceof Object[]) {
                            map.put(propertyName, Arrays.toString((Object[]) value));
                        } else {
                            populateMap(value, map, propertyName);
                        }
                    }
                } catch (Exception e) {
                    // Handle exception or log it
                }
            }
        }
    }

    private static String toKebabCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        StringBuilder result = new StringBuilder();
        result.append(Character.toLowerCase(str.charAt(0)));
        for (int i = 1; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                result.append('-').append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
