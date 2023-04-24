package org.asteriskjava.ami.databind.serializer.std;

import org.asteriskjava.ami.databind.AsteriskGenerator;
import org.asteriskjava.ami.databind.serializer.AsteriskSerializer;

public class ToStringSerializer implements AsteriskSerializer<Object> {
    @Override
    public void serialize(String field, Object value, AsteriskGenerator asteriskGenerator) {
        asteriskGenerator.writeFieldValue(value.toString());
    }
}
