package org.asteriskjava.core.databind.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.asteriskjava.core.databind.utils.AsteriskBoolean.toBoolean;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class AsteriskBooleanTest {
    @ParameterizedTest
    @MethodSource("values")
    void testIsTrue(Object value, boolean expected) {
        //when
        boolean actual = toBoolean(value);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    public static Stream<Arguments> values() {
        return Stream.of(
                arguments("yes", true),
                arguments("y", true),
                arguments("t", true),
                arguments("1", true),
                arguments("on", true),
                arguments("ON", true),
                arguments("Enabled", true),
                arguments("true", true),

                arguments("no", false),
                arguments("false", false),
                arguments("n", false),
                arguments("f", false),
                arguments("0", false),
                arguments("off", false),
                arguments(null, false)
        );
    }
}
