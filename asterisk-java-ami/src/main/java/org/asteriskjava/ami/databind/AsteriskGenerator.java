package org.asteriskjava.ami.databind;

import org.asteriskjava.core.NewlineDelimiter;

public class AsteriskGenerator {
    private static final String FIELD_NAME_VALUE_DELIMITER = ": ";

    private final NewlineDelimiter newlineDelimiter;

    private final StringBuilder stringBuilder = new StringBuilder();

    public AsteriskGenerator(NewlineDelimiter newlineDelimiter) {
        this.newlineDelimiter = newlineDelimiter;
    }

    public void writeFieldName(String name) {
        stringBuilder.append(name);
        stringBuilder.append(FIELD_NAME_VALUE_DELIMITER);
    }

    public void writeFieldValue(String value) {
        stringBuilder.append(value);
        stringBuilder.append(newlineDelimiter.getPattern());
    }

    public String generate() {
        return stringBuilder.toString();
    }
}
