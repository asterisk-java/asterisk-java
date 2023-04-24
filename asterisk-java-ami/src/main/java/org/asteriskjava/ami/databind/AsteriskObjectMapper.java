package org.asteriskjava.ami.databind;

import org.asteriskjava.ami.databind.annotation.AsteriskName;
import org.asteriskjava.ami.databind.annotation.AsteriskSerialize;
import org.asteriskjava.ami.databind.serializer.AsteriskSerializer;
import org.asteriskjava.ami.databind.serializer.AsteriskSerializers;
import org.asteriskjava.core.utils.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

public class AsteriskObjectMapper {
    private final AsteriskGenerator asteriskGenerator;
    private final AsteriskSerializers asteriskSerializers;

    public AsteriskObjectMapper(AsteriskGenerator asteriskGenerator, AsteriskSerializers asteriskSerializers) {
        this.asteriskGenerator = asteriskGenerator;
        this.asteriskSerializers = asteriskSerializers;
    }

    public String writeValue(Object value) {
        Class<?> clazz = value.getClass();
        Map<String, Method> getters = ReflectionUtils.getGetters(clazz, new ActionFieldsComparator());

        for (Entry<String, Method> entry : getters.entrySet()) {
            Method method = entry.getValue();

            Object currentValue = getCurrentValue(value, method);
            if (currentValue == null) {
                continue;
            }

            String currentName = getCurrentName(entry);
            asteriskGenerator.writeFieldName(currentName);

            serialize(method, currentValue, currentName);
        }

        return asteriskGenerator.generate();
    }

    private static String getCurrentName(Entry<String, Method> entry) {
        String name = entry.getKey();

        AsteriskName asteriskName = entry.getValue().getAnnotation(AsteriskName.class);
        if (asteriskName != null) {
            name = asteriskName.value();
        }

        return name;
    }

    private Object getCurrentValue(Object value, Method method) {
        try {
            return method.invoke(value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void serialize(Method method, Object currentValue, String currentName) {
        AsteriskSerialize asteriskSerialize = method.getAnnotation(AsteriskSerialize.class);
        if (asteriskSerialize != null) {
            Class<? extends AsteriskSerializer<?>> using = asteriskSerialize.value();
            AsteriskSerializer<Object> serializer = (AsteriskSerializer<Object>) asteriskSerializers.findSerializer(using);
            if (serializer != null) {
                serializer.serialize(currentName, currentValue, asteriskGenerator);
            }
        } else {
            AsteriskSerializer<Object> serializer = asteriskSerializers.findTypeSerializer(currentValue.getClass());
            serializer.serialize(currentName, currentValue, asteriskGenerator);
        }
    }

    static class ActionFieldsComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            if (o1.equals(o2)) {
                return 0;
            }
            if (o1.equalsIgnoreCase("Action") && o2.equalsIgnoreCase("ActionID") || (o1.equalsIgnoreCase("ActionID") && o2.equalsIgnoreCase("Action"))) {
                return 1;
            }
            if (o1.equalsIgnoreCase("Action") || o1.equalsIgnoreCase("ActionID")) {
                return -1;
            }
            return 1;
        }
    }
}
