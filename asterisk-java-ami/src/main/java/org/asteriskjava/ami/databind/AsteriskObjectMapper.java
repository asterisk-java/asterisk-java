package org.asteriskjava.ami.databind;

import org.asteriskjava.core.utils.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Map;

public class AsteriskObjectMapper {
    public String writeValue(Object value) {
        Class<?> clazz = value.getClass();
        Map<String, Method> getters = ReflectionUtils.getGetters(clazz);



        return null;
    }
}
