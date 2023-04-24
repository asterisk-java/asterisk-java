package org.asteriskjava.ami.databind.serializer.std;

import org.asteriskjava.ami.databind.AsteriskGenerator;
import org.asteriskjava.ami.databind.serializer.AsteriskSerializer;

import java.util.Map;

import static java.lang.String.format;

public class MapSerializer implements AsteriskSerializer<Map<String, String>> {
    @Override
    public void serialize(String field, Map<String, String> value, AsteriskGenerator asteriskGenerator) {
        value.forEach((key, value1) -> {
            asteriskGenerator.writeFieldName(field);
            String c = format("%s=%s", key, value1);
            asteriskGenerator.writeFieldValue(c);
        });
    }
}
