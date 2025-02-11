package org.github.seonwkim.core.utils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.Properties;

import org.github.seonwkim.core.PekkoConfiguration;
import org.github.seonwkim.core.PekkoSystemConfiguration;

public class PekkoConfigurationUtils {

    public static Properties toProperties(PekkoConfiguration config) {
        Map<String, String> map = toMap(config);
        Properties properties = new Properties();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            properties.setProperty(entry.getKey(), entry.getValue());
        }

        return properties;
    }

    private static Map<String, String> toMap(PekkoConfiguration config) {
        Map<String, String> propertiesMap = new HashMap<>();
        populateMap(config, propertiesMap, "pekko");
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
